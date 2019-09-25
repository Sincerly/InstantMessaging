package com.ysxsoft.imtalk.utils

import android.content.Context

object SpUtils {
    fun saveSp(context: Context, key: String, value: String) {
        val sp = context.getSharedPreferences("SAVE", Context.MODE_PRIVATE).edit()
        sp.putString(key, value)
        sp.commit()
    }

    fun getSp(context: Context, key: String): String {
        val getSp = context.getSharedPreferences("SAVE", Context.MODE_PRIVATE)
        return getSp.getString(key, "")
    }

    fun deleteSp(context: Context) {
        val deleteSp = context.getSharedPreferences("SAVE", Context.MODE_PRIVATE).edit()
        deleteSp.clear()
        deleteSp.commit()
    }

    fun savePwd(context: Context, key: String, value: String) {
        val sp = context.getSharedPreferences("PWD", Context.MODE_PRIVATE).edit()
        sp.putString(key, value)
        sp.commit()
    }

    fun getPwd(context: Context, key: String): String {
        val getSp = context.getSharedPreferences("PWD", Context.MODE_PRIVATE)
        return getSp.getString(key, "")
    }

    fun deletePWD(context: Context) {
        val deleteSp = context.getSharedPreferences("PWD", Context.MODE_PRIVATE).edit()
        deleteSp.clear()
        deleteSp.commit()
    }
}
