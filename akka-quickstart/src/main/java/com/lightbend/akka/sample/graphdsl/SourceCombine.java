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

public class SourceCombine {
    public static void demonstrateBuildSourceWithCombine() throws Exception {
        final ActorSystem system = ActorSystem.create("GraphDSL");
        final Materializer materializer = ActorMaterializer.create(system);

        //#source-combine
        Source<Integer, NotUsed> source1 = Source.single(1);
        Source<Integer, NotUsed> source2 = Source.single(2);

        final Source<Integer, NotUsed> sources = Source.combine(source1, source2, new ArrayList<>(),
                i -> Merge.<Integer>create(i));
        //#source-combine
        final CompletionStage<Integer> result=
                //#source-combine
                sources.runWith(Sink.<Integer, Integer>fold(0, (a,b) -> a + b), materializer);
        //#source-combine

        assertEquals(Integer.valueOf(3), result.toCompletableFuture().get(3, TimeUnit.SECONDS));
    }
    public static void main(String[] args){
        try {
            demonstrateBuildSourceWithCombine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
