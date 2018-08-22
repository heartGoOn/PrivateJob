package com.lf.ninghaisystem.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.activities.AnalysisActivity;
import com.lf.ninghaisystem.activities.BoostActivity;
import com.lf.ninghaisystem.activities.DepartmentEvaluateActivity;
import com.lf.ninghaisystem.activities.DutyAnalysisActivity;
import com.lf.ninghaisystem.activities.DutyPersonActivity;
import com.lf.ninghaisystem.activities.DutyReportActivity;
import com.lf.ninghaisystem.activities.HomeActivity;
import com.lf.ninghaisystem.activities.LoginActivity;
import com.lf.ninghaisystem.activities.MyApplication;
import com.lf.ninghaisystem.activities.ProjectDutyActivity;
import com.lf.ninghaisystem.activities.ProjectPlanActivity;
import com.lf.ninghaisystem.activities.ProjectProcessActivity;
import com.lf.ninghaisystem.activities.ProjectWordActivity;
import com.lf.ninghaisystem.activities.RepresentEvaluateActivity;
import com.lf.ninghaisystem.activities.base.FinishActivityManager;
import com.lf.ninghaisystem.adapter.ProjectMainAdapter;
import com.lf.ninghaisystem.bean.User;
import com.lf.ninghaisystem.bean.entity.LoginUser;
import com.lf.ninghaisystem.bean.entity.Project;
import com.lf.ninghaisystem.bean.entity.Result;
import com.lf.ninghaisystem.fragment.base.BaseBarFragment;
import com.lf.ninghaisystem.http.WebContact;
import com.lf.ninghaisystem.http.retrofit.RetrofitUtil;
import com.lf.ninghaisystem.util.JsonHelper;
import com.lf.ninghaisystem.util.SPHelper;
import com.lf.ninghaisystem.util.SignGenerate;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by admin on 2017/11/8.
 */

public class ProjectMainFragment extends BaseBarFragment {

    Project project;
    private GridView menuGridView;
    private ProjectMainAdapter adapter;
    private WebView webView;

    private LoginUser loginUser;

    private User user;
    private int isHistory, isOwn;


    @Override
    public void initData() {
        super.initData();
        Intent intent = getActivity().getIntent();
        project = (Project) getActivity().getIntent().getSerializableExtra("project");
        isHistory = intent.getIntExtra("isHistory", 0);
        isOwn = intent.getIntExtra("isOwn", 0);
        Log.e("ProjectMainFragment", String.valueOf(isHistory) + "~~~~" + isOwn);
        loginUser = MyApplication.loginUser;
        user = MyApplication.loginUser.getEmployeeInfo();
    }

    private Intent intent = null;

    @Override
    public void initViews(View rootView) {
        super.initViews(rootView);

        setTitle(project.getpName());
        setView(R.layout.fragment_project_main);
        //setRightTxt("评价");
        LoadWebView(rootView);

        menuGridView = rootView.findViewById(R.id.main_menu);
        adapter = new ProjectMainAdapter(getActivity());
        menuGridView.setAdapter(adapter);

        menuGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i) {

                    case 0://项目计划
                        intent = new Intent(getActivity(), ProjectPlanActivity.class);
                        intent.putExtra("project", project);
                        startActivity(intent);
                        break;
                    case 1://项目进度
                        intent = new Intent(getActivity(), ProjectProcessActivity.class);
                        intent.putExtra("project", project);
                        startActivity(intent);
                        break;
                    case 2://项目文档
                        intent = new Intent(getActivity(), ProjectWordActivity.class);
                        intent.putExtra("projectId", project.getProjectId());
                        startActivity(intent);
                        break;
                    case 3://履职记录
                        intent = new Intent(getActivity(), ProjectDutyActivity.class);
                        intent.putExtra("project", project);
                        intent.putExtra("isHistory", isHistory);
                        intent.putExtra("isOwn", isOwn);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(getActivity(), DutyPersonActivity.class);
                        intent.putExtra("project", project);
                        startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent(getActivity(), DutyAnalysisActivity.class);
                        intent.putExtra("project", project);
                        startActivity(intent);
                        break;
                    case 6:
                        departmentIsOpenCheck();
                        break;
                    case 7:
                        evaluatingIsOpenCheck();
                        break;
                    default:
                        break;

                }

            }
        });

    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    private void LoadWebView(View view) {

        webView = view.findViewById(R.id.webView);
        WebSettings wetSettings = webView.getSettings();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wetSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.loadUrl(WebContact.projectIntroStr);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showWaitDialog("加载中...").show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideWaitDialog();
                webView.loadUrl("javascript:getEngineering(" +
                        project.getProjectId() + "," + loginUser.getUid()
                        + ",'" + loginUser.getToken() + "')");
            }
        });
        //Log.v("id",project.getProjectId()+"");


    }

    private void evaluatingIsOpenCheck() {
        if (user.getIsCppccMember().equals("1")) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("projectId", project.getProjectId());
            hashMap.put("uid", MyApplication.loginUser.getUid());
            hashMap.put("token", MyApplication.loginUser.getToken());
            String sign = SignGenerate.generate(hashMap);
            hashMap.put("sign", sign);
            String json = JsonHelper.hashMapToJson(hashMap);
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
            RetrofitUtil.getService().checkEvaluatingIsOpen(body).enqueue(new Callback<Result<Boolean>>() {
                @Override
                public void onResponse(Response<Result<Boolean>> response, Retrofit retrofit) {

                    Result result = response.body();

                    if (result.getRet() == 200) {
                        if ((Boolean) result.getData()) {
                            intent = new Intent(getActivity(), RepresentEvaluateActivity.class);
                            intent.putExtra("project", project);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getActivity(), "功能未开放", Toast.LENGTH_SHORT).show();
                        }
                    } else if (result.getRet() == 111) {

                        SPHelper.clearLoginUser();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        FinishActivityManager.getManager().finishActivity(HomeActivity.class);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "您没有权限评价，具体联系系统管理人员", Toast.LENGTH_SHORT).show();
        }
    }

    private void departmentIsOpenCheck() {
        if (user.getIsNpcMember().equals("1") || user.getIsCppccMember().equals("1")) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("projectId", project.getProjectId());
            hashMap.put("uid", MyApplication.loginUser.getUid());
            hashMap.put("token", MyApplication.loginUser.getToken());
            String sign = SignGenerate.generate(hashMap);
            hashMap.put("sign", sign);

            String json = JsonHelper.hashMapToJson(hashMap);
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
            RetrofitUtil.getService().checkDepartmentIsOpen(body).enqueue(new Callback<Result<Boolean>>() {
                @Override
                public void onResponse(Response<Result<Boolean>> response, Retrofit retrofit) {

                    Result result = response.body();

                    if (result.getRet() == 200) {
                        if ((Boolean) result.getData()) {
                            intent = new Intent(getActivity(), DepartmentEvaluateActivity.class);
                            intent.putExtra("project", project);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getActivity(), "功能未开放", Toast.LENGTH_SHORT).show();
                        }
                    } else if (result.getRet() == 111) {

                        SPHelper.clearLoginUser();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        FinishActivityManager.getManager().finishActivity(HomeActivity.class);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "您没有权限评价，具体联系系统管理人员", Toast.LENGTH_SHORT).show();
        }
    }

    //private PopupWindow window;

   /* @Override
    protected void onRightBtnClick(View v) {
        super.onRightBtnClick(v);

        View popupView = getActivity().getLayoutInflater().inflate(R.layout.popupwindow, null);

        ListView lsvMore = (ListView) popupView.findViewById(R.id.lsvMore);
        lsvMore.setAdapter(new EvePopupAdapter(getActivity(), R.layout.item_popup));


        window = new PopupWindow(popupView, getResources().getDimensionPixelSize(R.dimen.popup_window_width)
                    , getResources().getDimensionPixelSize(R.dimen.popup_window_height));

        lsvMore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                switch ((String) view.getTag()) {
                    case "部门":
                        departmentIsOpenCheck();
                        break;

                    case "代表":
                        evaluatingIsOpenCheck();
                        break;

                    case "项目":

                        break;
                }
                window.dismiss();
            }
        });


        window.setOutsideTouchable(true);
        window.setFocusable(true);
        window.setBackgroundDrawable(new ColorDrawable(R.drawable.popupwindow_bg));
        window.update();
        window.showAsDropDown(rightTxt, 5, 0);

    }*/

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
