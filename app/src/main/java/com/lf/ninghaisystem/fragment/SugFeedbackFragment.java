package com.lf.ninghaisystem.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.activities.HomeActivity;
import com.lf.ninghaisystem.activities.LoginActivity;
import com.lf.ninghaisystem.activities.MyApplication;
import com.lf.ninghaisystem.activities.base.FinishActivityManager;
import com.lf.ninghaisystem.bean.entity.LoginUser;
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
 * Created by admin on 2017/12/11.
 */

public class SugFeedbackFragment extends BaseBarFragment {

    private EditText ed_feedback;
    private Button btn_feedback;
    private LoginUser loginUser;

    @Override
    public void initData() {
        super.initData();

        loginUser = MyApplication.loginUser;

    }

    @Override
    public void initViews(View rootView) {
        super.initViews(rootView);
        setView(R.layout.fragment_sug_feedback);
        setTitle("意见反馈");
        ed_feedback = rootView.findViewById(R.id.feedback_content);
        btn_feedback = rootView.findViewById(R.id.btn_submit);
        btn_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                netWorkAction();
            }
        });
    }

    private void netWorkAction() {
        String appContent = ed_feedback.getText().toString();
        if(TextUtils.isEmpty(appContent)) {
            Toast.makeText(getActivity(),"反馈内容不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("appContent",appContent);
        hashMap.put("uid",loginUser.getUid());
        hashMap.put("token",loginUser.getToken());
        String sign = SignGenerate.generate(hashMap);
        hashMap.put("sign",sign);
        String json = JsonHelper.hashMapToJson(hashMap);
        showWaitDialog("正在提交...").show();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);
        RetrofitUtil.getService().insertSugFeedback(body).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Response<Result> response, Retrofit retrofit) {

                Result result = response.body();
                if (result.getRet() == 200) {
                    hideWaitDialog();
                    Toast.makeText(getActivity(), "提交成功！", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                } else if(result.getRet() == 111) {
                    SPHelper.clearLoginUser();
                    hideWaitDialog();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    FinishActivityManager.getManager().finishActivity(HomeActivity.class);
                } else {
                    Toast.makeText(getActivity(), "提交失败！", Toast.LENGTH_SHORT).show();
                    hideWaitDialog();
                }

            }

            @Override
            public void onFailure(Throwable t) {

                Toast.makeText(getActivity(), "网络连接失败！", Toast.LENGTH_SHORT).show();
                hideWaitDialog();

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
