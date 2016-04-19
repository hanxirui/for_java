package com.hxr.javatone.io.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

    private final AsynchronousSocketChannel channel;

    public ReadCompletionHandler(final AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public void completed(final Integer result, final ByteBuffer attachment) {
        attachment.flip();

        byte[] body = new byte[attachment.remaining()];
        attachment.get(body);
        try {
            String req = new String(body, "UTF-8");
            System.out.println("The time server receive order: " + req);
            String currentTime =
                "QUERY TIME ORDER".equalsIgnoreCase(req) ? new java.util.Date(System.currentTimeMillis()).toString()
                    : "BAD ORDER";

            doWrite(currentTime);
        } catch (Exception t_e) {
            // TODO: handle exception
        }

    }

    @Override
    public void failed(final Throwable exc, final ByteBuffer attachment) {

        try {
            this.channel.close();
        } catch (Exception t_e) {
            // TODO: handle exception
        }

    }

    private void doWrite(final String currentTime) {
        if (currentTime != null && currentTime.trim().length() > 0) {
            byte[] bytes = (currentTime).getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            channel.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(final Integer result, final ByteBuffer buffer) {
                    // 如果没有发送完成，继续发送
                    if (buffer.hasRemaining()) {
                        channel.write(buffer, buffer, this);
                    }
                }

                @Override
                public void failed(final Throwable exc, final ByteBuffer attachment) {
                    try {
                        channel.close();
                    } catch (Exception t_e) {
                        // TODO: handle exception
                    }
                }
            });
        }
    }

}
