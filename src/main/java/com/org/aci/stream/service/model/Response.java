package com.org.aci.stream.service.model;

import java.util.List;

public class Response {
    private List<String> oids;
    private String message;
    private String error;

    public List<String> getOids() {
        return oids;
    }

    public void setOids(List<String> oids) {
        this.oids = oids;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
