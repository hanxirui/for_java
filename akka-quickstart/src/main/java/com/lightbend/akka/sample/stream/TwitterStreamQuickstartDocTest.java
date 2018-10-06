package com.lightbend.akka.sample.stream;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.japi.JavaPartialFunction;
//#imports
import akka.stream.*;
import akka.stream.javadsl.*;
import akka.testkit.javadsl.TestKit;
import akka.util.ByteString;
//#imports

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class TwitterStreamQuickstartDocTest {

    private static final long serialVersionUID = 1L;

    public static ActorSystem system;

    public static Materializer mat;

    // #model

    public static final Hashtag AKKA = new Hashtag("#akka");
    // #model

    public static final Source<Tweet, NotUsed> tweets = Source.from(
            Arrays.asList(new Tweet[] { new Tweet(new Author("rolandkuhn"), System.currentTimeMillis(), "#akka rocks!"),
                    new Tweet(new Author("patriknw"), System.currentTimeMillis(), "#akka !"),
                    new Tweet(new Author("bantonsson"), System.currentTimeMillis(), "#akka !"),
                    new Tweet(new Author("drewhk"), System.currentTimeMillis(), "#akka !"),
                    new Tweet(new Author("ktosopl"), System.currentTimeMillis(), "#akka on the rocks!"),
                    new Tweet(new Author("mmartynas"), System.currentTimeMillis(), "wow #akka !"),
                    new Tweet(new Author("akkateam"), System.currentTimeMillis(), "#akka rocks!"),
                    new Tweet(new Author("bananaman"), System.currentTimeMillis(), "#bananas rock!"),
                    new Tweet(new Author("appleman"), System.currentTimeMillis(), "#apples rock!"),
                    new Tweet(new Author("drama"), System.currentTimeMillis(), "we compared #apples to #oranges!") }));

    public TwitterStreamQuickstartDocTest() {
        system = ActorSystem.create("TwitterStreamQuickstartDocTest");
        mat = ActorMaterializer.create(system);
    }

    // public static void setup() {
    //
    // //#first-sample
    // //#materializer-setup
    // final ActorSystem system = ActorSystem.create("reactive-tweets");
    // final Materializer mat = ActorMaterializer.create(system);
    // //#first-sample
    // //#materializer-setup
    // }
    //
    // public static void tearDown() {
    // system.terminate();
    // system = null;
    // mat = null;
    // }

    // #model

    public void demonstrateFilterAndMap() {

        // #first-sample

        // #authors-filter-map
        final Source<Author, NotUsed> authors = tweets.filter(t -> t.hashtags().contains(AKKA)).map(t -> t.author);
        // #first-sample
        // #authors-filter-map

        // #authors-foreach-println
        authors.runForeach(a -> System.out.println(a), mat);
        // #authors-foreach-println
    }

    public void demonstrateMapConcat() {
        // #hashtags-mapConcat
        final Source<Hashtag, NotUsed> hashtags = tweets.mapConcat(t -> new ArrayList<Hashtag>(t.hashtags()));
        // #hashtags-mapConcat
        hashtags.runForeach(a -> System.out.println(a.name), mat);

    }

    public void demonstrateBroadcast() {
        final Sink<Author, CompletionStage<IOResult>> writeAuthors = authorSink("author");
        final Sink<Hashtag, CompletionStage<IOResult>> writeHashtags = lineSink("hashtag");

        // #graph-dsl-broadcast
        RunnableGraph.fromGraph(GraphDSL.create(b -> {
            final UniformFanOutShape<Tweet, Tweet> bcast = b.add(Broadcast.create(2));
            final FlowShape<Tweet, Author> toAuthor = b.add(Flow.of(Tweet.class).map(t -> t.author));
            final FlowShape<Tweet, Hashtag> toTags = b
                    .add(Flow.of(Tweet.class).mapConcat(t -> new ArrayList<Hashtag>(t.hashtags())));
            final SinkShape<Author> authors = b.add(writeAuthors);
            final SinkShape<Hashtag> hashtags = b.add(writeHashtags);

            b.from(b.add(tweets)).viaFanOut(bcast).via(toAuthor).to(authors);
            b.from(bcast).via(toTags).to(hashtags);
            return ClosedShape.getInstance();
        })).run(mat);
        // #graph-dsl-broadcast
    }

    public Sink<Author, CompletionStage<IOResult>> authorSink(String filename) {
        return Flow.of(Author.class).map(a -> ByteString.fromString(a.handle.toString() + "\n"))
                .toMat(FileIO.toPath(Paths.get(filename)), Keep.right());
    }

    public Sink<Hashtag, CompletionStage<IOResult>> lineSink(String filename) {
        return Flow.of(Hashtag.class).map(s -> ByteString.fromString(s.toString() + "\n"))
                .toMat(FileIO.toPath(Paths.get(filename)), Keep.right());
    }

    long slowComputation(Tweet t) {
        try {
            // act as if performing some heavy computation
            Thread.sleep(5000);
            System.out.println(t.author.handle);
        } catch (InterruptedException e) {
        }
        return 42;
    }

    public void demonstrateSlowProcessing() {
        // #tweets-slow-consumption-dropHead
        tweets.buffer(5, OverflowStrategy.dropHead()).map(t -> slowComputation(t)).runWith(Sink.ignore(), mat);
        // #tweets-slow-consumption-dropHead
    }

    public void demonstrateCountOnFiniteStream() {
        // #tweets-fold-count
        final Sink<Integer, CompletionStage<Integer>> sumSink = Sink.<Integer, Integer> fold(0,
                (acc, elem) -> acc + elem);

        final RunnableGraph<CompletionStage<Integer>> counter = tweets.map(t -> {
            System.out.println(t.body);
            return t;
        }).map(t -> 1).toMat(sumSink, Keep.right());

        final CompletionStage<Integer> sum = counter.run(mat);

        sum.thenAcceptAsync(c -> System.out.println("Total tweets processed: " + c), system.dispatcher());
        // #tweets-fold-count

        new Object() {
            // #tweets-fold-count-oneline
            final CompletionStage<Integer> sum = tweets.map(t -> 1).runWith(sumSink, mat);
            // #tweets-fold-count-oneline
        };
    }

    public void demonstrateMaterializeMultipleTimes() {
        // not really in second, just acting as if
        final Source<Tweet, NotUsed> tweetsInMinuteFromNow = tweets;

        // #tweets-runnable-flow-materialized-twice
        final Sink<Integer, CompletionStage<Integer>> sumSink = Sink.<Integer, Integer> fold(0,
                (acc, elem) -> acc + elem);
        final RunnableGraph<CompletionStage<Integer>> counterRunnableGraph = tweetsInMinuteFromNow
                .filter(t -> t.hashtags().contains(AKKA)).map(t -> 1).toMat(sumSink, Keep.right());

        // materialize the stream once in the morning
        final CompletionStage<Integer> morningTweetsCount = counterRunnableGraph.run(mat);
        // and once in the evening, reusing the blueprint
        final CompletionStage<Integer> eveningTweetsCount = counterRunnableGraph.run(mat);
        // #tweets-runnable-flow-materialized-twice

    }

    public static void main(String[] args) {

        TwitterStreamQuickstartDocTest test = new TwitterStreamQuickstartDocTest();
        // test.demonstrateFilterAndMap();
        // test.demonstrateMapConcat();
        // test.demonstrateBroadcast();
        // test.demonstrateSlowProcessing();
        // test.demonstrateCountOnFiniteStream();
        test.demonstrateMaterializeMultipleTimes();
        TestKit.shutdownActorSystem(system);
        system = null;
        mat = null;
    }

}

// #model
class Author {
    public final String handle;

    public Author(String handle) {
        this.handle = handle;
    }

    // ...

    // #model

    @Override
    public String toString() {
        return "Author(" + handle + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Author author = (Author) o;

        if (handle != null ? !handle.equals(author.handle) : author.handle != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return handle != null ? handle.hashCode() : 0;
    }
    // #model
}
// #model

// #model
class Hashtag {
    public final String name;

    public Hashtag(String name) {
        this.name = name;
    }

    // ...
    // #model

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Hashtag other = (Hashtag) obj;
        return name.equals(other.name);
    }

    @Override
    public String toString() {
        return "Hashtag(" + name + ")";
    }
    // #model
}
// #model

// #model
class Tweet {
    public final Author author;
    public final long timestamp;
    public final String body;

    public Tweet(Author author, long timestamp, String body) {
        this.author = author;
        this.timestamp = timestamp;
        this.body = body;
    }

    public Set<Hashtag> hashtags() {
        return Arrays.asList(body.split(" ")).stream().filter(a -> a.startsWith("#")).map(a -> new Hashtag(a))
                .collect(Collectors.toSet());
    }

    // ...
    // #model

    @Override
    public String toString() {
        return "Tweet(" + author + "," + timestamp + "," + body + ")";
    }

    // #model
}