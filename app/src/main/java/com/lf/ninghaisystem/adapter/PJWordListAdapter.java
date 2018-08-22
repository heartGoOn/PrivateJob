package com.lf.ninghaisystem.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.bean.entity.PJWord;

import java.util.List;

/**
 * Created by admin on 2017/12/5.
 */

public class PJWordListAdapter extends ArrayAdapter<PJWord> {

    private Context context;
    private int resource;
    private List<PJWord> pjWordList;

    public PJWordListAdapter(@NonNull Context context, int resource, @NonNull List<PJWord> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.pjWordList = objects;
    }

    @Override
    public int getCount() {
        return pjWordList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;

        if(convertView == null) {

            view =((Activity)context).getLayoutInflater().inflate(resource,null);

        } else {
            view = convertView;
        }

        TextView tv_title = view.findViewById(R.id.word_title);
        TextView tv_name = view.findViewById(R.id.word_name);

        tv_title.setText(pjWordList.get(position).getDocumentTitle());
        tv_name.setText(pjWordList.get(position).getUploadTime());

        return view;
    }
}
