package com.lf.ninghaisystem.fragment;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.activities.LoginActivity;
import com.lf.ninghaisystem.activities.MyApplication;
import com.lf.ninghaisystem.bean.entity.LoginUser;
import com.lf.ninghaisystem.bean.entity.Result;
import com.lf.ninghaisystem.contact.PinyinComparator;
import com.lf.ninghaisystem.contact.PinyinUtils;
import com.lf.ninghaisystem.contact.SearchEditText;
import com.lf.ninghaisystem.contact.SideBarView;
import com.lf.ninghaisystem.contact.SortAdapter;
import com.lf.ninghaisystem.contact.SortModel;
import com.lf.ninghaisystem.fragment.base.BaseBarFragment;
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
 * Created by admin on 2017/11/8.
 */

public class AddressbookFragment extends BaseBarFragment
        implements BGARefreshLayout.BGARefreshLayoutDelegate{

    private BGARefreshLayout mRefreshLayout;

    private SideBarView sideBarView;
    private TextView textDialog;
    private RecyclerView mRecyclerView;
    private SortAdapter adapter;
    private SearchEditText mSearchEditText;
    LinearLayoutManager manager;
    private List<SortModel> SourceDateList;

    /**
     * 根据拼音来排列RecyclerView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    private LoginUser loginUser;

    @Override
    public void initData() {
        super.initData();

        SourceDateList = new ArrayList<>();
        loginUser = MyApplication.loginUser;


    }

    @Override
    public void initViews(View rootView) {
        super.initViews(rootView);
        setTitle(R.string.title_addressbook);
        setView(R.layout.fragment_address);
        pinyinComparator = new PinyinComparator();
        sideBarView = rootView.findViewById(R.id.sideBar);
        textDialog = rootView.findViewById(R.id.dialog);
        sideBarView.setTextView(textDialog);
        sideBarView.setOnTouchingLetterChangedListener(new SideBarView.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                if(SourceDateList.size() == 0) {
                    return;
                }
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if(position != -1) {
                    manager.scrollToPositionWithOffset(position, 0);
                }
            }
        });
        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        //RecyclerView社置manager
        manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mSearchEditText = rootView.findViewById(R.id.search_edit);
        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        initRefreshLayout(rootView);
        mRefreshLayout.beginRefreshing();
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

    private void NetWorkAction() {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("uid",loginUser.getUid());
        hashMap.put("token",loginUser.getToken());
        String sign = SignGenerate.generate(hashMap);
        hashMap.put("sign",sign);
        String json = JsonHelper.hashMapToJson(hashMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);
        RetrofitUtil.getService().getAddressList(body).enqueue(new Callback<Result<List<SortModel>>>() {
            @Override
            public void onResponse(Response<Result<List<SortModel>>> response, Retrofit retrofit) {
                Result result = response.body();
                if(result == null) {
                    Toast.makeText(getActivity(),"网络请求出错",Toast.LENGTH_SHORT).show();
                    mRefreshLayout.endRefreshing();
                    return;
                }
                if(result.getRet() == 200) {
                    SourceDateList = (List<SortModel>) result.getData();
                    filledData(SourceDateList);
                    initListView();
                } else if(result.getRet() == 111) {
                    SPHelper.clearLoginUser();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
                mRefreshLayout.endRefreshing();
            }

            @Override
            public void onFailure(Throwable t) {
                Log.v("fail","解析失败");
                Toast.makeText(getActivity(),"网络连接失败",Toast.LENGTH_SHORT).show();
                mRefreshLayout.endRefreshing();
            }
        });

    }

    private void initListView() {
        // 根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator);
        adapter = new SortAdapter(getActivity(), SourceDateList);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new SortAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                call(SourceDateList.get(position).getPhoneNum());
            }
        });

    }

    /**
     * 为RecyclerView填充数据
     *
     */
    private void filledData(List<SortModel> data) {

        for (int i = 0; i < data.size(); i++) {

            //汉字转换成拼音
            String pinyin = data.get(i).getFirstChar();
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                data.get(i).setLetters(sortString.toUpperCase());
            } else {
                data.get(i).setLetters("#");
            }

        }

    }

    /**
     * 根据输入框中的值来过滤数据并更新RecyclerView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<>();

        if(SourceDateList.size() == 0) {
            return;
        }

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (SortModel sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name.indexOf(filterStr.toString()) != -1 ||
                        PinyinUtils.getFirstSpell(name).startsWith(filterStr.toString())
                        //不区分大小写
                        || PinyinUtils.getFirstSpell(name).toLowerCase().startsWith(filterStr.toString())
                        || PinyinUtils.getFirstSpell(name).toUpperCase().startsWith(filterStr.toString())
                        || sortModel.getPhoneNum().contains(filterStr)
                        ) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateList(filterDateList);
    }

    /**
     * 调用拨号界面
     * @param phone 电话号码
     */
    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected boolean hasBackBtn() {
        return false;
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

        NetWorkAction();

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout bgaRefreshLayout) {
        return false;
    }
}
