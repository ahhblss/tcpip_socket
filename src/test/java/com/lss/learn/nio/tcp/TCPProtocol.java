package com.lss.learn.nio.tcp;

import java.io.IOException;
import java.nio.channels.SelectionKey;

/**
 * Created by Sean on 2017/7/14.
 */
public interface TCPProtocol {

    void handleAccept(SelectionKey selectionKey) throws IOException;

    void handleRead(SelectionKey selectionKey) throws IOException;

    void handleWrite(SelectionKey selectionKey) throws IOException;
}
