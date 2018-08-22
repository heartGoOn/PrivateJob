package com.lf.ninghaisystem.bean;

/**
 * Created by admin on 2017/11/8.
 */

public class MineListItem {

    private String title;
    private int noticeNum;

    public MineListItem(String title) {
        this.title = title;
        noticeNum = 0;
    }

    public MineListItem(String title, int noticeNum) {
        this.title = title;
        this.noticeNum = noticeNum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNoticeNum() {
        return noticeNum;
    }

    public void setNoticeNum(int noticeNum) {
        this.noticeNum = noticeNum;
    }
}
