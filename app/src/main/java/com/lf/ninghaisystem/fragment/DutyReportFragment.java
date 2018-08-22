package com.lf.ninghaisystem.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.activities.DutyReportActivity;
import com.lf.ninghaisystem.activities.HomeActivity;
import com.lf.ninghaisystem.activities.LoginActivity;
import com.lf.ninghaisystem.activities.MyApplication;
import com.lf.ninghaisystem.activities.base.FinishActivityManager;
import com.lf.ninghaisystem.bean.Local;
import com.lf.ninghaisystem.bean.entity.DutyType;
import com.lf.ninghaisystem.bean.entity.ImageData;
import com.lf.ninghaisystem.bean.entity.LoginUser;
import com.lf.ninghaisystem.bean.entity.MyProject;
import com.lf.ninghaisystem.bean.entity.Project;
import com.lf.ninghaisystem.bean.entity.Result;
import com.lf.ninghaisystem.fragment.base.BaseBarFragment;
import com.lf.ninghaisystem.http.retrofit.RetrofitUtil;
import com.lf.ninghaisystem.imgcrop.Constants;
import com.lf.ninghaisystem.imgcrop.CropBorderOption;
import com.lf.ninghaisystem.imgcrop.CropBorderView;
import com.lf.ninghaisystem.imgcrop.FileUtis;
import com.lf.ninghaisystem.imgcrop.ImageCropActivity;
import com.lf.ninghaisystem.imgcrop.ImageCropFragment;
import com.lf.ninghaisystem.util.DialogHelp;
import com.lf.ninghaisystem.util.JsonHelper;
import com.lf.ninghaisystem.util.SPHelper;
import com.lf.ninghaisystem.util.SignGenerate;
import com.lf.ninghaisystem.widget.LocationWidget;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static android.app.Activity.RESULT_OK;

/**
 * Created by admin on 2017/11/10.
 */

public class DutyReportFragment extends BaseBarFragment implements LocationWidget.OnSuccessMapLocation {

    private int initAsyn = 2; //初始化请求线程数
    private int finishAsyn = 0; //成功完成的请求数

    private TextView tv_location;
    private ImageView pick_pic1, pick_pic2, pick_pic3;
    private LocationWidget locationWidget;
    private EditText etContent;

    private TextView tv_pj_result, tv_type_result, tv_time_result, tv_duty_content;
    private RelativeLayout btnProjectPick, btnDutyTypePick, btnTimePick;
    private OptionsPickerView projectPickerView, dutyTypePickerView;
    private TimePickerView pvTime;
    private Button btnSubmit;
    private CheckBox cb_isSMS;

    private List<MyProject> pickProjectDatas = new ArrayList<>();
    private List<DutyType> pickDutyTypeDatas = new ArrayList<>();

    CROPTYPE currentCropType;//当前裁剪的对象

    //插入所需要数据---
    private String[] imagePaths = {null, null, null};
    private MyProject myProject = null;
    private DutyType dutyType = null;
    private long selectTime = 0;    //毫秒
    private String strContent = null;  //输入的内容
    private String strPhoto = "";    //照片id
    private Local mLocal;   //获取到的地址--经纬度
    private ImageData imageData;
    private int[] imgId = {-1, -1, -1};   //-1表示没有图片
    private String isSms = "0";
    //插入
    private Project mProject;   //是否是指定项目
    private LoginUser loginUser;

    enum CROPTYPE {
        CARD1//头像
    }

    @Override
    public void initData() {
        super.initData();
        showWaitDialog("加载中...").show();
        Intent intent = getActivity().getIntent();
        mProject = (Project) intent.getSerializableExtra("project");
        loginUser = MyApplication.loginUser;

        initDutyTypeDatas();
        initTimePicker();

        mLocal = new Local();
    }

    @Override
    public void initViews(View rootView) {
        super.initViews(rootView);
        setTitle("履职汇报");
        setView(R.layout.fragment_duty_report);

        locationWidget = new LocationWidget(getActivity());
        pick_pic1 = rootView.findViewById(R.id.pick_pic1);
        pick_pic2 = rootView.findViewById(R.id.pick_pic2);
        pick_pic3 = rootView.findViewById(R.id.pick_pic3);

        tv_location = rootView.findViewById(R.id.location);
        btnProjectPick = rootView.findViewById(R.id.project_select);
        btnDutyTypePick = rootView.findViewById(R.id.duty_type);
        btnTimePick = rootView.findViewById(R.id.date_select);

        cb_isSMS = rootView.findViewById(R.id.isSms);
        initLocation();

        pick_pic1.setOnClickListener(this);
        pick_pic2.setOnClickListener(this);
        pick_pic3.setOnClickListener(this);

        tv_pj_result = rootView.findViewById(R.id.pj_select_result);
        tv_type_result = rootView.findViewById(R.id.type_select_result);
        tv_time_result = rootView.findViewById(R.id.time_select_result);
        tv_duty_content = rootView.findViewById(R.id.duty_content);
        btnSubmit = rootView.findViewById(R.id.btn_submit);
        etContent = rootView.findViewById(R.id.duty_content);

        btnProjectPick.setOnClickListener(this);
        btnDutyTypePick.setOnClickListener(this);
        btnTimePick.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        if (mProject == null) {  //如果不是制定项目　初始化
            initProjectDatas();
            initAsyn = 2;
        } else {
            initAsyn = 1;
            btnProjectPick.setEnabled(false);
            tv_pj_result.setText(mProject.getpName());
            myProject = new MyProject();
            myProject.setpName(mProject.getpName());
            myProject.setProjectId(mProject.getProjectId());
        }

        cb_isSMS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isSms = b ? "1" : "0";
            }
        });

    }

    private void openPicker(int flag) { //flag==0项目选择flag==1履职类型选择flag==2时间选择

        if (flag == 0) {

            projectPickerView.show();
        } else if (flag == 1) {
            dutyTypePickerView.show();
        } else if (flag == 2) {
            pvTime.show();
        }

    }

    private int lastPick = 1;   //上次选择的位置 1,2,3

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.project_select:
                openPicker(0);
                break;
            case R.id.duty_type:
                openPicker(1);
                break;
            case R.id.date_select:
                openPicker(2);
                break;
            case R.id.btn_submit:
                SubmitDutyPerform();
                break;
            case R.id.pick_pic3:
                if (imgId[1] == -1) {
                    Toast.makeText(getActivity(), "请先上传第2张图片", Toast.LENGTH_SHORT).show();
                } else {
                    showActionSheetSelectUploadIdentityCard(CROPTYPE.CARD1);
                    lastPick = 3;
                }
                break;
            case R.id.pick_pic2:
                if (imgId[0] == -1) {
                    Toast.makeText(getActivity(), "请先上传第1张图片", Toast.LENGTH_SHORT).show();

                } else {
                    showActionSheetSelectUploadIdentityCard(CROPTYPE.CARD1);
                    lastPick = 2;
                }
                break;
            case R.id.pick_pic1:
                showActionSheetSelectUploadIdentityCard(CROPTYPE.CARD1);
                lastPick = 1;
                break;
        }
    }

    private void initDutyTypeDatas() {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", loginUser.getUid());
        hashMap.put("token", loginUser.getToken());
        String sign = SignGenerate.generate(hashMap);
        hashMap.put("sign", sign);

        String json = JsonHelper.hashMapToJson(hashMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);

        RetrofitUtil.getService().getDutyType(body).enqueue(new Callback<Result<List<DutyType>>>() {
            @Override
            public void onResponse(Response<Result<List<DutyType>>> response, Retrofit retrofit) {

                Result result = response.body();
                if (result.getRet() == 200) {
                    pickDutyTypeDatas = (List<DutyType>) result.getData();
                    initDutyTypePicker();

                    if (++finishAsyn == initAsyn) {
                        hideWaitDialog();
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
                getActivity().finish();
                Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initProjectDatas() {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", loginUser.getUid());
        hashMap.put("token", loginUser.getToken());
        hashMap.put("type", 0);
        String sign = SignGenerate.generate(hashMap);
        hashMap.put("sign", sign);

        String json = JsonHelper.hashMapToJson(hashMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);

        RetrofitUtil.getService().getMyProjectList(body).enqueue(new Callback<Result<List<MyProject>>>() {
            @Override
            public void onResponse(Response<Result<List<MyProject>>> response, Retrofit retrofit) {

                Result result = response.body();
                if (result.getRet() == 200) {

                    pickProjectDatas = (List<MyProject>) result.getData();
                    initProjectPicker();

                    if (++finishAsyn == initAsyn) {
                        hideWaitDialog();
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
                getActivity().finish();
                Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initProjectPicker() {

        projectPickerView = new OptionsPickerView.Builder(getActivity(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {

                if (pickProjectDatas.size() != 0) {

                    myProject = pickProjectDatas.get(options1);
                    String str = myProject.getPickerViewText();
                    tv_pj_result.setText(str);
                }
            }
        })
                .setTitleText("项目选择")
                .setContentTextSize(20)//设置滚轮文字大小
                .build();

        projectPickerView.setPicker(pickProjectDatas);

    }

    private void initDutyTypePicker() {

        dutyTypePickerView = new OptionsPickerView.Builder(getActivity(), new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {

                if (pickDutyTypeDatas.size() > 0) {

                    dutyType = pickDutyTypeDatas.get(options1);
                    String str = dutyType.getPickerViewText();
                    tv_type_result.setText(str);
                }
            }
        })
                .setTitleText("履职类型")
                .setContentTextSize(20)//设置滚轮文字大小
                .build();

        dutyTypePickerView.setPicker(pickDutyTypeDatas);

    }

    private void initTimePicker() {

        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(selectedDate.get(Calendar.YEAR) - 10, 0, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(selectedDate.get(Calendar.YEAR) + 10, 11, 28);

        pvTime = new TimePickerView.Builder(getActivity(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                selectTime = date.getTime();
                tv_time_result.setText(getTime(date));
            }
        })
                .setTitleText("时间")
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("", "", "", "", "", "")
                .isCenterLabel(false)
                .setContentSize(21)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .build();

    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    private void initLocation() {

        locationWidget.setOnSuccessMapLocation(this);

        tv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tv_location.setEnabled(false);
                tv_location.setText("正在定位...");
                locationWidget.startLocation();

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationWidget.destroyLocation();
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


    @Override
    public void onMapLocation(Local local) {
        mLocal = local;
        tv_location.setText(local.getmLocation());
        tv_location.setEnabled(true);
    }

    private AlertDialog dialog;

    private void showActionSheetSelectUploadIdentityCard(final CROPTYPE cropType) {

        getActivity().setTheme(R.style.ActionSheetStyleiOS7);


        ActionSheet.createBuilder(getActivity(), getActivity().getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("拍照", "从相册选择", "查看大图")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                        currentCropType = cropType;
                        //SPUtil.put(PersonInfoActivity.this, "currentCropType", cropType.ordinal());
                        CropBorderView.borderOption = CropBorderOption.FIVE2EIGHT;
                        if (index == 0) {
                            ((DutyReportActivity) getActivity()).requestPermission(1, Manifest.permission.CAMERA, new Runnable() {
                                @Override
                                public void run() {
                                    //     GalleryFinal.openCamera(REQUEST_CAMERA_FRONT, callback);
                                    ImageCropFragment.startTakePhoto(getActivity());
                                }
                            }, new Runnable() {
                                @Override
                                public void run() {

                                }
                            });
                        } else if (index == 1) {
                            ((DutyReportActivity) getActivity()).requestPermission(1, Manifest.permission.READ_EXTERNAL_STORAGE, new Runnable() {
                                @Override
                                public void run() {
                                    ImageCropFragment.startPickPhoto(getActivity());
                                }
                            }, new Runnable() {
                                @Override
                                public void run() {

                                }
                            });
                        } else if (index == 2) {

                            if (imagePaths[lastPick - 1] != null) {
                                dialog = new AlertDialog.Builder(getActivity()).create();
                                ImageView imageView = getBigImgView(imagePaths[lastPick - 1]);
                                dialog.setView(imageView);
                                dialog.show();
                                imageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });
                            }
                        }
                    }
                }).show();
    }

    public void onResult(int requestCode, Intent data) {

        switch (requestCode) {
            case Constants.TAKE_PHOTO:
                //相机拍摄照片
                ImageCropFragment.startCropAct(getActivity(), Constants.PHOTONAME);
                break;
            case Constants.PICK_PHOTO:
                //从相册选择
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                ImageCropFragment.startCropAct(getActivity(), picturePath);
                break;
            case Constants.CROP_IMG:
                //更新图片
                String imagePath = data.getStringExtra("localpath");
                imageData = (ImageData) data.getSerializableExtra("ImageData");

                if (lastPick == 1) {
                    pick_pic1.setImageURI(Uri.fromFile(new File(imagePath)));
                } else if (lastPick == 2) {
                    pick_pic2.setImageURI(Uri.fromFile(new File(imagePath)));
                } else if (lastPick == 3) {
                    pick_pic3.setImageURI(Uri.fromFile(new File(imagePath)));
                }
                imgId[lastPick - 1] = imageData.getAttachmentId();
                imagePaths[lastPick - 1] = imagePath;

                break;
                /*if (currentCropType == null) {
                    return;
                }*/
                    /*if (SPUtil.contains(PersonInfoActivity.this, "currentCropType")) {
                        currentType = Integer.parseInt(SPUtil.get(PersonInfoActivity.this, "currentCropType", 0).toString());
                    }*/
                   /* switch (currentType) {
                        case 0:
                            urlCard1 = Constants.NETPICTAILAPPHEAD + data.getStringExtra("path") + Constants.NETPICTAILAPPEND;
                            imIcon.setImageURI(Uri.parse(urlCard1));
                            updateHeadImg(urlCard1);
                            break;
                    }*/

                /*case UPDATE_NICKNAME:
                    String nickName = data.getStringExtra("nick_name");
                    tv_nike_name.setText(nickName);
                    break;*/
        }
    }

    private void SubmitDutyPerform() {

        strContent = etContent.getText().toString();

        if (myProject == null || dutyType == null || selectTime == 0 || strContent == null || strContent == "") {
            Toast.makeText(getActivity(), "请完善所有信息!", Toast.LENGTH_SHORT).show();
            return;
        } else {

            for (int i = 0; i < imgId.length; i++) {
                if (imgId[i] != -1) {
                    if (i == 0) {
                        strPhoto += "" + imgId[i];
                    } else {
                        strPhoto += "," + imgId[i];
                    }
                }
            }

        }

        showWaitDialog("正在提交...").show();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("projectId", myProject.getProjectId());
        hashMap.put("performType", dutyType.getTypeId());
        hashMap.put("performTime", selectTime);
        hashMap.put("performDescription", strContent);
        hashMap.put("performPlace", mLocal.getmLocation());
        hashMap.put("performLatitude", mLocal.getmLatitude());
        hashMap.put("performLongitude", mLocal.getmLongitude());
        hashMap.put("attachmentIds", "[" + strPhoto + "]");
        hashMap.put("isSms", isSms);
        hashMap.put("uid", loginUser.getUid());
        hashMap.put("token", loginUser.getToken());
        String sign = SignGenerate.generate(hashMap);
        hashMap.put("sign", sign);

        String json = JsonHelper.hashMapToJson(hashMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);

        RetrofitUtil.getService().insertDutyPerform(body).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Response<Result> response, Retrofit retrofit) {

                Result result = response.body();
                if (result.getRet() == 200) {
                    if (FileUtis.deleteAllTempImg()) {
                        getActivity().setResult(RESULT_OK);
                        getActivity().finish();
                        Toast.makeText(getActivity(), "提交成功！", Toast.LENGTH_SHORT).show();
                    }

                } else if (result.getRet() == 111) {

                    SPHelper.clearLoginUser();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    FinishActivityManager.getManager().finishActivity(HomeActivity.class);
                } else {
                    Toast.makeText(getActivity(), "提交失败！", Toast.LENGTH_SHORT).show();
                }

                hideWaitDialog();

            }

            @Override
            public void onFailure(Throwable t) {
//                hideWaitDialog();
//                Toast.makeText(getContext(), "提交失败！", Toast.LENGTH_SHORT).show();
            }
        });


    }


    private ImageView getBigImgView(String imagePath) {
        ImageView imageView = new ImageView(getActivity());
        imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setImageURI(Uri.fromFile(new File(imagePath)));
        return imageView;
    }
}
