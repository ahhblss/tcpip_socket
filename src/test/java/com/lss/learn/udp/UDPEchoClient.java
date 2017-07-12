package com.lss.learn.udp;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.*;

/**
 * Created by Sean on 2017/7/12.
 */
public class UDPEchoClient {
    public static final String host = "127.0.0.1";
    public static final int port = 5562;
    public static final String msg = "hello server";

    public static void main(String[] args) {
        try {
            byte[] data = msg.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(data, data.length);
            sendPacket.setAddress(InetAddress.getByName(host));
            sendPacket.setPort(port);

            DatagramSocket clientSocket = new DatagramSocket();
            clientSocket.setSoTimeout(3000);
            clientSocket.send(sendPacket);
            System.out.println("send msg success");

            byte[] receiveData = new byte[sendPacket.getLength()];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            int tryTimes = 0;
            boolean isResponse = false;
            do {
                try {
                    clientSocket.receive(receivePacket);
                    if (!host.equalsIgnoreCase(receivePacket.getAddress().getHostAddress())) {
                        System.out.println("receive from unknowen address");
                        break;
                    }
                } catch (InterruptedIOException e) {
                    e.printStackTrace();
                    tryTimes++;
                    System.out.println("try more times");
                }
                isResponse = true;
            } while (!isResponse && tryTimes < 3);

            if (isResponse) {
                System.out.println("receive:" + new String(receivePacket.getData()));
            } else {
                System.out.println("receive nothing");
            }

            clientSocket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
