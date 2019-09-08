package com.ysxsoft.imtalk.widget.dialog

import android.app.Dialog
import android.content.Context
import android.view.View
import com.ysxsoft.imtalk.R

/**
 *Create By 胡
 *on 2019/9/8 0008
 */
abstract class RoomDialog:Dialog{

    constructor(context: Context) : super(context, R.style.musicdialog) {
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