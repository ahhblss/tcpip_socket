package com.lss.learn.coder;

import java.io.*;

/**
 * Created by Sean on 2017/7/13.
 * &BYTEMASK : 去掉高位数，只保留最低8位
 */
public class LengthFramer implements Framer {
    public static final int MAXMESSAGELENGTH = 65535;
    public static final int BYTEMASK = 0xff;        //    11111111
    public static final int SHORTMASK = 0xffff;//11111111 11111111
    public static final int BYTESHIFT = 8;

    private DataInputStream dis;

    public LengthFramer(InputStream dis) {
        this.dis = new DataInputStream(dis);
    }

    public void frameMsg(byte[] message, OutputStream outputStream) throws IOException {
        if (message.length > MAXMESSAGELENGTH) {
            throw new IOException("message too long");
        }

        outputStream.write((message.length >> BYTESHIFT) & BYTEMASK);
        outputStream.write(message.length & BYTEMASK);
        outputStream.write(message);
        outputStream.flush();
    }

    public byte[] nextMsg() throws IOException {
        int length;

        try {
            length = dis.readUnsignedShort();
        } catch (EOFException e) {
            e.printStackTrace();
            return null;
        }

        byte[] message = new byte[length];
        dis.readFully(message);

        return message;
    }
}
