package com.lf.ninghaisystem.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.activities.LoginActivity;
import com.lf.ninghaisystem.activities.MyApplication;
import com.lf.ninghaisystem.activities.ProjectMainAcitvity;
import com.lf.ninghaisystem.adapter.ProjectListAdapter;
import com.lf.ninghaisystem.bean.entity.Project;
import com.lf.ninghaisystem.bean.entity.Result;
import com.lf.ninghaisystem.fragment.base.BaseBarFragment;
import com.lf.ninghaisystem.fragment.base.NetWorkInterface;
import com.lf.ninghaisystem.http.retrofit.CancelableCallback;
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
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by admin on 2018/1/4.
 */

public class ChildProjectFragment extends BaseBarFragment implements
        BGARefreshLayout.BGARefreshLayoutDelegate, NetWorkInterface {

    private ImageView bgView;
    private BGARefreshLayout mRefreshLayout;
    private ListView listView;
    private ProjectListAdapter adapter;
    private List<Project> projectList;

    private String type = "-1"; //默认-1
    private String json;
    private final int pageSize = 10;   //请求个数
    private int pageIndex = 1;  //当前请求页

    private final int INITACTION = 9;   //初始化
    private final int UPDATEACTION = 10;    //下拉刷新
    private final int MOREACTION = 11;  //上拉加载
    private final int NOTIFYTYPE = 12;  //更新类型
    private boolean flag = true;    //是否可以加载更多
    private int count = 0;  //第一次加载

    private Project project;

    @Override
    public void initData() {
        super.initData();
        project = (Project) getActivity().getIntent().getSerializableExtra("project");
    }

    @Override
    public void initViews(View rootView) {
        super.initViews(rootView);
        setTitle(project.getpName());
        setView(R.layout.fragment_child_homepager);

        listView = rootView.findViewById(R.id.project_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getActivity(), ProjectMainAcitvity.class);
                intent.putExtra("project", projectList.get(i));
                intent.putExtra("isOwn",projectList.get(i).getIsOwn());
                getActivity().startActivity(intent);
            }
        });
        bgView = rootView.findViewById(R.id.bg_img);
        initRefreshLayout(rootView);
        beginRefreshing();
    }

    private void initListView() {
        adapter = new ProjectListAdapter(getActivity(), R.layout.item_project_list, projectList);
        listView.setAdapter(adapter);
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
        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("projectStatus", 0);
//        hashMap.put("projectType", type);
        hashMap.put("pageSize", pageSize + "");
        hashMap.put("pageIndex", pageIndex);
        hashMap.put("parentProjectId", project.getProjectId() + "");
        hashMap.put("uid", MyApplication.loginUser.getUid());
        hashMap.put("token", MyApplication.loginUser.getToken());
        String sign = SignGenerate.generate(hashMap);
        hashMap.put("sign", sign);
        json = JsonHelper.hashMapToJson(hashMap);
        if (count == 0) {
            netRequestAction(INITACTION, json);
            count++;
        } else {
            netRequestAction(UPDATEACTION, json);
        }

        flag = true;
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout bgaRefreshLayout) {
        int length = projectList.size();

        if (length == 0) {
            return false;
        } else if (length % pageSize == 0 && flag) {
            pageIndex++;

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("projectStatus", 0);
            hashMap.put("projectType", type);
            hashMap.put("pageSize", pageSize + "");
            hashMap.put("pageIndex", pageIndex);
            hashMap.put("parentProjectId", project.getProjectId() + "");
            hashMap.put("uid", MyApplication.loginUser.getUid());
            hashMap.put("token", MyApplication.loginUser.getToken());
            String sign = SignGenerate.generate(hashMap);
            hashMap.put("sign", sign);

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
        if (projectList.size() == 0) {
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
        projectList = (List<Project>) object;
        initListView();
    }

    @Override
    public void refreshRequest(Object object) {
        List<Project> datas = (List<Project>) object;

        if (datas.size() == 0 || projectList.size() == 0) {
            projectList = datas;
            adapter.updateItem(projectList);
            endRefreshing();
        } else {

            projectList = datas;
            adapter.updateItem(projectList);
            endRefreshing();

            if (projectList.get(0).getProjectId() == datas.get(0).getProjectId()) {
                Toast.makeText(getActivity(), "暂无新数据", Toast.LENGTH_SHORT).show();
            }

        }

    }

    @Override
    public void moreRequest(Object object) {
        List<Project> datas = (List<Project>) object;

        if (datas.size() == 0) {
            Toast.makeText(getActivity(), "无更多数据", Toast.LENGTH_SHORT).show();

            flag = false;
        } else {
            projectList.addAll(datas);
            adapter.updateItem(projectList);
        }

        endLoadingMore();
    }

    @Override
    public void netRequestAction(final int action, String json) {

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);

        RetrofitUtil.getService().getChildrentProjectList(body).enqueue(new CancelableCallback<Result<List<Project>>>() {
            @Override
            protected void onSuccess(Response<Result<List<Project>>> response, Retrofit retrofit) {
                Result result = response.body();
                if (result == null) {
                    if (action == INITACTION) {
                        initListView();
                    } else {
                        endLoadingMore();
                        endRefreshing();
                    }
                    return;
                }
                if (result.getRet() == 200) {
                    List<Project> datas = (List<Project>) result.getData();
                    if (action == INITACTION) {
                        firstRequest(datas);
                    } else if (action == NOTIFYTYPE) {
                        refreshRequest(datas);
                    } else if (action == UPDATEACTION) {
                        refreshRequest(datas);
                    } else {
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
                    endLoadingMore();
                } else if (action == NOTIFYTYPE) {
                    endRefreshing();
                }
            }
        });
    }
}
