package com.lf.ninghaisystem.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.activities.base.FinishActivityManager;
import com.lf.ninghaisystem.bean.entity.LoginUser;
import com.lf.ninghaisystem.bean.entity.Result;
import com.lf.ninghaisystem.broadcast.GetuiBroadcast;
import com.lf.ninghaisystem.fragment.HomeFragment;
import com.lf.ninghaisystem.http.JSContact;
import com.lf.ninghaisystem.http.WebContact;
import com.lf.ninghaisystem.http.retrofit.RetrofitUtil;
import com.lf.ninghaisystem.util.DateUtils;
import com.lf.ninghaisystem.util.SPHelper;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.File;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class HomeActivity extends AppCompatActivity {

    private GetuiBroadcast getuiBroadcast;
    private IntentFilter intentFilter;

    private DrawerLayout drawerLayout;
    private LinearLayout rightLayout;
    private boolean isOpen = false; //筛选是否打开
    private WebView webView;

    String keyword = "-1";
    String type = "-1";

    private HomeFragment homeFragment;
    private LoginUser loginUser;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            drawerLayout.closeDrawers();
            homeFragment.updateHomePagerProjectList(keyword, type);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FinishActivityManager.getManager().addActivity(this);
        getuiBroadcast = new GetuiBroadcast();
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.lf.ninghai.GETUI");
        homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_content, homeFragment).commit();
        reRequestToken();
        rightLayout = findViewById(R.id.main_right_drawer_layout);
        View view = getLayoutInflater().inflate(R.layout.drawer_right, null);
        webView = view.findViewById(R.id.webView);
        initWeb();
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        rightLayout.addView(view);
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                isOpen = true;
                rightLayout.setClickable(true);
                webView.loadUrl("javascript:GetInfosShow(" + loginUser.getUid()
                        + ",'" + loginUser.getToken() + "','" + keyword + "','" + type + "','a')");
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                isOpen = false;
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(getuiBroadcast, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(getuiBroadcast);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deleteDatabase("webview.db");
        deleteDatabase("webviewCache.db");
    }

    public void openDrawerLayout() {
        drawerLayout.openDrawer(Gravity.RIGHT);
    }

    public void closeDrawerLayout() {
        drawerLayout.closeDrawer(Gravity.RIGHT);
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    public void initWeb() {
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        WebSettings wetSettings = webView.getSettings();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wetSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        }
//        webView.setWebViewClient(new WebViewClient(){
//            @Override
//            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                handler.proceed();
//            }
//        });
        //webView.loadUrl(WebContact.projectFrontPageScreeningStr)

        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        //webView.loadUrl(WebContact.projectFrontPageScreeningStr)

        webView.addJavascriptInterface(new JSContact.CallFilterType() {
            @JavascriptInterface
            @Override
            public void filterType(String keyword, String str) {
                HomeActivity.this.keyword = keyword;
                type = str;
                handler.sendEmptyMessage(1);
                Log.e("aaaa", "filterType: " + str);
            }
        }, "test");

        webView.loadUrl(WebContact.projectFrontPageScreeningStr);

    }

    private void reRequestToken() {
        loginUser = MyApplication.loginUser;
        if (loginUser != null) {
            netWorkLogin();
        }

    }

    private void netWorkLogin() {
        final String account = loginUser.getAccount();
        final String password = loginUser.getPassword();
        String json = "{\"account\":\"" + account
                + "\",\"password\":\"" + password + "\",\"cid\":\"" + MyApplication.Cid + "\"}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);

        RetrofitUtil.getService().getLoginMsg(body).enqueue(new Callback<Result<LoginUser>>() {
            @Override
            public void onResponse(Response<Result<LoginUser>> response, Retrofit retrofit) {
                Result result = response.body();
                if (result == null) {
                    Toast.makeText(getBaseContext(), "网络请求出错", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (result.getRet() == 200) {
                    MyApplication.loginUser = (LoginUser) result.getData();
                    MyApplication.loginUser.setTokenDate(DateUtils.GetDate3());
                    MyApplication.loginUser.setAccount(account);
                    MyApplication.loginUser.setPassword(password);
                    SPHelper.saveUserMsg(MyApplication.loginUser);
                } else if (result.getRet() == 111) {
                    SPHelper.clearLoginUser();
                    Toast.makeText(getBaseContext(), "登录过期", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    HomeActivity.this.finish();
                } else {
                    Toast.makeText(getBaseContext(), "数据异常", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getBaseContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 && !isOpen) {
            exit();
            return true;
        } else if (isOpen) {
            closeDrawerLayout();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(HomeActivity.this, "再按一次退出人大履职", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

}
