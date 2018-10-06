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

public class AccessingthematerializedvalueinsidetheGraph {
    public void demonstrateMatValue() throws Exception {
        //#graph-dsl-matvalue
        final Sink<Integer, CompletionStage<Integer>> foldSink = Sink.<Integer, Integer>fold(0, (a, b) -> {
            return a + b;
        });

        final Flow<CompletionStage<Integer>, Integer, NotUsed> flatten =
                Flow.<CompletionStage<Integer>>create().mapAsync(4, x -> x);

        final Flow<Integer, Integer, CompletionStage<Integer>> foldingFlow = Flow.fromGraph(
                GraphDSL.create(foldSink,
                        (b, fold) -> {
                            return FlowShape.of(
                                    fold.in(),
                                    b.from(b.materializedValue()).via(b.add(flatten)).out());
                        }));
        //#graph-dsl-matvalue

        //#graph-dsl-matvalue-cycle
        // This cannot produce any value:
        final Source<Integer, CompletionStage<Integer>> cyclicSource = Source.fromGraph(
                GraphDSL.create(foldSink,
                        (b, fold) -> {
                            // - Fold cannot complete until its upstream mapAsync completes
                            // - mapAsync cannot complete until the materialized Future produced by
                            //   fold completes
                            // As a result this Source will never emit anything, and its materialited
                            // Future will never complete
                            b.from(b.materializedValue()).via(b.add(flatten)).to(fold);
                            return SourceShape.of(b.from(b.materializedValue()).via(b.add(flatten)).out());
                        }));

        //#graph-dsl-matvalue-cycle
    }
}
