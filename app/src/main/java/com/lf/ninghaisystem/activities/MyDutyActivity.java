package com.lf.ninghaisystem.activities;

import com.lf.ninghaisystem.activities.base.BaseActivity;
import com.lf.ninghaisystem.fragment.ResumptionFragment;

public class MyDutyActivity extends BaseActivity {

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initViews() {
        super.initViews();
        setFragment(new ResumptionFragment());
    }
}
