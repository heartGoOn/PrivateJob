package com.lf.ninghaisystem.fragment.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by admin on 2017/11/6.
 */

public class BaseFragment extends Fragment implements View.OnClickListener{

    protected LayoutInflater _inflate;
    protected View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this._inflate = inflater;
        rootView = inflater.inflate(getLayoutId(),container,false);
        initData();
        initViews(rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void initData() {

    }

    public void initViews(View rootView) {

    }

    protected int getLayoutId() {
        return 0;
    }

    protected View inflateView(int resId) {
        return _inflate.inflate(resId, null);
    }

    @Override
    public void onClick(View view) {

    }
}
