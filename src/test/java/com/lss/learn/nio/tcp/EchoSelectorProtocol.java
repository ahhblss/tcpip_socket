package com.lss.learn.nio.tcp;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by Sean on 2017/7/14.
 */
public class EchoSelectorProtocol implements TCPProtocol {

    private int bufSize;

    public EchoSelectorProtocol(int bufSize) {
        this.bufSize = bufSize;
    }

    public void handleAccept(SelectionKey selectionKey) throws IOException {

        System.out.println("handle accept");

        SocketChannel socketChannel = ((ServerSocketChannel) selectionKey.channel()).accept();
        socketChannel.configureBlocking(false);
        //在 OP_ACCEPT 到来时, 再将这个 Channel 的 OP_READ 注册到 Selector 中.
        // 注意, 这里我们如果没有设置 OP_READ 的话, 即 interest set 仍然是 OP_CONNECT 的话, 那么 select 方法会一直直接返回.
        socketChannel.register(selectionKey.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(bufSize));

    }

    public void handleRead(SelectionKey selectionKey) throws IOException {

        System.out.println("handle read");

        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
        long byteRead = socketChannel.read(buffer);
        if (byteRead == -1) {
            socketChannel.close();
        } else if (byteRead > 0) {
            selectionKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        }

    }

    public void handleWrite(SelectionKey selectionKey) throws IOException {

        System.out.println("handle write");

        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
        buffer.flip();

        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        socketChannel.write(buffer);

        if (!buffer.hasRemaining()) {
            selectionKey.interestOps(SelectionKey.OP_READ);
        }

        buffer.compact();
    }
}
