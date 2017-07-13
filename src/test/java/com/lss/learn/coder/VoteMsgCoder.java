package com.lss.learn.coder;

import java.io.IOException;

/**
 * Created by Sean on 2017/7/12.
 */
public interface VoteMsgCoder {
    
    byte[] encode(VoteMsg voteMsg) throws IOException;

    VoteMsg decode(byte[] data) throws IOException;
}
