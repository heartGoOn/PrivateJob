package com.lf.ninghaisystem.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lf.ninghaisystem.R;

/**
 * Created by admin on 2017/11/10.
 */

public class ProjectMainAdapter extends BaseAdapter {

    private Context context;

    public ProjectMainAdapter(Context context) {
        this.context = context;
    }

    private Integer[] imgs = {R.mipmap.project_plan,R.mipmap.project_report,
        R.mipmap.project_word,R.mipmap.project_record,R.mipmap.project_person,
        R.mipmap.project_analysis,R.mipmap.project_department,R.mipmap.project_legalrepresentive};

    private String[] titles = {"项目计划","进度报告","项目文档","履职记录",
        "履职代表","履职分析","部门评价","代表评价"};

    @Override
    public int getCount() {
        return imgs.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View view = null;
        if(convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(R.layout.item_project_main,null);
        } else {
            view = convertView;
        }

        ImageView menuIcon = view.findViewById(R.id.menu_icon);
        TextView menuTitle = view.findViewById(R.id.menu_title);

        menuIcon.setImageResource(imgs[i]);
        menuTitle.setText(titles[i]);

        return view;
    }
}
