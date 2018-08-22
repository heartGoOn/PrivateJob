package com.lf.ninghaisystem.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lf.ninghaisystem.activities.base.BaseActivity;
import com.lf.ninghaisystem.fragment.DepartmentEvaluateFragment;

public class DepartmentEvaluateActivity extends BaseActivity {

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initViews() {
        super.initViews();
        setFragment(new DepartmentEvaluateFragment());
    }
}
