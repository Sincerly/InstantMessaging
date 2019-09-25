package com.ysxsoft.imtalk.widget.dialog

import android.content.Context
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.widget.ABSDialog
import kotlinx.android.synthetic.main.bindiing_phone_layout.*

/**
 *Create By 胡
 *on 2019/9/24 0024
 */
class BindingPhoneDialog(mContext: Context):ABSDialog(mContext){
    override fun initView() {
        tv_cancle.setOnClickListener {
            dismiss()
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.bindiing_phone_layout
    }

}