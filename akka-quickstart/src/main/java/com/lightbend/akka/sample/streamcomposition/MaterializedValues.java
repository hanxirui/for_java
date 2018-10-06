package com.lightbend.akka.sample.streamcomposition;

import akka.NotUsed;
import akka.stream.javadsl.Flow;
import akka.util.ByteString;
import scala.collection.parallel.Combiner;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

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

public class MaterializedValues {
    /**
     * The constant system.
     */
    public static final ActorSystem system = ActorSystem.create("reactive-tweets");
    /**
     * The constant mat.
     */
    public static final Materializer mat = ActorMaterializer.create(system);

    public static void materializedValues() throws Exception {
        //#mat-combine-1
        // Materializes to CompletableFuture<Optional<Integer>>                   (red)
        final Source<Integer, CompletableFuture<Optional<Integer>>> source = Source.<Integer>maybe();

        // Materializes to NotUsed                                                (black)
        final Flow<Integer, Integer, NotUsed> flow1 = Flow.of(Integer.class).take(100);

        // Materializes to CompletableFuture<Optional<Integer>>                  (red)
        final Source<Integer, CompletableFuture<Optional<Integer>>> nestedSource =
                source.viaMat(flow1, Keep.left()).named("nestedSource");
        //#mat-combine-1

        //#mat-combine-2
        // Materializes to NotUsed                                                (orange)
        final Flow<Integer, ByteString, NotUsed> flow2 = Flow.of(Integer.class)
                .map(i -> ByteString.fromString(i.toString()));

        // Materializes to Future<OutgoingConnection>                             (yellow)
        final Flow<ByteString, ByteString, CompletionStage<OutgoingConnection>> flow3 =
                Tcp.get(system).outgoingConnection("localhost", 8080);

        // Materializes to Future<OutgoingConnection>                             (yellow)
        final Flow<Integer, ByteString, CompletionStage<OutgoingConnection>> nestedFlow =
                flow2.viaMat(flow3, Keep.right()).named("nestedFlow");
        //#mat-combine-2

        //#mat-combine-3
        // Materializes to Future<String>                                         (green)
        final Sink<ByteString, CompletionStage<String>> sink =
                Sink.<String, ByteString> fold("", (acc, i) -> acc + i.utf8String());

        // Materializes to Pair<Future<OutgoingConnection>, Future<String>>       (blue)
        final Sink<Integer, Pair<CompletionStage<OutgoingConnection>, CompletionStage<String>>> nestedSink =
                nestedFlow.toMat(sink, Keep.both());
        //#mat-combine-3

        //#mat-combine-4b
        // Materializes to Future<MyClass>                                        (purple)
        final RunnableGraph<CompletionStage<MyClass>> runnableGraph =
                nestedSource.toMat(nestedSink, Combiner::f);
        //#mat-combine-4b
    }
    //#mat-combine-4a
    static class MyClass {
        private CompletableFuture<Optional<Integer>> p;
        private OutgoingConnection conn;

        public MyClass(CompletableFuture<Optional<Integer>> p, OutgoingConnection conn) {
            this.p = p;
            this.conn = conn;
        }

        public void close() {
            p.complete(Optional.empty());
        }
    }

    static class Combiner {
        static CompletionStage<MyClass> f(CompletableFuture<Optional<Integer>> p,
                                          Pair<CompletionStage<OutgoingConnection>, CompletionStage<String>> rest) {
            return rest.first().thenApply(c -> new MyClass(p, c));
        }
    }
    //#mat-combine-4a
}
