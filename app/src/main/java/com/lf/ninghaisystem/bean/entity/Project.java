package com.lf.ninghaisystem.bean.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by admin on 2017/11/7.
 */

public class Project implements Serializable {

    private int projectId;

    @SerializedName("projectName")
    @Expose
    private String pName;

    @SerializedName("representativeTeamName")
    @Expose
    private String teamName;

    private String imgUrl;

    private int hasSubProject;

    private int isOwn;

    private int isHistory;

    public Project() {

    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getHasSubProject() {
        return hasSubProject;
    }

    public void setHasSubProject(int hasSubProject) {
        this.hasSubProject = hasSubProject;
    }

    public int getIsOwn() {
        return isOwn;
    }

    public void setIsOwn(int isOwn) {
        this.isOwn = isOwn;
    }

    public int getIsHistory() {
        return isHistory;
    }

    public void setIsHistory(int isHistory) {
        this.isHistory = isHistory;
    }
}
