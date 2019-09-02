package com.ysxsoft.imtalk.utils

import android.os.CountDownTimer
import android.widget.TextView

class CountDownTimeHelper {
    var time: Int = 0
    lateinit var view: TextView

    constructor(time: Int, view: TextView) {// 构造函数
        this.time = time
        this.view = view
        val time1 = MyTime((time * 1000).toLong(), 1000)
        time1.start()
    }

    inner class MyTime
    (millisInFuture: Long, countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {

        override fun onTick(millisUntilFinished: Long) {
            view.isEnabled = false
            view.text = "倒计时(" + millisUntilFinished / 1000 + "s)"
        }

        override fun onFinish() {
            view.isEnabled = true
            view.text = "重新发送"
        }
    }


}