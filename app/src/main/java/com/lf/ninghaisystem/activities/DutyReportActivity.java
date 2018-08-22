package com.lf.ninghaisystem.activities;

import android.content.Intent;
import android.widget.Toast;

import com.lf.ninghaisystem.activities.base.BaseActivity;
import com.lf.ninghaisystem.activities.base.CheckPermissionsActivity;
import com.lf.ninghaisystem.bean.Local;
import com.lf.ninghaisystem.fragment.DutyReportFragment;
import com.lf.ninghaisystem.widget.LocationWidget;

/**
 * Created by admin on 2017/11/10.
 */

public class DutyReportActivity extends BaseActivity {

    private DutyReportFragment dutyReportFragment;

    @Override
    public void initData() {
        super.initData();
        dutyReportFragment = new DutyReportFragment();
    }

    @Override
    public void initViews() {
        super.initViews();
        setFragment(dutyReportFragment);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            dutyReportFragment.onResult(requestCode,data);
        }
    }
}
