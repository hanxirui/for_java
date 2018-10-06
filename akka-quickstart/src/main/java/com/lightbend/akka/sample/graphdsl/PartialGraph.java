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

public class PartialGraph {
    public static void demonstrateBuildWithOpenPorts() throws Exception {
        final ActorSystem system = ActorSystem.create("GraphDSL");
        final Materializer mat = ActorMaterializer.create(system);
        //#simple-partial-graph-dsl
        final Graph<FanInShape2<Integer, Integer, Integer>, NotUsed> zip =
                ZipWith.create((Integer left, Integer right) -> Math.max(left, right));

        final Graph<UniformFanInShape<Integer, Integer>, NotUsed> pickMaxOfThree =
                GraphDSL.create(builder -> {
                    final FanInShape2<Integer, Integer, Integer> zip1 = builder.add(zip);
                    final FanInShape2<Integer, Integer, Integer> zip2 = builder.add(zip);

                    builder.from(zip1.out()).toInlet(zip2.in0());
                    // return the shape, which has three inputs and one output
                    return new UniformFanInShape<Integer, Integer>(zip2.out(),
                            new Inlet[] {zip1.in0(), zip1.in1(), zip2.in1()});
                });

        final Sink<Integer, CompletionStage<Integer>> resultSink = Sink.<Integer>head();

        final RunnableGraph<CompletionStage<Integer>> g =
                RunnableGraph.<CompletionStage<Integer>>fromGraph(
                        GraphDSL.create(resultSink, (builder, sink) -> {
                            // import the partial graph explicitly
                            final UniformFanInShape<Integer, Integer> pm = builder.add(pickMaxOfThree);

                            builder.from(builder.add(Source.single(1))).toInlet(pm.in(0));
                            builder.from(builder.add(Source.single(2))).toInlet(pm.in(1));
                            builder.from(builder.add(Source.single(3))).toInlet(pm.in(2));
                            builder.from(pm.out()).to(sink);
                            return ClosedShape.getInstance();
                        }));

        final CompletionStage<Integer> max = g.run(mat);
        //#simple-partial-graph-dsl
        assertEquals(Integer.valueOf(3), max.toCompletableFuture().get(3, TimeUnit.SECONDS));
    }
    public static void main(String[] args){
        try {
            demonstrateBuildWithOpenPorts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
