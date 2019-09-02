package com.ysxsoft.imtalk.view

import android.os.Bundle
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.utils.BaseActivity
import kotlinx.android.synthetic.main.payment_ok_layout.*

/**
 *Create By 胡
 *on 2019/8/1 0001
 */
class PaymentCompletionActivity : BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.payment_ok_layout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("提现")
        setBackVisibily()
        initView()
    }

    private fun initView() {
        tv_ok.setOnClickListener {
            finish()
        }

    }
}