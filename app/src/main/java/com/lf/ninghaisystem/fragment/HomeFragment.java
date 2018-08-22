package com.lf.ninghaisystem.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.fragment.base.BaseFragment;

/**
 * Created by admin on 2017/11/6.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener,ManagerFragment{

    private TextView mTabHome;
    private TextView mTabResumption;
    private TextView mTabAddressbook;
    private TextView mTabMine;

    private Fragment currentFragment;
    private HomePagerFragment homePagerFragment;
    private MineFragment mineFragment;
    private ResumptionFragment resumptionFragment;
    private AddressbookFragment addressbookFragment;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void initData() {
        super.initData();
        initFragmentList();
    }

    @Override
    public void initViews(View rootView) {
        super.initViews(rootView);
        mTabHome = rootView.findViewById(R.id.tab_menu_home);
        mTabResumption = rootView.findViewById(R.id.tab_menu_resumption);
        mTabAddressbook = rootView.findViewById(R.id.tab_menu_addressbook);
        mTabMine = rootView.findViewById(R.id.tab_menu_mine);
        mTabHome.setOnClickListener(this);
        mTabResumption.setOnClickListener(this);
        mTabAddressbook.setOnClickListener(this);
        mTabMine.setOnClickListener(this);
        LoadFirstFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tab_menu_home:
                setSelect();
                showFragment(homePagerFragment);
                mTabHome.setSelected(true);
                break;
            case R.id.tab_menu_resumption:
                setSelect();
                showFragment(resumptionFragment);
                mTabResumption.setSelected(true);
                break;
            case R.id.tab_menu_addressbook:
                setSelect();
                showFragment(addressbookFragment);
                mTabAddressbook.setSelected(true);
                break;
            case R.id.tab_menu_mine:
                setSelect();
                showFragment(mineFragment);
                mTabMine.setSelected(true);
                break;
        }

    }

    //初始化加载
    private void LoadFirstFragment() {

        /*mTabHome.setSelected(true);

        showFragment(homePagerFragment);*/

        mTabHome.setSelected(true);
        showFragment(homePagerFragment);

    }

    //重置选择
    private void setSelect() {
        mTabHome.setSelected(false);
        mTabResumption.setSelected(false);
        mTabAddressbook.setSelected(false);
        mTabMine.setSelected(false);
    }

    @Override
    public void showFragment(Fragment fg) {

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        if(!fg.isAdded()) {
            transaction.hide(currentFragment).add(R.id.id_container,fg);
        } else {
            transaction.hide(currentFragment).show(fg);
        }

        currentFragment = fg;

        transaction.commit();

    }

    @Override
    public void initFragmentList() {
        currentFragment = new Fragment();
        homePagerFragment = new HomePagerFragment();
        mineFragment = new MineFragment();
        resumptionFragment = new ResumptionFragment();
        addressbookFragment = new AddressbookFragment();
    }

    public void updateHomePagerProjectList(String keyword,String type) {
        homePagerFragment.updateHomePagerProjectList(keyword,type);
    }
}
