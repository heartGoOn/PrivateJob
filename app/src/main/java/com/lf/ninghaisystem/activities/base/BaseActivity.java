package com.lf.ninghaisystem.activities.base;

import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.broadcast.GetuiBroadcast;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2017/11/8.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private FrameLayout contentView;

    private Map<Integer, Runnable> allowablePermissionRunnables = new HashMap<>();
    private Map<Integer, Runnable> disallowablePermissionRunnables = new HashMap<>();

    private GetuiBroadcast getuiBroadcast;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        getuiBroadcast = new GetuiBroadcast();
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.lf.ninghai.GETUI");

        initData();
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(getuiBroadcast,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(getuiBroadcast);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void initData() {}

    public void initViews() {

    }

    /**
     * 设置View 或者fragment 二选一
     * @param layoutId
     */
    protected void setView(int layoutId) {
        setView(getLayoutInflater().inflate(layoutId,null));
    }

    protected void setView(View view) {
        contentView.addView(view, new FrameLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    protected void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().add(R.id.id_container, fragment)
                .commitAllowingStateLoss();
    }

    /**
     * 请求权限
     *
     * @param id                   请求授权的id 唯一标识即可
     * @param permission           请求的权限
     * @param allowableRunnable    同意授权后的操作
     * @param disallowableRunnable 禁止权限后的操作
     */
    public void requestPermission(int id, String permission, Runnable allowableRunnable, Runnable disallowableRunnable) {
        if (allowableRunnable == null) {
            throw new IllegalArgumentException("allowableRunnable == null");
        }

        allowablePermissionRunnables.put(id, allowableRunnable);
        if (disallowableRunnable != null) {
            disallowablePermissionRunnables.put(id, disallowableRunnable);
        }

        //版本判断
        if (Build.VERSION.SDK_INT >= 23) {
            //减少是否拥有权限
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this.getApplicationContext(), permission);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                //弹出对话框接收权限
                ActivityCompat.requestPermissions(this, new String[]{permission}, id);
                return;
            } else {
                allowableRunnable.run();
            }
        } else {
            allowableRunnable.run();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if(allowablePermissionRunnables.containsKey(requestCode)) {
                Runnable allowRun = allowablePermissionRunnables.get(requestCode);
                allowRun.run();
            }

        } else {
            if(disallowablePermissionRunnables.containsKey(requestCode)) {
                Runnable disallowRun = disallowablePermissionRunnables.get(requestCode);
                disallowRun.run();
            }

        }

    }

}
