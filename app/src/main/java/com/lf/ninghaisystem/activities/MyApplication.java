package com.lf.ninghaisystem.activities;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.telecom.Call;
import android.util.Log;
import android.widget.Toast;

import com.igexin.sdk.PushManager;
import com.lf.ninghaisystem.bean.entity.LoginUser;
import com.lf.ninghaisystem.http.retrofit.RetrofitUtil;
import com.lf.ninghaisystem.service.MyLifecycleHandler;
import com.lf.ninghaisystem.service.mPushService;
import com.lf.ninghaisystem.util.SPHelper;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.IOException;

/**
 * Created by admin on 2017/11/7.
 */

public class MyApplication extends Application {

    private static Context mContext;
    public static LoginUser loginUser;
    public static String Cid;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        loginUser = SPHelper.getUserMsg();
        registerActivityLifecycleCallbacks(new MyLifecycleHandler());
        FileDownloader.init(mContext);
        PushManager.getInstance().initialize(this.getApplicationContext(), mPushService.class);
        try {
            RetrofitUtil.init(this);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(mContext, "服务器未响应", Toast.LENGTH_SHORT);
        }
    }

    public static Context getmContext() {
        return mContext;
    }
}
