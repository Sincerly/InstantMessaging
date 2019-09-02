package com.ysxsoft.imtalk.widget

import android.app.Dialog
import android.content.Context
import android.view.View
import com.ysxsoft.imtalk.R

abstract class ABSDialog : Dialog {

    constructor(context: Context) : super(context, R.style.dialog) {

        setContentView(getLayoutResId())
        initView()
    };

    /**
     * 根据Id获取View
     */
    protected fun <T : View> getViewById(id: Int): T {
        return findViewById<View>(id) as T
    }

    protected abstract fun initView()

    protected abstract fun getLayoutResId(): Int
}