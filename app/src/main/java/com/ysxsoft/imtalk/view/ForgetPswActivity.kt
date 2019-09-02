package com.ysxsoft.imtalk.view

import android.os.Bundle
import android.text.TextUtils
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.CommonBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.AppUtil
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.CountDownTimeHelper
import com.ysxsoft.imtalk.utils.NetWork
import kotlinx.android.synthetic.main.activity_forget_psw.*
import kotlinx.android.synthetic.main.login_layout.*
import kotlinx.android.synthetic.main.title_layout.*
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers
import java.lang.annotation.AnnotationFormatError

class ForgetPswActivity : BaseActivity() {

    override fun getLayout(): Int {
        return R.layout.activity_forget_psw
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLightStatusBar(true)
        initStatusBar(topView)
        initView()
    }

    private fun initView() {
        setTitle("忘记密码")
        setBackVisibily()

        tv_get_code.setOnClickListener {
            if (TextUtils.isEmpty(et_phone.text.toString().trim())) {
                showToastMessage("手机号不能为空")
                return@setOnClickListener
            }

            if (!AppUtil.checkPhoneNum(et_phone.text.toString().trim())) {
                showToastMessage("手机号输入不正确")
                return@setOnClickListener
            }
            CountDownTimeHelper(60,tv_get_code)
            SendMsg(et_phone.text.toString().trim())
        }

        //提交
        btn_sure.setOnClickListener {
            if (TextUtils.isEmpty(et_phone.text.toString().trim())) {
                showToastMessage("手机号不能为空")
                return@setOnClickListener
            }
            if (!AppUtil.checkPhoneNum(et_phone.text.toString().trim())) {
                showToastMessage("手机号输入不能为空")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(et_code.text.toString().trim())) {
                showToastMessage("验证码不能为空")
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(et_psw1.text.toString().trim())) {
                showToastMessage("新密码不能为空")
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(et_psw2.text.toString().trim())) {
                showToastMessage("密码不能为空")
                return@setOnClickListener
            }

            if (!TextUtils.equals(et_psw1.text.toString().trim(), et_psw2.text.toString().trim())) {
                showToastMessage("两次输入密码不一致")
                return@setOnClickListener
            }
            saveData()
            finish()
        }
    }

    private fun saveData() {
        NetWork.getService(ImpService::class.java)
                .ForgetPwd(et_phone.text.toString().trim(),
                        et_code.text.toString().trim(),
                        et_psw1.text.toString().trim(),
                        et_psw2.text.toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Action1<CommonBean> {
                    override fun call(t: CommonBean?) {
                        showToastMessage(t!!.msg)
                        if (t.code == 0) {
                            finish()
                        }
                    }
                })
    }
}
