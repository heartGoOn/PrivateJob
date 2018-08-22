package com.lf.ninghaisystem.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.activities.HomeActivity;
import com.lf.ninghaisystem.activities.LoginActivity;
import com.lf.ninghaisystem.activities.MyApplication;
import com.lf.ninghaisystem.activities.base.FinishActivityManager;
import com.lf.ninghaisystem.bean.ProcessContent;
import com.lf.ninghaisystem.bean.entity.LoginUser;
import com.lf.ninghaisystem.bean.entity.Project;
import com.lf.ninghaisystem.bean.entity.Result;
import com.lf.ninghaisystem.fragment.base.BaseBarFragment;
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
 * Created by admin on 2017/12/1.
 */

public class NotifyProcessFragment extends BaseBarFragment {

    ProcessContent processContent;
    private EditText et_content;
    private LoginUser loginUser;

    @Override
    public void initData() {
        super.initData();
        loginUser = MyApplication.loginUser;
        processContent = (ProcessContent) getActivity().getIntent().getSerializableExtra("processContent");
    }

    @Override
    public void initViews(View rootView) {
        super.initViews(rootView);
        setView(R.layout.fragment_notify_process);
        setThisTitle();
        setRightTxt("保存");
        et_content = rootView.findViewById(R.id.notify_edit);
        et_content.setText(processContent.getContent());
    }

    private void setThisTitle() {
        String title = "";
        switch (processContent.getSelectQuarter()) {
            case "0":
                title = "全年建设目标";
                break;
            case "1":
                title = "第一季度末形象进度";
                break;
            case "2":
                title = "第二季度末形象进度";
                break;
            case "3":
                title = "第三季度末形象进度";
                break;
            case "4":
                title = "第四季度末形象进度";
                break;
        }

        setTitle(title);

    }

    @Override
    protected void onRightBtnClick(View v) {
        super.onRightBtnClick(v);

        showWaitDialog("正在保存");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("reportId", processContent.getReportId());
        hashMap.put("projectId", processContent.getProjectId());
        hashMap.put("reportType", Integer.parseInt(processContent.getSelectQuarter()));
        hashMap.put("reportContent", et_content.getText().toString());
        hashMap.put("uid", loginUser.getUid());
        hashMap.put("token", loginUser.getToken());
        String sign = SignGenerate.generate(hashMap);
        hashMap.put("sign", sign);

        String json = JsonHelper.hashMapToJson(hashMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);

        RetrofitUtil.getService().updateProcessReport(body).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Response<Result> response, Retrofit retrofit) {

                Result result = response.body();
                if(result.getRet() == 200) {

                    Toast.makeText(getActivity(),"保存成功",Toast.LENGTH_SHORT).show();
                    getActivity().finish();

                } else if(result.getRet() == 142) {
                    Toast.makeText(getActivity(),"更新失败",Toast.LENGTH_SHORT).show();
                } else if(result.getRet() == 111) {

                    SPHelper.clearLoginUser();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    FinishActivityManager.getManager().finishActivity(HomeActivity.class);
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
    protected boolean hasBackBtn() {
        return true;
    }

    @Override
    protected boolean hasRightBtn() {
        return true;
    }

    @Override
    protected boolean isRightImg() {
        return false;
    }
}
