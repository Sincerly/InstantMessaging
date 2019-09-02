package com.ysxsoft.imtalk.chatroom.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Create By 胡
 * on 2019/8/5 0005
 */
public class SpUtils {

    public static void saveSp(Context context, String key, String value) {
        SharedPreferences.Editor sp = context.getSharedPreferences("SAVE", Context.MODE_PRIVATE).edit();
        sp.putString(key, value);
        sp.apply();
    }

    public static String getSp(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("SAVE", Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    public static void deleteSp(Context context, String key, String value) {
        SharedPreferences.Editor deleteSp = context.getSharedPreferences("SAVE", Context.MODE_PRIVATE).edit();
        deleteSp.clear();
        deleteSp.apply();
    }


}
