package com.lss.learn.coder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Sean on 2017/7/13.
 */
public class DelimFramer implements Framer {

    private InputStream inputStream;
    private static final byte DELIMETER = '\n';

    public DelimFramer(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void frameMsg(byte[] message, OutputStream outputStream) throws IOException {
        for (byte field : message){
            if (field == DELIMETER){
                throw new IOException("content has delemeter:\n");
            }
        }

        outputStream.write(message);
        outputStream.write(DELIMETER);
        outputStream.flush();
    }

    public byte[] nextMsg() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int nextByte;

        //read Reads the next byte of data from the input stream
        while ((nextByte=inputStream.read()) != DELIMETER){
            if (nextByte ==-1){
                if (baos.size() == 0){
                    return null;
                }
                else {
                    throw new IOException("found no delemeter");
                }
            }
            baos.write(nextByte);
        }

        return baos.toByteArray();
    }
}
