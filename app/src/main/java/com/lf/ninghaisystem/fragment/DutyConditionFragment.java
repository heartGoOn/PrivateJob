package com.lf.ninghaisystem.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.activities.LoginActivity;
import com.lf.ninghaisystem.activities.MyApplication;
import com.lf.ninghaisystem.activities.ProjectMainAcitvity;
import com.lf.ninghaisystem.bean.Feedback;
import com.lf.ninghaisystem.bean.entity.Duty;
import com.lf.ninghaisystem.bean.entity.LoginUser;
import com.lf.ninghaisystem.bean.entity.Project;
import com.lf.ninghaisystem.bean.entity.Result;
import com.lf.ninghaisystem.fragment.base.BaseBarFragment;
import com.lf.ninghaisystem.http.WebContact;
import com.lf.ninghaisystem.http.retrofit.RetrofitUtil;
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
 * Created by admin on 2017/11/17.
 */

public class DutyConditionFragment extends BaseBarFragment {

    private static final String TAG = "DutyConditionFragment";
    private WebView webView;
    private LinearLayout linSubmit;
    private Button btnSubmit;
    private EditText etContent;
    private Duty duty;
    private int dutyPerformId;
    private int performId;
    private LoginUser loginUser;
    private int projectId;
    private int isHistory, isOwn;

    @Override
    public void initData() {
        super.initData();

        Intent intent = getActivity().getIntent();
        projectId = intent.getIntExtra("projectId", -1);
        duty = (Duty) intent.getSerializableExtra("duty");
        isHistory = intent.getIntExtra("isHistory", 0);
        isOwn = intent.getIntExtra("isOwn", 0);
        dutyPerformId = duty.getDutyPerformId();
        loginUser = MyApplication.loginUser;

    }

    @Override
    public void initViews(View rootView) {
        super.initViews(rootView);
        setView(R.layout.fragment_duty_condition);
        setTitle("履职情况");
        setRightTxt("查看项目");

        btnSubmit = rootView.findViewById(R.id.btn_submit);
        linSubmit = rootView.findViewById(R.id.lin_submit);
        etContent = rootView.findViewById(R.id.et_submit);


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("dutyPerformId", dutyPerformId);
        hashMap.put("uid", loginUser.getUid());
        hashMap.put("token", loginUser.getToken());
        String sign = SignGenerate.generate(hashMap);
        hashMap.put("sign", sign);

        String json = JsonHelper.hashMapToJson(hashMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);

        RetrofitUtil.getService().getFeedbackCount(body).enqueue(new Callback<Result<Feedback>>() {
            @Override
            public void onResponse(Response<Result<Feedback>> response, Retrofit retrofit) {

                Result result = response.body();
                if (result.getRet() == 200) {

                    Feedback feedback = (Feedback) result.getData();
                    performId = feedback.getPerformerId();
                    if (performId == loginUser.getUid()) {

                        if (feedback.getFeedbackCount() > 0) {
                            linSubmit.setVisibility(View.VISIBLE);
                        } else {
                            linSubmit.setVisibility(View.GONE);
                        }
                    } else {
                        linSubmit.setVisibility(View.VISIBLE);
                    }

                } else if (result.getRet() == 111) {
                    SPHelper.clearLoginUser();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                netWorkAction();
            }
        });

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(charSequence)) {
                    btnSubmit.setEnabled(false);
                } else {
                    btnSubmit.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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
        webView.loadUrl(WebContact.lzDetailStr);

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
                webView.loadUrl("javascript:getInfoShow(" + dutyPerformId + ","
                        + loginUser.getUid() + ",'" + loginUser.getToken() + "')");
            }
        });


    }

    private void netWorkAction() {
        showWaitDialog("发送中...").show();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("dutyPerformId", dutyPerformId);
        hashMap.put("evaluatingContent", etContent.getText().toString());
        hashMap.put("uid", loginUser.getUid());
        hashMap.put("token", loginUser.getToken());
        String sign = SignGenerate.generate(hashMap);
        hashMap.put("sign", sign);
        String json = JsonHelper.hashMapToJson(hashMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        RetrofitUtil.getService().insertEvaluating(body).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Response<Result> response, Retrofit retrofit) {
                Result result = response.body();
                if (result.getRet() == 200) {
                    etContent.setText("");
                    webView.loadUrl("javascript:getInfoShow(" + dutyPerformId + ","
                            + loginUser.getUid() + ",'" + loginUser.getToken() + "')");

                } else if (result.getRet() == 111) {
                    SPHelper.clearLoginUser();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
                hideWaitDialog();
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    protected void onRightBtnClick(View v) {
        super.onRightBtnClick(v);
        Intent intent = new Intent(getActivity(), ProjectMainAcitvity.class);
        Project project = new Project();
        project.setProjectId(duty.getProjectId());
        project.setpName(duty.getDutyTitle());
        project.setTeamName("");
        intent.putExtra("project", project);
        intent.putExtra("isHistory", isHistory);
        intent.putExtra("isOwn", isOwn);
        Log.e(TAG, "onRightBtnClick: " + isOwn + "~~~~~~" + isHistory);
        startActivity(intent);

    }

    @Override
    protected boolean hasBackBtn() {
        return true;
    }

    @Override
    protected boolean hasRightBtn() {
        return (projectId == -1);
    }

    @Override
    protected boolean isRightImg() {
        return false;
    }
}
