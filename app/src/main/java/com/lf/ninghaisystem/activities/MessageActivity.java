package com.lf.ninghaisystem.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.BaseAdapter;

import com.lf.ninghaisystem.activities.base.BaseActivity;
import com.lf.ninghaisystem.fragment.MessageFragment;

public class MessageActivity extends BaseActivity {

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initViews() {
        super.initViews();
        setFragment(new MessageFragment());
    }
}
