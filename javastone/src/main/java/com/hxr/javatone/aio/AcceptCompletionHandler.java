package com.hxr.javatone.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AsyncTimeServerHandler> {

    @Override
    public void completed(final AsynchronousSocketChannel result, final AsyncTimeServerHandler attachment) {
        attachment.serverChannel.accept(attachment, this);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        result.read(buffer, buffer, new ReadCompletionHandler(result));

    }

    @Override
    public void failed(final Throwable exc, final AsyncTimeServerHandler attachment) {
        attachment.latch.countDown();
    }

}
