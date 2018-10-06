package com.lightbend.akka.sample.bidirectional;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import akka.NotUsed;
import akka.stream.javadsl.GraphDSL;
import akka.testkit.javadsl.TestKit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import akka.actor.ActorSystem;
import akka.japi.pf.PFBuilder;
import akka.stream.*;
import akka.stream.javadsl.*;
import akka.stream.stage.*;
import akka.util.ByteIterator;
import akka.util.ByteString;
import akka.util.ByteStringBuilder;
import static org.junit.Assert.assertArrayEquals;

public class CodecDemo {
    static interface Message {}
    static class Ping implements Message {
        final int id;
        public Ping(int id) { this.id = id; }
        @Override
        public boolean equals(Object o) {
            if (o instanceof Ping) {
                return ((Ping) o).id == id;
            } else {
                return false;
            }
        }
        @Override
        public int hashCode() {
            return id;
        }
    }
    static class Pong implements Message {
        final int id;
        public Pong(int id) { this.id = id; }
        @Override
        public boolean equals(Object o) {
            if (o instanceof Pong) {
                return ((Pong) o).id == id;
            } else{
                return false;
            }
        }
        @Override
        public int hashCode() {
            return id;
        }
    }

    //#codec-impl
    public static ByteString toBytes(Message msg) {
        //#implementation-details-elided
        if (msg instanceof Ping) {
            final int id = ((Ping) msg).id;
            return new ByteStringBuilder().putByte((byte) 1)
                    .putInt(id, ByteOrder.LITTLE_ENDIAN).result();
        } else {
            final int id = ((Pong) msg).id;
            return new ByteStringBuilder().putByte((byte) 2)
                    .putInt(id, ByteOrder.LITTLE_ENDIAN).result();
        }
        //#implementation-details-elided
    }

    public static Message fromBytes(ByteString bytes) {
        //#implementation-details-elided
        final ByteIterator it = bytes.iterator();
        switch(it.getByte()) {
            case 1:
                return new Ping(it.getInt(ByteOrder.LITTLE_ENDIAN));
            case 2:
                return new Pong(it.getInt(ByteOrder.LITTLE_ENDIAN));
            default:
                throw new RuntimeException("message format error");
        }
        //#implementation-details-elided
    }
    //#codec-impl

    //#codec
    @SuppressWarnings("unused")
    //#codec
    public final BidiFlow<Message, ByteString, ByteString, Message, NotUsed> codecVerbose =
            BidiFlow.fromGraph(GraphDSL.create(b -> {
                final FlowShape<Message, ByteString> top =
                        b.add(Flow.of(Message.class).map(CodecDemo::toBytes));
                final FlowShape<ByteString, Message> bottom =
                        b.add(Flow.of(ByteString.class).map(CodecDemo::fromBytes));
                return BidiShape.fromFlows(top, bottom);
            }));

    public final BidiFlow<Message, ByteString, ByteString, Message, NotUsed> codec =
            BidiFlow.fromFunctions(CodecDemo::toBytes, CodecDemo::fromBytes);
    //#codec
}
