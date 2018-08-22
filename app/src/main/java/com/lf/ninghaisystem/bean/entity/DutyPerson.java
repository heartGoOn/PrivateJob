package com.lf.ninghaisystem.bean.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by admin on 2017/11/13.
 */

public class DutyPerson implements Serializable{

    @SerializedName("employeeName")
    @Expose
    private String name;

    @SerializedName("employeeId")
    @Expose
    private int id;

    @SerializedName("liveness")
    @Expose
    private String status;

    private transient boolean selected;

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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
