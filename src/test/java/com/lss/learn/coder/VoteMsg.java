package com.lss.learn.coder;

/**
 * Created by Sean on 2017/7/12.
 */
public class VoteMsg {

    private boolean queryFalg;
    private boolean responseFlag;
    private int candidateId;//[0-1000]
    private long voteCount;//none zero

    private static final int MAX_CANDIDATED_ID = 1000;

    public VoteMsg(boolean queryFalg, boolean responseFlag, int candidateId, long voteCount) {
        if (voteCount != 0 && !responseFlag) {
            throw new IllegalArgumentException("request vote count must be zero");
        }

        if (candidateId < 0 || candidateId > MAX_CANDIDATED_ID) {
            throw new IllegalArgumentException("bad candidatedId");
        }

        if (voteCount < 0) {
            throw new IllegalArgumentException("votecount must be > zero");
        }

        this.queryFalg = queryFalg;
        this.responseFlag = responseFlag;
        this.candidateId = candidateId;
        this.voteCount = voteCount;
    }

    public boolean isQueryFalg() {
        return queryFalg;
    }

    public void setQueryFalg(boolean queryFalg) {
        this.queryFalg = queryFalg;
    }

    public boolean isResponseFlag() {
        return responseFlag;
    }

    public void setResponseFlag(boolean responseFlag) {
        this.responseFlag = responseFlag;
    }

    public int getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(int candidateId) {
        if (candidateId < 0 || candidateId > MAX_CANDIDATED_ID) {
            throw new IllegalArgumentException("bad candidatedId");
        }
        this.candidateId = candidateId;
    }

    public long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(long voteCount) {
        if ((voteCount != 0 && !responseFlag) || voteCount < 0) {
            throw new IllegalArgumentException("bad vote count");
        }
        this.voteCount = voteCount;
    }

    @Override
    public String toString() {
        return this.candidateId+"has "+this.voteCount+" supporters";
    }
}
