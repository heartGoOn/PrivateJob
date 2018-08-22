package com.lf.ninghaisystem.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.activities.MyApplication;
import com.lf.ninghaisystem.bean.entity.Project;
import com.lf.ninghaisystem.fragment.base.BaseBarFragment;
import com.lf.ninghaisystem.http.WebContact;

/**
 * Created by admin on 2017/11/16.
 */

public class ProjectPlanFragment extends BaseBarFragment {

    Project project;
    private WebView webView;

    @Override
    public void initData() {
        super.initData();
        project = (Project) getActivity().getIntent().getSerializableExtra("project");
    }

    @Override
    public void initViews(View rootView) {
        super.initViews(rootView);
        setView(R.layout.fragment_project_plan);
        setTitle("项目计划");
        LoadWebView(rootView);
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    private void LoadWebView(View view) {
        webView = view.findViewById(R.id.webView);
        WebSettings wetSettings = webView.getSettings();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wetSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.loadUrl(WebContact.projectPlanStr);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showWaitDialog("加载中...").show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideWaitDialog();
                webView.loadUrl("javascript:getProjectPlan("
                        + project.getProjectId() + ",-1," + MyApplication.loginUser.getUid()
                        + ",'" + MyApplication.loginUser.getToken() + "','a')");
            }
        });

    }

    @Override
    protected boolean hasBackBtn() {
        return true;
    }

    @Override
    protected boolean hasRightBtn() {
        return false;
    }

    @Override
    protected boolean isRightImg() {
        return false;
    }
}
