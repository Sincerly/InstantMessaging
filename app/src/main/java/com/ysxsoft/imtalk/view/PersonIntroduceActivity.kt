package com.ysxsoft.imtalk.view

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.CommonBean
import com.ysxsoft.imtalk.bean.UserInfoBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import kotlinx.android.synthetic.main.person_introduce_layout.*
import kotlinx.android.synthetic.main.title_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers
import kotlin.math.log

/**
 *Create By 胡
 *on 2019/7/16 0016
 */
class PersonIntroduceActivity : BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.person_introduce_layout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tv_title_right.visibility = View.VISIBLE
        tv_title_right.setText("完成")
        setTitle("修改个人介绍")
        setBackVisibily()
        initView()
        requestData()
    }

    private fun requestData() {
        NetWork.getService(ImpService::class.java)
                .GetUserInfo(SpUtils.getSp(mContext, "uid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<UserInfoBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: UserInfoBean?) {
                        if (t!!.code == 0) {
                            ed_introduce.setText(t.data.user_desc)
                        }
                    }

                    override fun onCompleted() {
                    }
                })


    }


    private fun initView() {
        tv_title_right.setOnClickListener {
            if (TextUtils.isEmpty(ed_introduce.text.toString().trim())) {
                showToastMessage("个人介绍不能为空")
                return@setOnClickListener
            }
        }
        ed_introduce.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tv_num.setText((60 - s!!.length).toString())
            }
        })
        tv_title_right.setOnClickListener {
            saveData()
        }
    }

    private fun saveData() {
        NetWork.getService(ImpService::class.java)
                .UserDesc(SpUtils.getSp(mContext, "uid"), ed_introduce.text.toString().trim())
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