package com.lf.ninghaisystem.bean.entity;

import com.bigkoo.pickerview.model.IPickerViewData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 2017/12/4.
 */

public class MyProject implements IPickerViewData{

    private int projectId;

    @SerializedName("projectName")
    @Expose
    private String pName;

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

    @Override
    public String getPickerViewText() {
        return this.pName;
    }
}
