package com.ysxsoft.imtalk.view

import android.os.Bundle
import android.view.View
import com.ysxsoft.imtalk.MainActivity
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.CommonBean
import com.ysxsoft.imtalk.bean.RegisterBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import com.ysxsoft.imtalk.widget.dialog.DatePickerDialog
import kotlinx.android.synthetic.main.improving_data_layout.*
import kotlinx.android.synthetic.main.title_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *Create By 胡
 *on 2019/7/13 0013
 */
class ImprovingDataActivity : BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.improving_data_layout
    }

    var dateStr: String? = null
    var sex = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initStatusBar(topView)
        setTitle("完善资料")
        initView()
    }

    private fun initView() {
        cb_boy.setOnClickListener {
            sex = 1
            cb_boy.isChecked = true
            img_boy_ok.visibility = View.VISIBLE
            img_girl_ok.visibility = View.GONE
            cb_girl.isChecked = false
        }

        cb_girl.setOnClickListener {
            sex = 2
            cb_girl.isChecked = true
            img_girl_ok.visibility = View.VISIBLE
            img_boy_ok.visibility = View.GONE
            cb_boy.isChecked = false
        }

        tv_birth.setOnClickListener {
            val dialog = DatePickerDialog(mContext)
            dialog.setClickDatePicker(object : DatePickerDialog.ClickDatePicker {
                override fun datePicker(date: String) {
                    dateStr = date
                    tv_birth.setText(date)
                }
            })
            dialog.show()
        }
        tv_next.setOnClickListener {
 //            startActivity(MainActivity::class.java)
            saveData()
        }

    }

    private fun saveData() {
        NetWork.getService(ImpService::class.java)
                .ImprovingData(SpUtils.getSp(mContext,"uid"), sex.toString(), ed_nikeName.text.toString().trim(), dateStr!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CommonBean> {
                    override fun onError(e: Throwable?) {

                    }

                    override fun onNext(t: CommonBean?) {
                        if (t!!.code == 0) {
                            startActivity(MainActivity::class.java)
                        }
                    }

                    override fun onCompleted() {
                    }
                })


    }
}