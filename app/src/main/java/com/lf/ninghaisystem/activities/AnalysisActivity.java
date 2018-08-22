package com.lf.ninghaisystem.activities;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.bean.entity.Project;
import com.lf.ninghaisystem.broadcast.GetuiBroadcast;
import com.lf.ninghaisystem.fragment.AnalysisFragment;
import com.lf.ninghaisystem.http.JSContact;
import com.lf.ninghaisystem.http.WebContact;

public class AnalysisActivity extends AppCompatActivity {

    private GetuiBroadcast getuiBroadcast;
    private IntentFilter intentFilter;

    //侧边筛选
    private DrawerLayout drawerLayout;
    private LinearLayout rightLayout;
    private boolean isOpen = false; //筛选是否打开
    private WebView webView;

    private AnalysisFragment analysisFragment;

    private Project project;
    private String select = "-1|-1|-1|选择项目";
    private int canDo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        getuiBroadcast = new GetuiBroadcast();
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.lf.ninghai.GETUI");

        analysisFragment = new AnalysisFragment();

        getSupportFragmentManager().beginTransaction()

                .add(R.id.main_content, analysisFragment).commit();

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
                webView.loadUrl("javascript:GetInfosShow(" + MyApplication.loginUser.getUid()
                        + ",'" + MyApplication.loginUser.getToken() + "','" + select + "','a')");
                webView.loadUrl("javascript:GetInfosShow(" + MyApplication.loginUser.getUid()
                        + ",'" + MyApplication.loginUser.getToken() + "','" + select + "','a')");


                webView.loadUrl("javascript:GetInfosShow(" + MyApplication.loginUser.getUid()
                        + ",'" + MyApplication.loginUser.getToken() + "','" + select + "','a'," + canDo + ")");

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
    protected void onResume() {
        super.onResume();
        registerReceiver(getuiBroadcast, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(getuiBroadcast);
    }

    public void openDrawerLayout() {
        drawerLayout.openDrawer(Gravity.RIGHT);
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    public void initWeb() {
        project = (Project) getIntent().getSerializableExtra("project");
        if (project != null) {
            select = "-1|-1|" + project.getProjectId() + "|" + project.getpName();
            canDo = 0;
        } else {
            canDo = 1;
        }

        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.loadUrl(WebContact.lzAnalysisScreenStr);
        WebSettings wetSettings = webView.getSettings();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wetSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.addJavascriptInterface(new JSContact.NotifyAnalysis() {

            @JavascriptInterface
            @Override
            public void filterType(final String str) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        drawerLayout.closeDrawer(Gravity.RIGHT);
                        select = str;
                    }
                });
            }

            @JavascriptInterface
            @Override
            public void notifyAnalysis(String date, String projectId) {
                analysisFragment.updateHomePagerProjectList(date, projectId);
            }
        }, "test");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isOpen) {
            drawerLayout.closeDrawer(Gravity.RIGHT);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
