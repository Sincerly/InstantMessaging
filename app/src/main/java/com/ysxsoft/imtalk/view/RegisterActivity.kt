package com.ysxsoft.imtalk.view

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.RegisterBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.give_dialog_layout.view.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class RegisterActivity : BaseActivity() {

    override fun getLayout(): Int {
        return R.layout.activity_register
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLightStatusBar(false)
        initView()
    }

    private fun initView() {
        img_logo.setImageBitmap(AppUtil.getLogoBitmap(mContext))

        tv_get_code.setOnClickListener {
            if (TextUtils.isEmpty(et_phone.text.toString().trim())) {
                showToastMessage("手机号不能为空")
                return@setOnClickListener
            }

            if (!AppUtil.checkPhoneNum(et_phone.text.toString().trim())) {
                showToastMessage("手机号输入不正确")
                return@setOnClickListener
            }
            CountDownTimeHelper(60, tv_get_code)
            SendMsg(et_phone.text.toString().trim())
        }

        //注册
        tv_register.setOnClickListener {
            if (TextUtils.isEmpty(et_phone.text.toString().trim())) {
                showToastMessage("手机号不能为空")
                return@setOnClickListener
            }

//            if (!AppUtil.checkPhoneNum(et_phone.text.toString().trim())) {
//                showToastMessage("手机号输入不正确")
//                return@setOnClickListener
//            }
            if (TextUtils.isEmpty(et_code.text.toString().trim())) {
                showToastMessage("验证码不能为空")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(et_psw1.text.toString().trim())) {
                showToastMessage("密码不能为空")
                return@setOnClickListener
            }
            if (et_psw1.text.toString().trim().length < 6) {
                showToastMessage("密码不能少于六位")
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(et_psw2.text.toString().trim())) {
                showToastMessage("再次输入密码不能为空")
                return@setOnClickListener
            }

            if (!TextUtils.equals(et_psw1.text.toString().trim(), et_psw2.text.toString().trim())) {
                showToastMessage("再次输入密码不一致")
                return@setOnClickListener
            }

            saveData()

        }
        //已有账号？立即登录
        ll_login.setOnClickListener {
            finish()
        }
    }

    private fun saveData() {
        NetWork.getService(ImpService::class.java)
                .Register(et_phone.text.toString().trim(), et_psw1.text.toString().trim(), et_psw2.text.toString().trim(), et_code.text.toString().trim(), "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<RegisterBean> {
                    override fun onError(e: Throwable?) {

                    }

                    override fun onNext(t: RegisterBean?) {
                        showToastMessage(t!!.msg)
                        if (t!!.code == 0) {
                            loginToIM(t.data.token)
                            SpUtils.saveSp(mContext, "chat_token", t.data.token)
                            SpUtils.saveSp(mContext, "uid", t.data.uid)
                            com.ysxsoft.imtalk.chatroom.utils.SpUtils.saveSp(mContext, "uid", t.data.uid.toString())
                            startActivity(ImprovingDataActivity::class.java)
                            finish()
                        }
                    }

                    override fun onCompleted() {
                    }
                })

    }

}
