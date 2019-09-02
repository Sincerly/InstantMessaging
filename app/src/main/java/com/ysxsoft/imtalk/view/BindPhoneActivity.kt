package com.ysxsoft.imtalk.view

import android.os.Bundle
import android.text.TextUtils
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.CommonBean
import com.ysxsoft.imtalk.chatroom.net.retrofit.RetrofitUtil
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.*
import kotlinx.android.synthetic.main.activity_bind_phone.*
import kotlinx.android.synthetic.main.title_layout.*
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers

class BindPhoneActivity : BaseActivity() {

    override fun getLayout(): Int {
        return R.layout.activity_bind_phone;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLightStatusBar(true)
        initStatusBar(topView)
        initView()
    }

    private fun initView() {
        setBackVisibily()
        setTitle("绑定手机")
        tv_get_code.setOnClickListener {
            CountDownTimeHelper(60,tv_get_code)
            SendMsg(et_phone.text.toString().trim())
        }
        btn_sure.setOnClickListener {
            if (TextUtils.isEmpty(et_phone.text.toString().trim())){
                showToastMessage("手机号不能为空")
                return@setOnClickListener
            }

            if (!AppUtil.checkPhoneNum(et_phone.text.toString().trim())){
                showToastMessage("手机号输入不正确")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(et_code.text.toString().trim())){
                showToastMessage("验证码不能为空")
                return@setOnClickListener
            }
            saveData()
        }
    }

    private fun saveData() {
        val map = HashMap<String, String>()
        map.put("uid",SpUtils.getSp(mContext,"uid"))
        map.put("mobile",et_phone.text.toString().trim())
        map.put("code",et_code.text.toString().trim())
        val body = RetrofitUtil.createJsonRequest(map)
        NetWork.getService(ImpService::class.java)
                .bind_mobile(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Action1<CommonBean>{
                    override fun call(t: CommonBean?) {
                        showToastMessage(t!!.msg)
                        if (t.code==0){
                            finish()
                        }
                    }
                })


    }

}
