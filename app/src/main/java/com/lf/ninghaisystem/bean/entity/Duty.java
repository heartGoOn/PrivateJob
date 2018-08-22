package com.lf.ninghaisystem.bean.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by admin on 2017/11/10.
 */

public class Duty implements Serializable {


    private int dutyPerformId;
    private int projectId;
    @SerializedName("projectName")
    @Expose
    private String dutyTitle;
    @SerializedName("performTime")
    @Expose
    private String dutyDate;
    @SerializedName("performerName")
    @Expose
    private String dutyPeople;
    private int performer;
    private String performerContent;
    private int isHistory;
    private int isOwn;

    public int getIsHistory() {
        return isHistory;
    }

    public void setIsHistory(int isHistory) {
        this.isHistory = isHistory;
    }

    public int getIsOwn() {
        return isOwn;
    }

    public void setIsOwn(int isOwn) {
        this.isOwn = isOwn;
    }

    public String getDutyTitle() {
        return dutyTitle;
    }

    public void setDutyTitle(String dutyTitle) {
        this.dutyTitle = dutyTitle;
    }

    public String getDutyDate() {
        return dutyDate;
    }

    public void setDutyDate(String dutyDate) {
        this.dutyDate = dutyDate;
    }

    public String getDutyPeople() {
        return dutyPeople;
    }

    public void setDutyPeople(String dutyPeople) {
        this.dutyPeople = dutyPeople;
    }

    public int getDutyPerformId() {
        return dutyPerformId;
    }

    public void setDutyPerformId(int dutyPerformId) {
        this.dutyPerformId = dutyPerformId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getPerformer() {
        return performer;
    }

    public void setPerformer(int performer) {
        this.performer = performer;
    }

    public String getPerformerContent() {
        return performerContent;
    }

    public void setPerformerContent(String performerContent) {
        this.performerContent = performerContent;
    }

    @Override
    public String toString() {
        return "Duty{" +
                "dutyPerformId=" + dutyPerformId +
                ", projectId=" + projectId +
                ", dutyTitle='" + dutyTitle + '\'' +
                ", dutyDate='" + dutyDate + '\'' +
                ", dutyPeople='" + dutyPeople + '\'' +
                ", performer=" + performer +
                ", performerContent='" + performerContent + '\'' +
                ", isHistory=" + isHistory +
                ", isOwn=" + isOwn +
                '}';
    }
}
