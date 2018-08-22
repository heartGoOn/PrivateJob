package com.lf.ninghaisystem.fragment;

import android.view.View;

import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.fragment.base.BaseBarFragment;

/**
 * Created by admin on 2017/11/14.
 */

public class ForgetFragment extends BaseBarFragment {

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initViews(View rootView) {
        super.initViews(rootView);
        setView(R.layout.fragment_forget);
        setTitle("忘记密码");
    }

    @Override
    protected boolean hasBackBtn() {
        return true;
    }

    @Override
    protected boolean hasRightBtn() {
        return false;
    }

    @Override
    protected boolean isRightImg() {
        return false;
    }
}
