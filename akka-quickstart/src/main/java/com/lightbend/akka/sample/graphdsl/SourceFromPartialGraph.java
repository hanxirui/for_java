package com.lightbend.akka.sample.graphdsl;
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
public class SourceFromPartialGraph {
    //#source-from-partial-graph-dsl
    // first create an indefinite source of integer numbers
    static class Ints implements Iterator<Integer> {
        private int next = 0;
        @Override
        public boolean hasNext() {
            return true;
        }
        @Override
        public Integer next() {
            return next++;
        }
    }
    //#source-from-partial-graph-dsl

    public static void demonstrateBuildSourceFromPartialGraphDSLCreate() throws Exception {
        final ActorSystem system = ActorSystem.create("GraphDSL");
        final Materializer mat = ActorMaterializer.create(system);

        //#source-from-partial-graph-dsl
        final Source<Integer, NotUsed> ints = Source.fromIterator(() -> new Ints());

        final Source<Pair<Integer, Integer>, NotUsed> pairs = Source.fromGraph(
                GraphDSL.create(
                        builder -> {
                            final FanInShape2<Integer, Integer, Pair<Integer, Integer>> zip =
                                    builder.add(Zip.create());

                            builder.from(builder.add(ints.filter(i -> i % 2 == 0))).toInlet(zip.in0());
                            builder.from(builder.add(ints.filter(i -> i % 2 == 1))).toInlet(zip.in1());

                            return SourceShape.of(zip.out());
                        }));

        final CompletionStage<Pair<Integer, Integer>> firstPair =
                pairs.runWith(Sink.<Pair<Integer, Integer>>head(), mat);
        //#source-from-partial-graph-dsl
        assertEquals(new Pair<>(0, 1), firstPair.toCompletableFuture().get(3, TimeUnit.SECONDS));
    }

    public static void main(String[] args){
        try {
            demonstrateBuildSourceFromPartialGraphDSLCreate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
