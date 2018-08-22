package com.lf.ninghaisystem.bean;

/**
 * Created by admin on 2017/11/15.
 */

public class Local {

    private double mLongitude;
    private double mLatitude;
    private String mLocation;

    public Local() {

        mLongitude = 0;
        mLatitude = 0;
        mLocation = "";
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public String getmLocation() {
        return mLocation;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }
}
