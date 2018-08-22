package com.lf.ninghaisystem.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.bean.entity.DutyNotice;

import java.util.List;

/**
 * Created by admin on 2017/11/13.
 */

public class DutyNoticeAdapter extends ArrayAdapter<DutyNotice>{

    private List<DutyNotice> dutyNoticeList;
    private Context context;
    private int resource;

    public DutyNoticeAdapter(@NonNull Context context, int resource, @NonNull List<DutyNotice> objects) {
        super(context, resource, objects);

        this.context = context;
        this.dutyNoticeList = objects;
        this.resource = resource;

    }

    @Override
    public int getCount() {
        return dutyNoticeList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;
        DutyNotice dutyNotice = dutyNoticeList.get(position);

        if(convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(resource,null);
        } else {
            view = convertView;
        }

        ImageView noticePoint = view.findViewById(R.id.notice_point);
        TextView noticeTitle = view.findViewById(R.id.duty_notice_title);
        TextView noticeText = view.findViewById(R.id.duty_notice_text);
        TextView noticeSend = view.findViewById(R.id.duty_notice_send);
        TextView noticeDate = view.findViewById(R.id.duty_notice_date);
        if(dutyNotice.isRead()) {
            noticePoint.setVisibility(View.INVISIBLE);
        } else {
            noticePoint.setVisibility(View.VISIBLE);
        }

        noticeTitle.setText(dutyNotice.getTitle());
        noticeText.setText(dutyNotice.getMessage());
        noticeSend.setText(dutyNotice.getSendPeople());
        noticeDate.setText(dutyNotice.getDate());

        return view;
    }
}
