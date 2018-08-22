package com.lf.ninghaisystem.imgcrop;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;


import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.activities.base.BaseActivity;
import com.lf.ninghaisystem.util.ImageUtils;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Administrator on 2016/1/6.
 */
public class ImageCropActivity extends BaseActivity{

    @Override
    public void initViews() {
        super.initViews();
        setFragment(new ImageCropFragment());
    }

    @Override
    public void initData() {


    }

   /* synchronized public void checkTask() {
        finishedSize++;
        if(finishedSize == taskSize) {
            myHandler.removeCallbacks(percentRun);
            hideProgressDialog();
            Intent intent = getIntent();
            intent.putExtra("path", imgUrl);
            if(file!=null) {
                intent.putExtra("localpath", file.getAbsolutePath());
            }
            setResult(RESULT_OK, intent);
            hideWaitDialog();
            finish();
        }
    }

    private void hideProgressDialog() {
        if(mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }*/

    /*private void saveFileToLocal() {
        long timestamp = System.currentTimeMillis();
        String time = formatter.format(new Date());

        String fileName ="/cropped" + time + timestamp + ".jpg";

        Bitmap bm = cropimageview.getCropImage();

        data = ImageUtils.convertBitMapToByteArray(bm);

        //保存图片到本地
        file =  FileUtis.saveBitmap2File(bm, Constants.DIRPATH,fileName);

        taskSize = 0;
        finishedSize = 0;
        per = 0;

        *//*if (data != null&&data.length!=0) {
            //保存图片到七牛服务器上传至7niu
//            Log.i("file",fileName);
            FileUploadHelper.uploadData(data,0, uploadListener);
            taskSize++;
        }*//*
        if(taskSize!=0){
            showProgressDialog();
            myHandler.post(percentRun);
        }else{
            Intent intent = getIntent();
            intent.putExtra("path", imgUrl);
            if(file!=null) {
                intent.putExtra("localpath", file.getAbsolutePath());
            }
            setResult(RESULT_OK, intent);
            hideWaitDialog();
            finish();
        }

    }*/




}
