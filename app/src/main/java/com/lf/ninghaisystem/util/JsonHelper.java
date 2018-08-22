package com.lf.ninghaisystem.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by admin on 2017/12/8.
 */

public class JsonHelper {

    public static String hashMapToJson(HashMap map) {
        String string = "{";
        for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
            Map.Entry e = (Map.Entry) it.next();
            string += "\"" + e.getKey() + "\":";
            if(e.getValue() instanceof String) {
                if(((String) e.getValue()).contains("[") && ((String) e.getValue()).contains("]")) {
                    string += e.getValue()+",";
                } else {
                    string += "\"" + e.getValue() + "\",";
                }
            } else {
                string += e.getValue()+",";
            }
        }
        string = string.substring(0, string.lastIndexOf(","));
        string += "}";
        return string;
    }

}
