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

public class NestedFlow {
    /**
     * The constant system.
     */
    public static final ActorSystem system = ActorSystem.create("reactive-tweets");
    /**
     * The constant mat.
     */
    public static final Materializer mat = ActorMaterializer.create(system);

    private static void nestedFlow() {

        final Source<Integer, NotUsed> nestedSource = Source.single(0) // An atomic source
                .map(i -> i + 1) // an atomic processing stage
                .named("nestedSource"); // wraps up the current Source and gives it a name

        final Flow<Integer, Integer, NotUsed> nestedFlow = Flow.of(Integer.class).filter(i -> i != 0) // an atomic
                // processing
                // stage
                .map(i -> i - 2) // another atomic processing stage
                .named("nestedFlow"); // wraps up the Flow, and gives it a name

        final Sink<Integer, NotUsed> nestedSink = nestedFlow.to(Sink.fold(0, (acc, i) -> {
            System.out.println(i);
            return acc + i;
        })) // wire an atomic sink to the nestedFlow
                .named("nestedSink"); // wrap it up

        // #reuse
        // Create a RunnableGraph from our components
        final RunnableGraph<NotUsed> runnableGraph = nestedSource.to(nestedSink);
        NotUsed run = runnableGraph.run(mat);

        // Usage is uniform, no matter if modules are composite or atomic
        // final RunnableGraph<NotUsed> runnableGraph2 =
        // Source.single(0).to(Sink.fold(0, (acc, i) -> acc + i));
        final RunnableGraph<NotUsed> runnableGraph2 = Source.single(0).via(Flow.of(Integer.class).map(i -> 1 + i))
                .via(Flow.of(Integer.class).filter(i -> i != 0).map(i -> i - 2))
                .via(Flow.of(Integer.class).fold(0, (acc, i) -> {
                    System.out.println(i);
                    return acc + i;
                })).to(Sink.foreach(System.out::println));
        runnableGraph2.run(mat);
        system.terminate();
        // #reuse
    }

    public static void main(String[] args) {
        nestedFlow();
    }
}
