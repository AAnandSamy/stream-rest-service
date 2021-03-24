package com.org.aci.stream.service.model;
import java.util.List;
import java.util.StringJoiner;


public class ReqValidateModel {
    private CoreTransaction msg;
    private String sql;
    private String exp_rs_XML;
    private String functionality_name;


    public ReqValidateModel(CoreTransaction msg, String sql, String exp_rs_XML, String functionality_name) {
        this.msg = msg;
        this.sql = sql;
        this.exp_rs_XML = exp_rs_XML;
        this.functionality_name = functionality_name;
    }
    public ReqValidateModel() {
    }

    public CoreTransaction getMsg() {
        return msg;
    }

    public void setMsg(CoreTransaction msg) {
        this.msg = msg;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getExp_rs_XML() {
        return exp_rs_XML;
    }

    public void setExp_rs_XML(String exp_rs_XML) {
        this.exp_rs_XML = exp_rs_XML;
    }

    public String getFunctionality_name() {
        return functionality_name;
    }

    public void setFunctionality_name(String functionality_name) {
        this.functionality_name = functionality_name;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ReqValidateModel.class.getSimpleName() + "[", "]")
                .add("msg=" + msg)
                .add("sql='" + sql + "'")
                .add("exp_rs_XML='" + exp_rs_XML + "'")
                .add("functionality_name='" + functionality_name + "'")
                .toString();
    }
}
