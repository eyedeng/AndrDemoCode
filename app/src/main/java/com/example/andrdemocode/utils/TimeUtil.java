package com.example.andrdemocode.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author dengyan
 * @date 2025/1/16
 * @desc
 */
public class TimeUtil {
    private static final String DATA_FORMAT = "MM-dd-HHmmss";

    public static String getFormattedTime() {
        return new SimpleDateFormat(DATA_FORMAT, Locale.getDefault()).format(new Date());
    }

}
