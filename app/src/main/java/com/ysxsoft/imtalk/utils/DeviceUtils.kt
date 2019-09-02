package com.ysxsoft.imtalk.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.TelephonyManager
import android.text.TextUtils
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.util.*

object DeviceUtils {

    /**
     * 获取屏幕宽高度
     *
     * @param context
     * @param isWidth 是否获取屏幕宽度true为是，false为获取高度
     * @return
     */
    fun getScreenWidthAndHeight(context: Context, isWidth: Boolean): Int {
        val metrics = context.resources
                .displayMetrics
        return if (isWidth) metrics.widthPixels else metrics.heightPixels
    }

    /**
     * @param context
     * @param isWidth
     * @return
     */
    fun getNormalDialogWidthOrHeight(context: Context, isWidth: Boolean): Int {
        return if (isWidth) {
            getScreenWidthAndHeight(context, isWidth) * 4 / 5
        } else getScreenWidthAndHeight(context, isWidth) / 2
    }

    /**
     * 获取设备状态栏高度
     *
     * @param context
     * @return
     */
    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * 获取app版本名称
     *
     * @param context
     * @return
     */
    fun getVersionName(context: Context): String {
        val pi: PackageInfo

        try {
            val pm = context.packageManager
            pi = pm.getPackageInfo(context.packageName,
                    PackageManager.GET_CONFIGURATIONS)
            return pi.versionName
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return "1.0.0"
    }

    /**
     * 获取配置渠道号
     *
     * @return
     */
    fun getChannel(context: Context): String? {
        var appInfo: ApplicationInfo? = null
        try {
            appInfo = context.packageManager
                    .getApplicationInfo(context.packageName,
                            PackageManager.GET_META_DATA)
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        return if (appInfo != null && appInfo.metaData != null) {
            appInfo.metaData.get("UMENG_CHANNEL").toString()
        } else null
    }

    /**
     * 获取umeng APPKEY
     *
     * @param context
     * @return
     */
    fun getUmengAppKey(context: Context): String? {
        var appInfo: ApplicationInfo? = null
        try {
            appInfo = context.packageManager
                    .getApplicationInfo(context.packageName,
                            PackageManager.GET_META_DATA)
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        return if (appInfo != null && appInfo.metaData != null) {
            appInfo.metaData.get("UMENG_APPKEY").toString()
        } else null
    }

    /**
     * 获取umeng SECRET
     *
     * @param context
     * @return
     */
    fun getUmengAppSecret(context: Context): String? {
        var appInfo: ApplicationInfo? = null
        try {
            appInfo = context.packageManager
                    .getApplicationInfo(context.packageName,
                            PackageManager.GET_META_DATA)
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        return if (appInfo != null && appInfo.metaData != null) {
            appInfo.metaData.get("UMENG_MESSAGE_SECRET").toString()
        } else null
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    fun getProcessName(pid: Int): String? {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
            var processName = reader.readLine()
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim { it <= ' ' }
            }
            return processName
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        } finally {
            try {
                reader?.close()
            } catch (exception: IOException) {
                exception.printStackTrace()
            }

        }
        return null
    }

    /**
     * 获取手机IMEI号
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission", "HardwareIds")
    fun getPhoneIMEI(context: Context): String {
        return (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).deviceId
    }

    /**
     * 获取手机IMSI号
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission", "HardwareIds")
    fun getPhoneIMSI(context: Context): String {
        return (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).subscriberId
    }

    /**
     * 获取制造商
     *
     * @return
     */
    fun getVendor(): String {
        return Build.MANUFACTURER
    }

    /**
     * 获取产品型号
     *
     * @return
     */
    fun getProduct(): String {
        return Build.PRODUCT
    }

    /**
     * 获取当前默认系统使用语言类型
     *
     * @return
     */
    fun getSystemLanguage(): String {
        return Locale.getDefault().language
    }

    /**
     * 获取当前系统版本
     *
     * @return
     */
    fun getOsVersion(): String {
        return Build.VERSION.RELEASE
    }

}