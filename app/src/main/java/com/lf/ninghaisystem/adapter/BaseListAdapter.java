package com.lf.ninghaisystem.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by admin on 2017/11/14.
 */

public class BaseListAdapter extends ArrayAdapter {

    public BaseListAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
    }
}
