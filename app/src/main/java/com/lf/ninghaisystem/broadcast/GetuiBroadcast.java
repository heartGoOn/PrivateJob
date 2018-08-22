package com.lf.ninghaisystem.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.activities.MessageActivity;
import com.lf.ninghaisystem.util.DialogHelp;

/**
 * Created by admin on 2017/12/18.
 */

public class GetuiBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.v("broadcast","OK");
        if(intent.getAction().equals("com.lf.ninghai.GETUI")) {

            final String type = intent.getStringExtra("type");
            final String content = intent.getStringExtra("content");

            DialogHelp.getConfirmDialog(context, content,"立即前往", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent1 = new Intent(context,MessageActivity.class);
                    intent1.putExtra("type",type);
                    context.startActivity(intent1);
                }
            }).setCancelable(false).create().show();

        }
    }
}
