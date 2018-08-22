package com.lf.ninghaisystem.bean;

import java.io.Serializable;

/**
 * Created by admin on 2017/12/1.
 */

public class ProcessContent implements Serializable{

    private int projectId;
    private int reportId;
    //private int reportYears;
    private String selectQuarter;
    private String content;

    public ProcessContent(int projectId, int reportId, String selectQuarter, String content) {
        this.projectId = projectId;
        this.reportId = reportId;
        this.selectQuarter = selectQuarter;
        this.content = content;
    }

    public int getReportId() {
        return reportId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

//    public int getReportYears() {
//        return reportYears;
//    }
//
//    public void setReportYears(int reportYears) {
//        this.reportYears = reportYears;
//    }

    public String getSelectQuarter() {
        return selectQuarter;
    }

    public void setSelectQuarter(String selectQuarter) {
        this.selectQuarter = selectQuarter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
