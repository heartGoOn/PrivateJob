package com.lf.ninghaisystem.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.bean.entity.DutyPerson;

import java.util.List;

/**
 * Created by admin on 2017/11/14.
 */

public class DutyPersonListAdapter extends ArrayAdapter<DutyPerson> {

    private Context context;
    private int resource;
    private List<DutyPerson> dutyPersonList;

    public DutyPersonListAdapter(@NonNull Context context, int resource, @NonNull List<DutyPerson> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.dutyPersonList = objects;

    }

    @Override
    public int getCount() {
        return dutyPersonList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;
        DutyPerson dutyPerson = dutyPersonList.get(position);

        if(convertView == null) {
            view = LayoutInflater.from(context).inflate(resource,null);
        } else {
            view = convertView;
        }

        TextView tv_name = view.findViewById(R.id.duty_person_name);
        TextView tv_status = view.findViewById(R.id.duty_person_status);

        tv_name.setText(dutyPerson.getName());
        tv_status.setText(dutyPerson.getStatus());

        return view;
    }

    public void updateItem(List<DutyPerson> list) {
        this.dutyPersonList = list;
        notifyDataSetChanged();
    }

}
