package com.lf.ninghaisystem.activities;

import com.lf.ninghaisystem.activities.base.BaseActivity;
import com.lf.ninghaisystem.fragment.DutyPersonFragment;

public class DutyPersonActivity extends BaseActivity {

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initViews() {
        super.initViews();
        setFragment(new DutyPersonFragment());
    }
}
