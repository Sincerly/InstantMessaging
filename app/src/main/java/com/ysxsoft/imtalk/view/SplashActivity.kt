package com.ysxsoft.imtalk.view

import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import com.ysxsoft.imtalk.MainActivity
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.SpUtils
import kotlinx.android.synthetic.main.room_tag_layout.*
import kotlinx.android.synthetic.main.splash_layout.*

/**
 *Create By 胡
 *on 2019/7/3 0003
 */
class SplashActivity : BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.splash_layout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarFullTransparent()
        initView()
    }

    private fun initView() {
        var timer = object : CountDownTimer(3000, 1000) {
            override fun onFinish() {
                tv_time.setText("0")
                if (TextUtils.isEmpty(SpUtils.getSp(mContext, "uid")) || SpUtils.getSp(mContext, "uid") == null) {
                    startActivity(LoginActivity::class.java)
                    finish()
                }else{
                    startActivity(MainActivity::class.java)
                    finish()
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                tv_time.setText((millisUntilFinished / 1000).toString())
            }

        }.start()
        ll_time.setOnClickListener {
            timer.cancel()
            if (!TextUtils.isEmpty(SpUtils.getSp(mContext, "uid")) && SpUtils.getSp(mContext, "uid") != null) {
                startActivity(MainActivity::class.java)
                finish()
            }else{
                startActivity(LoginActivity::class.java)
                finish()
            }
        }


    }
}