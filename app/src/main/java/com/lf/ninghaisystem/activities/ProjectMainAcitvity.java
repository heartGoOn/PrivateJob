package com.lf.ninghaisystem.activities;


import com.lf.ninghaisystem.activities.base.BaseActivity;
import com.lf.ninghaisystem.fragment.ProjectMainFragment;

/**
 * Created by admin on 2017/11/8.
 */

public class ProjectMainAcitvity extends BaseActivity {

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initViews() {
        super.initViews();
        setFragment(new ProjectMainFragment());
    }

}
