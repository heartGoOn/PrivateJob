package com.lf.ninghaisystem.service;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.igexin.sdk.message.SetTagCmdMessage;
import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.activities.LoginActivity;
import com.lf.ninghaisystem.activities.MessageActivity;
import com.lf.ninghaisystem.activities.MyApplication;
import com.lf.ninghaisystem.activities.UserMsgActivity;
import com.lf.ninghaisystem.bean.Getui;
import com.lf.ninghaisystem.bean.entity.Result;
import com.lf.ninghaisystem.http.retrofit.RetrofitUtil;
import com.lf.ninghaisystem.util.DialogHelp;
import com.lf.ninghaisystem.util.JsonHelper;
import com.lf.ninghaisystem.util.SPHelper;
import com.lf.ninghaisystem.util.SignGenerate;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by admin on 2017/12/1.
 */

public class mIntentService extends GTIntentService {

    @Override
    public void onReceiveServicePid(Context context, int i) {

    }

    @Override
    public void onReceiveClientId(Context context, String s) {

        Log.e(TAG, "onReceiveClientId -> " + "clientid = " + s);
        MyApplication.Cid = s;

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("uid",MyApplication.loginUser.getUid());
        hashMap.put("token",MyApplication.loginUser.getToken());
        hashMap.put("cid",s);
        String sign = SignGenerate.generate(hashMap);
        hashMap.put("sign",sign);
        String json = JsonHelper.hashMapToJson(hashMap);

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);

        RetrofitUtil.getService().updateCid(body).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Response<Result> response, Retrofit retrofit) {

                Result result = response.body();


            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {

        String appid = msg.getAppid();
        String taskid = msg.getTaskId();
        String messageid = msg.getMessageId();
        String pkg = msg.getPkgName();
        byte[] payload = msg.getPayload();
        //Log.e(TAG, "receiver payload = null");
        String cid = msg.getClientId();

        // 第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
        boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
        Log.d(TAG, "call sendFeedbackMessage = " + (result ? "success" : "failed"));

        Log.d(TAG, "onReceiveMessageData -> " + "appid = " + appid + "\ntaskid = " + taskid + "\nmessageid = " + messageid + "\npkg = " + pkg
                + "\ncid = " + cid);
        if (payload == null) {
            Log.e(TAG, "receiver payload = null");
        } else {
            String data = new String(payload);
            Log.d(TAG, "receiver payload = " + data);

            final String typeFlag = data.substring(data.indexOf("【"),data.indexOf("】")+1);
            final String content = data.substring(data.indexOf("】")+1,data.length());

            if(MyLifecycleHandler.isApplicationVisible()) {    //如果在前台

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(MyApplication.getmContext(),content,Toast.LENGTH_LONG).show();
                        Intent intent = new Intent("com.lf.ninghai.GETUI");
                        intent.putExtra("type",typeFlag);
                        intent.putExtra("content",content);
                        sendOrderedBroadcast(intent,null);
                    }
                });

                Log.d(TAG,typeFlag);
                Log.d(TAG,content);
            } else {
                NotificationHelper.createNotification(MessageActivity.class, content, typeFlag);
            }
            /*// 测试消息为了观察数据变化
            if (data.equals(getResources().getString(R.string.push_transmission_data))) {
                data = data + "-" + cnt;
                cnt++;
            }
            sendMessage(data, 0);*/
        }
        Log.d(TAG, "----------------------------------------------------------------------------------------------");

    }

    @Override
    public void onReceiveOnlineState(Context context, boolean b) {

    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {



    }
}
