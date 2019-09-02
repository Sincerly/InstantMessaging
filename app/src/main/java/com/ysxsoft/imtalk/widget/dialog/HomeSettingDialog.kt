package com.ysxsoft.imtalk.widget.dialog

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.widget.ABSDialog
import kotlinx.android.synthetic.main.home_setting_dialog_layout.*

/**
 *Create By 胡
 *on 2019/7/17 0017
 */
class HomeSettingDialog : ABSDialog {

    constructor(mContext: Context):super(mContext){
        val window = window
        window.setGravity(Gravity.BOTTOM)
        val params = window.attributes
        params.width=ViewGroup.LayoutParams.MATCH_PARENT
        params.height=ViewGroup.LayoutParams.WRAP_CONTENT
        window.attributes=params
    }

    override fun initView() {
        tv_cancle.setOnClickListener {
            dismiss()
        }

        tv_ht.setOnClickListener {
            dismiss()
            if (homeSettingClickListener!=null){
                homeSettingClickListener!!.clickHT()
            }
        }

        tv_exit.setOnClickListener {
            dismiss()
            if (homeSettingClickListener!=null){
                homeSettingClickListener!!.clickExit()
            }
        }

        tv_small.setOnClickListener {
            dismiss()
            if (homeSettingClickListener!=null){
                homeSettingClickListener!!.clickSmall()
            }
        }

        tv_report.setOnClickListener {
            dismiss()
            if (homeSettingClickListener!=null){
                homeSettingClickListener!!.clickReport()
            }
        }


    }

    override fun getLayoutResId(): Int {
        return R.layout.home_setting_dialog_layout
    }

    interface HomeSettingClickListener {
        fun clickHT();
        fun clickExit();
        fun clickSmall();
        fun clickReport();
    }

    private var homeSettingClickListener: HomeSettingClickListener? = null
    fun setHomeSettingClickListener(homeSettingClickListener: HomeSettingClickListener) {
        this.homeSettingClickListener = homeSettingClickListener
    }

}