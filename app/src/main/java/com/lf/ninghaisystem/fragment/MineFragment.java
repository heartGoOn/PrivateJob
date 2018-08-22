package com.lf.ninghaisystem.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.activities.HomeActivity;
import com.lf.ninghaisystem.activities.LoginActivity;
import com.lf.ninghaisystem.activities.MessageActivity;
import com.lf.ninghaisystem.activities.MyApplication;
import com.lf.ninghaisystem.activities.MyDutyActivity;
import com.lf.ninghaisystem.activities.ProjectEvaluateActivity;
import com.lf.ninghaisystem.activities.RepresentEvaluateActivity;
import com.lf.ninghaisystem.activities.SettingActivity;
import com.lf.ninghaisystem.activities.SugFeedbackActivity;
import com.lf.ninghaisystem.activities.UserMsgActivity;
import com.lf.ninghaisystem.activities.base.FinishActivityManager;
import com.lf.ninghaisystem.adapter.MineListAdapter;
import com.lf.ninghaisystem.bean.MineListItem;
import com.lf.ninghaisystem.bean.User;
import com.lf.ninghaisystem.bean.entity.Result;
import com.lf.ninghaisystem.fragment.base.BaseBarFragment;
import com.lf.ninghaisystem.fragment.base.BaseFragment;
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
 * Created by admin on 2017/11/8.
 */

public class MineFragment extends BaseFragment {

    private ListView listView;
    private MineListAdapter adapter;
    private List<MineListItem> datas;
    private User user;

    private TextView tv_p1, tv_p2, tv_p3, tv_name;
    private int flag = 0;   //是否有项目评价

    @Override
    public void initData() {
        super.initData();

        user = MyApplication.loginUser.getEmployeeInfo();

        if (user != null) {

            if (user.getIsNpcMember().equals("1")) {
                flag = 0;
            } else {
                flag = 1;
            }

        }

        datas = new ArrayList<>();
        datas.add(new MineListItem("个人信息"));
        datas.add(new MineListItem("消息列表", 3));
        datas.add(new MineListItem("履职情况"));
        if (flag == 0) {
            datas.add(new MineListItem("项目评价"));
        }
        datas.add(new MineListItem("意见反馈"));
        datas.add(new MineListItem("设置"));

    }

    private Intent intent = null;

    @Override
    public void initViews(View rootView) {
        super.initViews(rootView);

        tv_p1 = rootView.findViewById(R.id.user_position);
        tv_p2 = rootView.findViewById(R.id.user_position2);
        tv_p3 = rootView.findViewById(R.id.user_position3);
        tv_name = rootView.findViewById(R.id.user_name);

        //init();

        listView = rootView.findViewById(R.id.mine_list);
        adapter = new MineListAdapter(getActivity(), R.layout.item_mine, datas);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                switch ((String) view.getTag()) {

                    case "个人信息"://个人信息
                        intent = new Intent(getActivity(), UserMsgActivity.class);
                        //intent.putExtra("user",user);
                        getActivity().startActivity(intent);
                        break;
                    case "消息列表"://消息列表
                        intent = new Intent(getActivity(), MessageActivity.class);
                        getActivity().startActivityForResult(intent, 1);
                        break;
                    case "履职情况"://履职情况
                        intent = new Intent(getActivity(), MyDutyActivity.class);
                        intent.putExtra("isMine", true);
                        getActivity().startActivity(intent);
                        break;
                    case "项目评价"://项目评价
                        projectIsOpenCheck();
                        break;
                    case "设置"://设置
                        intent = new Intent(getActivity(), SettingActivity.class);
                        getActivity().startActivity(intent);
                        break;
                    case "意见反馈"://意见反馈
                        intent = new Intent(getActivity(), SugFeedbackActivity.class);
                        getActivity().startActivity(intent);
                        break;
                }

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        netWorkAction();
    }

    private void init() {

        if (user != null) {

            datas.get(1).setNoticeNum(user.getUnread());

            adapter.updateItem(datas);

            String type = "";

            if (user.getIsNpcMember().equals("1")) {
                tv_p1.setVisibility(View.VISIBLE);
                tv_p1.setText("人大代表");
                type += "  人大代表";
            }
            if (user.getIsGovernmentOffices().equals("1")) {
                tv_p2.setVisibility(View.VISIBLE);
                tv_p2.setText("政府工作人员");
                type += "  政府工作人员";
            }
            if (user.getIsCppccMember().equals("1")) {
                tv_p3.setVisibility(View.VISIBLE);
                tv_p3.setText("人大工作人员");
                type += "  人大工作人员";
            }

//            if (user.getIsNpcMember().equals("1") && user.getIsGovernmentOffices().equals("1")) {   //人大代表
//                tv_p1.setVisibility(View.VISIBLE);
//                tv_p2.setVisibility(View.VISIBLE);
//                tv_p1.setText("人大代表");
//                tv_p2.setText("政府工作人员");
//                type += "人大代表,政府工作人员";
//            } else if (user.getIsNpcMember().equals("1")) {
//                tv_p1.setVisibility(View.VISIBLE);
//                tv_p2.setVisibility(View.GONE);
//                tv_p1.setText("人大代表");
//                type += "人大代表";
//            } else if (user.getIsGovernmentOffices().equals("1")) {
//                tv_p2.setVisibility(View.VISIBLE);
//                tv_p1.setVisibility(View.GONE);
//                tv_p2.setText("政府工作人员");
//                type += "政府工作人员";
//            } else {
//                tv_p2.setVisibility(View.GONE);
//                tv_p1.setVisibility(View.GONE);
//            }
            user.setType(type);
            MyApplication.loginUser.setEmployeeInfo(user);
            tv_name.setText(user.getName());
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    private void netWorkAction() {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", MyApplication.loginUser.getUid());
        hashMap.put("token", MyApplication.loginUser.getToken());
        String sign = SignGenerate.generate(hashMap);
        hashMap.put("sign", sign);

        String json = JsonHelper.hashMapToJson(hashMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);

        RetrofitUtil.getService().getMineMsg(body).enqueue(new Callback<Result<User>>() {
            @Override
            public void onResponse(Response<Result<User>> response, Retrofit retrofit) {

                Result result = response.body();
                if (result == null) {

                    return;
                }
                if (result.getRet() == 200) {

                    user = (User) result.getData();
                    MyApplication.loginUser.setEmployeeInfo(user);
                    init();

                } else if (result.getRet() == 111) {

                    SPHelper.clearLoginUser();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    FinishActivityManager.getManager().finishActivity(HomeActivity.class);
                }

            }

            @Override
            public void onFailure(Throwable t) {

                init();
                Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void projectIsOpenCheck() {
        if (user.getIsNpcMember().equals("1") || user.getIsCppccMember().equals("1")) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("uid", MyApplication.loginUser.getUid());
            hashMap.put("token", MyApplication.loginUser.getToken());
            String sign = SignGenerate.generate(hashMap);
            hashMap.put("sign", sign);

            String json = JsonHelper.hashMapToJson(hashMap);
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);

            RetrofitUtil.getService().checkProjectIsOpen(body).enqueue(new Callback<Result<Boolean>>() {
                @Override
                public void onResponse(Response<Result<Boolean>> response, Retrofit retrofit) {
                    Result result = response.body();

                    if (result.getRet() == 200) {
                        if ((Boolean) result.getData()) {
                            intent = new Intent(getActivity(), ProjectEvaluateActivity.class);
                            getActivity().startActivity(intent);
                        } else {
                            Toast.makeText(getActivity(), "功能未开放", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_SHORT).show();
                }
            });

        }else {
            Toast.makeText(getContext(), "您没有权限评价，具体联系系统管理人员", Toast.LENGTH_SHORT).show();
        }
    }


}
