package com.ysxsoft.imtalk.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout

abstract class AbsLinearLayout : LinearLayout {

    /**
     * 布局资源ID
     */
    protected abstract val layoutResId: Int

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
        initAttributeSet(attrs)
    }

    /**
     * 初始化布局
     */
    private fun init() {
        LayoutInflater.from(context).inflate(layoutResId, this)
        initView()
    }

    /**
     * 初始化属性
     */
    protected fun initAttributeSet(attrs: AttributeSet) {

    }

    /**
     *
     * 获取指定资源Id的View
     */
    protected fun <T : View> getViewById(resId: Int): T {
        return findViewById<View>(resId) as T
    }

    /**
     * 初始化view
     */
    protected abstract fun initView()
}
