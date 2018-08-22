package com.lf.ninghaisystem.imgcrop;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.activities.LoginActivity;
import com.lf.ninghaisystem.activities.MyApplication;
import com.lf.ninghaisystem.bean.entity.ImageData;
import com.lf.ninghaisystem.bean.entity.Result;
import com.lf.ninghaisystem.fragment.base.BaseBarFragment;
import com.lf.ninghaisystem.http.retrofit.RetrofitUtil;
import com.lf.ninghaisystem.util.ImageUtils;
import com.lf.ninghaisystem.util.SPHelper;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static android.app.Activity.RESULT_OK;

/**
 * Created by admin on 2017/11/15.
 */

public class ImageCropFragment extends BaseBarFragment {

    CropImageView cropimageview;

    private Bundle b;

    private Bitmap mBitmap;
    private Bitmap mScaleBitmap;
    private Bitmap rotabm;
    private String mPhotoPath;
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
    private String imgPath;

    //上传图片
    int taskSize = 0;
    int finishedSize = 0;
    double per;
    String imgUrl="";
    File file;
    byte[] data;

    Handler myHandler = new Handler();

    //上传图片显示进度
    private ProgressDialog mProgressDialog;
    Runnable percentRun = new Runnable() {
        @Override
        public void run() {
            changeProgress((int) (((per) / taskSize)*100));
            myHandler.postDelayed(this, 100);
        }
    };

    private void changeProgress(int percent) {
        if(mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.setProgress(percent);
        }
    }

    private void showProgressDialog() {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("正在上传图片...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMax(100);
        mProgressDialog.show();
    }

    @Override
    public void initData() {
        super.initData();

    }

    @Override
    public void initViews(View rootView) {
        super.initViews(rootView);

        setView(R.layout.activity_image_crop);
        setTitle("图片裁剪");
        setRightTxt("裁剪");

        cropimageview = rootView.findViewById(R.id.cropimageview);

        ((ImageCropActivity)getActivity()).requestPermission(1, Manifest.permission.READ_EXTERNAL_STORAGE, new Runnable() {
            @Override
            public void run() {
                b = getActivity().getIntent().getExtras();
                if (b != null) {
                    mPhotoPath = b.getString("PHOTO_PATH");
                }
                if (!TextUtils.isEmpty(mPhotoPath)) {
                    try {
                        mBitmap = ImageUtils.getScaleBitmap(getActivity(),mPhotoPath);
                        mScaleBitmap = FileUtis.scaleBitmap(mBitmap, mBitmap.getWidth() / 2, mBitmap.getHeight() / 2);
                        mBitmap.recycle();
                        cropimageview.setImageBitmap(mScaleBitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Runnable() {
            @Override
            public void run() {
                //showToast("没有权限");
                getActivity().finish();
            }
        });


    }

    @Override
    protected boolean hasBackBtn() {
        return true;
    }

    @Override
    protected boolean hasRightBtn() {
        return true;
    }

    @Override
    protected boolean isRightImg() {
        return false;
    }


    @Override
    protected void onRightBtnClick(View v) {
        super.onRightBtnClick(v);
        showWaitDialog("正在裁剪并上传...").show();
        saveFileToLocal();
    }

    private String fileName;

    private void saveFileToLocal() {

        long timestamp = System.currentTimeMillis();
        String time = formatter.format(new Date());

        fileName ="/cropped" + time + timestamp + ".jpg";

        Bitmap bm = cropimageview.getCropImage();

        data = ImageUtils.convertBitMapToByteArray(bm);

        //保存图片到本地
        file =  FileUtis.saveBitmap2File(bm, Constants.DIRPATH,fileName);


        if (data != null&&data.length!=0) {
            //保存图片到七牛服务器上传至7niu

            fileName ="/cropped" + time + timestamp + ".jpg";

            String token = MyApplication.loginUser.getToken();
            String uid = MyApplication.loginUser.getUid()+"";
            RequestBody requestBody = new MultipartBuilder().
                    addFormDataPart("token", token).
                    addFormDataPart("uid", uid).
                    addFormDataPart("file", fileName, RequestBody.create(MediaType.parse("image/png"), new File(Constants.DIRPATH+fileName))).
                    build();

            //MultipartBody.Part file = MultipartBody.Part.createFormData("interactionFile", "test.png", body);
            RetrofitUtil.getService().postImageList(requestBody).enqueue(new Callback<Result<List<ImageData>>>() {
                @Override
                public void onResponse(Response<Result<List<ImageData>>> response, Retrofit retrofit) {

                    Result result = response.body();
                    if (result.getRet() == 200) {

                        List<ImageData> imageDatas = (List<ImageData>) result.getData();

                        Log.v("OK","photo");
                        Intent intent = getActivity().getIntent();
                        intent.putExtra("ImageData",imageDatas.get(0));
                        intent.putExtra("localpath", Constants.DIRPATH+fileName);
                        getActivity().setResult(RESULT_OK, intent);
                        getActivity().finish();

                    } else if(result.getRet() == 111){
                        SPHelper.clearLoginUser();
                        Toast.makeText(getActivity(),"登录过期",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(),LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        Toast.makeText(getActivity(),"上传失败",Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Throwable t) {
                    Log.v("Fail","photo");
                }
            });

        } else {
            Toast.makeText(getActivity(),"文件不存在",Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }

        /*if(taskSize!=0){
            showProgressDialog();
            myHandler.post(percentRun);
        }else{
            Intent intent = getIntent();
            intent.putExtra("path", imgUrl);
            if(file!=null) {
                intent.putExtra("localpath", file.getAbsolutePath());
            }
            setResult(RESULT_OK, intent);*/
            //hideWaitDialog();


       // }

    }


    public static void startCropAct(Activity activity, String picPath) {
        Intent intent = new Intent(activity, ImageCropActivity.class);
        intent.putExtra("PHOTO_PATH", picPath);
        activity.startActivityForResult(intent,Constants.CROP_IMG);
    }


    public static void startTakePhoto(Activity act) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //下面这句指定调用相机拍照后的照片存储的路径
        File file= new File(Constants.PHOTONAME);
        Uri imgUri;
        if (Build.VERSION.SDK_INT >= 24) {
            imgUri = FileProvider.getUriForFile(act.getApplicationContext(), "com.lf.ninghaisystem.fileprovider", file);
        } else {
            imgUri = Uri.fromFile(file);
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        act.startActivityForResult(intent, Constants.TAKE_PHOTO);

    }

    public static void startPickPhoto(Activity act){
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        act.startActivityForResult(intent, Constants.PICK_PHOTO);
    }
}
