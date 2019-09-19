package com.ysxsoft.imtalk.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesUtils {
    fun <T> save(context: Context, key: String, t: T) {
        val editor = get(context).edit()//获取编辑器
        if (t is String) {
            editor.putString(key, t as String)
        } else if (t is Float) {
            editor.putFloat(key, t as Float)
        } else if (t is Boolean) {
            editor.putBoolean(key, t as Boolean)
        } else if (t is Long) {
            editor.putLong(key, t as Long)
        } else if (t is Int) {
            editor.putInt(key, t as Int)
        }
        editor.commit()//提交修改
    }

    operator fun get(context: Context): SharedPreferences {
        return context.getSharedPreferences("ysxsoft_hai", Context.MODE_PRIVATE)
    }

    fun saveCarName(context: Context, value: String) {
        save(context, "carName", value)
    }

    fun getCarName(context: Context): String {
        return get(context).getString("carName", "")
    }

    fun saveCarPic(context: Context, value: String) {
        save(context, "carPic", value)
    }

    fun getCarPic(context: Context): String {
        return get(context).getString("carPic", "")
    }


}
