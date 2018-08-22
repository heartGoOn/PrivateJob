package com.lf.ninghaisystem.activities;

import android.app.Activity;
import android.os.Bundle;

import com.lf.ninghaisystem.activities.base.BaseActivity;
import com.lf.ninghaisystem.fragment.ForgetFragment;

public class ForgetActivity extends BaseActivity {

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initViews() {
        super.initViews();
        setFragment(new ForgetFragment());
    }
}
