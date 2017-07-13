package com.lss.learn.coder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Sean on 2017/7/13.
 */
public class VoteClientTcp {
    public static final String host = "127.0.0.1";
    public static final int port = 6666;

    public static void main(String[] args) {

        try {
            Socket socket = new Socket(host, port);
            System.out.println("connected to server");

            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            VoteMsgBinCoder coder = new VoteMsgBinCoder();
            //创建查询消息
            VoteMsg queryMsg = new VoteMsg(true,false,1,0);
            byte[] message = coder.encode(queryMsg);

            LengthFramer lengthFramer = new LengthFramer(is);
            lengthFramer.frameMsg(message,os);


            //创建投票消息
            queryMsg.setQueryFalg(false);
            message = coder.encode(queryMsg);
            lengthFramer.frameMsg(message,os);

            //接受查询结果
            message = lengthFramer.nextMsg();
            queryMsg = coder.decode(message);
            System.out.println(queryMsg);

            //接收投票结果
            message = lengthFramer.nextMsg();
            queryMsg = coder.decode(message);
            System.out.println(queryMsg);

            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
