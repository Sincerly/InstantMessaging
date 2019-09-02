package com.ysxsoft.imtalk.view

import android.os.Bundle
import android.text.TextUtils
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.CommonBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import kotlinx.android.synthetic.main.activity_wyfk.*
import kotlinx.android.synthetic.main.title_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class WyfkActivity : BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.activity_wyfk;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLightStatusBar(true)
        initStatusBar(topView)
        initView()
    }

    private fun initView() {
        setBackVisibily()
        setTitle("我要反馈")


        //提交
        btn_sure.setOnClickListener {
            if (TextUtils.isEmpty(et.text.toString().trim())) {
                showToastMessage("反馈内容不能为空")
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(ed_QQ.text.toString().trim())) {
                showToastMessage("微信/QQ不能为空")
                return@setOnClickListener
            }
            saveData()
        }
    }

    private fun saveData() {
        NetWork.getService(ImpService::class.java)
                .couple_back(SpUtils.getSp(mContext,"uid"),et.text.toString().trim(),ed_QQ.text.toString().trim())
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
