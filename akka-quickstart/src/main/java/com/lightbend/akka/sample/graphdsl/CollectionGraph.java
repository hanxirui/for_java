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

public class CollectionGraph {
    public static void beAbleToConstructClosedGraphFromList() throws Exception {
        final ActorSystem system = ActorSystem.create("GraphDSL");
        final Materializer materializer = ActorMaterializer.create(system);
        // #graph-from-list
        // create the source
        final Source<String, NotUsed> in = Source.from(Arrays.asList("ax", "bx", "cx"));
        // generate the sinks from code
        List<String> prefixes = Arrays.asList("a", "b", "c");
        final List<Sink<String, CompletionStage<String>>> list = new ArrayList<>();
        for (String prefix : prefixes) {
            final Sink<String, CompletionStage<String>> sink = Flow.of(String.class)
                    .filter(str -> str.startsWith(prefix)).toMat(Sink.head(), Keep.right());
            list.add(sink);
        }

        final RunnableGraph<List<CompletionStage<String>>> g = RunnableGraph.fromGraph(GraphDSL.create(list,
                (GraphDSL.Builder<List<CompletionStage<String>>> builder, List<SinkShape<String>> outs) -> {


                    final UniformFanOutShape<String, String> bcast = builder.add(Broadcast.create(outs.size()));
                    final Outlet<String> source = builder.add(in).out();


                    builder.from(source).viaFanOut(bcast);

                    for (SinkShape<String> sink : outs) {
                        builder.from(bcast).to(sink);
                    }

                    return ClosedShape.getInstance();
                }));

        List<CompletionStage<String>> result = g.run(materializer);
        // #graph-from-list

        assertEquals(3, result.size());
        assertEquals("ax", result.get(0).toCompletableFuture().get(1, TimeUnit.SECONDS));
        assertEquals("bx", result.get(1).toCompletableFuture().get(1, TimeUnit.SECONDS));
        assertEquals("cx", result.get(2).toCompletableFuture().get(1, TimeUnit.SECONDS));

        result.get(0).thenRun(()->system.terminate());

    }

    public static void main(String[] args) {
        try {
            beAbleToConstructClosedGraphFromList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
