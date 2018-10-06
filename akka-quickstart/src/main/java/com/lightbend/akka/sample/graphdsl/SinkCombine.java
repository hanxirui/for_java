package com.lightbend.akka.sample.graphdsl;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorRef;
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
import akka.testkit.javadsl.TestKit;
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
import akka.NotUsed;
import akka.stream.javadsl.Broadcast;

import java.util.ArrayList;
import java.util.Arrays;

public class SinkCombine {
    public static void demonstrateBuildSinkWithCombine() throws Exception {
        final ActorSystem system = ActorSystem.create("GraphDSL");
        final Materializer mat = ActorMaterializer.create(system);

        final TestKit probe = new TestKit(system);
        ActorRef actorRef =  probe.getRef();

        //#sink-combine
        Sink<Integer, NotUsed> sendRemotely = Sink.actorRef(actorRef, "Done");
        Sink<Integer, CompletionStage<Done>> localProcessing = Sink.<Integer>foreach(a -> {
            System.out.println(a);
        } );
        Sink<Integer, NotUsed> sinks = Sink.combine(sendRemotely,localProcessing, new ArrayList<>(), a -> Broadcast.create(a));

        Source.<Integer>from(Arrays.asList(new Integer[]{0, 1, 2})).runWith(sinks, mat);
        //#sink-combine
        probe.expectMsgEquals(0);
        probe.expectMsgEquals(1);
        probe.expectMsgEquals(2);
    }

    public static void main(String[] args){
        try {
            demonstrateBuildSinkWithCombine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
