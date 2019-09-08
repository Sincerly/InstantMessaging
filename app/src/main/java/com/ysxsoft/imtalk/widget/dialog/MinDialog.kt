package com.ysxsoft.imtalk.widget.dialog

import android.content.Context
import com.ysxsoft.imtalk.R

/**
 *Create By 胡
 *on 2019/9/8 0008
 */
class MinDialog:RoomDialog{

    constructor(mContext: Context):super(mContext){
        this.setCanceledOnTouchOutside(false)
        this.setCancelable(false)
    }
    override fun initView() {

    }

    override fun getLayoutResId(): Int {
        return R.layout.floatwindow_layout
    }

}