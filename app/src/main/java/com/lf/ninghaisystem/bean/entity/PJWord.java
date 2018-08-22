package com.lf.ninghaisystem.bean.entity;

import java.io.Serializable;

/**
 * Created by admin on 2017/11/24.
 * 文档列表
 */

public class PJWord implements Serializable{

    private int documentId;
    private int projectId;
    private String documentTitle;
    private String documentName;
    private String documentUrl;
    private int documentStatus;
    private String documentRemark;
    private String uploadTime;

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public int getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(int documentStatus) {
        this.documentStatus = documentStatus;
    }

    public String getDocumentRemark() {
        return documentRemark;
    }

    public void setDocumentRemark(String documentRemark) {
        this.documentRemark = documentRemark;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }
}
