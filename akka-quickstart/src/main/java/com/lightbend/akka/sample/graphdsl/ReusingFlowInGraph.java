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
import org.junit.ClassRule;
import org.junit.Test;
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

public class ReusingFlowInGraph {
    public static void demonstrateReusingFlowInGraph() throws Exception {
        final ActorSystem system = ActorSystem.create("GraphDSL");
        final Materializer materializer = ActorMaterializer.create(system);

        //#graph-dsl-reusing-a-flow
        final Sink<Integer, CompletionStage<Integer>> topHeadSink = Sink.head();
        final Sink<Integer, CompletionStage<Integer>> bottomHeadSink = Sink.head();
        final Flow<Integer, Integer, NotUsed> sharedDoubler = Flow.of(Integer.class).map(elem -> elem * 2);

        final RunnableGraph<Pair<CompletionStage<Integer>, CompletionStage<Integer>>> g =
                RunnableGraph.<Pair<CompletionStage<Integer>, CompletionStage<Integer>>>fromGraph(
                        GraphDSL.create(
                                topHeadSink, // import this sink into the graph
                                bottomHeadSink, // and this as well
                                Keep.both(),
                                (b, top, bottom) -> {
                                    final UniformFanOutShape<Integer, Integer> bcast =
                                            b.add(Broadcast.create(2));
                                    final SourceShape<Integer> source = b.add(Source.single(1));
                                    final FlowShape<Integer, Integer> flow1 = b.add(sharedDoubler);
                                    final FlowShape<Integer, Integer> flow2 = b.add(sharedDoubler);

                                    b.from(source).viaFanOut(bcast)
                                            .via(flow1).to(top);
                                    b.from(bcast).via(flow2).to(bottom);
                                    return ClosedShape.getInstance();
                                }
                        )
                );
        //#graph-dsl-reusing-a-flow
        final Pair<CompletionStage<Integer>, CompletionStage<Integer>> pair = g.run(materializer);
        CompletionStage<Integer> first = pair.first();
        CompletionStage<Integer> second = pair.second();
//        assertEquals(Integer.valueOf(2), first.toCompletableFuture().get(3, TimeUnit.SECONDS));
//        assertEquals(Integer.valueOf(2), second.toCompletableFuture().get(3, TimeUnit.SECONDS));
        System.out.println(first.toCompletableFuture().get(1, TimeUnit.SECONDS));
        System.out.println(second.toCompletableFuture().get(1, TimeUnit.SECONDS));
        second.thenRun(()->system.terminate());
    }
    public static void main(String[] args){
        try {
            demonstrateReusingFlowInGraph();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
