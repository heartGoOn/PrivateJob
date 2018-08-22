package com.lf.ninghaisystem.bean.entity;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Result<T> {

    @SerializedName("ret")
    @Expose
    private int ret;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private T data;

    private int totalPage;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * @return The msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg The msg
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}