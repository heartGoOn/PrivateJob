package com.lf.ninghaisystem.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.bean.entity.DutyRank;

import java.util.List;

/**
 * Created by admin on 2017/11/16.
 */

public class DutyRankAdapter extends ArrayAdapter<DutyRank> {

    private Context context;
    private int resource;
    private List<DutyRank> dutyRankList;

    public DutyRankAdapter(@NonNull Context context, int resource, @NonNull List<DutyRank> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.dutyRankList = objects;

    }

    @Override
    public int getCount() {
        return dutyRankList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;
        DutyRank dutyRank = dutyRankList.get(position);

        if(convertView == null) {

            view = ((Activity)context).getLayoutInflater().inflate(resource,null);
        } else {
            view = convertView;
        }
        ImageView iv_imgRank = view.findViewById(R.id.img_rank);
        TextView tv_txtRank = view.findViewById(R.id.txt_rank);
        ImageView iv_imgHead = view.findViewById(R.id.img_head);
        TextView tv_personName = view.findViewById(R.id.duty_person_name);
        TextView tv_personStatus = view.findViewById(R.id.duty_person_status);
        TextView tv_dutyNum = view.findViewById(R.id.duty_num);

        if(dutyRank.getRank() <= 3 && dutyRank.getRank() >0) {
            iv_imgRank.setVisibility(View.VISIBLE);
            tv_txtRank.setVisibility(View.INVISIBLE);
            switch (dutyRank.getRank()) {
                case 1:
                    iv_imgRank.setImageResource(R.mipmap.dutyanaysis_no1);
                    break;
                case 2:
                    iv_imgRank.setImageResource(R.mipmap.dutyanaysis_no2);
                    break;
                case 3:
                    iv_imgRank.setImageResource(R.mipmap.dutyanaysis_no3);
                    break;
            }
        } else {
            iv_imgRank.setVisibility(View.INVISIBLE);
            tv_txtRank.setVisibility(View.VISIBLE);
            tv_txtRank.setText(dutyRank.getRank()+"");
        }

        tv_dutyNum.setText("履职次数 " + dutyRank.getDutyNum());
        tv_personName.setText(dutyRank.getName());
        tv_personStatus.setText(dutyRank.getStatus());

        return view;
    }

    public void updateItem(List<DutyRank> datas) {

        this.dutyRankList = datas;
        notifyDataSetChanged();
    }
}
