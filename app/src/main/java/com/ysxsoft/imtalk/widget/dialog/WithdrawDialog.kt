package com.ysxsoft.imtalk.widget.dialog

import android.content.Context
import android.view.Gravity
import android.view.WindowManager
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.widget.ABSDialog
import kotlinx.android.synthetic.main.withdraw_dialog_layout.*

/**
 *Create By 胡
 *on 2019/7/16 0016
 */
class WithdrawDialog : ABSDialog {
    constructor(mContext: Context) : super(mContext) {
        val window = window
        window.setGravity(Gravity.BOTTOM)
        val params = window.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = params
    }

    override fun initView() {
        tv_cancle.setOnClickListener {
            dismiss()
        }
        tv_ok.setOnClickListener {
            dismiss()
            if (payTypeClickListener!=null){
                payTypeClickListener!!.payType(1)
            }
        }
        tv_alipay.setOnClickListener {
            dismiss()
            if (payTypeClickListener!=null){
                payTypeClickListener!!.payType(1)
            }
        }
        tv_bank_card.setOnClickListener {
            dismiss()
            if (payTypeClickListener!=null){
                payTypeClickListener!!.payType(2)
            }
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.withdraw_dialog_layout
    }

    interface PayTypeClickListener{
        /**
         * payType 1：支付宝 2：银行卡
         */
        fun payType(payType:Int)
    }
    private var payTypeClickListener: PayTypeClickListener?=null
    fun setPayTypeClickListener(payTypeClickListener: PayTypeClickListener){
        this.payTypeClickListener=payTypeClickListener
    }
}