package com.lf.ninghaisystem.contact;

import java.util.Comparator;

/**
 * Created by chen on 2017/11/12.
 */

public class PinyinComparator implements Comparator<SortModel> {

    @Override
    public int compare(SortModel sortModel, SortModel t1) {
        if (sortModel.getLetters().equals("@")
                || t1.getLetters().equals("#")) {
            return -1;
        } else if (sortModel.getLetters().equals("#")
                || t1.getLetters().equals("@")) {
            return 1;
        } else {
            return sortModel.getLetters().compareTo(t1.getLetters());
        }
    }
}
