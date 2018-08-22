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

import com.lf.ninghaisystem.bean.User;
import com.lf.ninghaisystem.bean.entity.PopupType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/11/10.
 */

public class PopupAdapter extends ArrayAdapter {


    List<PopupType> types = new ArrayList<>();

    private User user;

    Context context;
    int resource;
    int isOwn;  //是否为用户自己项目

    int isHistory;

    public PopupAdapter(@NonNull Context context, int resource, int isOwn, int isHistory) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.isOwn = isOwn;
        this.isHistory = isHistory;
        user = MyApplication.loginUser.getEmployeeInfo();
        add();
    }

    @Override
    public int getCount() {
        return types.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(resource, null);
        } else {
            view = convertView;
        }

        ImageView menuIcon = view.findViewById(R.id.menu_icon);
        TextView menuTitle = view.findViewById(R.id.menu_title);
        menuIcon.setImageResource(types.get(position).getImgId());
        menuTitle.setText(types.get(position).getName());
        view.setTag(menuTitle.getText().toString());

        return view;
    }

    private void add() {
        if (user.getIsNpcMember().equals("1") && isOwn == 1 && isHistory == 0) {
            types.add(new PopupType(R.mipmap.duty_report, "履职"));
        }
        if (user.getIsCppccMember().equals("1") && isHistory == 0) {
            types.add(new PopupType(R.mipmap.duty_booster, "提醒"));
        }
        types.add(new PopupType(R.mipmap.duty_anaysis, "分析"));

    }
}