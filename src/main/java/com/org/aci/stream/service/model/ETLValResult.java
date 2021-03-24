package com.org.aci.stream.service.model;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Table(name = "test_results")
public class ETLValResult {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String functionality_name;

    @Column
    private String field_name;

    @Column
    private String test;

   @Column(name = "actionDt", columnDefinition = "TIMESTAMP")
    private LocalDateTime actionDt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFunctionality_name() {
        return functionality_name;
    }

    public void setFunctionality_name(String functionality_name) {
        this.functionality_name = functionality_name;
    }

    public String getField_name() {
        return field_name;
    }

    public void setField_name(String field_name) {
        this.field_name = field_name;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public LocalDateTime getActionDt() {
        return actionDt;
    }

    public void setActionDt(LocalDateTime actionDt) {
        this.actionDt = actionDt;
    }
}
