package com.lss.learn.inetaddress;

import org.junit.Test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * Created by Sean on 2017/7/12.
 */
public class InetAddressTest {

    @Test
    public void printLocalInetAddressTest(){
        try {
            Enumeration<NetworkInterface> networkInterfaceEnumeration =  NetworkInterface.getNetworkInterfaces();
            while (networkInterfaceEnumeration.hasMoreElements()){
                NetworkInterface networkInterface = networkInterfaceEnumeration.nextElement();
                System.out.print(networkInterface.getName());
                System.out.println(":");
                Enumeration<InetAddress> inetAddressEnumeration = networkInterface.getInetAddresses();
                while (inetAddressEnumeration.hasMoreElements()){
                    InetAddress inetAddress = inetAddressEnumeration.nextElement();
                    System.out.print(inetAddress.getHostAddress());
                    System.out.print(",");
                }
                System.out.println();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void getInetAdderssByHostName(){
        String hostName = "www.putiandi.com";
        try {
            InetAddress[] inetAddresses =  InetAddress.getAllByName(hostName);
            for (InetAddress inetAddress : inetAddresses){
                System.out.println(inetAddress.getHostName()+":"+inetAddress.getHostAddress());
            }
            System.out.println(InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
