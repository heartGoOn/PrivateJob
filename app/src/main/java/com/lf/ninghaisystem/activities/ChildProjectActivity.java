package com.lf.ninghaisystem.activities;


import com.lf.ninghaisystem.activities.base.BaseActivity;
import com.lf.ninghaisystem.fragment.ChildProjectFragment;

public class ChildProjectActivity extends BaseActivity {

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initViews() {
        super.initViews();
        setFragment(new ChildProjectFragment());
    }


}
