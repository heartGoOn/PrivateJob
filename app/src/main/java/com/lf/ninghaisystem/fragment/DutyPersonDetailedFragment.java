package com.lf.ninghaisystem.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.activities.LoginActivity;
import com.lf.ninghaisystem.activities.MyApplication;
import com.lf.ninghaisystem.bean.entity.DutyPDetail;
import com.lf.ninghaisystem.bean.entity.DutyPerson;
import com.lf.ninghaisystem.bean.entity.Result;
import com.lf.ninghaisystem.fragment.base.BaseBarFragment;
import com.lf.ninghaisystem.http.retrofit.CancelableCallback;
import com.lf.ninghaisystem.http.retrofit.RetrofitUtil;
import com.lf.ninghaisystem.util.JsonHelper;
import com.lf.ninghaisystem.util.SPHelper;
import com.lf.ninghaisystem.util.SignGenerate;

import java.util.HashMap;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by admin on 2017/11/23.
 */

public class DutyPersonDetailedFragment extends BaseBarFragment {

    private DutyPerson dutyPerson;
    private TextView tv_phone;
    private TextView tv_type;
    private TextView tv_area;
    private TextView tv_department;
    private TextView tv_team;

    @Override
    public void initData() {
        super.initData();

        dutyPerson = (DutyPerson) getActivity().getIntent().getSerializableExtra("dutyPerson");
        netRequestAction();
    }

    @Override
    public void initViews(View rootView) {
        super.initViews(rootView);
        setView(R.layout.fragment_duty_p_detail);
        setTitle(dutyPerson.getName());

        tv_phone = rootView.findViewById(R.id.phone);
        tv_type = rootView.findViewById(R.id.type);
        tv_area = rootView.findViewById(R.id.area);
        tv_department = rootView.findViewById(R.id.department);
        tv_team = rootView.findViewById(R.id.team);

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

    private void netRequestAction() {

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("employeeId",dutyPerson.getId()+"");
        hashMap.put("uid", MyApplication.loginUser.getUid());
        hashMap.put("token",MyApplication.loginUser.getToken());
        String sign = SignGenerate.generate(hashMap);
        hashMap.put("sign",sign);

        String json = JsonHelper.hashMapToJson(hashMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);

        RetrofitUtil.getService().getDutyPDetail(body).enqueue(new CancelableCallback<Result<DutyPDetail>>() {
            @Override
            protected void onSuccess(Response<Result<DutyPDetail>> response, Retrofit retrofit) {

                Result result = response.body();
                if(result.getRet() == 200) {

                    DutyPDetail dutyPDetail = (DutyPDetail) result.getData();
                    tv_phone.setText(dutyPDetail.getPhone());
                    tv_type.setText(dutyPDetail.getRepresentativeType());
                    tv_area.setText(dutyPDetail.getArea());
                    tv_department.setText(dutyPDetail.getDepartment());
                    tv_team.setText(dutyPDetail.getRepresentativeTeam());
                } else if(result.getRet() == 111){
                    SPHelper.clearLoginUser();
                    Toast.makeText(getActivity(),"登录过期",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(),LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    Toast.makeText(getActivity(),"数据异常",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            protected void onFail(Throwable t) {

            }
        });

    }

}
