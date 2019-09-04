package com.lightbend.akka.sample.streamcomposition;

import akka.NotUsed;
import akka.stream.Attributes;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;

import akka.stream.javadsl.*;

public class Attributez {
    public void attributes() throws Exception {
        //#attributes-inheritance
        final Source<Integer, NotUsed> nestedSource =
                Source.single(0)
                        .map(i -> i + 1)
                        .named("nestedSource"); // Wrap, no inputBuffer set

        final Flow<Integer, Integer, NotUsed> nestedFlow =
                Flow.of(Integer.class).filter(i -> i != 0)
                        .via(Flow.of(Integer.class)
                                .map(i -> i - 2)
                                .withAttributes(Attributes.inputBuffer(4, 4))) // override
                        .named("nestedFlow"); // Wrap, no inputBuffer set

        final Sink<Integer, NotUsed> nestedSink =
                nestedFlow.to(Sink.fold(0, (acc, i) -> acc + i)) // wire an atomic sink to the nestedFlow
                        .withAttributes(Attributes.name("nestedSink")
                                .and(Attributes.inputBuffer(3, 3))); // override
        //#attributes-inheritance
    }
}
