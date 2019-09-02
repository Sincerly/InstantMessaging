package com.ysxsoft.imtalk.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.title_layout.*


abstract class BaseFragment : Fragment() {
    lateinit var mContext: Context
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(getLayoutResId(), null)
        mContext = this!!.activity!!
        initListener()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    fun setTitle(title: String) {
        tv_title.setText(title)
    }

    protected abstract fun getLayoutResId(): Int

    open fun initListener() {}
    open fun initUi() {}

    /**
     * 弹出Toast信息
     */
    protected fun showToastMessage(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * 弹出Toast信息
     */
    protected fun showToastMessage(resId: Int) {
        showToastMessage(resources.getString(resId))
    }

    /**
     * 打印Log，tag默认为类的名字
     */
    protected fun log(text: String) {
        log(this.javaClass.name, text)
    }

    /**
     * 打印Log
     */
    protected fun log(tag: String, text: String) {
        Log.i(tag, text)
    }

    protected fun startActivity(clazz: Class<*>) {
        startActivity(Intent(activity, clazz))
    }

    /**
     * 获取状态栏高度
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

}