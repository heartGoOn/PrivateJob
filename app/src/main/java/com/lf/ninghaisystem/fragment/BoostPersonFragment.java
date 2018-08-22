package com.lf.ninghaisystem.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.activities.HomeActivity;
import com.lf.ninghaisystem.activities.LoginActivity;
import com.lf.ninghaisystem.activities.MyApplication;
import com.lf.ninghaisystem.activities.base.FinishActivityManager;
import com.lf.ninghaisystem.bean.entity.DutyPerson;
import com.lf.ninghaisystem.bean.entity.LoginUser;
import com.lf.ninghaisystem.bean.entity.Result;
import com.lf.ninghaisystem.fragment.base.BaseBarFragment;
import com.lf.ninghaisystem.http.retrofit.RetrofitUtil;
import com.lf.ninghaisystem.util.JsonHelper;
import com.lf.ninghaisystem.util.SPHelper;
import com.lf.ninghaisystem.util.SignGenerate;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by admin on 2017/12/11.
 */

public class BoostPersonFragment extends BaseBarFragment implements BoostPersonAdapter.OnCheckListener {

    private ListView boostPersonListView;
    private List<DutyPerson> dutyPersonList;
    private BoostPersonAdapter adapter;
    private CheckBox cb_all;
    private int projectId;
    private LoginUser loginUser;
    private String[] lastChecks;
    private List<String> datas;
    private boolean isAll;

    @Override
    public void initData() {
        super.initData();

        loginUser = MyApplication.loginUser;
        Intent intent = getActivity().getIntent();
        projectId = intent.getIntExtra("projectId", -1);
        lastChecks = intent.getStringExtra("checkStr").split("\\|");
        isAll = intent.getBooleanExtra("checkAll", false);
        datas = Arrays.asList(lastChecks);

        //Log.v("lastChecks",lastChecks[0]);

        dutyPersonList = new ArrayList<>();

        netWorkAction();
    }

    @Override
    public void initViews(View rootView) {
        super.initViews(rootView);
        setView(R.layout.fragment_boost_person);
        setTitle("提醒对象");
        setRightTxt("确认");
        boostPersonListView = rootView.findViewById(R.id.person_list);
        cb_all = rootView.findViewById(R.id.all_check);
        cb_all.setChecked(isAll);

    }

    @Override
    protected void onRightBtnClick(View v) {
        super.onRightBtnClick(v);
        Intent intent = new Intent();
        intent.putExtra("checkStr", adapter.getCheckStr());
        intent.putExtra("checkAll", cb_all.isChecked());
        getActivity().setResult(5, intent);
        getActivity().finish();

    }

    private void initListView() {

        for (int i = 0; i < dutyPersonList.size(); i++) {

            DutyPerson dutyPerson = dutyPersonList.get(i);

            if (datas.contains(dutyPerson.getId() + "")) {
                dutyPersonList.get(i).setSelected(true);
            }

        }

        adapter = new BoostPersonAdapter(getActivity(), R.layout.item_duty_person_select, dutyPersonList);
        boostPersonListView.setAdapter(adapter);
        adapter.setOnCheckListener(this);
        cb_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cb_all.isChecked()) {
                    for (int i = 0; i < dutyPersonList.size(); i++) {
                        dutyPersonList.get(i).setSelected(true);
                    }
                } else {
                    for (int i = 0; i < dutyPersonList.size(); i++) {
                        dutyPersonList.get(i).setSelected(false);
                    }
                }
                adapter.updateItem(dutyPersonList);
            }
        });

    }

    private void netWorkAction() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("projectId", projectId);
        hashMap.put("uid", loginUser.getUid());
        hashMap.put("token", loginUser.getToken());
        String sign = SignGenerate.generate(hashMap);
        hashMap.put("sign", sign);
        String json = JsonHelper.hashMapToJson(hashMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        RetrofitUtil.getService().getDutyPersonSelect(body).enqueue(new Callback<Result<List<DutyPerson>>>() {
            @Override
            public void onResponse(Response<Result<List<DutyPerson>>> response, Retrofit retrofit) {
                Result result = response.body();
                if (result.getRet() == 200) {
                    dutyPersonList = (List<DutyPerson>) result.getData();
                    initListView();
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
                getActivity().finish();
                Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_SHORT).show();
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


    @Override
    public void OnNoneCheck() {
        cb_all.setChecked(false);
    }

    @Override
    public void OnAllCheck() {
        cb_all.setChecked(true);
    }
}

class BoostPersonAdapter extends ArrayAdapter<DutyPerson> {

    private List<DutyPerson> dutyPersonList;
    int resource;
    Context context;
    private String checkStr = "";
    private int countFlag = 0;

    public BoostPersonAdapter(@NonNull Context context, int resource, @NonNull List<DutyPerson> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        dutyPersonList = objects;
    }

    @Override
    public int getCount() {
        return dutyPersonList.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        DutyPerson dutyPerson = dutyPersonList.get(position);

        if (convertView == null) {

            convertView = ((Activity) context).getLayoutInflater().inflate(resource, null);

        }

        final CheckBox checkBox = convertView.findViewById(R.id.check_box);

        if (dutyPerson.isSelected()) {
            countFlag++;
        }
        checkBox.setChecked(dutyPerson.isSelected());
        checkBox.setText(dutyPerson.getName());

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dutyPersonList.get(position).setSelected(checkBox.isChecked());
                if (!checkBox.isChecked()) {
                    onCheckListener.OnNoneCheck();
                    countFlag--;
                    countFlag--;
                } else {
                    countFlag++;
                    countFlag++;
                }
                if (countFlag == dutyPersonList.size() * 2) {
                    onCheckListener.OnAllCheck();
                }
            }
        });
        dutyPerson = null;
        return convertView;
    }

    public String getCheckStr() {

        for (int i = 0; i < dutyPersonList.size(); i++) {

            DutyPerson dutyPerson = dutyPersonList.get(i);

            if (dutyPerson.isSelected()) {

                checkStr += (dutyPerson.getId() + "|");

            }

        }

        if (!TextUtils.isEmpty(checkStr)) {
            checkStr = checkStr.substring(0, checkStr.length() - 1);
        }

        return checkStr;

    }

    public void updateItem(List<DutyPerson> datas) {
        dutyPersonList = datas;
        countFlag = 0;
        notifyDataSetChanged();
    }

    interface OnCheckListener {
        void OnNoneCheck();

        void OnAllCheck();
    }

    private OnCheckListener onCheckListener;

    public void setOnCheckListener(OnCheckListener onCheckListener) {
        this.onCheckListener = onCheckListener;
    }
}
