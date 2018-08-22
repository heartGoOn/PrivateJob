package com.lf.ninghaisystem.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2017/11/24.
 */

public class SignGenerate {

    public static String generate(HashMap<String,Object> body) {
        ArrayList<String> valueList = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : body.entrySet()) {
            if(entry.getKey() != null)
                valueList.add(entry.getKey());
        }
        Collections.sort(valueList);
        String rs = "";
        for (String str: valueList) {
            rs = rs + str + body.get(str);
        }
        //rs += "ab138154c7d19f56bacede3e7c2c8ce7";
        return DigestUtils.md5Hex(rs);
    }


}
