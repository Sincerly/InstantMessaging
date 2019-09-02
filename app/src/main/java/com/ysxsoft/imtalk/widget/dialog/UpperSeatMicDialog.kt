package com.ysxsoft.imtalk.widget.dialog

import android.content.Context
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.widget.ABSDialog
import kotlinx.android.synthetic.main.upper_seatmic_dialog_layout.*

/**
 *Create By 胡
 *on 2019/7/17 0017
 *
 */
class UpperSeatMicDialog : ABSDialog {

    constructor(mContext: Context) : super(mContext) {
        val window = window
        window.setGravity(Gravity.BOTTOM)
        val params = window.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height - WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = params
    }


    override fun initView() {
        tv_cancle.setOnClickListener {
            dismiss()
        }

        tv_sm.setOnClickListener {
            dismiss()
            if (onDialogClickListener != null) {
                onDialogClickListener!!.SMclick(tv_sm)
            }
        }

        tv_btsm.setOnClickListener {
            dismiss()
            if (onDialogClickListener != null) {
                onDialogClickListener!!.BTSMclick(tv_btsm)
            }
        }

        tv_close_mic.setOnClickListener {
            dismiss()
            if (onDialogClickListener != null) {
                onDialogClickListener!!.CSMclick(tv_close_mic)
            }
        }
        tv_bm.setOnClickListener {
            dismiss()
            if (onDialogClickListener != null) {
                onDialogClickListener!!.BMclick(tv_bm)
            }

        }

    }

    override fun getLayoutResId(): Int {
        return R.layout.upper_seatmic_dialog_layout
    }

    interface OnDialogClickListener {
        fun SMclick(view:TextView)
        fun BTSMclick(view:TextView)
        fun CSMclick(view:TextView)
        fun BMclick(view:TextView)
    }

    private var onDialogClickListener: OnDialogClickListener? = null
    fun setOnDialogClickListener(onDialogClickListener: OnDialogClickListener) {
        this.onDialogClickListener = onDialogClickListener
    }

}