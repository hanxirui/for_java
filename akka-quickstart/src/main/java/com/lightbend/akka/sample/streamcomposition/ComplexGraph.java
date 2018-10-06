package com.lightbend.akka.sample.streamcomposition;

import akka.NotUsed;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import akka.NotUsed;
import akka.stream.ClosedShape;
import akka.testkit.javadsl.TestKit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import akka.actor.ActorSystem;
import akka.japi.Pair;
import akka.stream.*;
import akka.stream.javadsl.*;
import akka.stream.javadsl.Tcp.OutgoingConnection;
import akka.util.ByteString;
public class ComplexGraph {
    public void complexGraph() throws Exception {
        //#complex-graph
        RunnableGraph.fromGraph(
                GraphDSL.create(builder -> {
                    final Outlet<Integer> A = builder.add(Source.single(0)).out();
                    final UniformFanOutShape<Integer, Integer> B = builder.add(Broadcast.create(2));
                    final UniformFanInShape<Integer, Integer> C = builder.add(Merge.create(2));
                    final FlowShape<Integer, Integer> D =
                            builder.add(Flow.of(Integer.class).map(i -> i + 1));
                    final UniformFanOutShape<Integer, Integer> E = builder.add(Balance.create(2));
                    final UniformFanInShape<Integer, Integer> F = builder.add(Merge.create(2));
                    final Inlet<Integer> G = builder.add(Sink.<Integer> foreach(System.out::println)).in();

                    builder.from(F).toFanIn(C);
                    builder.from(A).viaFanOut(B).viaFanIn(C).toFanIn(F);
                    builder.from(B).via(D).viaFanOut(E).toFanIn(F);
                    builder.from(E).toInlet(G);
                    return ClosedShape.getInstance();
                }));
        //#complex-graph

        //#complex-graph-alt
        RunnableGraph.fromGraph(
                GraphDSL.create(builder -> {
                    final SourceShape<Integer> A = builder.add(Source.single(0));
                    final UniformFanOutShape<Integer, Integer> B = builder.add(Broadcast.create(2));
                    final UniformFanInShape<Integer, Integer> C = builder.add(Merge.create(2));
                    final FlowShape<Integer, Integer> D =
                            builder.add(Flow.of(Integer.class).map(i -> i + 1));
                    final UniformFanOutShape<Integer, Integer> E = builder.add(Balance.create(2));
                    final UniformFanInShape<Integer, Integer> F = builder.add(Merge.create(2));
                    final SinkShape<Integer> G = builder.add(Sink.foreach(System.out::println));

                    builder.from(F.out()).toInlet(C.in(0));
                    builder.from(A).toInlet(B.in());
                    builder.from(B.out(0)).toInlet(C.in(1));
                    builder.from(C.out()).toInlet(F.in(0));
                    builder.from(B.out(1)).via(D).toInlet(E.in());
                    builder.from(E.out(0)).toInlet(F.in(1));
                    builder.from(E.out(1)).to(G);
                    return ClosedShape.getInstance();
                }));
        //#complex-graph-alt
    }
}
