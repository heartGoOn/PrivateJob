package com.lf.ninghaisystem.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.activities.HomeActivity;
import com.lf.ninghaisystem.activities.LoginActivity;
import com.lf.ninghaisystem.activities.MessageActivity;
import com.lf.ninghaisystem.activities.MyApplication;
import com.lf.ninghaisystem.activities.base.FinishActivityManager;
import com.lf.ninghaisystem.bean.entity.LoginUser;
import com.lf.ninghaisystem.fragment.base.BaseBarFragment;
import com.lf.ninghaisystem.util.CleanMessageUtil;
import com.lf.ninghaisystem.util.DialogHelp;
import com.lf.ninghaisystem.util.SPHelper;

import static com.igexin.sdk.GTServiceManager.context;

/**
 * Created by admin on 2017/11/8.
 */

public class SettingFragment extends BaseBarFragment {

    private TextView tv_exitLogin;
    private TextView tv_cache_size;
    private RelativeLayout rl_clear_cache;

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initViews(View rootView) {
        super.initViews(rootView);
        setTitle("设置");
        setView(R.layout.fragment_setting);

        tv_exitLogin = rootView.findViewById(R.id.exit_login);
        tv_exitLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                SPHelper.clearLoginUser();
                getActivity().finish();
                FinishActivityManager.getManager().finishActivity(HomeActivity.class);
            }
        });
        tv_cache_size = rootView.findViewById(R.id.cache_size);
        try {
            tv_cache_size.setText(CleanMessageUtil.getTotalCacheSize(getActivity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        rl_clear_cache = rootView.findViewById(R.id.cache_clear);
        rl_clear_cache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogHelp.getConfirmDialog(getActivity(), "确认清空缓存？","确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CleanMessageUtil.clearAllCache(getActivity());
                        try {
                            tv_cache_size.setText(CleanMessageUtil.getTotalCacheSize(getActivity()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).setCancelable(false).create().show();
            }
        });

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
