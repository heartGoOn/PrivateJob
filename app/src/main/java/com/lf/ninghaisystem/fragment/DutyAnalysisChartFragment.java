package com.lf.ninghaisystem.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.activities.MyApplication;
import com.lf.ninghaisystem.bean.entity.Project;
import com.lf.ninghaisystem.fragment.base.BaseFragment;
import com.lf.ninghaisystem.http.WebContact;

/**
 * Created by admin on 2017/11/16.
 */

public class DutyAnalysisChartFragment extends BaseFragment {

    private Project project;
    private WebView webView;
    private int projectId;

    @Override
    public void initData() {
        super.initData();

        project = (Project) getActivity().getIntent().getSerializableExtra("project");
        if(project == null) {
            projectId = -1;
        } else {
            projectId = project.getProjectId();
        }
    }

    @Override
    public void initViews(View rootView) {
        super.initViews(rootView);

        LoadWebView(rootView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_analysis_chart;
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    private void LoadWebView(View view) {

        webView = view.findViewById(R.id.webView);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl(WebContact.lzAnalysisStr);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.loadUrl("javascript:GetInfosShow('-1|-1',"
                        +MyApplication.loginUser.getUid()+",'"
                        +MyApplication.loginUser.getToken()+"',"+projectId+")");
            }
        });
        //Log.v("id",project.getProjectId()+"");


    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    public void updateHomePagerProjectList(final String date, final String projectId) {

        webView.post(new Runnable() {
            @Override
            public void run() {

                webView.loadUrl("javascript:GetInfosShow('"+date+"',"
                        +MyApplication.loginUser.getUid()+",'"
                        +MyApplication.loginUser.getToken()+"',"+projectId+")");

            }
        });

    }
}
