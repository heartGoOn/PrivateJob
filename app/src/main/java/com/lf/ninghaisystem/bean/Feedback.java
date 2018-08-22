package com.lf.ninghaisystem.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 2017/12/11.
 */

public class Feedback {

    @SerializedName("number")
    @Expose
    private int feedbackCount;
    private int performerId;

    public int getFeedbackCount() {
        return feedbackCount;
    }

    public void setFeedbackCount(int feedbackCount) {
        this.feedbackCount = feedbackCount;
    }

    public int getPerformerId() {
        return performerId;
    }

    public void setPerformerId(int performerId) {
        this.performerId = performerId;
    }
}
