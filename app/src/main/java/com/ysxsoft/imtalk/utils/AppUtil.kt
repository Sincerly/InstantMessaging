package com.ysxsoft.imtalk.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.PendingIntent
import android.content.ClipboardManager
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.LocationManager
import android.media.AudioManager
import android.media.RingtoneManager
import android.media.SoundPool
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Vibrator
import android.provider.Settings
import android.support.annotation.DrawableRes
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.gson.Gson
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.lang.reflect.Type
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

//静态类
object AppUtil {


    fun getResourcesUri(@DrawableRes id: Int, mContext: Context): String {
        val resources = mContext.resources
        var uriPath = ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(id) + "/" +
                resources.getResourceTypeName(id) + "/" +
                resources.getResourceEntryName(id);
        return uriPath;
    }


    /**
     * 判断网络类型
     *
     * @param context
     * @return
     */
    fun NetType(context: Context): String? {
        try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = cm.activeNetworkInfo
            var typeName = info.typeName.toLowerCase() // WIFI/MOBILE
            if (typeName.equals("wifi", ignoreCase = true)) {
            } else {
                typeName = info.extraInfo.toLowerCase()
                // 3gnet/3gwap/uninet/uniwap/cmnet/cmwap/ctnet/ctwap
            }
            return typeName
        } catch (e: Exception) {
            return null
        }
    }

    /**
     * 网络是否可用
     *
     * @param mContext
     * @return
     */
    fun isNetworkAvaiable(context: Context): Boolean? {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = cm.activeNetworkInfo
        return info != null && info.isConnected
    }

    /**
     * 关闭软键盘
     *
     * @param mContext
     * @param view
     */

    fun colseKeyboard(context: Context, view: View) {
        try {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        } catch (e: Exception) {
        }
    }

    /**
     * 关闭手机软件盘
     * @param activity
     */
    fun colsePhoneKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive && activity.currentFocus != null) {
            if (activity.currentFocus!!.windowToken != null) {
                imm.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
    }

    /**
     * 打开软件盘
     *
     * @param mContext
     */

    fun openKeyboard(context: Context) {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    /**
     * 是否正在运行
     *
     * @param context
     * @param packageName
     * @return
     */
    fun isRunning(context: Context, packageName: String): Boolean? {
        var isAppRunning = false
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val list = am.getRunningTasks(100)
        for (info in list) {
            if (info.topActivity.packageName == packageName && info.baseActivity.packageName == packageName) {
                isAppRunning = true
                break
            }
        }
        return isAppRunning
    }

    /**
     * 启动app
     *
     * @param context
     * @param appName
     */
    fun launchApp(context: Context, appName: String) {
        val packageManager = context.packageManager
        // 获取手机里的应用列表
        val pInfo = packageManager.getInstalledPackages(0)
        for (i in pInfo.indices) {
            val p = pInfo[i]
            // 获取相关包的<application>中的label信息，也就是-->应用程序的名字
            val label = packageManager
                    .getApplicationLabel(p.applicationInfo).toString()
            println(label)
            if (label == appName) { // 比较label
                val pName = p.packageName // 获取包名
                var intent: Intent? = Intent()
                // 获取intent
                intent = packageManager.getLaunchIntentForPackage(pName)
                context.startActivity(intent)

            }
        }
    }

    /**
     * 获取当前应用的包名
     */

    fun getCurrentPageName(context: Context): String {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        //完整类名
        val runningActivity = activityManager.getRunningTasks(1)[0].topActivity.className
        return runningActivity.substring(runningActivity.lastIndexOf(".") + 1)
    }

    /**
     * 分享功能
     *
     * @param mContext      上下文
     * @param activityTitle Activity的名字
     * @param msgTitle      消息标题
     * @param msgText       消息内容
     * @param imgPath       图片路径，不分享图片则传null
     */


    fun shareMsg(mContext: Context, activityTitle: String,
                 msgTitle: String, msgText: String, imgPath: String?) {
        val intent = Intent(Intent.ACTION_SEND)
        if (imgPath == null || imgPath == "") {
            intent.type = "text/plain" // 纯文本
        } else {
            val f = File(imgPath)
            if (f.exists() && f.isFile) {
                intent.type = "image/jpg"
                val u = Uri.fromFile(f)
                intent.putExtra(Intent.EXTRA_STREAM, u)
            }
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle)
        intent.putExtra(Intent.EXTRA_TEXT, msgText)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        mContext.startActivity(Intent.createChooser(intent, activityTitle))
    }

    /**
     * GSON解析json数据
     *
     * @param json
     * @param cls
     */

    fun <T> parse(json: String, cls: Type): T? {
        try {
            val gson = Gson()
            return gson.fromJson<T>(json, cls)
        } catch (e: Exception) {
            Log.i("", "数据解析异常--" + e.message)
        }
        return null
    }


    /**
     * 拍照sd卡地址
     */
    fun getImageSDpath(): String {
        return if (Build.MANUFACTURER.equals("meizu", ignoreCase = true)) {
            Environment.getExternalStorageDirectory().toString() + "/Camera/Nuctech"
        } else {
            Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera/Nuctech"
        }
    }

    /**
     * 创建图片名称
     *
     * @param dateTaken
     * @return
     */
    fun createName(dateTaken: Long): String {
        val date = Date(dateTaken)
        @SuppressLint("SimpleDateFormat")
        val dateFormat = SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss")
        return dateFormat.format(date)
    }

    /**
     * 获得软件版本名字
     */
    fun getVersionName(context: Context): String {
        val manager = context.packageManager
        try {
            val info = manager.getPackageInfo(context.packageName, 0)
            return info.versionName
        } catch (e: PackageManager.NameNotFoundException) {
        }

        return ""
    }

    /**
     * 获取版本号
     * 获得软件VersionCode
     */
    fun getVersionCode(context: Context): Int {
        val manager = context.packageManager
        try {
            val info = manager.getPackageInfo(context.packageName, 0)
            return info.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
        }

        return 0
    }

    /**
     * 获得包名
     *
     * @param context
     * @return
     */
    fun getPackageName(context: Context): String {
        val manager = context.packageManager
        try {
            val info = manager.getPackageInfo(context.packageName, 0)
            return info.packageName
        } catch (e: PackageManager.NameNotFoundException) {
        }

        return ""
    }

    /**
     * 获取图标 bitmap
     *
     * @param context
     */
    @Synchronized
    fun getLogoBitmap(context: Context): Bitmap {
        var packageManager: PackageManager? = null
        var applicationInfo: ApplicationInfo? = null
        try {
            packageManager = context.applicationContext.packageManager
            applicationInfo = packageManager!!.getApplicationInfo(context.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            applicationInfo = null
        }

        val d = packageManager!!.getApplicationIcon(applicationInfo) //xxx根据自己的情况获取drawable
        val w = d.getIntrinsicWidth()
        val h = d.getIntrinsicHeight()
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
//        val bd = d as BitmapDrawable
        return bitmap
    }

    /**
     * 定时器
     *
     * @param handler
     * @param t
     * @return
     */
    fun showCheckCodeTimer(handler: Handler, t: Int): Timer {
        val timer = Timer()
        timer.schedule(MyTimerTask(handler, t), 0, 1000)
        return timer
    }

    internal class MyTimerTask(private val handler: Handler, private var t: Int) : TimerTask() {

        override fun run() {
            if (t >= 0) {
                handler.sendEmptyMessage(t)
                t--
            }
        }
    }

    /**
     * 检测手机号的格式是否正确
     *
     * @param phonenumber
     * @return
     */
    fun checkPhoneNum(phonenumber: String): Boolean {
        val regExp = "^13[0-9]{9}$|^14[0-9]{9}$|^15[0-9]{9}$|^18[0-9]{9}$|16[0-9]{9}$|^17[0-9]{9}$"
        val p = Pattern.compile(regExp)
        val m = p.matcher(phonenumber)
        return m.find()
    }

    /**
     * 检测身份证号格式是否正确
     *
     * @param idNum
     * @return
     */
    fun checkIdNum(idNum: String): Boolean {
        val regExp = "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$"
        val p = Pattern.compile(regExp)
        val m = p.matcher(idNum)
        return m.find()
    }

    /**
     * 核对车牌号
     *
     * @param carNBum
     * @return
     */
    fun checkCarNum(carNBum: String): Boolean {
        val regExp = "[\u4e00-\u9fa5]{1}[a-zA-Z]{1}[a-zA-Z_0-9]{5}"
        val p = Pattern.compile(regExp)
        val m = p.matcher(carNBum)
        return m.find()
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dip2px(context: Context, dpValue: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * 手机震动
     *
     * @param mContext
     */
    @SuppressLint("MissingPermission")
    fun showRock(mContext: Context) {
        var vibrator = mContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val pattern = longArrayOf(100, 400, 100, 400) // 停止 开启 停止 开启
        vibrator.vibrate(pattern, -1)// 重复两次上面的pattern 如果只想震动一次，index设为-1
//        vibrator = null
    }

    /**
     * 播放系统提示音
     *
     * @param mContext
     */
    fun playSystemNotificationVoice(mContext: Context) {
        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val r = RingtoneManager.getRingtone(mContext, notification)
        r.play()
    }

    /**
     * 播放自定义提示音
     */
    fun playCustomeVoice(mContext: Context, voiceId: Int) {
        val soundPool = SoundPool(5, AudioManager.STREAM_SYSTEM, 3)
        soundPool.load(mContext, voiceId, 1)
        soundPool.setOnLoadCompleteListener { soundPool, sampleId, status ->
            var soundPool = soundPool
            soundPool!!.play(1, 1f, 1f, 0, 0, 1f)
            soundPool = null
        }

    }

    /**
     * 打电话
     *
     * @param context
     * @param number
     */
    @SuppressLint("MissingPermission")
    fun callPhone(context: Context, number: String) {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$number"))
        context.startActivity(intent)
    }

    /**
     * 获取屏幕宽
     *
     * @param mContext
     * @return
     */
    fun getScreenWidth(mContext: Context): Int {
        val dm = DisplayMetrics()
        val wm = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(dm)
        return dm.widthPixels
    }

    /**
     * 获取屏幕高
     *
     * @param mContext
     * @return
     */
    fun getScreenHeight(mContext: Context): Int {
        val dm = DisplayMetrics()
        val wm = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(dm)
        return dm.heightPixels
    }

    /**
     * 根据逗号分割字符串
     *
     * @param str
     * @return
     */
    fun splitStr(str: String): String {
        val `as` = str.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (i in `as`.indices) {
            return `as`[i]
        }
        return str
    }

    /**
     * 根据点分割
     *
     * @param str
     * @return
     */
    fun splitStrPoint(str: String): String {
        val `as` = str.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (i in `as`.indices) {
            return `as`[i]
        }
        return str
    }

    /**
     * 去掉双引号
     *
     * @param string
     * @return
     */
    fun stringReplace(string: String): String {
        //去掉" "号
        return string.replace("\"", "")

    }

    /**
     * 下载完apk  通知系统安装  打开安装界面
     *
     * @param c
     * @param fileSavePath apk路径
     */
    private fun openAPKInstall(c: Context, fileSavePath: String) {
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = Intent.ACTION_VIEW
        intent.setDataAndType(Uri.parse("file://$fileSavePath"), "application/vnd.android.package-archive")
        c.startActivity(intent)
    }

    /**
     * 判断当前gps是否开启
     *
     * @param c
     * @return
     */
    fun isGPSEnable(c: Context): Boolean {
        /*
         * 用Setting.System来读取也可以，只是这是更旧的用法 String str =
         * Settings.System.getString(getContentResolver(),
         * Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
         */
        val str = Settings.Secure.getString(c.contentResolver, Settings.Secure.LOCATION_PROVIDERS_ALLOWED)
        Log.v("GPS", str)
        return str?.contains("gps") ?: false
    }

    /**
     * 判断GPS是否开启，GPS
     *
     * @param context
     * @return true 表示开启
     */
    fun isOPen(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        val gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        return if (gps) {
            true
        } else false

    }

    /**
     * 辅助gps 是否开启
     *
     * @param context
     * @return
     */
    fun isAGpsOpen(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        val network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return if (network) {
            true
        } else false
    }

    /**
     * 强制帮用户打开GPS
     *
     * @param context
     */
    fun openGPS(context: Context) {
        val GPSIntent = Intent()
        GPSIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider")
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE")
        GPSIntent.data = Uri.parse("custom:3")
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send()
        } catch (e: PendingIntent.CanceledException) {
            e.printStackTrace()
        }

    }

    /**
     * 跳转到gps页面
     *
     * @param c
     */
    fun initGpsSetttings(c: Context) {
        val lm = c.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // 判断GPS是否正常启动
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(c, "请开启GPS导航...", Toast.LENGTH_SHORT).show()
            // 返回开启GPS导航设置界面
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            (c as Activity).startActivityForResult(intent, 0)
            return
        }
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context
     * @param className 某个界面名称
     */
    fun isForeground(context: Context?, className: String): Boolean {
        if (context == null || TextUtils.isEmpty(className)) {
            return false
        }
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val list = am.getRunningTasks(1)
        if (list != null && list.size > 0) {
            val cpn = list[0].topActivity
            if (className == cpn.className) {
                return true
            }
        }
        return false
    }


    /**
     * 获取字符串的前三位
     *
     * @param str
     * @param i   前几位
     * @return
     */
    fun subBefore3Num(str: String, i: Int): String {
        return str.substring(0, i)
    }

    /**
     * 获取字符串的后四位数
     *
     * @param str 字符串
     * @param i   后几位
     * @return
     */
    fun subAfter4Num(str: String, i: Int): String {
        return str.substring(str.length - i, str.length)
    }

    /**
     * 复制
     * @param context
     * @param text
     */
    fun CopyToClipboard(context: Context, text: String) {
        val clip = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        //clip.getText(); // 粘贴
        clip.text = text // 复制
    }

    /**
     * All, 年月日时分秒
     * Year,年
     * Year_Mouth,年月
     * Year_Mouth_Day 年月日
     */
    enum class AppTime {
        All,
        Year,
        Year_Mouth,
        Year_Mouth_Day,
        H_M_S
    }

    private var format: String? = null

    /**
     * 格式化时间
     *
     * @param time
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    fun FormarTime(appTime: AppTime, time: Long): String? {
        when (appTime) {
            AppUtil.AppTime.All -> {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                format = dateFormat.format(time)
            }
            AppUtil.AppTime.Year -> {
                val dateFormat1 = SimpleDateFormat("yyyy")
                format = dateFormat1.format(time)
            }
            AppUtil.AppTime.Year_Mouth -> {
                val dateFormat2 = SimpleDateFormat("yyyy-MM")
                format = dateFormat2.format(time)
            }
            AppUtil.AppTime.Year_Mouth_Day -> {
                val dateFormat3 = SimpleDateFormat("yyyy-MM-dd")
                format = dateFormat3.format(time)
            }
            AppUtil.AppTime.H_M_S -> {
                val dateFormat4 = SimpleDateFormat("HH:mm:ss")
                format = dateFormat4.format(time)
            }
        }
        return format
    }


}


