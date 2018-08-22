package com.lf.ninghaisystem.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.activities.MyApplication;
import com.lf.ninghaisystem.activities.NotifyProcessActivity;
import com.lf.ninghaisystem.bean.HasReport;
import com.lf.ninghaisystem.bean.ProcessContent;
import com.lf.ninghaisystem.bean.entity.BannerEntity;
import com.lf.ninghaisystem.bean.entity.Project;
import com.lf.ninghaisystem.bean.entity.Result;
import com.lf.ninghaisystem.fragment.base.BaseBarFragment;
import com.lf.ninghaisystem.http.JSContact;
import com.lf.ninghaisystem.http.WebContact;
import com.lf.ninghaisystem.http.retrofit.RetrofitUtil;
import com.lf.ninghaisystem.util.JsonHelper;
import com.lf.ninghaisystem.util.SPHelper;
import com.lf.ninghaisystem.util.SignGenerate;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by admin on 2017/11/16.
 */

public class ProjectProcessFragment extends BaseBarFragment {

    Project project;
    private WebView webView;
    private Button btnAddProcess;
    //    private int mOriginButtonTop;
//    private GestureDetectorCompat mDetectorCompat;
    private int hasReport = 1;


    @Override
    public void initData() {
        super.initData();
        project = (Project) getActivity().getIntent().getSerializableExtra("project");
        /*HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("projectId", project.getProjectId());
        hashMap.put("uid", MyApplication.loginUser.getUid());
        hashMap.put("token", MyApplication.loginUser.getToken());
        hashMap.put("sign", SignGenerate.generate(hashMap));
        String json = JsonHelper.hashMapToJson(hashMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        RetrofitUtil.getService().checkHasReport(body).enqueue(new Callback<Result<HasReport>>() {
            @Override
            public void onResponse(Response<Result<HasReport>> response, Retrofit retrofit) {
                Result result = response.body();
                if (result == null) {
                    Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (result.getRet() == 200) {
                    hasReport = ((HasReport) result.getData()).getStatus();
                    init();
                } else if (result.getRet() == 111) {
                    SPHelper.clearLoginUser();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }


            }

            @Override
            public void onFailure(Throwable t) {
                hideWaitDialog();
                Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });*/

    }

    @Override
    public void initViews(View rootView) {
        super.initViews(rootView);
        setView(R.layout.fragment_project_process);
        setTitle("进度报告");
        LoadWebView(rootView);

        /*btnAddProcess = rootView.findViewById(R.id.add_report);
        btnAddProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showWaitDialog("添加中...").show();

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("projectId", project.getProjectId());
                hashMap.put("uid", MyApplication.loginUser.getUid());
                hashMap.put("token", MyApplication.loginUser.getToken());
                hashMap.put("sign", SignGenerate.generate(hashMap));

                String json = JsonHelper.hashMapToJson(hashMap);
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
                RetrofitUtil.getService().insertReportProcess(body).enqueue(new Callback<Result<HasReport>>() {
                    @Override
                    public void onResponse(Response<Result<HasReport>> response, Retrofit retrofit) {

                        Result result = response.body();
                        hideWaitDialog();
                        if (result == null) {
                            Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (result.getRet() == 200) {

                            Toast.makeText(getActivity(), "添加进度成功", Toast.LENGTH_SHORT).show();
                            hasReport = 1;
                            init();
                            webView.post(new Runnable() {
                                @Override
                                public void run() {
                                    webView.loadUrl("javascript:getReportSchedule(" + project.getProjectId()
                                            + ",-1," + MyApplication.loginUser.getUid()
                                            + ",'" + MyApplication.loginUser.getToken() + "','a')");
                                }
                            });

                        } else if (result.getRet() == 111) {

                            SPHelper.clearLoginUser();
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        } else {

                            Toast.makeText(getActivity(), "添加失败", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(getActivity(), "添加失败", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });*/
    }

    /*private void init() {
        if (hasReport == 1) {
            btnAddProcess.setVisibility(View.GONE);
        } else {
            btnAddProcess.setVisibility(View.VISIBLE);
        }
    }*/

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
        webView.loadUrl(WebContact.projectProcessStr);
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

                int cando = 0;

                if (MyApplication.loginUser.getEmployeeInfo().getIsGovernmentOffices().equals("1")) {
                    cando = 1;
                } else {
                    cando = 0;
                }

                webView.loadUrl("javascript:getReportSchedule(" + project.getProjectId()
                        + ",-1," + MyApplication.loginUser.getUid()
                        + ",'" + MyApplication.loginUser.getToken() + "','a'," + cando + ")");

            }
        });

        webView.addJavascriptInterface(new JSContact.NotifyProcess() {

            /*@JavascriptInterface
            @Override
            public void notifyProcess(Object obj) {
                ProcessContent processContent= (ProcessContent) obj;
                Toast.makeText(getActivity(),processContent.getProjectId(),Toast.LENGTH_SHORT).show();
            }*/

            @JavascriptInterface
            @Override
            public void notifyProcess(int projectId, int reportId, String selectQuarter, String content) {


                ProcessContent processContent = new ProcessContent(projectId, reportId, selectQuarter, content);

                //Toast.makeText(getActivity(),processContent.getProjectId(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), NotifyProcessActivity.class);
                intent.putExtra("processContent", processContent);
                startActivity(intent);

            }
        }, "test");

    }

    @Override
    public void onStart() {
        super.onStart();
        webView.loadUrl("javascript:getReportSchedule(" + project.getProjectId()
                + ",-1," + MyApplication.loginUser.getUid()
                + ",'" + MyApplication.loginUser.getToken() + "','a')");
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

    /*class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            if (Math.abs(distanceY) > Math.abs(distanceX)) {//判断是否竖直滑动
                int buttonTop = btnAddProcess.getTop();
                int buttonBottom = btnAddProcess.getBottom();

                //是否向下滑动
                boolean isScrollDown = e1.getRawY() < e2.getRawY() ? true : false;

                //根据滑动方向和mButton当前的位置判断是否需要移动Button的位置
                if (!ifNeedScroll(isScrollDown)) return false;

                if (isScrollDown) {
                    //下滑上移Button

                    btnAddProcess.setTop(mOriginButtonTop);
                    btnAddProcess.setBottom(buttonBottom - (int) Math.abs(distanceY));

                } else if (!isScrollDown) {
                    //上滑下移Button
                    btnAddProcess.setTop(buttonTop + (int) Math.abs(distanceY));
                    btnAddProcess.setBottom(buttonBottom + (int) Math.abs(distanceY));
                }
            }

            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        //写一个方法，根据滑动方向和mButton当前的位置，判断按钮是否应该继续滑动
        private boolean ifNeedScroll(boolean isScrollDown) {
            int nowButtonTop = btnAddProcess.getTop();

            //button不能超出原来的上边界
            if (isScrollDown && nowButtonTop <= mOriginButtonTop) return false;

            //判断按钮是否在屏幕范围内，如果不在，则不需要再移动位置
            if (!isScrollDown) {
                return isInScreen(btnAddProcess);
            }

            return true;
        }

        //判断一个控件是否在屏幕范围内
        private boolean isInScreen(View view) {
            int width, height;
            Point p = new Point();
            getActivity().getWindowManager().getDefaultDisplay().getSize(p);
            width = p.x;
            height = p.y;

            Rect rect = new Rect(0, 0, width, height);

            if (!view.getLocalVisibleRect(rect)) return false;

            return true;
        }

    }*/
}
