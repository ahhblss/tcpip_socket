package com.lss.learn.coder;

import java.io.*;

/**
 * Created by Sean on 2017/7/13.
 */
public class VoteMsgBinCoder implements VoteMsgCoder {

    public static final int MIN_WIRE_LENGTH = 4;
    public static final int MAX_WIRE_LENGTH = 16;
    public static final int MAGIC = 0x5400;//           01010100  00000000
    public static final int MAGIC_MASK = 0xfc00;//      11111100  00000000
    public static final int MAGIC_SHIFT = 8;
    public static final int RESPONSE_FLAG = 0x0200;//   00000010  00000000
    public static final int QUERY_FLAG = 0x0100;//     00000001  00000000

    public byte[] encode(VoteMsg voteMsg) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        short magicAndFlags = MAGIC;
        if (voteMsg.isQueryFalg()) {
            magicAndFlags |= QUERY_FLAG;
        }
        if (voteMsg.isResponseFlag()) {
            magicAndFlags |= RESPONSE_FLAG;
        }

        dos.writeShort(magicAndFlags);

        dos.writeShort((short) voteMsg.getCandidateId());

        if (voteMsg.isResponseFlag()) {
            dos.writeLong(voteMsg.getVoteCount());
        }

        dos.flush();

        byte[] data = baos.toByteArray();
        return data;
    }

    public VoteMsg decode(byte[] data) throws IOException {
        if (data.length < MIN_WIRE_LENGTH) {
            throw new IOException("bad data");
        }

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(byteArrayInputStream);

        int magic = dis.readShort();
        if ((magic & MAGIC_MASK) != MAGIC) {
            throw new IOException("bad magic");
        }

        boolean queryFlag = (magic & QUERY_FLAG) != 0;
        boolean responseFlag = (magic & RESPONSE_FLAG) != 0;

        int candidateId = dis.readShort();
        if (candidateId <0 || candidateId > 1000){
            throw new IOException("bad candidatedId"+candidateId);
        }

        long voteCount = 0;
        if (responseFlag){
            voteCount = dis.readLong();
            if (voteCount<0){
                throw new IOException("bad voteCount:"+voteCount);
            }
        }

        return new VoteMsg(queryFlag,responseFlag,candidateId,voteCount);
    }
}
