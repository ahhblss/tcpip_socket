package com.lss.learn.coder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Sean on 2017/7/13.
 */
public class VoteService {

    private Map<Integer, Long> voteData = new ConcurrentHashMap<Integer, Long>();

    public VoteMsg handleRequest(VoteMsg voteMsg) {

        //已经是一个响应信息则直接返回
        if (voteMsg.isResponseFlag()) {
            return voteMsg;
        }

        voteMsg.setResponseFlag(true);

        int candidatedId = voteMsg.getCandidateId();
        Long voteCount = voteData.get(candidatedId);

        if (voteCount == null) {
            voteCount = 0l;
        }


        if (!voteMsg.isQueryFalg()) {
            voteCount++;
            voteData.put(candidatedId,voteCount);
        }

        voteMsg.setVoteCount(voteCount);
        return voteMsg;
    }

    public Map<Integer, Long> getVoteData() {
        return voteData;
    }

    public void setVoteData(Map<Integer, Long> voteData) {
        this.voteData = voteData;
    }
}
