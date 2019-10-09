package com.ysxsoft.imtalk.view

import android.os.Bundle
import android.text.TextUtils
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.BindInfoBean
import com.ysxsoft.imtalk.bean.CommonBean
import com.ysxsoft.imtalk.chatroom.net.retrofit.RetrofitUtil
import com.ysxsoft.imtalk.chatroom.task.AuthManager
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.*
import kotlinx.android.synthetic.main.activity_bind_zfb.*
import kotlinx.android.synthetic.main.title_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers

class BindZfbActivity : BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.activity_bind_zfb
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLightStatusBar(true)
        initStatusBar(topView)
        initView()
        getBindInfo()
    }
    private fun getBindInfo() {
        val map = HashMap<String, String>()
        map.put("uid", AuthManager.getInstance().currentUserId)
        val body = RetrofitUtil.createJsonRequest(map)
        NetWork.getService(ImpService::class.java)
                .getBindInfo(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BindInfoBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: BindInfoBean?) {
                        if ("0".equals(t!!.code)){
                            et_zfb.setText(t.data.zfb_accounts)
                            et_name.setText(t.data.zfb_name)
                        }
                    }

                    override fun onCompleted() {
                    }
                })

    }

    private fun initView() {
        setBackVisibily()
        setTitle("绑定支付宝账号")
        tv_get_code.setOnClickListener {
            if (TextUtils.isEmpty(et_zfb.text.toString().trim())) {
                showToastMessage("支付宝账号不能为空")
                return@setOnClickListener
            }
            CountDownTimeHelper(60, tv_get_code)
            SendMsg(et_zfb.text.toString().trim())
        }
        btn_sure.setOnClickListener {
            if (TextUtils.isEmpty(et_zfb.text.toString().trim())) {
                showToastMessage("支付宝账号不能为空")
                return@setOnClickListener
            }
//            if (!AppUtil.checkPhoneNum(et_zfb.text.toString().trim())) {
//                showToastMessage("支付宝账号不正确")
//                return@setOnClickListener
//            }

            if (TextUtils.isEmpty(et_name.text.toString().trim())) {
                showToastMessage("真实姓名不能为空")
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(et_code.text.toString().trim())) {
                showToastMessage("验证码不能为空")
                return@setOnClickListener
            }
            saveData()
        }
    }

    private fun saveData() {
        val map = HashMap<String, String>()
        map.put("uid",SpUtils.getSp(mContext,"uid"))
        map.put("zfb_accounts",et_zfb.text.toString().trim())
        map.put("zfb_name",et_name.text.toString().trim())
        map.put("code",et_code.text.toString().trim())
        val body = RetrofitUtil.createJsonRequest(map)
        NetWork.getService(ImpService::class.java)
                .bind_zfb(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Observer<CommonBean>{
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: CommonBean?) {
                        showToastMessage(t!!.msg)
                        if (t.code==0){
                            finish()
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }
}
