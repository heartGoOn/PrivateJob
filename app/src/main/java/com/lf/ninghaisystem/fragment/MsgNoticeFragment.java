package com.lf.ninghaisystem.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.Toast;

import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.activities.HomeActivity;
import com.lf.ninghaisystem.activities.LoginActivity;
import com.lf.ninghaisystem.activities.MyApplication;
import com.lf.ninghaisystem.activities.base.FinishActivityManager;
import com.lf.ninghaisystem.adapter.DutyListAdapter;
import com.lf.ninghaisystem.adapter.DutyNoticeAdapter;
import com.lf.ninghaisystem.bean.entity.DutyNotice;
import com.lf.ninghaisystem.bean.entity.LoginUser;
import com.lf.ninghaisystem.bean.entity.Result;
import com.lf.ninghaisystem.fragment.base.BaseFragment;
import com.lf.ninghaisystem.http.JSContact;
import com.lf.ninghaisystem.http.WebContact;
import com.lf.ninghaisystem.http.retrofit.RetrofitUtil;
import com.lf.ninghaisystem.util.JsonHelper;
import com.lf.ninghaisystem.util.SPHelper;
import com.lf.ninghaisystem.util.SignGenerate;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by admin on 2017/11/13.
 */

public class MsgNoticeFragment extends BaseFragment {

    private WebView webView;
    private LoginUser loginUser;

    @Override
    public void initData() {
        super.initData();
        loginUser = MyApplication.loginUser;
    }

    @Override
    public void initViews(View rootView) {
        super.initViews(rootView);

    }

    @Override
    public void onResume() {
        super.onResume();
        LoadWebView(rootView);
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    private void LoadWebView(View view) {

        webView = view.findViewById(R.id.webView);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.loadUrl(WebContact.messageNotifyStr);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.loadUrl("javascript:GetInfosShow(1," + MyApplication.loginUser.getUid()
                        + ",'" + MyApplication.loginUser.getToken() + "','a')");
            }
        });

        webView.addJavascriptInterface(new JSContact.NoticeMsg() {

            @JavascriptInterface
            @Override
            public void noticeMsg(String noticeId) {

                updateReadStatus(Integer.parseInt(noticeId));

            }
        }, "test");

    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    private void updateReadStatus(int noticeId) {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("noticeId", noticeId);
        hashMap.put("isRead", 1);
        hashMap.put("uid", loginUser.getUid());
        hashMap.put("token", loginUser.getToken());
        String sign = SignGenerate.generate(hashMap);
        hashMap.put("sign", sign);

        String json = JsonHelper.hashMapToJson(hashMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);

        RetrofitUtil.getService().updateBoostRead(body).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Response<Result> response, Retrofit retrofit) {

                Result result = response.body();
                if (result.getRet() == 200) {

                    webView.loadUrl(WebContact.messageNotifyStr);
                    /*webView.loadUrl("javascript:GetInfosShow(1,"+ MyApplication.loginUser.getUid()
                            +",'"+MyApplication.loginUser.getToken()+"','a')");*/

                } else if (result.getRet() == 111) {

                    SPHelper.clearLoginUser();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    FinishActivityManager.getManager().finishActivity(HomeActivity.class);
                    getActivity().finish();
                }


            }

            @Override
            public void onFailure(Throwable t) {

                Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_msg_notice;
    }


}
