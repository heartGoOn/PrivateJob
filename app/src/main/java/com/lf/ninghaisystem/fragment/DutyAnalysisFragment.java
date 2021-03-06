package com.lf.ninghaisystem.fragment;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.adapter.BaseViewPagerAdapter;
import com.lf.ninghaisystem.fragment.base.BaseBarFragment;

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
 * Created by admin on 2017/11/16.
 */

public class DutyAnalysisFragment extends BaseBarFragment {

    private MagicIndicator magicIndicator;
    private ViewPager viewPager;

    private List<Fragment> fragmentList;
    private String[] titles = {"活跃度排行","履职","部门"};

    @Override
    public void initData() {
        super.initData();

        fragmentList = new ArrayList<>();
        fragmentList.add(new DutyAnalysisRankFragment());
        fragmentList.add(new DutyAnalysisChartFragment());
        fragmentList.add(new DpAnalysisChartFragment());
    }

    @Override
    public void initViews(View rootView) {
        super.initViews(rootView);
        setView(R.layout.fragment_duty_analysis);
        setTitle("履职分析");

        magicIndicator = rootView.findViewById(R.id.analysis_tab);
        viewPager = rootView.findViewById(R.id.analysis_viewpager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new BaseViewPagerAdapter(getChildFragmentManager()
                ,fragmentList));

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
