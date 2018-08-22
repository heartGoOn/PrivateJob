package com.lf.ninghaisystem.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lf.ninghaisystem.activities.base.BaseActivity;
import com.lf.ninghaisystem.fragment.RepresentEvaluateFragment;

public class RepresentEvaluateActivity extends BaseActivity {

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initViews() {
        super.initViews();
        setFragment(new RepresentEvaluateFragment());
    }
}
