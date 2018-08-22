package com.lf.ninghaisystem.contact;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by chen on 2017/11/12.
 */

public class SortModel {

    @SerializedName("employeeName")
    @Expose
    private String name;
    private String letters;//显示拼音的首字母
    @SerializedName("employeeCellPhone")
    @Expose
    private String phoneNum;
    @SerializedName("firstChar")
    @Expose
    private String firstChar;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLetters() {
        return letters;
    }

    public void setLetters(String letters) {
        this.letters = letters;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getFirstChar() {
        return firstChar;
    }

    public void setFirstChar(String firstChar) {
        this.firstChar = firstChar;
    }
}
