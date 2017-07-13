package com.lss.learn.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Sean on 2017/7/12.
 */
public class TCPEchoServer {

    private static final int port = TCPEchoClient.port;
    private static final int msgLength = TCPEchoClient.msg.getBytes().length;

    public static void main(String[] args) {
        byte[] data = new byte[msgLength];
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Socket socket = null;
            while (true) {
                socket = serverSocket.accept();
                System.out.println("remote:"+socket.getRemoteSocketAddress());
                InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();
                int byteReceive = 0;
                do {
                    //This method blocks until input data is available
                    byteReceive = is.read(data);
                    if (byteReceive !=-1){
                        os.write(data,0,byteReceive);
                    }
                }while (byteReceive !=-1);

                socket.close();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
