package com.lss.learn.nio.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

/**
 * Created by Sean on 2017/7/14.
 */
public class TCPEchoServerNonBlocking {

    public static final int port = TCPEchoClientNonBlocking.port;
    public static final int BUFFSIZE = 256;
    public static final int TIMEOUT = 3000;

    public static void main(String[] args) {

        try {
            Selector selector = Selector.open();

            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);

            // 将 channel 注册到 selector 中.
            // 通常我们都是先注册一个 OP_ACCEPT 事件, 然后在 OP_ACCEPT 到来时, 再将这个 Channel 的 OP_READ
            // 注册到 Selector 中.
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            TCPProtocol tcpProtocol = new EchoSelectorProtocol(BUFFSIZE);

            while (true) {
                // 通过调用 select 方法, 阻塞地等待 channel I/O 可操作
                if (selector.select(TIMEOUT) == 0) {
                    System.out.print(".");
                    continue;
                }

                // 获取 I/O 操作就绪的 SelectionKey, 通过 SelectionKey 可以知道哪些 Channel 的哪类 I/O 操作已经就绪
                Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
                while (selectionKeyIterator.hasNext()) {
                    SelectionKey selectionKey = selectionKeyIterator.next();

                    // 当 OP_ACCEPT 事件到来时, 我们就有从 ServerSocketChannel 中获取一个 SocketChannel,
                    // 代表客户端的连接
                    // 注意, 在 OP_ACCEPT 事件中, 从 key.channel() 返回的 Channel 是 ServerSocketChannel.
                    // 而在 OP_WRITE 和 OP_READ 中, 从 key.channel() 返回的是 SocketChannel.
                    if (selectionKey.isAcceptable()) {
                        tcpProtocol.handleAccept(selectionKey);
                    }

                    if (selectionKey.isReadable()) {
                        tcpProtocol.handleRead(selectionKey);
                    }

                    if (selectionKey.isValid() && selectionKey.isWritable()) {
                        tcpProtocol.handleWrite(selectionKey);
                    }

                    // 当获取一个 SelectionKey 后, 就要将它删除, 表示我们已经对这个 IO 事件进行了处理
                    selectionKeyIterator.remove();

                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
