package com.lightbend.akka.sample.graphdsl;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.ClosedShape;
import akka.stream.Outlet;
import akka.stream.UniformFanInShape;
import akka.stream.UniformFanOutShape;
import akka.stream.javadsl.*;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import akka.NotUsed;
import akka.japi.Pair;
import akka.stream.*;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import org.scalatest.run;
import scala.collection.Seq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DeadlockDemos {

    // Running this we observe that after a few numbers have been printed, no more elements are logged to the console -
    // all processing stops after some time. After some investigation we observe that:
    //
    // through merging from source we increase the number of elements flowing in the cycle
    // by broadcasting back to the cycle we do not decrease the number of elements in the cycle
    // Since Akka Streams (and Reactive Streams in general) guarantee bounded processing (see the “Buffering” section
    // for more details) it means that only a bounded number of elements are buffered over any time span. Since our
    // cycle gains more and more elements, eventually all of its internal buffers become full, backpressuring source
    // forever. To be able to process more elements from source elements would need to leave the cycle somehow.
    //
    // If we modify our feedback loop by replacing the Merge junction with a MergePreferred we can avoid the deadlock.
    // MergePreferred is unfair as it always tries to consume from a preferred input port if there are elements
    // available before trying the other lower priority input ports. Since we feed back through the preferred port it is
    // always guaranteed that the elements in the cycles can flow.
    public void demonstrateDeadlockedCycle() {
        // #deadlocked
        // WARNING! The graph below deadlocks!
        final Flow<Integer, Integer, NotUsed> printFlow = Flow.of(Integer.class).map(s -> {
            System.out.println(s);
            return s;
        });

        RunnableGraph.fromGraph(GraphDSL.create(b -> {
            Source<Integer, NotUsed> source = Source.from(Arrays.asList(new Integer[] { 1, 2, 3 }));
            final UniformFanInShape<Integer, Integer> merge = b.add(Merge.create(2));
            final UniformFanOutShape<Integer, Integer> bcast = b.add(Broadcast.create(2));
            final Outlet<Integer> src = b.add(source).out();
            final FlowShape<Integer, Integer> printer = b.add(printFlow);
            final SinkShape<Integer> ignore = b.add(Sink.ignore());

            b.from(src).viaFanIn(merge).via(printer).viaFanOut(bcast).to(ignore);
            b.to(merge).fromFanOut(bcast);
            return ClosedShape.getInstance();
        }));
        // #deadlocked
    }

    // If we run the example we see that the same sequence of numbers are printed over and over again, but the
    // processing does not stop. Hence, we avoided the deadlock, but source is still back-pressured forever, because
    // buffer space is never recovered: the only action we see is the circulation of a couple of initial elements from
    // source.
    //
    // Note
    // What we see here is that in certain cases we need to choose between boundedness and liveness. Our first example
    // would not deadlock if there were an infinite buffer in the loop, or vice versa, if the elements in the cycle were
    // balanced (as many elements are removed as many are injected) then there would be no deadlock.
    public void demonstrateUnfairCycle() {
        final Flow<Integer, Integer, NotUsed> printFlow = Flow.of(Integer.class).map(s -> {
            System.out.println(s);
            return s;
        });
        // #unfair
        // WARNING! The graph below stops consuming from "source" after a few steps
        RunnableGraph.fromGraph(GraphDSL.create(b -> {
            Source<Integer, NotUsed> source = Source.from(Arrays.asList(new Integer[] { 1, 2, 3 }));
            final akka.stream.scaladsl.MergePreferred.MergePreferredShape<Integer> merge = b
                    .add(MergePreferred.create(1));
            final UniformFanOutShape<Integer, Integer> bcast = b.add(Broadcast.create(2));
            final Outlet<Integer> src = b.add(source).out();
            final FlowShape<Integer, Integer> printer = b.add(printFlow);
            final SinkShape<Integer> ignore = b.add(Sink.ignore());

            b.from(src).viaFanIn(merge).via(printer).viaFanOut(bcast).to(ignore);
            b.to(merge.preferred()).fromFanOut(bcast);
            return ClosedShape.getInstance();
        }));
        // #unfair
    }

    // To make our cycle both live (not deadlocking) and fair we can introduce a dropping element on the feedback arc.
    // In this case we chose the buffer() operation giving it a dropping strategy OverflowStrategy.dropHead.

    // If we run this example we see that
    //
    // The flow of elements does not stop, there are always elements printed
    // We see that some of the numbers are printed several times over time (due to the feedback loop) but on average the
    // numbers are increasing in the long term
    // This example highlights that one solution to avoid deadlocks in the presence of potentially unbalanced cycles
    // (cycles where the number of circulating elements are unbounded) is to drop elements. An alternative would be to
    // define a larger buffer with OverflowStrategy.fail which would fail the stream instead of deadlocking it after all
    // buffer space has been consumed.
    public void demonstrateDroppingCycle() {
        final Flow<Integer, Integer, NotUsed> printFlow = Flow.of(Integer.class).map(s -> {
            System.out.println(s);
            return s;
        });
        // #dropping
        RunnableGraph.fromGraph(GraphDSL.create(b -> {
            Source<Integer, NotUsed> source = Source.from(Arrays.asList(new Integer[] { 1, 2, 3 }));
            final UniformFanInShape<Integer, Integer> merge = b.add(Merge.create(2));
            final UniformFanOutShape<Integer, Integer> bcast = b.add(Broadcast.create(2));
            final FlowShape<Integer, Integer> droppyFlow = b
                    .add(Flow.of(Integer.class).buffer(10, OverflowStrategy.dropHead()));
            final Outlet<Integer> src = b.add(source).out();
            final FlowShape<Integer, Integer> printer = b.add(printFlow);
            final SinkShape<Integer> ignore = b.add(Sink.ignore());

            b.from(src).viaFanIn(merge).via(printer).viaFanOut(bcast).to(ignore);
            b.to(merge).via(droppyFlow).fromFanOut(bcast);
            return ClosedShape.getInstance();
        }));
        // #dropping
    }

    // As we discovered in the previous examples, the core problem was the unbalanced nature of the feedback loop. We
    // circumvented this issue by adding a dropping element, but now we want to build a cycle that is balanced from the
    // beginning instead. To achieve this we modify our first graph by replacing the Merge junction with a ZipWith.
    // Since ZipWith takes one element from source and from the feedback arc to inject one element into the cycle, we
    // maintain the balance of elements.
    // Still, when we try to run the example it turns out that no element is printed at all! After some investigation we
    // realize that:
    //
    // In order to get the first element from source into the cycle we need an already existing element in the cycle
    // In order to get an initial element in the cycle we need an element from source
    // These two conditions are a typical “chicken-and-egg” problem. The solution is to inject an initial element into
    // the cycle that is independent from source. We do this by using a Concat junction on the backwards arc that
    // injects a single element using Source.single.
    public void demonstrateZippingCycle() {
        Source<Integer, NotUsed> source = Source.from(Arrays.asList(new Integer[] { 1, 2, 3 }));

        final Flow<Integer, Integer, NotUsed> printFlow = Flow.of(Integer.class).map(s -> {
            System.out.println(s);
            return s;
        });
        // #zipping-dead
        // WARNING! The graph below never processes any elements
        RunnableGraph.fromGraph(GraphDSL.create(b -> {
            final FanInShape2<Integer, Integer, Integer> zip = b
                    .add(ZipWith.create((Integer left, Integer right) -> left));
            final UniformFanOutShape<Integer, Integer> bcast = b.add(Broadcast.create(2));
            final FlowShape<Integer, Integer> printer = b.add(printFlow);
            final SinkShape<Integer> ignore = b.add(Sink.ignore());

            b.from(b.add(source)).toInlet(zip.in0());
            b.from(zip.out()).via(printer).viaFanOut(bcast).to(ignore);
            b.to(zip.in1()).fromFanOut(bcast);
            return ClosedShape.getInstance();
        }));
        // #zipping-dead
    }

    // When we run the above example we see that processing starts and never stops. The important takeaway from this
    // example is that balanced cycles often need an initial “kick-off” element to be injected into the cycle.
    public void demonstrateLiveZippingCycle() {

        Source<Integer, NotUsed> source = Source.from(Arrays.asList(new Integer[] { 1, 2, 3 }));

        final Flow<Integer, Integer, NotUsed> printFlow = Flow.of(Integer.class).map(s -> {
            System.out.println(s);
            return s;
        });
        // #zipping-live
        RunnableGraph.fromGraph(GraphDSL.create(b -> {
            final FanInShape2<Integer, Integer, Integer> zip = b
                    .add(ZipWith.create((Integer left, Integer right) -> left));
            final UniformFanOutShape<Integer, Integer> bcast = b.add(Broadcast.create(2));
            final UniformFanInShape<Integer, Integer> concat = b.add(Concat.create());
            final FlowShape<Integer, Integer> printer = b.add(printFlow);
            final SinkShape<Integer> ignore = b.add(Sink.ignore());

            b.from(b.add(source)).toInlet(zip.in0());
            b.from(zip.out()).via(printer).viaFanOut(bcast).to(ignore);
            b.to(zip.in1()).viaFanIn(concat).from(b.add(Source.single(1)));
            b.to(concat).fromFanOut(bcast);
            return ClosedShape.getInstance();
        }));
        // #zipping-live
    }
}
