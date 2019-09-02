package com.ysxsoft.imtalk.widget

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * 禁止滑动的Viewpager
 */
@SuppressLint("ViewConstructor")
class MyViewPager : ViewPager {

    private var isCanScroll = true

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    fun setScanScroll(isCanScroll: Boolean) {
        this.isCanScroll = isCanScroll
    }


    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return false
    }


    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return false
    }

}
