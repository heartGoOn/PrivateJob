package com.lf.ninghaisystem.bean.entity;

import java.io.Serializable;

/**
 * Created by admin on 2017/12/8.
 */

public class BannerEntity implements Serializable{

    private String title;
    private String content;
    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
