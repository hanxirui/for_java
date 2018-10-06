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

public class SimpleGraph {
    public static void demonstrateBuildSimpleGraph() throws Exception {
        final ActorSystem system = ActorSystem.create("GraphDSL");
        final Materializer materializer = ActorMaterializer.create(system);

        // #simple-graph-dsl
        final Source<Integer, NotUsed> in = Source.from(Arrays.asList(1, 2, 3, 4, 5));
        final Sink<List<String>, CompletionStage<List<String>>> sink = Sink.head();
        final Flow<Integer, Integer, NotUsed> f1 = Flow.of(Integer.class).map(elem -> elem + 10);
        final Flow<Integer, Integer, NotUsed> f2 = Flow.of(Integer.class).map(elem -> elem + 20);
        final Flow<Integer, String, NotUsed> f3 = Flow.of(Integer.class).map(elem -> elem.toString());
        final Flow<Integer, Integer, NotUsed> f4 = Flow.of(Integer.class).map(elem -> elem + 30);
        // create() function binds sink, out which is sink's out port and builder DSL
        final RunnableGraph<CompletionStage<List<String>>> result = RunnableGraph.fromGraph(GraphDSL
                .create( // we need to reference out's shape in the builder DSL below (in to() function)
                        sink, // previously created sink (Sink)
                        (builder, out) -> { // variables: builder (GraphDSL.Builder) and out (SinkShape)
//                            final UniformFanOutShape<Integer, Integer> bcast = builder.add(Broadcast.create(2));
                            final UniformFanOutShape<Integer, Integer> balance = builder.add(Balance.create(2));
                            final UniformFanInShape<Integer, Integer> merge = builder.add(Merge.create(2));

                            final Outlet<Integer> source = builder.add(in).out();
                            builder.from(source).via(builder.add(f1)).viaFanOut(balance).via(builder.add(f2))
                                    .viaFanIn(merge).via(builder.add(f3.grouped(1000))).to(out); // to() expects a
                                                                                                 // SinkShape
                            builder.from(balance).via(builder.add(f4)).toFanIn(merge);
//                            builder.from(bcast).via(builder.add(f4)).viaFanIn(merge);
                            return ClosedShape.getInstance();
                        }));
        // #simple-graph-dsl
        // final List<String> list = result.run(materializer).toCompletableFuture().get(3, TimeUnit.SECONDS);
        CompletionStage<List<String>> completionStage = result.run(materializer);
        final List<String> list = completionStage.toCompletableFuture().get(3, TimeUnit.SECONDS);
        final String[] res = list.toArray(new String[] {});
        for (int i = 0; i < res.length; i++) {
            System.out.print(res[i]);
            if (i != res.length - 1) {
                System.out.print(",");
            } else {
                System.out.println();
            }
        }

        Arrays.sort(res, null);
        for (int i = 0; i < res.length; i++) {
            System.out.print(res[i]);
            if (i != res.length - 1) {
                System.out.print(",");
            } else {
                System.out.println();
            }
        }
        completionStage.thenRun(() -> system.terminate());
        // assertArrayEquals(new String[]{"31", "32", "33", "34", "35", "41", "42", "43", "44", "45"}, res);
    }

    public static void main(String[] args) {
        try {
            demonstrateBuildSimpleGraph();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
