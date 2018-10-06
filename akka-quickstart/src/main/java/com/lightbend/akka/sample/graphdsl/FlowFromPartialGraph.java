package com.lightbend.akka.sample.graphdsl;

import akka.NotUsed;
import akka.stream.FlowShape;
import akka.stream.UniformFanOutShape;
import akka.stream.javadsl.Broadcast;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Zip;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.japi.Pair;
import akka.stream.*;
import akka.stream.javadsl.*;
import akka.testkit.javadsl.TestKit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;

public class FlowFromPartialGraph {
    public static void demonstrateBuildFlowFromPartialGraphDSLCreate() throws Exception {
        final ActorSystem system = ActorSystem.create("GraphDSL");
        final Materializer mat = ActorMaterializer.create(system);


        //#flow-from-partial-graph-dsl
        final Flow<Integer, Pair<Integer, String>, NotUsed> pairs = Flow.fromGraph(GraphDSL.create(
                b -> {
                    final UniformFanOutShape<Integer, Integer> bcast = b.add(Broadcast.create(2));
                    final FanInShape2<Integer, String, Pair<Integer, String>> zip =
                            b.add(Zip.create());

                    b.from(bcast).toInlet(zip.in0());
                    b.from(bcast).via(b.add(Flow.of(Integer.class).map(i -> i.toString()))).toInlet(zip.in1());

                    return FlowShape.of(bcast.in(), zip.out());
                }));

        //#flow-from-partial-graph-dsl
        final CompletionStage<Pair<Integer, String>> matSink =
                //#flow-from-partial-graph-dsl
                Source.single(1).via(pairs).runWith(Sink.<Pair<Integer, String>>head(), mat);
        //#flow-from-partial-graph-dsl

        assertEquals(new Pair<>(1, "1"), matSink.toCompletableFuture().get(3, TimeUnit.SECONDS));
    }

    public static void main(String[] args){
        try {
            demonstrateBuildFlowFromPartialGraphDSLCreate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
