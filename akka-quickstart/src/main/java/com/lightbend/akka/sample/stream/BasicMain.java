package com.lightbend.akka.sample.stream;

import akka.NotUsed;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Cancellable;
import akka.japi.Pair;
import akka.stream.*;
import akka.stream.javadsl.*;
import akka.util.ByteString;

import java.lang.annotation.ElementType;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * The type Basic main.
 */
public class BasicMain {
    /**
     * The constant system.
     */
    public static final ActorSystem system = ActorSystem.create("reactive-tweets");
    /**
     * The constant mat.
     */
    public static final Materializer mat = ActorMaterializer.create(system);

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {

        // basic();

        // wireUpStream();

        sourcePreMaterialization();
    }

    /**
     *
     *
     */
    private static void basic() {
        final Source<Integer, NotUsed> source = Source.from(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        // note that the Future is scala.concurrent.Future
        final Sink<Integer, CompletionStage<Integer>> sink = Sink.<Integer, Integer> fold(0, (aggr, next) -> {
            System.out.println(aggr);
            return aggr + next;
        });

        // connect the Source to the Sink, obtaining a RunnableFlow
        final RunnableGraph<CompletionStage<Integer>> runnable = source.toMat(sink, Keep.right());
        final RunnableGraph<CompletionStage<IOResult>> fileRun = source.toMat(lineSink("basicMain1.txt"), Keep.right());
        CompletionStage<IOResult> completionStage = source.runWith(lineSink("basicMain2.txt"), mat);

        // materialize the flow;a stream can be materialized multiple times
        final CompletionStage<Integer> sum = runnable.run(mat);
        final CompletionStage<IOResult> fileSum = fileRun.run(mat);
        completionStage.thenRun(() -> system.terminate());
    }

    /**
     * There are various ways to wire up different parts of a stream, the following examples show some of the available
     * options.
     */
    private static void wireUpStream() {
        // Explicitly creating and wiring up a Source,Sink and Flow
        RunnableGraph<NotUsed> runnableGraph = Source.from(Arrays.asList(1, 2, 3, 4))
                .via(Flow.of(Integer.class).map(elem -> elem * 2)).to(Sink.foreach(System.out::println));

        // Starting from a Source
        final Source<Integer, NotUsed> source = Source.from(Arrays.asList(1, 2, 3, 4)).map(elem -> elem * 2);
        RunnableGraph<NotUsed> runnableGraph1 = source.to(Sink.foreach(System.out::println));

        // Starting from a Sink
        final Sink<Integer, NotUsed> sink = Flow.of(Integer.class).map(elem -> elem * 2)
                .to(Sink.foreach(System.out::println));
        RunnableGraph<NotUsed> runnableGraph2 = Source.from(Arrays.asList(1, 2, 3, 4)).to(sink);
        runnableGraph.run(mat);
        runnableGraph1.run(mat);
        runnableGraph2.run(mat);

        system.terminate();
    }

    public static void sourcePreMaterialization() {
        // #source-prematerialization
        Source<String, ActorRef> matValuePoweredSource = Source.actorRef(100, OverflowStrategy.fail());

        Pair<ActorRef, Source<String, NotUsed>> actorRefSourcePair = matValuePoweredSource.preMaterialize(mat);

        actorRefSourcePair.first().tell("Hello!", ActorRef.noSender());
        actorRefSourcePair.first().tell("Hello!", ActorRef.noSender());
//        Thread thread = new Thread(() -> {
//            while (true) {
//                actorRefSourcePair.first().tell("Hello!", ActorRef.noSender());
//                System.out.println(System.currentTimeMillis());
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        thread.start();
        // pass source around for materialization
        actorRefSourcePair.second().runWith(Sink.foreach(System.out::println), mat);
        // #source-prematerialization
    }

    private static void transformingMaterialized() throws Exception {

        Duration oneSecond = Duration.ofSeconds(1);
        Flow<Integer, Integer, Cancellable> throttler = Flow
                .fromGraph(GraphDSL.create(Source.tick(oneSecond, oneSecond, ""), (b, tickSource) -> {
                    FanInShape2<String, Integer, Integer> zip = b.add(ZipWith.create(Keep.right()));
                    b.from(tickSource).toInlet(zip.in0());
                    return FlowShape.of(zip.in1(), zip.out());
                }));

        // #flow-mat-combine

        // An empty source that can be shut down explicitly from the outside
        Source<Integer, CompletableFuture<Optional<Integer>>> source = Source.<Integer> maybe();

        // A flow that internally throttles elements to 1/second, and returns a Cancellable
        // which can be used to shut down the stream
        Flow<Integer, Integer, Cancellable> flow = throttler;

        // A sink that returns the first element of a stream in the returned Future
        Sink<Integer, CompletionStage<Integer>> sink = Sink.head();

        // By default, the materialized value of the leftmost stage is preserved
        RunnableGraph<CompletableFuture<Optional<Integer>>> r1 = source.via(flow).to(sink);

        // Simple selection of materialized values by using Keep.right
        RunnableGraph<Cancellable> r2 = source.viaMat(flow, Keep.right()).to(sink);
        RunnableGraph<CompletionStage<Integer>> r3 = source.via(flow).toMat(sink, Keep.right());

        // Using runWith will always give the materialized values of the stages added
        // by runWith() itself
        CompletionStage<Integer> r4 = source.via(flow).runWith(sink, mat);
        CompletableFuture<Optional<Integer>> r5 = flow.to(sink).runWith(source, mat);
        Pair<CompletableFuture<Optional<Integer>>, CompletionStage<Integer>> r6 = flow.runWith(source, sink, mat);

        // Using more complex combinations
        RunnableGraph<Pair<CompletableFuture<Optional<Integer>>, Cancellable>> r7 = source.viaMat(flow, Keep.both())
                .to(sink);

        RunnableGraph<Pair<CompletableFuture<Optional<Integer>>, CompletionStage<Integer>>> r8 = source.via(flow)
                .toMat(sink, Keep.both());

        RunnableGraph<Pair<Pair<CompletableFuture<Optional<Integer>>, Cancellable>, CompletionStage<Integer>>> r9 = source
                .viaMat(flow, Keep.both()).toMat(sink, Keep.both());

        RunnableGraph<Pair<Cancellable, CompletionStage<Integer>>> r10 = source.viaMat(flow, Keep.right()).toMat(sink,
                Keep.both());

        // It is also possible to map over the materialized values. In r9 we had a
        // doubly nested pair, but we want to flatten it out

        RunnableGraph<Cancellable> r11 = r9.mapMaterializedValue((nestedTuple) -> {
            CompletableFuture<Optional<Integer>> p = nestedTuple.first().first();
            Cancellable c = nestedTuple.first().second();
            CompletionStage<Integer> f = nestedTuple.second();

            // Picking the Cancellable, but we could also construct a domain class here
            return c;
        });
        // #flow-mat-combine
    }

    /**
     * Line sink sink.
     *
     * @param filename the filename
     * @return the sink
     */
    public static Sink<Integer, CompletionStage<IOResult>> lineSink(String filename) {
        return Flow.of(Integer.class).map(s -> ByteString.fromString(s.toString() + "\n"))
                .toMat(FileIO.toPath(Paths.get(filename)), Keep.right());
    }

    //#materializer-from-actor-context
    final class RunWithMyself extends AbstractActor {

        ActorMaterializer mat = ActorMaterializer.create(context());

        @Override
        public void preStart() throws Exception {
            Source
                    .repeat("hello")
                    .runWith(Sink.onComplete(tryDone -> {
                        System.out.println("Terminated stream: " + tryDone);
                    }), mat);
        }

        @Override
        public Receive createReceive() {
            return receiveBuilder().match(String.class, p -> {
                // this WILL terminate the above stream as well
                context().stop(self());
            }).build();
        }
    }
    //#materializer-from-actor-context

    //#materializer-from-system-in-actor
    final class RunForever extends AbstractActor {
        final ActorMaterializer mat;

        RunForever(ActorMaterializer mat) {
            this.mat = mat;
        }

        @Override
        public void preStart() throws Exception {
            Source
                    .repeat("hello")
                    .runWith(Sink.onComplete(tryDone -> {
                        System.out.println("Terminated stream: " + tryDone);
                    }), mat);
        }

        @Override
        public Receive createReceive() {
            return receiveBuilder().match(String.class, p -> {
                // will NOT terminate the stream (it's bound to the system!)
                context().stop(self());
            }).build();
        }
        //#materializer-from-system-in-actor

    }
}
