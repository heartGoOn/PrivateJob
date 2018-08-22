package com.lf.ninghaisystem.contact;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

import com.lf.ninghaisystem.R;

/**
 * Created by chen on 2017/11/11.
 */


public class SearchEditText extends android.support.v7.widget.AppCompatEditText implements TextWatcher, View.OnFocusChangeListener{

    private Drawable mClearDrawable;

    public SearchEditText(Context context) {
        this(context,null);
    }

    public SearchEditText(Context context, AttributeSet attrs) {
        this(context, attrs,R.attr.editTextStyle);
    }

    public SearchEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        setClearIconVisible(charSequence.length() > 0);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (b) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getCompoundDrawables()[2] != null) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                boolean touchable = event.getX() > (getWidth() - getPaddingRight() -
                        mClearDrawable.getIntrinsicWidth()) &&
                        (event.getX() < (getWidth() - getPaddingRight()));
                if(touchable) {
                    this.setText("");
                }
            }
        }

        return super.onTouchEvent(event);
    }

    private void init() {
        mClearDrawable = getCompoundDrawables()[2];     //获取四个方向 [2]表示right
        if (mClearDrawable == null) {
            mClearDrawable = getResources()
                    .getDrawable(R.drawable.clear);
        }
        mClearDrawable.setBounds(0, 0,
                mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
        setClearIconVisible(false);
        setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    public void setShakeAnimation(){
        this.setAnimation(shakeAnimation(5));
    }


    public static Animation shakeAnimation(int counts){
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }

}
