package com.ysxsoft.imtalk.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.R.id.*
import com.ysxsoft.imtalk.bean.AddBankInfoBean
import com.ysxsoft.imtalk.bean.BankListBean
import com.ysxsoft.imtalk.bean.CommonBean
import com.ysxsoft.imtalk.bean.GetBankInfoBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.BaseApplication.Companion.mContext
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import kotlinx.android.synthetic.main.activity_bank_card_edit.*
import kotlinx.android.synthetic.main.title_layout.*
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers

class BankCardEditActivity : BaseActivity() {

    companion object {
        fun starBankCardEditActivity(mContext: Context, user_bank_id: String, flag: String) {
            val intent = Intent(mContext, BankCardEditActivity::class.java)
            intent.putExtra("flag", flag)
            intent.putExtra("user_bank_id", user_bank_id)
            mContext.startActivity(intent)
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_bank_card_edit
    }

    var datas: MutableList<BankListBean.DataBean>? = null
    var bankId: String? = null
    var flag: String? = null
    var user_bank_id: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        flag = intent.getStringExtra("flag")
        user_bank_id = intent.getStringExtra("user_bank_id")
        setLightStatusBar(true)
        initStatusBar(topView)
        if ("1".equals(flag)) {
            setTitle("编辑银行卡")
        } else {
            setTitle("添加银行卡")
        }
        setBackVisibily()
        initView()
        requestData()
        if ("1".equals(flag)) {
            getBankData()
        }
    }

    private fun getBankData() {
        NetWork.getService(ImpService::class.java)
                .getBankInfo(SpUtils.getSp(mContext, "uid"), user_bank_id!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Action1<GetBankInfoBean> {
                    override fun call(t: GetBankInfoBean?) {
                        if (t!!.code == 0) {
                            et_name.setText(t.data.real_name)
                            tv_bank_name.setText(t.data.bank_name)
                            bankId = t.data.bank_id
                            et_psw1.setText(t.data.bank_address)
                            et_psw2.setText(t.data.bank_number)
                        }
                    }
                })
    }

    private fun requestData() {
        NetWork.getService(ImpService::class.java)
                .BankList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Action1<BankListBean> {
                    override fun call(t: BankListBean?) {
                        if (t!!.code == 0) {
                            datas = t.data
                        }
                    }
                })
    }

    private fun initView() {
        tv_bank_name.setOnClickListener {
            val pvOptions = OptionsPickerBuilder(mContext, OnOptionsSelectListener { options1, options2, options3, v ->
                //返回的分别是三个级别的选中位置
                bankId = datas!!.get(options1).id.toString()
                val tx = datas!!.get(options1).bank_name
                tv_bank_name!!.text = tx
            })
                    .setTitleText("")
                    .setDividerColor(Color.BLACK)
                    .setTextColorCenter(Color.BLACK)
                    .setContentTextSize(20)
                    .build<BankListBean.DataBean>();
            pvOptions.setPicker(datas)//一级选择器
            pvOptions!!.show()
        }
        //提交
        btn_sure.setOnClickListener {
            if ("1".equals(flag)) {
                edSave()
            } else
                saveData()
        }
    }

    private fun edSave() {
        NetWork.getService(ImpService::class.java)
                .editBankInfo(SpUtils.getSp(mContext,"uid"),user_bank_id!!,bankId!!,et_name.text.toString().trim(),et_psw2.text.toString().trim(),et_psw1.text.toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Action1<AddBankInfoBean>{
                    override fun call(t: AddBankInfoBean?) {
                        showToastMessage(t!!.msg)
                        if (t.code == 0) {
                            finish()
                        }
                    }
                })

    }

    private fun saveData() {
        NetWork.getService(ImpService::class.java)
                .addBankInfo(SpUtils.getSp(mContext, "uid"), bankId!!, et_name.text.toString().trim(), et_psw2.text.toString().trim(), et_psw1.text.toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Action1<AddBankInfoBean> {
                    override fun call(t: AddBankInfoBean?) {
                        showToastMessage(t!!.msg)
                        if (t.code == 0) {
                            finish()
                        }
                    }
                })

    }
}
