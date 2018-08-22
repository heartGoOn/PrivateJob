package com.lf.ninghaisystem.contact;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created by chen on 2017/11/11.
 */

public class PinyinUtils {

    /**
     *  获取拼音
     */
    public static String getPinyin(String inputString) {

        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);     //小写
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);       //不带发音
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        char[] input = inputString.trim().toCharArray();
        String output = "";

        try {
            for (char curChar : input) {
                //是否为中文字符
                if(Character.toString(curChar).matches("[\\u4E00-\\u9FA5]+")) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(curChar, format);
                    output += temp[0];
                } else {
                    output += Character.toString(curChar);
                }
            }

        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }

        return output;

    }

    /**
     * 获取第一个字的拼音首字母
     */
    public static String getFirstSpell(String chinese) {

        StringBuffer pinyinBf = new StringBuffer();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (char curChar : arr) {
            if(curChar > 128) {
                try {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(curChar, defaultFormat);
                    if(temp != null) {
                        pinyinBf.append(temp[0].charAt(0));
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pinyinBf.append(curChar);
            }
        }
        // \\W表示非单词
        return pinyinBf.toString().replace("\\W","").trim();
    }

}
