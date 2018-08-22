package com.lf.ninghaisystem.http;

import android.webkit.JavascriptInterface;

/**
 * Created by admin on 2017/11/24.
 */

public interface JSContact {

    interface CallFilterType {

        @JavascriptInterface
        void filterType(String keyword, String str);
    }

    interface NotifyProcess {
        //obj == ProcessContent
        @JavascriptInterface
        void notifyProcess(int projectId, int reportId, String selectQuarter, String content);
    }

    interface NotifyAnalysis {

        //记录筛选
        @JavascriptInterface
        void filterType(String str);

        //更新筛选后界面
        @JavascriptInterface
        void notifyAnalysis(String date, String projectId);
    }

    interface FeedbackMsg {
        //获取反馈信息
        @JavascriptInterface
        void feedbackMsg(String evaluatingId, String projectId, String projectName, String dutyPerformId, String  isOwn, String  isHistory);
    }

    interface NoticeMsg {
        //更新阅读状态
        @JavascriptInterface
        void noticeMsg(String noticeId);
    }

}
