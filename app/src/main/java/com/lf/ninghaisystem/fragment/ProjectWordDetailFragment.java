package com.lf.ninghaisystem.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.activities.HomeActivity;
import com.lf.ninghaisystem.activities.LoginActivity;
import com.lf.ninghaisystem.activities.MyApplication;
import com.lf.ninghaisystem.activities.WordActivity;
import com.lf.ninghaisystem.adapter.DutyListAdapter;
import com.lf.ninghaisystem.adapter.PJWordListAdapter;
import com.lf.ninghaisystem.bean.entity.Duty;
import com.lf.ninghaisystem.bean.entity.PJWord;
import com.lf.ninghaisystem.bean.entity.PjWordType;
import com.lf.ninghaisystem.bean.entity.Result;
import com.lf.ninghaisystem.fragment.base.BaseBarFragment;
import com.lf.ninghaisystem.fragment.base.NetWorkInterface;
import com.lf.ninghaisystem.http.retrofit.RetrofitUtil;
import com.lf.ninghaisystem.util.JsonHelper;
import com.lf.ninghaisystem.util.SPHelper;
import com.lf.ninghaisystem.util.SignGenerate;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.util.HashMap;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by admin on 2017/12/5.
 */

public class ProjectWordDetailFragment extends BaseBarFragment
        implements BGARefreshLayout.BGARefreshLayoutDelegate,NetWorkInterface {

    private ImageView bgView;
    private BGARefreshLayout mRefreshLayout;
    private ListView pjWdDetailListView;
    private PJWordListAdapter adapter;
    private List<PJWord> pjWordList;

    private final int INITACTION = 9;   //初始化
    private final int UPDATEACTION = 10;    //下拉刷新
    private final int MOREACTION = 11;  //上拉加载
    private int count = 0;  //第一次加载
    private boolean flag = true;    //是否可以加载更多

    private String json;    //请求json

    private PjWordType pjWordType = null;
    private int projectId;
    private int documentType;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void initData() {
        super.initData();

        Intent intent = getActivity().getIntent();

        projectId = intent.getIntExtra("projectId",0);
        pjWordType = (PjWordType) intent.getSerializableExtra("pjWordType");

        documentType = pjWordType.getTypeId();

    }

    @Override
    public void initViews(View rootView) {
        super.initViews(rootView);
        setView(R.layout.fragment_project_word);
        setTitle(pjWordType.getTypeName());

        pjWdDetailListView = rootView.findViewById(R.id.project_word_list);
        pjWdDetailListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            Intent intent = new Intent(getActivity(), WordActivity.class);
            intent.putExtra("word",pjWordList.get(i));
            startActivity(intent);

            }
        });

        bgView = rootView.findViewById(R.id.bg_img);

        initRefreshLayout(rootView);

        beginRefreshing();

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

    private void initListView() {

        adapter = new PJWordListAdapter(getActivity(),R.layout.item_project_word,pjWordList);
        pjWdDetailListView.setAdapter(adapter);
        endRefreshing();
    }

    public void beginRefreshing() {
        mRefreshLayout.beginRefreshing();
    }

    public void endRefreshing() {
        mRefreshLayout.endRefreshing();
        if (pjWordList.size() == 0) {
            bgView.setVisibility(View.VISIBLE);
        } else {
            bgView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout bgaRefreshLayout) {

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("projectId", projectId);
        hashMap.put("documentType", documentType);
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
        return false;
    }

    @Override
    public void firstRequest(Object object) {
        initListView();
    }

    @Override
    public void refreshRequest(Object object) {

        List<PJWord> datas = (List<PJWord>) object;

        if(datas.size() == 0 || pjWordList.size() == 0) {
            pjWordList = datas;
            //adapter.updateItem(pjWordList);
            endRefreshing();
        } else {

            pjWordList = datas;
            //adapter.updateItem(pjWordList);
            endRefreshing();

            if(pjWordList.get(0).getDocumentId() == datas.get(0).getDocumentId()) {

                Toast.makeText(getActivity(),"暂无新数据",Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void moreRequest(Object object) {

    }

    @Override
    public void netRequestAction(final int action, String json) {

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);

        RetrofitUtil.getService().getPjWordList(body).enqueue(new Callback<Result<List<PJWord>>>() {
            @Override
            public void onResponse(Response<Result<List<PJWord>>> response, Retrofit retrofit) {

                Result result = response.body();
                if (result.getRet() == 200) {
                    List<PJWord> datas = (List<PJWord>) result.getData();
                    if(action == INITACTION) { //如果是第一次请求
                        pjWordList = datas;
                        firstRequest(pjWordList);
                    } else if(action == UPDATEACTION){
                        //判断数据是否有新数据 -- 下拉刷新
                        refreshRequest(datas);

                    } else if(action == MOREACTION) {
                        moreRequest(datas);
                    }

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
        return false;
    }

    @Override
    protected boolean isRightImg() {
        return false;
    }
}
