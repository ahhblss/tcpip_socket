package com.lss.learn.coder;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Created by Sean on 2017/7/12.
 */
public class VoteMsgTextCoder implements VoteMsgCoder {

    public static final String MAGIC = "voting";
    public static final String VOTE = "v";
    public static final String QUERY = "q";
    public static final String RESPONSER = "r";
    public static final String CHARTSETNAME = "UTF-8";
    public static final String DELEMETER = " ";
    public static final int MAX_WIRE_LENGTH = 2000;


    /**
     * wireFormat: "voteproto" <"v"|"q"> [responseflag] [candidatedId] [votecount]
     *
     * @param voteMsg
     * @return
     * @throws IOException
     */
    public byte[] encode(VoteMsg voteMsg) throws IOException {
        StringBuffer sb = new StringBuffer(MAGIC);
        sb.append(DELEMETER);
        sb.append(voteMsg.isQueryFalg() ? QUERY : VOTE).append(DELEMETER);
        sb.append(voteMsg.isResponseFlag() ? RESPONSER + DELEMETER : "");
        sb.append(voteMsg.getCandidateId()).append(DELEMETER);
        sb.append(voteMsg.getVoteCount());
        return sb.toString().getBytes(CHARTSETNAME);
    }

    public VoteMsg decode(byte[] data) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        Scanner scanner = new Scanner(new InputStreamReader(bais, CHARTSETNAME));

        boolean queryFalg;
        boolean responseFlag;
        int candidateId;//[0-1000]
        long voteCount;//none zero
        String token;

        token = scanner.next();
        if (!MAGIC.equalsIgnoreCase(token)) {
            throw new IOException("bad magic:" + token);
        }

        token = scanner.next();
        if (VOTE.equalsIgnoreCase(token)) {
            queryFalg = false;
        } else if (QUERY.equalsIgnoreCase(token)) {
            queryFalg = true;
        } else {
            throw new IOException("bad operation flag" + token);
        }

        token = scanner.next();
        if (RESPONSER.equalsIgnoreCase(token)) {
            responseFlag = true;
            token = scanner.next();
        } else {
            responseFlag = false;
        }

        candidateId = Integer.parseInt(token);

        if (responseFlag) {
            token = scanner.next();
            voteCount = Long.parseLong(token);
        } else {
            voteCount = 0;
        }

        return new VoteMsg(queryFalg,responseFlag,candidateId,voteCount);
    }
}
