package com.lf.ninghaisystem.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by admin on 2017/11/7.
 */

public class User implements Serializable{

    @SerializedName("picture")
    @Expose
    private String headerUrl;
    @SerializedName("employeeName")
    @Expose
    private String name;

    @SerializedName("employeeCellPhone")
    @Expose
    private String phoneNum;

    private String isNpcMember;
    private String isPartMember;
    private String isCppccMember;
    private String isGovernmentOffices;
    private int unread;

    private transient String type;

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public String getHeaderUrl() {
        return headerUrl;
    }

    public void setHeaderUrl(String headerUrl) {
        this.headerUrl = headerUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getIsNpcMember() {
        return isNpcMember;
    }

    public void setIsNpcMember(String isNpcMember) {
        this.isNpcMember = isNpcMember;
    }

    public String getIsPartMember() {
        return isPartMember;
    }

    public void setIsPartMember(String isPartMember) {
        this.isPartMember = isPartMember;
    }

    public String getIsCppccMember() {
        return isCppccMember;
    }

    public void setIsCppccMember(String isCppccMember) {
        this.isCppccMember = isCppccMember;
    }

    public String getIsGovernmentOffices() {
        return isGovernmentOffices;
    }

    public void setIsGovernmentOffices(String isGovernmentOffices) {
        this.isGovernmentOffices = isGovernmentOffices;
    }
}
