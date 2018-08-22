package com.lf.ninghaisystem.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.bean.entity.PJWord;
import com.lf.ninghaisystem.fragment.base.BaseBarFragment;
import com.lf.ninghaisystem.imgcrop.Constants;
import com.lf.ninghaisystem.imgcrop.FileUtis;
import com.lf.ninghaisystem.util.FileUtils;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;
import java.io.IOException;

/**
 * Created by admin on 2017/12/5.
 */

public class WordFragment extends BaseBarFragment {

    private PJWord pjWord;
    private Button btnOpen;
    private NumberProgressBar progressBar;
    private String type;
    private String path;
    private TextView tv_name,tv_msg;

    @Override
    public void initData() {
        super.initData();

        Intent intent = getActivity().getIntent();
        pjWord = (PJWord) intent.getSerializableExtra("word");
        type = pjWord.getDocumentUrl().substring(pjWord.getDocumentUrl().lastIndexOf("."),pjWord.getDocumentUrl().length());
        path = Constants.LOADPATH + "/" + pjWord.getDocumentName() + type;
    }

    @Override
    public void initViews(View rootView) {
        super.initViews(rootView);

        setView(R.layout.fragment_word);
        setTitle("文档");
        setRightTxt("下载");

        progressBar = rootView.findViewById(R.id.number_progress_bar);
        btnOpen = rootView.findViewById(R.id.btn_open);
        tv_name = rootView.findViewById(R.id.word_name);
        tv_msg = rootView.findViewById(R.id.word_msg);
        String pjName = pjWord.getDocumentName()+pjWord.getDocumentUrl().substring(pjWord.getDocumentUrl().lastIndexOf("."),pjWord.getDocumentUrl().length());

        tv_name.setText(pjName);
        if(hasCompleteFile(pjName)) {

            btnOpen.setVisibility(View.VISIBLE);

            tv_msg.setText(getResources().getString(R.string.word_msg2));

        }

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.v("path",path);

                Intent intent = null;
                switch (type) {

                    case ".docx":
                        intent = FileUtils.getWordFileIntent(path);
                        try {
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(getActivity(),"请安装第三方软件打开文档",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case ".xlsx":
                        intent = FileUtils.getExcelFileIntent(path);
                        try {
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(getActivity(),"请安装第三方软件打开文档",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case ".pdf":
                        intent = FileUtils.getPdfFileIntent(path);
                        try {
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(getActivity(),"请安装第三方软件打开文档",Toast.LENGTH_SHORT).show();
                        }
                        break;
                }

            }
        });

    }

    @Override
    protected void onRightBtnClick(View v) {
        super.onRightBtnClick(v);

        progressBar.setProgress(0);
        progressBar.setVisibility(View.VISIBLE);
        downLoadAction();
    }

    private void downLoadAction() {

        FileDownloader.getImpl().create(pjWord.getDocumentUrl())
                .setPath(path)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                        progressBar.setProgress((int)(soFarBytes / (totalBytes*1.0) * 100));

                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        progressBar.setProgress(0);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(),"下载完成",Toast.LENGTH_SHORT).show();
                        btnOpen.setVisibility(View.VISIBLE);
                        tv_msg.setText(getResources().getString(R.string.word_msg2));

                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        progressBar.setProgress(0);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(),"下载失败",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        progressBar.setProgress(0);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(),"下载失败",Toast.LENGTH_SHORT).show();
                    }
                }).start();

    }

    //判断本地目录是否存在
    private boolean hasCompleteFile(String fileName) {

        File dir = new File(Constants.LOADPATH);
        if (dir.exists()) {
            String names[] = dir.list();
            for (String str : names) {

                if(str.equals(fileName)) {
                    return true;
                }
            }
        } else {
            dir.mkdirs();
        }

        return false;
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
}
