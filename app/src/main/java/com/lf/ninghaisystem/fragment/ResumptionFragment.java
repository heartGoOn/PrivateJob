package com.lf.ninghaisystem.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.activities.AnalysisActivity;
import com.lf.ninghaisystem.activities.BoostActivity;
import com.lf.ninghaisystem.activities.DutyConditionActivity;
import com.lf.ninghaisystem.activities.DutyReportActivity;
import com.lf.ninghaisystem.activities.LoginActivity;
import com.lf.ninghaisystem.activities.MyApplication;
import com.lf.ninghaisystem.adapter.DutyListAdapter;
import com.lf.ninghaisystem.adapter.PopupAdapter;
import com.lf.ninghaisystem.bean.User;
import com.lf.ninghaisystem.bean.entity.Duty;
import com.lf.ninghaisystem.bean.entity.LoginUser;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static android.app.Activity.RESULT_OK;

/**
 * Created by admin on 2017/11/8.
 */

public class ResumptionFragment extends BaseBarFragment
        implements BGARefreshLayout.BGARefreshLayoutDelegate, NetWorkInterface {

    private ImageView bgView;
    private BGARefreshLayout mRefreshLayout;
    private ListView dutyListView;
    private DutyListAdapter adapter;
    private List<Duty> dutyList;

    private final int INITACTION = 9;   //初始化
    private final int UPDATEACTION = 10;    //下拉刷新
    private final int MOREACTION = 11;  //上拉加载
    private int count = 0;  //第一次加载
    private boolean flag = true;    //是否可以加载更多

    private String json;    //请求json
    private final int pageSize = 10;   //请求个数
    private int pageIndex = 1;  //当前请求页
    private Project mProject;  //当前项目
    private int projectId;
    private int isOwn;
    int isHistory = 0;//自己默认的

    private LoginUser loginUser;
    private User user;
    private boolean isMine; //标记是否为个人履职

    public final static int REPORT = 20;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        beginRefreshing();
    }

    @Override
    public void initData() {
        super.initData();
        loginUser = MyApplication.loginUser;
        user = loginUser.getEmployeeInfo();
        dutyList = new ArrayList<>();
        Intent intent = getActivity().getIntent();
        mProject = (Project) intent.getSerializableExtra("project");
        isOwn = intent.getIntExtra("isOwn", 0);
        isHistory = intent.getIntExtra("isHistory", 0);
        isMine = intent.getBooleanExtra("isMine", false);
        if (mProject != null) {
            projectId = mProject.getProjectId();
//            isOwn = mProject.getIsOwn();
        } else {
            projectId = -1;
            isOwn = 1;
        }

    }

    @Override
    public void initViews(View rootView) {
        super.initViews(rootView);

        setTitle(R.string.title_resumption);
        setRightImg(R.mipmap.duty_more);
        setView(R.layout.fragment_resumption);

        dutyListView = rootView.findViewById(R.id.duty_list);
        dutyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getActivity(), DutyConditionActivity.class);
                intent.putExtra("projectId", projectId);
                intent.putExtra("duty", dutyList.get(i));
                intent.putExtra("isOwn", dutyList.get(i).getIsOwn());
                intent.putExtra("isHistory", dutyList.get(i).getIsHistory());
                startActivity(intent);
            }
        });

        bgView = rootView.findViewById(R.id.bg_img);

        initRefreshLayout(rootView);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case REPORT:
                    beginRefreshing();
                    break;
            }
        }
    }

    private Intent intent = null;
    private PopupWindow window;

    @Override
    protected void onRightBtnClick(View v) {
        super.onRightBtnClick(v);
        View popupView = getActivity().getLayoutInflater().inflate(R.layout.popupwindow, null);
        ListView lsvMore = (ListView) popupView.findViewById(R.id.lsvMore);
        lsvMore.setAdapter(new PopupAdapter(getActivity(), R.layout.item_popup, isOwn, isHistory));
        if (user == null) {
            window = new PopupWindow(popupView, 0, 0);
        }
        //只显示一行
        else if (isHistory == 1) {
            window = new PopupWindow(popupView, getResources().getDimensionPixelSize(R.dimen.popup_window_width)
                    , getResources().getDimensionPixelSize(R.dimen.popup_window_height2));
        } else if (user.getIsNpcMember().equals("0")
                && user.getIsCppccMember().equals("0")) {
            window = new PopupWindow(popupView, getResources().getDimensionPixelSize(R.dimen.popup_window_width)
                    , getResources().getDimensionPixelSize(R.dimen.popup_window_height2));

        } else if (user.getIsNpcMember().equals("1") && isOwn == 0 && user.getIsCppccMember().equals("0")) {
            window = new PopupWindow(popupView, getResources().getDimensionPixelSize(R.dimen.popup_window_width)
                    , getResources().getDimensionPixelSize(R.dimen.popup_window_height2));
        }
        //显示3行
        else if (user.getIsNpcMember().equals("1")
                && user.getIsCppccMember().equals("1")
                && isOwn == 1
                && isHistory == 0) {

            window = new PopupWindow(popupView, getResources().getDimensionPixelSize(R.dimen.popup_window_width)
                    , getResources().getDimensionPixelSize(R.dimen.popup_window_height));
        } else {
            window = new PopupWindow(popupView, getResources().getDimensionPixelSize(R.dimen.popup_window_width)
                    , getResources().getDimensionPixelSize(R.dimen.popup_window_height1));
        }

        lsvMore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                switch ((String) view.getTag()) {
                    case "履职":

                        intent = new Intent(getActivity(), DutyReportActivity.class);
                        if (mProject != null) {
                            intent.putExtra("project", mProject);
                        }
                        startActivityForResult(intent, REPORT);
                        break;

                    case "提醒":

                        intent = new Intent(getActivity(), BoostActivity.class);
                        if (mProject != null) {
                            intent.putExtra("project", mProject);
                        }
                        getActivity().startActivity(intent);
                        break;

                    case "分析":
                        intent = new Intent(getActivity(), AnalysisActivity.class);
                        if (mProject != null) {
                            intent.putExtra("project", mProject);
                        }
                        getActivity().startActivity(intent);
                        break;
                }
                window.dismiss();
            }
        });


        window.setOutsideTouchable(true);
        window.setFocusable(true);
        window.setBackgroundDrawable(new ColorDrawable(R.drawable.popupwindow_bg));
        window.update();
        window.showAsDropDown(rightImg, 5, 0);
    }

    @Override
    protected boolean hasBackBtn() {
        if (projectId == -1 && !isMine) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected boolean hasRightBtn() {

        return true;
    }

    @Override
    protected boolean isRightImg() {

        return true;
    }

    private void initListView() {

        adapter = new DutyListAdapter(getActivity(), R.layout.item_duty, dutyList);
        dutyListView.setAdapter(adapter);
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
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout bgaRefreshLayout) {

        pageIndex = 1;

        HashMap<String, Object> hashMap = new HashMap<>();
        if (!isMine) {
            hashMap.put("projectId", projectId + "");
        }
        hashMap.put("pageIndex", pageIndex);
        hashMap.put("pageSize", pageSize);
        hashMap.put("uid", loginUser.getUid());
        hashMap.put("token", loginUser.getToken());
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

        int length = dutyList.size();

        if (length == 0) {
            return false;
        } else if (length % pageSize == 0 && flag) {
            pageIndex++;

            HashMap<String, Object> hashMap = new HashMap<>();
            if (!isMine) {
                hashMap.put("projectId", projectId + "");
            }
            hashMap.put("pageIndex", pageIndex);
            hashMap.put("pageSize", pageSize);
            hashMap.put("uid", loginUser.getUid());
            hashMap.put("token", loginUser.getToken());
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
        if (dutyList.size() == 0) {
            bgView.setVisibility(View.VISIBLE);
        } else {
            bgView.setVisibility(View.INVISIBLE);
        }
    }

    public void endLoadingMore() {
        mRefreshLayout.endLoadingMore();
    }

    @Override
    public void netRequestAction(final int action, String json) {

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);

        if (!isMine) {

            RetrofitUtil.getService().getDutyList(body).enqueue(new CancelableCallback<Result<List<Duty>>>() {
                @Override
                protected void onSuccess(Response<Result<List<Duty>>> response, Retrofit retrofit) {

                    Result result = response.body();
                    if (result == null) {
                        endRefreshing();
                        return;
                    }
                    if (result.getRet() == 200) {
                        List<Duty> datas = (List<Duty>) result.getData();
                        if (action == INITACTION) { //如果是第一次请求
                            dutyList = datas;
                            firstRequest(dutyList);
                        } else if (action == UPDATEACTION) {
                            //判断数据是否有新数据 -- 下拉刷新
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
                protected void onFail(Throwable t) {

                    if (action == INITACTION) {
                        initListView();
                    } else if (action == MOREACTION) {
                        endLoadingMore();
                    } else {
                        endRefreshing();
                    }

                }
            });
        } else {

            RetrofitUtil.getService().getMyDutyList(body).enqueue(new Callback<Result<List<Duty>>>() {
                @Override
                public void onResponse(Response<Result<List<Duty>>> response, Retrofit retrofit) {

                    Result result = response.body();
                    if (result == null) {
                        if (action == INITACTION) {
                            initListView();
                        } else {
                            endLoadingMore();
                            endRefreshing();
                        }
                    }
                    if (result.getRet() == 200) {
                        List<Duty> datas = (List<Duty>) result.getData();
                        if (action == INITACTION) { //如果是第一次请求
                            dutyList = datas;
                            firstRequest(dutyList);
                        } else if (action == UPDATEACTION) {
                            //判断数据是否有新数据 -- 下拉刷新
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
                        endLoadingMore();
                    } else {
                        endRefreshing();
                    }

                }
            });

        }

    }

    @Override
    public void firstRequest(Object object) {

        initListView();
    }

    @Override
    public void refreshRequest(Object object) {

        List<Duty> datas = (List<Duty>) object;

        if (datas.size() == 0 || dutyList.size() == 0) {
            dutyList = datas;
            adapter.updateItem(dutyList);
            endRefreshing();
        } else {

            dutyList = datas;
            adapter.updateItem(dutyList);
            endRefreshing();

            if (dutyList.get(0).getDutyPerformId() == datas.get(0).getDutyPerformId()) {

                Toast.makeText(getActivity(), "暂无新数据", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void moreRequest(Object object) {

        List<Duty> datas = (List<Duty>) object;

        if (datas.size() == 0) {
            Toast.makeText(getActivity(), "无更多数据", Toast.LENGTH_SHORT).show();

            flag = false;
        } else {
            dutyList.addAll(datas);
            adapter.updateItem(dutyList);
        }

        endLoadingMore();

    }


}