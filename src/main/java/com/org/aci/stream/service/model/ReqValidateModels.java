package com.org.aci.stream.service.model;

import java.util.List;
import java.util.StringJoiner;

public class ReqValidateModels {
    private int delays;
    private List<ReqValidateModel> msgValReq;

/*    public ReqValidateModels(int delays, List<ReqValidateModel> msgValReq) {
        this.delays = delays;
        this.msgValReq = msgValReq;
    }*/

    public int getDelays() {
        return delays;
    }

    public void setDelays(int delays) {
        this.delays = delays;
    }

    public List<ReqValidateModel> getMsgValReq() {
        return msgValReq;
    }

    public void setMsgValReq(List<ReqValidateModel> msgValReq) {
        this.msgValReq = msgValReq;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ReqValidateModels.class.getSimpleName() + "[", "]")
                .add("delays=" + delays)
                .add("msgValReq=" + msgValReq)
                .toString();
    }
}
