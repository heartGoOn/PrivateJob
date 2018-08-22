package com.lf.ninghaisystem.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.lf.ninghaisystem.activities.MyApplication;
import com.lf.ninghaisystem.bean.entity.LoginUser;

/**
 * Created by admin on 2017/12/7.
 */

public class SPHelper {

    private static final String TAG = "user";
    private static final Context context = MyApplication.getmContext();
    private static final String ACCOUNTSTR = "account";
    private static final String UIDSTR = "uid";
    private static final String TOKENSTR = "token";
    private static final String DATESTR = "date";
    private static final String PASSWORDSTR = "password";

    /**
     * 保存登录基本值
     */
    public static void saveUserMsg(LoginUser loginUser) {
        SharedPreferences preferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ACCOUNTSTR, loginUser.getAccount());
        editor.putInt(UIDSTR, loginUser.getUid());
        editor.putString(TOKENSTR, loginUser.getToken());
        editor.putString(DATESTR, loginUser.getTokenDate());
        editor.putString(PASSWORDSTR, loginUser.getPassword());
        editor.commit();
    }

    /**
     * 获取缓存的基本值
     *
     * @return
     */
    public static LoginUser getUserMsg() {
        LoginUser loginUser = new LoginUser();
        SharedPreferences preferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        loginUser.setAccount(preferences.getString(ACCOUNTSTR, ""));
        loginUser.setToken(preferences.getString(TOKENSTR, ""));
        loginUser.setUid(preferences.getInt(UIDSTR, -1));
        loginUser.setTokenDate(preferences.getString(DATESTR, "1990-01-01 12:00"));
        loginUser.setPassword(preferences.getString(PASSWORDSTR, ""));
        return loginUser;
    }

    public static void clearLoginUser() {
        LoginUser loginUser = new LoginUser();
        loginUser.setAccount(MyApplication.loginUser.getAccount());
        loginUser.setTokenDate(MyApplication.loginUser.getTokenDate());
        loginUser.setToken("");
        loginUser.setUid(-1);
        loginUser.setPassword("");
        saveUserMsg(loginUser);
        MyApplication.loginUser = null; //去掉内存中的全局登录信息
    }
}
