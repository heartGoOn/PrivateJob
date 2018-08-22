package com.lf.ninghaisystem.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.activities.AnalysisActivity;
import com.lf.ninghaisystem.adapter.BaseViewPagerAdapter;
import com.lf.ninghaisystem.fragment.base.BaseBarFragment;
import com.lf.ninghaisystem.http.JSContact;
import com.lf.ninghaisystem.http.WebContact;

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
import java.util.List;

/**
 * Created by admin on 2017/11/24.
 */

public class AnalysisFragment extends BaseBarFragment {

    private MagicIndicator magicIndicator;
    private ViewPager viewPager;

    private List<Fragment> fragmentList;
    private String[] titles = {"项目","履职","部门"};

    private DutyAnalysisChartFragment dutyAnalysisChartFragment;
    private DpAnalysisChartFragment dpAnalysisChartFragment;

    @Override
    public void initData() {
        super.initData();

        dutyAnalysisChartFragment = new DutyAnalysisChartFragment();
        dpAnalysisChartFragment = new DpAnalysisChartFragment();

        fragmentList = new ArrayList<>();
        fragmentList.add(new PjAnalysisChartFragment());
        fragmentList.add(dutyAnalysisChartFragment);
        fragmentList.add(dpAnalysisChartFragment);
    }

    @Override
    public void initViews(View rootView) {
        super.initViews(rootView);
        setView(R.layout.fragment_analysis);
        setTitle("分析");
        setRightImg(R.mipmap.analysis_screen);

        rightImg.setVisibility(View.INVISIBLE);
        rightImg.setEnabled(false);
        magicIndicator = rootView.findViewById(R.id.analysis_tab);
        viewPager = rootView.findViewById(R.id.analysis_viewpager);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new BaseViewPagerAdapter(getChildFragmentManager()
                ,fragmentList));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0) {//不可筛选
                    rightImg.setVisibility(View.INVISIBLE);
                    rightImg.setEnabled(false);
                } else {
                    rightImg.setVisibility(View.VISIBLE);
                    rightImg.setEnabled(true);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return titles.length;
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
        ViewPagerHelper.bind(magicIndicator,viewPager);
    }

    public void updateHomePagerProjectList(String date, String projectId) {
        dutyAnalysisChartFragment.updateHomePagerProjectList(date,projectId);
        dpAnalysisChartFragment.updateHomePagerProjectList(date,projectId);
    }

    @Override
    protected void onRightBtnClick(View v) {
        super.onRightBtnClick(v);
        ((AnalysisActivity)getActivity()).openDrawerLayout();
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
        return true;
    }
}
