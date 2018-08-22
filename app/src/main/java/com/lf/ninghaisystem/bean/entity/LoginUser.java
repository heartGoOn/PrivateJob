package com.lf.ninghaisystem.bean.entity;


import com.lf.ninghaisystem.bean.User;

/**
 * Created by admin on 2017/12/7.
 */

public class LoginUser {

    private int uid;
    private String token;
    private User employeeInfo;
    private transient String tokenDate;
    private transient String account;
    private transient String password;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public User getEmployeeInfo() {
        return employeeInfo;
    }

    public void setEmployeeInfo(User employeeInfo) {
        this.employeeInfo = employeeInfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenDate() {
        return tokenDate;
    }

    public void setTokenDate(String tokenDate) {
        this.tokenDate = tokenDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
