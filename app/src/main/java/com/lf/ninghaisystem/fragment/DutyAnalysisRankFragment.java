package com.lf.ninghaisystem.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.activities.LoginActivity;
import com.lf.ninghaisystem.activities.MyApplication;
import com.lf.ninghaisystem.adapter.DutyRankAdapter;
import com.lf.ninghaisystem.bean.entity.DutyRank;
import com.lf.ninghaisystem.bean.entity.Project;
import com.lf.ninghaisystem.bean.entity.Result;
import com.lf.ninghaisystem.fragment.base.BaseFragment;
import com.lf.ninghaisystem.fragment.base.NetWorkInterface;
import com.lf.ninghaisystem.http.retrofit.RetrofitUtil;
import com.lf.ninghaisystem.util.JsonHelper;
import com.lf.ninghaisystem.util.SPHelper;
import com.lf.ninghaisystem.util.SignGenerate;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by admin on 2017/11/16.
 */

public class DutyAnalysisRankFragment extends BaseFragment
        implements BGARefreshLayout.BGARefreshLayoutDelegate,NetWorkInterface {

    private ImageView bgView;
    private BGARefreshLayout mRefreshLayout;

    private ListView rankListView;
    private DutyRankAdapter adapter;
    private List<DutyRank> dutyRankList;

    private final int INITACTION = 9;   //初始化
    private final int UPDATEACTION = 10;    //下拉刷新
    private final int MOREACTION = 11;  //上拉加载
    private int count = 0;  //第一次加载
    private boolean flag = true;    //是否可以加载更多

    private String json;    //请求json
    private final int pageSize = 10;   //请求个数
    private int pageIndex = 1;  //当前请求页
    private Project project;

    @Override
    public void initData() {
        super.initData();

        dutyRankList = new ArrayList<>();

        project = (Project) getActivity().getIntent().getSerializableExtra("project");

    }

    @Override
    public void initViews(View rootView) {
        super.initViews(rootView);

        rankListView = rootView.findViewById(R.id.analysis_rank_list);
        bgView = rootView.findViewById(R.id.bg_img);
        initRefreshLayout(rootView);
        beginRefreshing();
    }

    private void initListView() {

        adapter = new DutyRankAdapter(getActivity(),R.layout.item_duty_analysis_active,dutyRankList);
        rankListView.setAdapter(adapter);
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
    protected int getLayoutId() {
        return R.layout.fragment_analysis_rank;
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout bgaRefreshLayout) {

        /*json = "{\"projectId\":2,\"pageSize\":\"10\",\"pageIndex\":1,\"uid\":1" +
                ",\"token\":\"1iph5co3\",\"sign\":\"3A1F833B30000C4AA66745103A698664\"}";*/

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

        int length = dutyRankList.size();

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

    public void endRefreshing() {mRefreshLayout.endRefreshing();
        if (dutyRankList.size() == 0) {
            bgView.setVisibility(View.VISIBLE);
        } else {
            bgView.setVisibility(View.INVISIBLE);
        }}

    public void endLoadingMore() {
        mRefreshLayout.endLoadingMore();
    }

    @Override
    public void netRequestAction(final int action,String json) {

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);

        RetrofitUtil.getService().getDutyRankList(body).enqueue(new Callback<Result<List<DutyRank>>>() {
            @Override
            public void onResponse(Response<Result<List<DutyRank>>> response, Retrofit retrofit) {

                Result result = response.body();

                if(result == null) {
                    if(action == INITACTION) {
                        initListView();
                    } else {
                        endLoadingMore();
                        endRefreshing();
                    }
                    return;
                }
                if(result.getRet() == 200) {

                    List<DutyRank> datas = (List<DutyRank>) result.getData();

                    if(action == INITACTION) {
                        dutyRankList = datas;
                        firstRequest(dutyRankList);
                    } else if(action == UPDATEACTION){
                        refreshRequest(datas);
                    } else if (action == MOREACTION) {
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
            public void onFailure(Throwable t) {

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

    @Override
    public void firstRequest(Object object) {
        initListView();
    }

    @Override
    public void refreshRequest(Object object) {

        List<DutyRank> datas = (List<DutyRank>) object;

        if(datas.size() == 0 || dutyRankList.size() == 0) {
            dutyRankList = datas;
            adapter.updateItem(dutyRankList);
            endRefreshing();
        } else {

            dutyRankList = datas;
            adapter.updateItem(dutyRankList);
            endRefreshing();

            if(dutyRankList.get(0).getId() == datas.get(0).getId()) {
                Toast.makeText(getActivity(),"暂无新数据",Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void moreRequest(Object object) {

        List<DutyRank> datas = (List<DutyRank>) object;

        if (datas.size() == 0) {
            Toast.makeText(getActivity(), "无更多数据", Toast.LENGTH_SHORT).show();

            flag = false;
        } else {
            dutyRankList.addAll(datas);
            adapter.updateItem(dutyRankList);
        }

        endLoadingMore();

    }
}
