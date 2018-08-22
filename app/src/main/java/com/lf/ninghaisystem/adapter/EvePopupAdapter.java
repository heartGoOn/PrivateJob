package com.lf.ninghaisystem.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.activities.MyApplication;

import java.util.List;

/**
 * Created by admin on 2018/1/4.
 */

public class EvePopupAdapter extends ArrayAdapter {

    private String[] datas = {"部门", "代表", "项目"};
    private int[] imgs = {R.mipmap.project_department,R.mipmap.project_legalrepresentive,R.mipmap.project_project_evaluation};

    Context context;
    int resource;

    public EvePopupAdapter(@NonNull Context context, int resource) {
        super(context, resource);

        this.context = context;
        this.resource = resource;

    }

    @Override
    public int getCount() {
        return 3;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        if(convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(resource,null);
        } else {
            view = convertView;
        }

        ImageView menuIcon = view.findViewById(R.id.menu_icon);
        TextView menuTitle = view.findViewById(R.id.menu_title);

        menuIcon.setImageResource(imgs[position]);
        menuTitle.setText(datas[position]);

        view.setTag(menuTitle.getText().toString());

        return view;
    }
}
