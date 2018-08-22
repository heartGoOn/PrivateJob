package com.lf.ninghaisystem.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.activities.DutyPersonDetailedActivity;
import com.lf.ninghaisystem.activities.LoginActivity;
import com.lf.ninghaisystem.activities.MyApplication;
import com.lf.ninghaisystem.adapter.DutyPersonListAdapter;
import com.lf.ninghaisystem.bean.entity.DutyPerson;
import com.lf.ninghaisystem.bean.entity.DutyRank;
import com.lf.ninghaisystem.bean.entity.Project;
import com.lf.ninghaisystem.bean.entity.Result;
import com.lf.ninghaisystem.fragment.base.BaseBarFragment;
import com.lf.ninghaisystem.fragment.base.NetWorkInterface;
import com.lf.ninghaisystem.http.retrofit.CancelableCallback;
import com.lf.ninghaisystem.http.retrofit.RetrofitUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;

import com.lf.ninghaisystem.util.JsonHelper;
import com.lf.ninghaisystem.util.SPHelper;
import com.lf.ninghaisystem.util.SignGenerate;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by admin on 2017/11/14.
 */

public class DutyPersonFragment extends BaseBarFragment
        implements BGARefreshLayout.BGARefreshLayoutDelegate,NetWorkInterface {

    private ImageView bgView;
    private BGARefreshLayout mRefreshLayout;
    private ListView dutyPersonListView;
    private DutyPersonListAdapter adapter;
    private List<DutyPerson> dutyPersonList;

    private final int INITACTION = 9;   //初始化
    private final int UPDATEACTION = 10;    //下拉刷新
    private final int MOREACTION = 11;  //上拉加载
    private boolean flag = true;    //是否可以加载更多

    private int count = 0;  //第一次加载
    private String json;    //请求json
    private final int pageSize = 10;   //请求个数
    private int pageIndex = 1;  //当前请求页

    private Project project;

    @Override
    public void initData() {
        super.initData();

        dutyPersonList = new ArrayList<>();
        project = (Project) getActivity().getIntent().getSerializableExtra("project");
    }

    @Override
    public void initViews(View rootView) {
        super.initViews(rootView);
        setView(R.layout.fragment_duty_person);
        setTitle("履职代表");

        dutyPersonListView = rootView.findViewById(R.id.duty_person_list);
        dutyPersonListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), DutyPersonDetailedActivity.class);
                intent.putExtra("dutyPerson",dutyPersonList.get(i));
                startActivity(intent);
            }
        });

        bgView = rootView.findViewById(R.id.bg_img);
        initRefreshLayout(rootView);
        beginRefreshing();

    }

    private void initListView() {

        adapter = new DutyPersonListAdapter(getActivity(),R.layout.item_duty_person,dutyPersonList);
        dutyPersonListView.setAdapter(adapter);
        endRefreshing();

    }

    private void initRefreshLayout(View rootView) {

        mRefreshLayout = rootView.findViewById(R.id.fresh_layout);
        // 为BGARefreshLayout 设置代理
        mRefreshLayout.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(getActivity(), true);
        // 设置下拉刷新和上拉加载更多的风格
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);

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

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout bgaRefreshLayout) {

        pageIndex = 1;

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("projectId",project.getProjectId());
        hashMap.put("pageIndex",pageIndex);
        hashMap.put("pageSize",pageSize);
        hashMap.put("uid", MyApplication.loginUser.getUid());
        hashMap.put("token",MyApplication.loginUser.getToken());
        String sign = SignGenerate.generate(hashMap);
        hashMap.put("sign",sign);

        json = JsonHelper.hashMapToJson(hashMap);

        if(count == 0 ) {
            netRequestAction(INITACTION,json);
            count++;
        } else {
            netRequestAction(UPDATEACTION,json);
        }

        flag = true;
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout bgaRefreshLayout) {

        int length = dutyPersonList.size();
        if (length == 0) {
            return false;
        } else if (length % pageSize == 0 && flag) {
            pageIndex++;

            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("projectId",project.getProjectId());
            hashMap.put("pageIndex",pageIndex);
            hashMap.put("pageSize",pageSize);
            hashMap.put("uid", MyApplication.loginUser.getUid());
            hashMap.put("token",MyApplication.loginUser.getToken());
            String sign = SignGenerate.generate(hashMap);
            hashMap.put("sign",sign);

            json = JsonHelper.hashMapToJson(hashMap);

            netRequestAction(MOREACTION, json);
            return true;

        } else {
            return false;
        }

    }

    public void beginRefreshing() {
        mRefreshLayout.beginRefreshing();
    }

    public void endRefreshing() {
        mRefreshLayout.endRefreshing();

        if (dutyPersonList.size() == 0) {
            bgView.setVisibility(View.VISIBLE);
        } else {
            bgView.setVisibility(View.INVISIBLE);
        }
    }

    public void endLoadingMore() {
        mRefreshLayout.endLoadingMore();
    }

    @Override
    public void firstRequest(Object object) {
        initListView();
    }

    @Override
    public void refreshRequest(Object object) {

        List<DutyPerson> datas = (List<DutyPerson>) object;

        if(datas.size() == 0 || dutyPersonList.size() == 0) {
            dutyPersonList = datas;
            adapter.updateItem(dutyPersonList);
            endRefreshing();
        } else {

            dutyPersonList = datas;
            adapter.updateItem(dutyPersonList);
            endRefreshing();

            if(dutyPersonList.get(0).getId() == datas.get(0).getId()) {
                Toast.makeText(getActivity(),"暂无新数据",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void moreRequest(Object object) {

        List<DutyPerson> datas = (List<DutyPerson>) object;

        if (datas.size() == 0) {
            Toast.makeText(getActivity(), "无更多数据", Toast.LENGTH_SHORT).show();

            flag = false;
        } else {
            dutyPersonList.addAll(datas);
            adapter.updateItem(dutyPersonList);
        }

        endLoadingMore();

    }

    public void netRequestAction(final int action, String json) {

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);

        RetrofitUtil.getService().getDutyPersonList(body).enqueue(new CancelableCallback<Result<List<DutyPerson>>>() {
            @Override
            protected void onSuccess(Response<Result<List<DutyPerson>>> response, Retrofit retrofit) {

                Result result = response.body();
                if(result == null) {
                    endRefreshing();
                    return;
                }
                if (result.getRet() == 200) {

                    List<DutyPerson> datas = (List<DutyPerson>) result.getData();

                    if(action == INITACTION) { //如果是第一次请求
                        dutyPersonList = datas;
                        firstRequest(dutyPersonList);
                    } else if(action == UPDATEACTION){
                        //判断数据是否有新数据 -- 下拉刷新
                        refreshRequest(datas);

                    } else if(action == MOREACTION) {
                        moreRequest(datas);
                    }

                } else if (result.getRet() == 111) {

                    SPHelper.clearLoginUser();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }

            }

            @Override
            protected void onFail(Throwable t) {

                if (action == INITACTION) {
                    initListView();
                } else if (action == MOREACTION) {
                    //endLoadingMore();
                } else {
                    endRefreshing();
                }

            }
        });

    }

}
