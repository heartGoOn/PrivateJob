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
import com.lf.ninghaisystem.bean.entity.Duty;
import com.lf.ninghaisystem.bean.entity.DutyPerson;

import java.util.List;

/**
 * Created by admin on 2017/11/10.
 */

public class DutyListAdapter extends ArrayAdapter<Duty> {

    private Context context;
    private int resource;
    private List<Duty> dutyList;

    public DutyListAdapter(@NonNull Context context, int resource, @NonNull List<Duty> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.dutyList = objects;
    }

    @Override
    public int getCount() {
        return dutyList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;
        Duty duty = dutyList.get(position);

        if(convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(resource,null);
        } else {
            view = convertView;
        }

        TextView tv_title = view.findViewById(R.id.duty_item_title);
        TextView tv_dateTime = view.findViewById(R.id.duty_item_date);
        TextView tv_people = view.findViewById(R.id.duty_item_people);

        tv_title.setText(duty.getPerformerContent());
        tv_dateTime.setText(duty.getDutyDate());
        tv_people.setText(duty.getDutyPeople());

        return view;
    }

    public void updateItem(List<Duty> list) {
        this.dutyList = list;
        notifyDataSetChanged();
    }
}
