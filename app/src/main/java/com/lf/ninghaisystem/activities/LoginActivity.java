package com.lf.ninghaisystem.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.igexin.sdk.PushManager;
import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.activities.base.CheckPermissionsActivity;
import com.lf.ninghaisystem.bean.entity.LoginUser;
import com.lf.ninghaisystem.bean.entity.Result;
import com.lf.ninghaisystem.http.retrofit.RetrofitUtil;
import com.lf.ninghaisystem.service.mIntentService;
import com.lf.ninghaisystem.service.mPushService;
import com.lf.ninghaisystem.util.CheckPhoneUtils;
import com.lf.ninghaisystem.util.DateUtils;
import com.lf.ninghaisystem.util.SPHelper;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LoginActivity extends CheckPermissionsActivity {

    private Button btnLogin;

    private TextView btnForget;
    private EditText et_account,et_password;
    private static final int REQUEST_PERMISSION = 0;
    private LoginUser loginUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyApplication.loginUser = SPHelper.getUserMsg();
        this.loginUser = MyApplication.loginUser;
        if(loginUser.getToken()!="" && loginUser.getUid()!=-1 && loginUser.getToken()!=null) {
            Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();
        }

        setContentView(R.layout.activity_login);

        PackageManager pkgManager = getPackageManager();

        // 读写 sd card 权限非常重要, android6.0默认禁止的, 建议初始化之前就弹窗让用户赋予该权限
        boolean sdCardWritePermission =
                pkgManager.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, getPackageName()) == PackageManager.PERMISSION_GRANTED;

        // read phone state用于获取 imei 设备信息
        boolean phoneSatePermission =
                pkgManager.checkPermission(Manifest.permission.READ_PHONE_STATE, getPackageName()) == PackageManager.PERMISSION_GRANTED;

        if (Build.VERSION.SDK_INT >= 23 && !sdCardWritePermission || !phoneSatePermission) {
            requestPermission();
        } else {
            PushManager.getInstance().initialize(this.getApplicationContext(),mPushService.class);

        }

        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(),mIntentService.class);

        initViews();

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE},
                REQUEST_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] paramArrayOfInt) {

        super.onRequestPermissionsResult(requestCode, permissions, paramArrayOfInt);
        if(requestCode == REQUEST_PERMISSION) {
            PushManager.getInstance().initialize(this.getApplicationContext(),mPushService.class);
        }
    }

    private void initViews() {

        /**
         * textView渐变
         */
        Shader shader = new LinearGradient(0,0,0,90,
                Color.parseColor("#fcf4b4"),Color.parseColor("#e2bd63"), Shader.TileMode.CLAMP);
        ((TextView)findViewById(R.id.login_title)).getPaint().setShader(shader);
        et_account = findViewById(R.id.edit_account);
        et_password = findViewById(R.id.edit_password);

        if(loginUser.getAccount() != null) {
            et_account.setText(loginUser.getAccount());
        }

        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(et_account.getText().toString().length()>0 &&
                        et_password.getText().toString().length()>0) {
                    netWorkLogin();
                } else {
                    Toast.makeText(getBaseContext(),"帐号密码不能为空",Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnForget = findViewById(R.id.txt_forget);
        btnForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String str = et_account.getText().toString();
                if(CheckPhoneUtils.isPhone(str)) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("重置");
                    builder.setMessage("确认后重置密码,并发送至"+str);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            netWorkAction(str);
                            dialogInterface.dismiss();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.setCancelable(false);
                    dialog.show();

                } else {
                    Toast.makeText(getBaseContext(),"手机号码异常",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void netWorkLogin() {

        final String account = et_account.getText().toString();
        final String password = stringToMD5(et_password.getText().toString());

        String json = "{\"account\":\""+account
                +"\",\"password\":\""+password+"\",\"cid\":\""+MyApplication.Cid+"\"}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);

        RetrofitUtil.getService().getLoginMsg(body).enqueue(new Callback<Result<LoginUser>>() {
            @Override
            public void onResponse(Response<Result<LoginUser>> response, Retrofit retrofit) {

                Result result = response.body();
                if(result == null) {
                    Toast.makeText(getBaseContext(),"网络请求出错",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(result.getRet() == 200) {
                    MyApplication.loginUser = (LoginUser) result.getData();
                    MyApplication.loginUser.setTokenDate(DateUtils.GetDate3());
                    MyApplication.loginUser.setAccount(account);
                    MyApplication.loginUser.setPassword(password);
                    SPHelper.saveUserMsg(MyApplication.loginUser);
                    Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getBaseContext(),"帐号或密码错误",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }

    private void netWorkAction(String phone) {

        String json = "{\"cellphone\":\""+phone+"\"}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);

        RetrofitUtil.getService().resetPassword(body).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Response<Result> response, Retrofit retrofit) {

                Result result = response.body();
                if(result.getRet() == 200) {
                    Toast.makeText(getBaseContext(), result.getMsg(), Toast.LENGTH_SHORT).show();
                } else if(result.getRet() == 111){
                    SPHelper.clearLoginUser();
                    Toast.makeText(LoginActivity.this,"登录过期",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this,LoginActivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();
                } else {
                    Toast.makeText(LoginActivity.this,"数据异常",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.v("fail","解析失败");
            }
        });

    }

    /**
     * 将字符串转成MD5值
     * @param string 需要转换的字符串
     * @return 字符串的MD5值
     */
    public static String stringToMD5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString();
    }

}
