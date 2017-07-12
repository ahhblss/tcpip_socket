package com.lss.learn.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by Sean on 2017/7/12.
 */
public class UDPEchoServer {
    public static final int port = UDPEchoClient.port;
    public static final int maxLength = 255;

    public static void main(String[] args) {
        DatagramPacket datagramPacket = new DatagramPacket(new byte[maxLength],maxLength);
        try {
            DatagramSocket serverSocket = new DatagramSocket(port);
            while (true){
                serverSocket.receive(datagramPacket);
                System.out.println("receive:"+new String(datagramPacket.getData()));
                serverSocket.send(datagramPacket);
                datagramPacket.setLength(maxLength);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
