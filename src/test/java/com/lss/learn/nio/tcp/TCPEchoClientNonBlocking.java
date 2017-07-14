package com.lss.learn.nio.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by Sean on 2017/7/14.
 */
public class TCPEchoClientNonBlocking {

    public static final String host = "127.0.0.1";

    public static final int port = 5566;

    public static final String msg = "hello nio";

    public static void main(String[] args) {

        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            if (!socketChannel.connect(new InetSocketAddress(host, port))) {
                while (!socketChannel.finishConnect()) {
                    System.out.println("waiting for connection finished");
                }
            }
            System.out.println("connected server successfully");

            byte[] data = msg.getBytes();
            ByteBuffer writeBuf = ByteBuffer.wrap(data);
            ByteBuffer readbuf = ByteBuffer.allocate(data.length);

            int totalByteReceive = 0;
            int byteReceive = 0;

            while (totalByteReceive < data.length) {

                if (writeBuf.hasRemaining()) {
                    socketChannel.write(writeBuf);
                }

                if ((byteReceive = socketChannel.read(readbuf)) == -1) {
                    throw new SocketException("connection closed unexpected");
                }

                totalByteReceive += byteReceive;
                System.out.print(".");
            }

            System.out.println("receive:" + new String(readbuf.array()));
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
