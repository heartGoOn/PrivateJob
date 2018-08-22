package com.lf.ninghaisystem.bean.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 2017/11/16.
 */

public class DutyRank{


    @SerializedName("performTimes")
    @Expose
    private int dutyNum;
    private int rank;
    @SerializedName("employeeName")
    @Expose
    private String name;

    @SerializedName("employeeId")
    @Expose
    private int id;

    @SerializedName("liveness")
    @Expose
    private String status;

    private String profilePicUrl;

    public int getDutyNum() {
        return dutyNum;
    }

    public void setDutyNum(int dutyNum) {
        this.dutyNum = dutyNum;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }
}
