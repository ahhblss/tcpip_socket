package com.lss.learn.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Sean on 2017/7/12.
 */
public class TCPEchoClientTwo {
    public static final String host ="127.0.0.1";
    public static final int port = 5555;
    public static final String msg = "hello server";

    public static void main(String[] args) {
        try {
            Socket clientSocket = new Socket(host,port);
            System.out.println("connected to server");

            InputStream is = clientSocket.getInputStream();
            OutputStream os = clientSocket.getOutputStream();

            byte[] data = msg.getBytes();
            os.write(data);
            os.flush();


            int totalByteReceive = 0;
            int byteReceive = 0;

            do {
                byteReceive = is.read(data,totalByteReceive,data.length-totalByteReceive);
                if (byteReceive == -1){
                    System.out.println("connection closed");
                    break;
                }
                totalByteReceive+=byteReceive;
            }while (totalByteReceive < data.length);

            System.out.println("receive:"+new String(data));

            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
