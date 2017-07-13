package com.lss.learn.coder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Sean on 2017/7/13.
 */
public class VoteServerTcp {
    public static final int port = VoteClientTcp.port;
    public static final VoteService voteService = new VoteService();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Socket socket;
            while (true) {
                socket = serverSocket.accept();
                System.out.println(Thread.currentThread().getId());
                InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();

                VoteMsgBinCoder coder = new VoteMsgBinCoder();
                LengthFramer lengthFramer = new LengthFramer(is);

                //处理消息
                try {
                    byte[] message;
                    while ((message = lengthFramer.nextMsg()) != null) {
                        VoteMsg voteMsg = coder.decode(message);
                        voteMsg = voteService.handleRequest(voteMsg);
                        message = coder.encode(voteMsg);
                        lengthFramer.frameMsg(message, os);
                    }
                } catch (IOException e) {
                    System.out.println("error handle message");
                    e.printStackTrace();
                } finally {
                    System.out.println("close connection");
                    socket.close();
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
