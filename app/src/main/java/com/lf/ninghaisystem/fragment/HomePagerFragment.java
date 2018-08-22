package com.lf.ninghaisystem.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.activities.BannerContentActivity;
import com.lf.ninghaisystem.activities.HomeActivity;
import com.lf.ninghaisystem.activities.LoginActivity;
import com.lf.ninghaisystem.activities.MessageActivity;
import com.lf.ninghaisystem.activities.MyApplication;
import com.lf.ninghaisystem.adapter.BaseViewPagerAdapter;
import com.lf.ninghaisystem.bean.entity.BannerEntity;
import com.lf.ninghaisystem.bean.entity.Result;
import com.lf.ninghaisystem.fragment.base.BaseBarFragment;
import com.lf.ninghaisystem.http.InterfaceManage;
import com.lf.ninghaisystem.http.retrofit.RetrofitUtil;
import com.lf.ninghaisystem.util.JsonHelper;
import com.lf.ninghaisystem.util.SPHelper;
import com.lf.ninghaisystem.util.SignGenerate;
import com.lf.ninghaisystem.widget.GlideImageLoader;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


/**
 * Created by admin on 2017/11/6.
 */

public class HomePagerFragment extends BaseBarFragment {

    private Banner banner;
    private ViewPager viewPager;
    private CommonNavigator commonNavigator;
    //    private PagerSlidingTabStrip tabs;
    private MagicIndicator magicIndicator;
    private ImageView openDrawerBtn;

    private List<Fragment> fragmentList;
    private HomePagerChildFragment homePagerChildFragment;
    private HomePagerHistoryFragment homePagerHistoryFragment;

    private String json;

    String[] titles = {"正在跟进", "历史项目"};
    private List<String> titleList;
    private List<String> urlList;
    private List<String> contentList;

    private List<BannerEntity> bannerEntityList;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        netWorkAction();

    }

    @Override
    public void initData() {
        super.initData();

        homePagerChildFragment = new HomePagerChildFragment();
        homePagerHistoryFragment = new HomePagerHistoryFragment();

        fragmentList = new ArrayList<>();
        fragmentList.add(homePagerChildFragment);
        fragmentList.add(homePagerHistoryFragment);

        homePagerChildFragment.setOnTotalPageListener(new InterfaceManage.OnTotalPageListener() {
            @Override
            public void totalPageListener(int total) {

                titles[0] = "正在跟进 (" + total + ")";
                commonNavigator.notifyDataSetChanged();
            }
        });

        homePagerHistoryFragment.setOnTotalPageListener(new InterfaceManage.OnTotalPageListener() {
            @Override
            public void totalPageListener(int total) {

                titles[1] = "历史项目 (" + total + ")";
                commonNavigator.notifyDataSetChanged();

            }
        });

        titleList = new ArrayList<>();
        urlList = new ArrayList<>();
        contentList = new ArrayList<>();
        bannerEntityList = new ArrayList<>();
    }

    @Override
    public void initViews(View rootView) {
        super.initViews(rootView);

        setRightImg(R.mipmap.home_message);
        setTitle(R.string.title_home_bar);
        setView(R.layout.view_homepager);

        banner = rootView.findViewById(R.id.banner);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        banner.setImageLoader(new GlideImageLoader());
        banner.setDelayTime(3000);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

                Intent intent = new Intent(getActivity(), BannerContentActivity.class);
                intent.putExtra("bannerEntity", bannerEntityList.get(position));
                startActivity(intent);

            }
        });

        viewPager = rootView.findViewById(R.id.home_viewPager);
        viewPager.setAdapter(new BaseViewPagerAdapter(getChildFragmentManager(), fragmentList));

//        tabs = rootView.findViewById(R.id.home_tab);
//        tabs.setViewPager(viewPager);
        magicIndicator = rootView.findViewById(R.id.home_tab);
        commonNavigator = new CommonNavigator(getActivity());
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return fragmentList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {

                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(Color.BLACK);
                colorTransitionPagerTitleView.setSelectedColor(getResources().getColor(R.color.colorPrimary));
                colorTransitionPagerTitleView.setText(titles[index]);

                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewPager.setCurrentItem(index);
                    }
                });

                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(getResources().getColor(R.color.colorPrimary));
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        LinearLayout titleContent = commonNavigator.getTitleContainer();    //必须在设置完之后
        titleContent.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        titleContent.setDividerPadding(UIUtil.dip2px(getActivity(), 15));
        titleContent.setDividerDrawable(getResources().getDrawable(R.drawable.line_gray));
        ViewPagerHelper.bind(magicIndicator, viewPager);

        openDrawerBtn = rootView.findViewById(R.id.open_drawer);
        openDrawerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity) getActivity()).openDrawerLayout();
            }
        });

    }

    private void init(List<BannerEntity> datas) {

        for (int i = 0; i < datas.size(); i++) {

            titleList.add(datas.get(i).getTitle());
            urlList.add(datas.get(i).getUrl());
            contentList.add(datas.get(i).getContent());
        }

        banner.setImages(urlList);
        banner.setBannerTitles(titleList);
        banner.start();

    }

    private void netWorkAction() {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", MyApplication.loginUser.getToken());
        String sign = SignGenerate.generate(hashMap);
        hashMap.put("sign", sign);
        json = JsonHelper.hashMapToJson(hashMap);

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        RetrofitUtil.getService().getBannerList(body).enqueue(new Callback<Result<List<BannerEntity>>>() {
            @Override
            public void onResponse(Response<Result<List<BannerEntity>>> response, Retrofit retrofit) {

                Result result = response.body();
                if (result == null) {

                    return;
                }

                if (result.getRet() == 200) {

                    bannerEntityList = (List<BannerEntity>) result.getData();
                    init(bannerEntityList);

                } else if (result.getRet() == 111) {

                    SPHelper.clearLoginUser();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }

    @Override
    protected void onRightBtnClick(View v) {
        super.onRightBtnClick(v);
        Intent intent = new Intent(getActivity(), MessageActivity.class);
        startActivity(intent);
    }

    @Override
    protected boolean hasBackBtn() {
        return false;
    }

    @Override
    protected boolean hasRightBtn() {
        return true;
    }

    @Override
    protected boolean isRightImg() {
        return true;
    }

    public void updateHomePagerProjectList(String keyword, String type) {
        homePagerChildFragment.updateHomePagerProjectList(keyword, type);
        homePagerHistoryFragment.updateHomePagerProjectList(keyword, type);
    }
}
