package com.stanley.myapplication.contactrecord;

/**
 * Created by XieDugu on 2016/2/13.
 */
public class ContactRecord {

    private int contrcdId;
    private String contactName;
    private String contrcdDateTime;//--MM-dd hh:mm
    private int contrcdType;
    private long duration;
    //--1: incoming
    //--2: outgoing
    //--3: miss

    public int getContrcdId() {
        return contrcdId;
    }
    public void setContrcdId(int contrcdId) {
        this.contrcdId = contrcdId;
    }

    public String getContactName() {
        return contactName;
    }
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContrcdDateTime() {
        return contrcdDateTime;
    }
    public void setContrcdDateTime(String contrcdDate) {
        this.contrcdDateTime = contrcdDate;
    }

    public int getContrcdType() {
        return contrcdType;
    }
    public void setContrcdType(int contrcdType) {
        this.contrcdType = contrcdType;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
