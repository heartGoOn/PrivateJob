package com.lf.ninghaisystem.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.activities.LoginActivity;
import com.lf.ninghaisystem.activities.MyApplication;
import com.lf.ninghaisystem.activities.ProjectWordDetailActivity;
import com.lf.ninghaisystem.bean.entity.PjWordType;
import com.lf.ninghaisystem.bean.entity.Result;
import com.lf.ninghaisystem.fragment.base.BaseBarFragment;
import com.lf.ninghaisystem.http.retrofit.CancelableCallback;
import com.lf.ninghaisystem.http.retrofit.RetrofitUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lf.ninghaisystem.util.JsonHelper;
import com.lf.ninghaisystem.util.SPHelper;
import com.lf.ninghaisystem.util.SignGenerate;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by admin on 2017/11/16.
 */

public class ProjectWordFragment extends BaseBarFragment {

    private ProjectWordTypeAdapter adapter;
    private ListView lv_type;
    private List<PjWordType> pjWordTypeList = new ArrayList<>();
    private int projectId;

    @Override
    public void initData() {
        super.initData();

        projectId = getActivity().getIntent().getIntExtra("projectId",0);

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("uid", MyApplication.loginUser.getUid());
        hashMap.put("token",MyApplication.loginUser.getToken());
        String sign = SignGenerate.generate(hashMap);
        hashMap.put("sign",sign);

        String json = JsonHelper.hashMapToJson(hashMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);

        RetrofitUtil.getService().getPjWordTypeList(body).enqueue(new CancelableCallback<Result<List<PjWordType>>>() {
            @Override
            protected void onSuccess(Response<Result<List<PjWordType>>> response, Retrofit retrofit) {

                Result result = response.body();
                if(result.getRet() == 200) {

                    pjWordTypeList = (List<PjWordType>) result.getData();
                    initListView();
                } else if(result.getRet() == 111) {

                    SPHelper.clearLoginUser();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }

            }

            @Override
            protected void onFail(Throwable t) {

            }
        });

    }

    @Override
    public void initViews(View rootView) {
        super.initViews(rootView);
        setView(R.layout.fragment_project_word_type);
        setTitle("项目文档");
        lv_type = rootView.findViewById(R.id.pj_word_type_list);
        lv_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ProjectWordDetailActivity.class);
                intent.putExtra("projectId",projectId);
                intent.putExtra("pjWordType",pjWordTypeList.get(i));
                startActivity(intent);
            }
        });

    }

    private void initListView() {
        adapter = new ProjectWordTypeAdapter(getActivity(),R.layout.item_project_word_type,pjWordTypeList);
        lv_type.setAdapter(adapter);
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

class ProjectWordTypeAdapter extends ArrayAdapter<PjWordType> {

    List<PjWordType> pjWordTypeList = new ArrayList<>();
    int resource;
    Context context;

    public ProjectWordTypeAdapter(@NonNull Context context, int resource, @NonNull List<PjWordType> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.pjWordTypeList = objects;

    }

    @Override
    public int getCount() {
        return pjWordTypeList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;

        if(convertView == null) {

            view =((Activity)context).getLayoutInflater().inflate(resource,null);

        } else {
            view = convertView;
        }

        TextView tv_name = view.findViewById(R.id.word_type);
        tv_name.setText(pjWordTypeList.get(position).getTypeName());

        return view;
    }
}
