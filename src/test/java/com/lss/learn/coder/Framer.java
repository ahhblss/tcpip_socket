package com.lss.learn.coder;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Sean on 2017/7/13.
 */
public interface Framer {

    void frameMsg(byte[] message, OutputStream outputStream) throws IOException;

    byte[] nextMsg() throws IOException;
}
