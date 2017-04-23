package com.darshanbshah.odsystem;

/**
 * Created by iamDarshan on 22/04/17.
 */

public class DataProvider {
    private String key, rollno, reason, from, to;

    public DataProvider(String key, String rollno, String reason, String from, String to) {
        this.rollno = rollno;
        this.reason = reason;
        this.from = from;
        this.to = to;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}