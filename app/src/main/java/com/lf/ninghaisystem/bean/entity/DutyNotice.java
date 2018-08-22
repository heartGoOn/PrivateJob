package com.lf.ninghaisystem.bean.entity;

/**
 * Created by admin on 2017/11/13.
 */

public class DutyNotice {

    private String title;
    private String message;
    private String sendPeople;
    private String date;
    private boolean isRead;

    public DutyNotice() {
    }

    public DutyNotice(String title, String message, String sendPeople, String date, boolean isRead) {
        this.title = title;
        this.message = message;
        this.sendPeople = sendPeople;
        this.date = date;
        this.isRead = isRead;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendPeople() {
        return sendPeople;
    }

    public void setSendPeople(String sendPeople) {
        this.sendPeople = sendPeople;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
