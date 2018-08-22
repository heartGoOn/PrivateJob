package com.lf.ninghaisystem.activities;



import com.lf.ninghaisystem.activities.base.BaseActivity;
import com.lf.ninghaisystem.fragment.DutyPersonDetailedFragment;

public class DutyPersonDetailedActivity extends BaseActivity {

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initViews() {
        super.initViews();
        setFragment(new DutyPersonDetailedFragment());
    }
}
