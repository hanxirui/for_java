package com.lightbend.akka.sample.stream;
//for test

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.util.ByteString;

import java.nio.file.Paths;
import java.math.BigInteger;
import java.time.Duration;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

//for stream
import akka.stream.*;
import akka.stream.javadsl.*;

public class Main {
    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("QuickStart");
        final Materializer materializer = ActorMaterializer.create(system);

        // The Source type is parameterized with two types: the first one is the type of element that this source emits
        // and the second one may signal that running the source produces some auxiliary value (e.g. a network source
        // may provide information about the bound port or the peer’s address). Where no auxiliary information is
        // produced, the type akka.NotUsed is used—and a simple range of integers surely falls into this category.
        final Source<Integer, NotUsed> source = Source.range(1, 10);

        // 1. sample have no terminate
        // source.runForeach(i -> System.out.println(i), materializer);

        // 2.sample have terminate
        // final CompletionStage<Done> done = source.runForeach(i -> System.out.println(i), materializer);
        // done.thenRun(() -> system.terminate());

        // 3.another example materializer to file
        final Source<BigInteger, NotUsed> factorials = source.scan(BigInteger.ONE, (acc, next) -> {
            return acc.multiply(BigInteger.valueOf(next));
        });

        // final CompletionStage<IOResult> result = factorials.map(num -> ByteString.fromString(num.toString() + "\n"))
        // .runWith(FileIO.toPath(Paths.get("factorials.txt")), materializer);
        // result.whenComplete((i,throwable)-> {System.out.println(i.count());});

        // 4 resuable pieces
        // final CompletionStage<IOResult> finalResult =
        // factorials.map(BigInteger::toString).runWith(lineSink("factorial2.txt"), materializer);

        // finalResult.thenRun(() -> system.terminate());

        // 5. Time-Based Processing
        factorials.zipWith(Source.range(0, 99), (num, idx) -> String.format("%d! = %s", idx, num))
                .throttle(1, Duration.ofSeconds(1)).runForeach(s -> System.out.println(s), materializer);

    }

    public static Sink<String, CompletionStage<IOResult>> lineSink(String filename) {
        return Flow.of(String.class).map(s -> ByteString.fromString(s + "\n")).toMat(FileIO.toPath(Paths.get(filename)),
                Keep.right());
    }
}
