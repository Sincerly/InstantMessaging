package com.ysxsoft.imtalk.utils

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.AndroidException
import android.util.Log
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.CommonBean
import com.ysxsoft.imtalk.chatroom.utils.log.SLog
import com.ysxsoft.imtalk.impservice.ImpService
import io.rong.imkit.RongIM
import io.rong.imlib.RongIMClient
import kotlinx.android.synthetic.main.title_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers
import java.util.*

abstract class BaseActivity : AppCompatActivity() {

    lateinit var mContext: Context
    private var mEnableListenKeyboardState = false

    var mActivityPageManager: ActivityPageManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        mActivityPageManager = ActivityPageManager.getInstance()
        mActivityPageManager!!.addActivity(this)
        Log.e("activity", localClassName)
        mContext = this
        initUi()
    }

    override fun onResume() {
        super.onResume()
        if (mEnableListenKeyboardState) {
            addKeyboardStateListener()
        }
    }


    /**
     * 启动键盘状态监听
     *
     * @param enable
     */
    fun enableKeyboardStateListener(enable: Boolean) {
        mEnableListenKeyboardState = enable
    }

    /**
     * 添加键盘显示监听
     */
    private fun addKeyboardStateListener() {
        window.decorView.viewTreeObserver.addOnGlobalLayoutListener(onKeyboardStateChangedListener)
    }

    /**
     * 移除键盘显示监听
     */
    private fun removeKeyBoardStateListener() {
        window.decorView.viewTreeObserver.removeOnGlobalLayoutListener(onKeyboardStateChangedListener)
    }

    /**
     * 监听键盘显示状态
     */
    private val onKeyboardStateChangedListener = object : ViewTreeObserver.OnGlobalLayoutListener {
        internal var mScreenHeight = 0

        private val screenHeight: Int
            get() {
                if (mScreenHeight > 0) {
                    return mScreenHeight
                }
                val point = Point()
                (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(point)
                mScreenHeight = point.y
                return mScreenHeight
            }

        override fun onGlobalLayout() {
            val rect = Rect()
            // 获取当前窗口显示范围
            window.decorView.getWindowVisibleDisplayFrame(rect)
            val screenHeight = screenHeight
            val keyboardHeight = screenHeight - rect.bottom // 输入法的高度
            var isActive = false
            if (Math.abs(keyboardHeight) > screenHeight / 4) {
                isActive = true // 超过屏幕1/4则表示弹出了输入法
            }

            onKeyboardStateChanged(isActive, keyboardHeight)
        }
    }

    /**
     * 当软键盘显示时回调
     * 此回调在调用[BaseActivity.enableKeyboardStateListener]启用监听键盘显示
     *
     * @param isShown
     * @param height
     */
    open fun onKeyboardStateChanged(isShown: Boolean, height: Int) {

    }


    open fun initUi() {

    }

    /**
     * 显示并关闭界面
     */
    fun setBackVisibily() {
        img_back.visibility = View.VISIBLE
        img_back.setOnClickListener {
            finish()
        }
    }


    /**
     * 关闭界面
     * @param v
     */
    fun clickBack(v: View) {
        finish()
    }

    abstract fun getLayout(): Int

    fun setTitle(title: String) {
        tv_title.setText(title)
    }

    /**
     * 弹出Toast信息
     */
    protected fun showToastMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * 弹出Toast信息
     */
    protected fun showToastMessage(resId: Int) {
        showToastMessage(resources.getString(resId))
    }


    protected fun startActivity(clazz: Class<*>) {
        startActivity(Intent(this, clazz))
    }

    /**
     * 全透状态栏
     */
    protected fun setStatusBarFullTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //虚拟键盘也透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    protected fun setLightStatusBar(dark: Boolean) {
        // 5.0 以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val decorView = window.decorView
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //			activity.getWindow().addFlags(SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//SYSTEM_UI_FLAG_LIGHT_STATUS_BAR深色字体   释放会导致无法截屏
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)//SYSTEM_UI_FLAG_LIGHT_STATUS_BAR深色字体
        }
        if (dark) {
            val originFlag = window.decorView.systemUiVisibility
            window.decorView.systemUiVisibility = originFlag or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR//黑色
        } else {
            val originFlag = window.decorView.systemUiVisibility
            window.decorView.systemUiVisibility = originFlag or View.SYSTEM_UI_FLAG_VISIBLE//白色
        }
    }

    /**
     * 半透明状态栏
     */
    protected fun setHalfTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            val decorView = window.decorView
            val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility = option
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //虚拟键盘也透明
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 如果需要内容紧贴着StatusBar
     * 应该在对应的xml布局文件中，设置根布局fitsSystemWindows=true。
     */
    private var contentViewGroup: View? = null

    protected fun setFitSystemWindow(fitSystemWindow: Boolean) {
        if (contentViewGroup == null) {
            contentViewGroup = (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)
        }
        contentViewGroup!!.fitsSystemWindows = fitSystemWindow
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    protected fun getStateBar(): Int {
        var result = 0
        val resourceId = this.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = this.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun initStatusBar(topView: View) {
        topView.layoutParams = LinearLayout.LayoutParams(DeviceUtils.getScreenWidthAndHeight(mContext, true), getStateBar())
    }

    fun initToolBar(topView: View) {
        topView.layoutParams.height = getStateBar()
    }

    /**
     * 发送短信验证码
     */
    fun SendMsg(phone: String) {
        NetWork.getService(ImpService::class.java)
                .telCode(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CommonBean>{
                    override fun onError(e: Throwable?) {
                        Log.d("tag>>>>",e!!.message.toString())
                    }

                    override fun onNext(t: CommonBean?) {
                        showToastMessage(t!!.msg)
                    }

                    override fun onCompleted() {
                    }
                })
    }

    val TAG = "BaseActivity"

    fun loginToIM(imToken: String?) {
        RongIM.connect(imToken, object : RongIMClient.ConnectCallback() {
            override fun onTokenIncorrect() {
                SLog.e(TAG, "RongIMClient onTokenIncorrect")
            }

            override fun onSuccess(s: String) {
                SLog.d(TAG, "RongIMClient connect success")
            }

            override fun onError(errorCode: RongIMClient.ErrorCode) {
                SLog.e(TAG, "RongIMClient connect onError:" + errorCode.value + "-" + errorCode.message)
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        mActivityPageManager!!.removeActivity(this)
    }
}