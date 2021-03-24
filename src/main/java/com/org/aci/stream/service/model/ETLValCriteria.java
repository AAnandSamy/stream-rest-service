package com.org.aci.stream.service.model;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.StringJoiner;

@Entity
@Table(name = "test_criteria")
public class ETLValCriteria {
    @Id
    @Column
    private String functionality_name;
    @Column(length = 5000)
    private String sql;

    @Column(length = 10000)
    private String exp_rs_XML;

    @Column(length = 20000)
    private String msg;

    @Column(name = "actionDt", columnDefinition = "TIMESTAMP")
    private LocalDateTime actionDt;

    public String getFunctionality_name() {
        return functionality_name;
    }

    public void setFunctionality_name(String functionality_name) {
        this.functionality_name = functionality_name;
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public LocalDateTime getActionDt() {
        return actionDt;
    }

    public void setActionDt(LocalDateTime actionDt) {
        this.actionDt = actionDt;
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", ETLValCriteria.class.getSimpleName() + "[", "]")
                .add("functionality_name='" + functionality_name + "'")
                .add("sql='" + sql + "'")
                .add("exp_rs_XML='" + exp_rs_XML + "'")
                .add("msg='" + msg + "'")
                .add("actionDt=" + actionDt)
                .toString();
    }
}
