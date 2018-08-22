package com.lf.ninghaisystem.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.activities.MyApplication;
import com.lf.ninghaisystem.bean.User;
import com.lf.ninghaisystem.fragment.base.BaseBarFragment;

/**
 * Created by admin on 2017/11/8.
 */

public class UserMsgFragment extends BaseBarFragment {

    private TextView tv_name,tv_type,tv_num;
    private User user;

    @Override
    public void initData() {
        super.initData();

        /*Intent intent = getActivity().getIntent();
        user = (User) intent.getSerializableExtra("user");*/
        user = MyApplication.loginUser.getEmployeeInfo();
    }

    @Override
    public void initViews(View rootView) {
        super.initViews(rootView);
        setTitle("个人信息");
        setView(R.layout.fragment_user_msg);
        tv_name = rootView.findViewById(R.id.user_name);
        tv_type = rootView.findViewById(R.id.user_type);
        tv_num = rootView.findViewById(R.id.user_num);
        if(user!=null) {
            tv_name.setText(user.getName());
            tv_type.setText(user.getType());
            tv_num.setText(user.getPhoneNum());
        }
    }

    @Override
    protected boolean hasBackBtn() {
        return true;
    }

    @Override
    protected boolean hasRightBtn() {
        return false;
    }

    @Override
    protected boolean isRightImg() {
        return false;
    }
}
