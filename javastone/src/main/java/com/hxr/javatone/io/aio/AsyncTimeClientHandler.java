package com.hxr.javatone.io.aio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

public class AsyncTimeClientHandler implements CompletionHandler<Void, AsyncTimeClientHandler>, Runnable {

    private AsynchronousSocketChannel client;
    private final String host;
    private final int port;
    private CountDownLatch latch;

    public AsyncTimeClientHandler( final String host,  final int port) {
        this.host = host;
        this.port = port;

        try {
            client = AsynchronousSocketChannel.open();
        } catch (Exception t_e) {
            // TODO: handle exception
        }
    }

    @Override
    public void run() {
        latch = new CountDownLatch(1);
        client.connect(new InetSocketAddress(host, port), this, this);
        try {
            latch.await();
            client.close();
        } catch (Exception t_e) {
            // TODO: handle exception
        }

    }

    @Override
    public void completed(final Void result, final AsyncTimeClientHandler attachment) {
        byte[] req = "QUERY TIME ORDER".getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
        writeBuffer.put(req);
        writeBuffer.flip();
        client.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {

            @Override
            public void completed(final Integer result, final ByteBuffer attachment) {
                if (attachment.hasRemaining()) {
                    client.write(attachment, attachment, this);
                } else {
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    client.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {

                        @Override
                        public void completed(final Integer result, final ByteBuffer attachment) {
                            attachment.flip();
                            byte[] bytes = new byte[attachment.remaining()];
                            attachment.get(bytes);
                            String body;
                            try {
                                body = new String(bytes, "UTF-8");
                                System.out.println("Now is:" + body);
                                latch.countDown();
                            } catch (Exception t_e) {
                                // TODO: handle exception
                            }

                        }

                        @Override
                        public void failed(final Throwable exc, final ByteBuffer attachment) {
                            // TODO Auto-generated method stub

                        }

                    });
                }
            }

            @Override
            public void failed(final Throwable exc, final ByteBuffer attachment) {
                // TODO Auto-generated method stub

            }

        });
    }

    @Override
    public void failed(final Throwable exc, final AsyncTimeClientHandler attachment) {
        // TODO Auto-generated method stub

    }

}
