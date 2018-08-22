package com.lf.ninghaisystem.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.activities.BoostPersonActivity;
import com.lf.ninghaisystem.activities.HomeActivity;
import com.lf.ninghaisystem.activities.LoginActivity;
import com.lf.ninghaisystem.activities.MyApplication;
import com.lf.ninghaisystem.activities.base.FinishActivityManager;
import com.lf.ninghaisystem.bean.entity.DutyPerson;
import com.lf.ninghaisystem.bean.entity.LoginUser;
import com.lf.ninghaisystem.bean.entity.MyProject;
import com.lf.ninghaisystem.bean.entity.Project;
import com.lf.ninghaisystem.bean.entity.Result;
import com.lf.ninghaisystem.fragment.base.BaseBarFragment;
import com.lf.ninghaisystem.http.retrofit.RetrofitUtil;
import com.lf.ninghaisystem.util.JsonHelper;
import com.lf.ninghaisystem.util.SPHelper;
import com.lf.ninghaisystem.util.SignGenerate;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by admin on 2017/11/14.
 */

public class BoostFragment extends BaseBarFragment {

    private int initAsyn = 1; //初始化请求线程数
    private int finishAsyn = 0; //成功完成的请求数

    private OptionsPickerView projectPickerView;
    private RelativeLayout btnProjectPick, btnBoostPerson;
    private TextView tv_pj_result, tv_pr_result;
    private EditText et_boost;
    private List<MyProject> pickProjectDatas = new ArrayList<>();

    //插入所需要数据---
    private MyProject myProject = null;

    private Project mProject;   //是否是指定项目
    private LoginUser loginUser;

    @Override
    public void initData() {
        super.initData();

        showWaitDialog("加载中...").show();

        Intent intent = getActivity().getIntent();
        mProject = (Project) intent.getSerializableExtra("project");
        loginUser = MyApplication.loginUser;

    }

    @Override
    public void initViews(View rootView) {
        super.initViews(rootView);
        setView(R.layout.fragment_boost);
        setTitle("提醒");
        setRightTxt("发送");
        btnProjectPick = rootView.findViewById(R.id.project_select);
        tv_pj_result = rootView.findViewById(R.id.pj_select_result);
        btnBoostPerson = rootView.findViewById(R.id.boost_person);
        tv_pr_result = rootView.findViewById(R.id.pr_select_result);
        et_boost = rootView.findViewById(R.id.boost_content);
        btnProjectPick.setOnClickListener(this);
        btnBoostPerson.setOnClickListener(this);

        if (mProject == null) {
            initProjectDatas();
            initAsyn = 1;
        } else {
            initAsyn = 0;
            hideWaitDialog();
            btnProjectPick.setEnabled(false);
            tv_pj_result.setText(mProject.getpName());
            myProject = new MyProject();
            myProject.setpName(mProject.getpName());
            myProject.setProjectId(mProject.getProjectId());
        }
    }

    private void initProjectDatas() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", loginUser.getUid());
        hashMap.put("token", loginUser.getToken());
        hashMap.put("type", 1);
        String sign = SignGenerate.generate(hashMap);
        hashMap.put("sign", sign);
        String json = JsonHelper.hashMapToJson(hashMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        RetrofitUtil.getService().getMyProjectList(body).enqueue(new Callback<Result<List<MyProject>>>() {
            @Override
            public void onResponse(Response<Result<List<MyProject>>> response, Retrofit retrofit) {
                Result result = response.body();
                if (result == null) {
                    Toast.makeText(getActivity(), "网络请求出错", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (result.getRet() == 200) {
                    pickProjectDatas = (List<MyProject>) result.getData();
                    initProjectPicker();
                } else if (result.getRet() == 111) {
                    SPHelper.clearLoginUser();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    FinishActivityManager.getManager().finishActivity(HomeActivity.class);
                }
                if (++finishAsyn == initAsyn) {
                    hideWaitDialog();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initProjectPicker() {
        projectPickerView = new OptionsPickerView.Builder(getActivity(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                if (pickProjectDatas.size() != 0) {
                    myProject = pickProjectDatas.get(options1);
                    String str = myProject.getPickerViewText();
                    tv_pj_result.setText(str);
                }
            }
        })
                .setTitleText("项目选择")
                .setContentTextSize(20)//设置滚轮文字大小
                .build();

        projectPickerView.setPicker(pickProjectDatas);

    }

    private String checkStr = "";
    private boolean isAll = false;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == 5 && data != null) {
                checkStr = data.getStringExtra("checkStr");
                isAll = data.getBooleanExtra("checkAll", false);
                Log.v("checkStr", checkStr);
                if (!TextUtils.isEmpty(checkStr)) {
                    tv_pr_result.setText("已选择");
                } else {
                    tv_pr_result.setText("选择提醒对象");
                }
            }
        }
    }

    @Override
    protected void onRightBtnClick(View v) {
        super.onRightBtnClick(v);
        String boostContent = et_boost.getText().toString();
        if (TextUtils.isEmpty(checkStr) || TextUtils.isEmpty(boostContent)) {
            Toast.makeText(getActivity(), "请填写必要信息", Toast.LENGTH_SHORT).show();
        } else {
            showWaitDialog("发送中...").show();
            rightTxt.setEnabled(false);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("memberIds", checkStr);
            hashMap.put("projectId", myProject.getProjectId());
            hashMap.put("content", boostContent);
            hashMap.put("uid", loginUser.getUid());
            hashMap.put("token", loginUser.getToken());
            String sign = SignGenerate.generate(hashMap);
            hashMap.put("sign", sign);
            String json = JsonHelper.hashMapToJson(hashMap);
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
            RetrofitUtil.getService().insertBoost(body).enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Response<Result> response, Retrofit retrofit) {
                    Result result = response.body();
                    hideWaitDialog();
                    if (result == null) {
                        Toast.makeText(getActivity(), "网络请求出错", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (result.getRet() == 200) {

                        Toast.makeText(getActivity(), "提醒成功！", Toast.LENGTH_SHORT).show();
                        getActivity().finish();

                    } else if (result.getRet() == 111) {
                        SPHelper.clearLoginUser();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        FinishActivityManager.getManager().finishActivity(HomeActivity.class);
                    } else {
                        Toast.makeText(getActivity(), "提醒失败！", Toast.LENGTH_SHORT).show();
                        rightTxt.setEnabled(true);
                    }

                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_SHORT).show();
                    rightTxt.setEnabled(true);
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.project_select:
                projectPickerView.show();
                break;
            case R.id.boost_person:
                if (myProject != null) {
                    Intent intent = new Intent(getActivity(), BoostPersonActivity.class);
                    intent.putExtra("projectId", myProject.getProjectId());
                    intent.putExtra("checkStr", checkStr);
                    intent.putExtra("checkAll", isAll);
                    Log.v("checkStr1", checkStr);
                    this.startActivityForResult(intent, 10);
                } else {
                    Toast.makeText(getActivity(), "请先选择项目", Toast.LENGTH_SHORT).show();
                }
                break;
        }
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
