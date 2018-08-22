package com.lf.ninghaisystem.bean.entity;

/**
 * Created by admin on 2018/1/17.
 */

public class PopupType {
    int imgId;
    String name;

    public PopupType(int imgId, String name) {
        this.imgId = imgId;
        this.name = name;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
