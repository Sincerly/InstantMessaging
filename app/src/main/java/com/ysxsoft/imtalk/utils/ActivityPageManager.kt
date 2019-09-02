package com.ysxsoft.imtalk.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import android.widget.ListView
import java.util.*

class ActivityPageManager {
    private var activityStack: Stack<Activity>? = null

    companion object {//静态内部类
        private var instance: ActivityPageManager? = null
        fun getInstance(): ActivityPageManager? {
            if (instance == null) {
                instance = ActivityPageManager()
            }
            return instance
        }
    }

    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: Activity) {
        if (activityStack == null) {
            activityStack = Stack<Activity>()
        }
        activityStack!!.add(activity)
    }

    /**
     * 移除Activity
     */
    fun removeActivity(activity: Activity) {
        if (activityStack == null) {
            activityStack = Stack<Activity>()
        }
        activityStack!!.remove(activity)
    }

    /**
     * 当前Actiivty
     */
    fun currentActivity(): Activity {
        return activityStack!!.lastElement()
    }

    /**
     * 关闭当前Activity
     */
    fun finishActivity() {
        val activity = activityStack!!.lastElement()
        finishActivity(activity)
    }

    /**
     * 关闭某一个Actiivty
     */
    fun finishActivity(activity: Activity?) {
        var activity = activity
        if (activity != null) {
            activityStack!!.remove(activity)
            activity.finish()
            activity = null
        }
    }

    /**
     * 关闭某一个Actiivty
     */
    fun finishActivity(cls: Class<*>) {
        for (activity in this!!.activityStack!!) {
            if (activity.javaClass == cls) {
                finishActivity(activity)
            }
        }
    }

    /**
     * 清除所有Activity
     */
    fun finishAllActivity() {
        var i = 0
        val size = activityStack!!.size
        while (i < size) {
            if (null != activityStack!!.get(i)) {
                activityStack!!.get(i).finish()
            }
            i++
        }
        activityStack!!.clear()
    }

    /**
     * 清除View资源引用
     */
    fun unbindReferences(view: View?) {
        try {
            if (view != null) {
                view.destroyDrawingCache()
                unbindViewReferences(view)
                if (view is ViewGroup) {
                    unbindViewGroupReferences((view as ViewGroup?)!!)
                }
            }
        } catch (e: Throwable) {
        }

    }

    /**
     * 清除View资源引用
     */
    private fun unbindViewGroupReferences(viewGroup: ViewGroup) {
        val nrOfChildren = viewGroup.childCount
        for (i in 0 until nrOfChildren) {
            val view = viewGroup.getChildAt(i)
            unbindViewReferences(view)
            if (view is ViewGroup)
                unbindViewGroupReferences(view)
        }
        try {
            viewGroup.removeAllViews()
        } catch (mayHappen: Throwable) {
        }

    }

    private fun unbindViewReferences(view: View) {
        try {
            view.setOnClickListener(null)
            view.setOnCreateContextMenuListener(null)
            view.onFocusChangeListener = null
            view.setOnKeyListener(null)
            view.setOnLongClickListener(null)
            view.setOnClickListener(null)
        } catch (mayHappen: Throwable) {
        }

        var d: Drawable? = view.background
        if (d != null) {
            d.callback = null
        }

        if (view is ImageView) {
            d = view.drawable
            if (d != null) {
                d.callback = null
            }
            view.setImageDrawable(null)
            view.setBackgroundDrawable(null)
        }

        if (view is WebView) {
            var webview: WebView? = view
            webview!!.stopLoading()
            webview.clearFormData()
            webview.clearDisappearingChildren()
            webview.webChromeClient = null
            webview.webViewClient = null
            webview.destroyDrawingCache()
            webview.destroy()
            webview = null
        }

        if (view is ListView) {
            try {
                view.removeAllViewsInLayout()
            } catch (mayHappen: Throwable) {
            }

            view.destroyDrawingCache()
        }
    }

    /**
     * 退出程序
     */
    fun exit(context: Context?) {
        try {
            finishAllActivity()
            if (context != null) {
                val activityMgr = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                activityMgr.restartPackage(context.packageName)
            }
            System.exit(0)
            android.os.Process.killProcess(android.os.Process.myPid())
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}