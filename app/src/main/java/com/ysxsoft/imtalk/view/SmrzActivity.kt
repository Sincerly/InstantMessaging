package com.ysxsoft.imtalk.view

import android.os.Bundle
import android.text.TextUtils
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.R.id.et_id
import com.ysxsoft.imtalk.R.id.et_name
import com.ysxsoft.imtalk.bean.CommonBean
import com.ysxsoft.imtalk.bean.GetRealInfoBean
import com.ysxsoft.imtalk.chatroom.net.retrofit.RetrofitUtil
import com.ysxsoft.imtalk.chatroom.task.AuthManager
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.BaseApplication.Companion.mContext
import com.ysxsoft.imtalk.utils.CheckCreateUserAction
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import kotlinx.android.synthetic.main.activity_smrz.*
import kotlinx.android.synthetic.main.room_tag_layout.*
import kotlinx.android.synthetic.main.title_layout2.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers

class SmrzActivity : BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.activity_smrz;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLightStatusBar(false)
        initStatusBar(topView)
        initView()
        getData()
    }

    private fun getData() {
        val map = HashMap<String, String>()
        map.put("uid",AuthManager.getInstance().currentUserId)
        val body = RetrofitUtil.createJsonRequest(map)
        NetWork.getService(ImpService::class.java)
                .get_real_info(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Observer<GetRealInfoBean>{
                    override fun onError(e: Throwable?) {

                    }

                    override fun onNext(t: GetRealInfoBean?) {
                        if("0".equals(t!!.code)){
                            et_name.setText(t.data.username)
                            et_id.setText(t.data.number_id)
                        }
                    }

                    override fun onCompleted() {
                    }
                })

    }

    private fun initView() {

        setTitle("实名认证")
        img_back.setOnClickListener { finish() }

        //提交
        btn_sure.setOnClickListener {
            if (TextUtils.isEmpty(et_name.text.toString().trim())) {
                showToastMessage("真实姓名不能为空")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(et_id.text.toString().trim())) {
                showToastMessage("身份证号不能为空")
                return@setOnClickListener
            }
            if (!"该身份证有效！".equals(CheckCreateUserAction.IDCardValidate(et_id.text.toString().trim()))) {
                showToastMessage("身份证无效")
                return@setOnClickListener
            }
            saveData()
        }
    }

    private fun saveData() {
        NetWork.getService(ImpService::class.java)
                .realName(SpUtils.getSp(mContext, "uid"), et_name.text.toString().trim(), et_id.text.toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CommonBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: CommonBean?) {
                        showToastMessage(t!!.msg)
                        if (t.code == 0) {
                            finish()
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }
}
