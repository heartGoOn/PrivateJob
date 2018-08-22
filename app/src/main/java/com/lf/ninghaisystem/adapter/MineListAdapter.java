package com.lf.ninghaisystem.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.bean.MineListItem;

import java.util.List;

/**
 * Created by admin on 2017/11/8.
 */

public class MineListAdapter extends ArrayAdapter<MineListItem> {

    private List<MineListItem> mineListItems;
    Context context;
    int resource;

    public MineListAdapter(@NonNull Context context, int resource, @NonNull List<MineListItem> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.mineListItems = objects;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;
        MineListItem mineListItem = mineListItems.get(position);

        if(convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(resource,null);
        } else {
            view = convertView;
        }

        TextView tv_title = view.findViewById(R.id.list_title);
        TextView tv_notice = view.findViewById(R.id.notice_txt);

        tv_title.setText(mineListItem.getTitle());

        if(mineListItem.getNoticeNum() > 0) {
            tv_notice.setVisibility(View.VISIBLE);
            tv_notice.setText(mineListItem.getNoticeNum() + "");
        } else if(mineListItem.getNoticeNum() <= 0) {
            tv_notice.setVisibility(View.GONE);
        }

        view.setTag(mineListItem.getTitle());

        return view;

    }

    @Override
    public int getCount() {
        return mineListItems.size();
    }

    public void updateItem(List<MineListItem> datas) {
        mineListItems = datas;
        this.notifyDataSetChanged();
    }

}
