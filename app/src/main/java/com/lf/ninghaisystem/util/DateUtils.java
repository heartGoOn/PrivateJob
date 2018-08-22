package com.lf.ninghaisystem.util;

import android.text.SpannableString;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by admin on 2017/12/1.
 */

public class DateUtils {

    public static SpannableString GetTime() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.CHINESE);
        long sysTime = System.currentTimeMillis();

        SpannableString timeTxt = new SpannableString(dateFormat.format(sysTime));
        Log.v("timeTxt",timeTxt+"");
        return timeTxt;
    }

    public static SpannableString GetDate1() {


        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMd日", Locale.CHINESE);
        long sysTime = System.currentTimeMillis();
        SpannableString timeTxt = new SpannableString(dateFormat.format(sysTime));

        return timeTxt;
    }

    public static SpannableString GetDate2() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MMMdd日", Locale.CHINESE);
        long sysTime = System.currentTimeMillis();
        SpannableString timeTxt = new SpannableString(dateFormat.format(sysTime));

        return timeTxt;
    }

    //2017-09-29
    public static String GetDate3() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long sysTime = System.currentTimeMillis();
        String timeTxt = new String(dateFormat.format(sysTime));
        return timeTxt;
    }
    public static String GetDate3_1(int dates) {
        long sysTime = System.currentTimeMillis();

        String timeTxt="";
        for (int i =0;i<dates;i++){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            timeTxt = new String(dateFormat.format(sysTime));
            sysTime -= 24 * 3600 * 1000;
        }



        return timeTxt;

    }

    public static String GetMonth() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        long sysTime = System.currentTimeMillis();
        String timeTxt = new String(dateFormat.format(sysTime));

        return timeTxt;

    }

    /*获取星期几*/
    public static String GetWeek(){
        Calendar cal = Calendar.getInstance();
        int i = cal.get(Calendar.DAY_OF_WEEK);
        switch (i) {
            case 1:
                return "星期日";
            case 2:
                return "星期一";
            case 3:
                return "星期二";
            case 4:
                return "星期三";
            case 5:
                return "星期四";
            case 6:
                return "星期五";
            case 7:
                return "星期六";
            default:
                return "";
        }
    }

    public static String getYear() {

        return Calendar.getInstance().get(Calendar.YEAR)+"";
    }

    /**
     * 计算相差的小时
     *
     * @param starTime
     * @param endTime
     * @return
     */
    public static float getTimeDifferenceHour(String starTime, String endTime) {
        float hour1 = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try {
            Date parse = dateFormat.parse(starTime);
            Date parse1 = dateFormat.parse(endTime);

            long diff = parse1.getTime() - parse.getTime();
            String string = Long.toString(diff);

            float parseFloat = Float.parseFloat(string);

            hour1 = parseFloat / (60 * 60 * 1000);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return hour1;

    }
}
