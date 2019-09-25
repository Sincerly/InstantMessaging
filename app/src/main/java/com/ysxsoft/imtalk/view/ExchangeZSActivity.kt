package com.ysxsoft.imtalk.view

import android.content.Context
import android.content.Intent
import android.media.audiofx.BassBoost
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.TextView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.CommonBean
import com.ysxsoft.imtalk.bean.DiamondBean
import com.ysxsoft.imtalk.chatroom.net.retrofit.RetrofitUtil
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import com.ysxsoft.imtalk.widget.dialog.BindingPhoneDialog
import kotlinx.android.synthetic.main.exchange_jb_layout.*
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers
import java.math.BigDecimal

/**
 *Create By 胡
 *on 2019/7/16 0016
 */
class ExchangeZSActivity : BaseActivity() {

    companion object {
        fun starExchangeZSActivity(mContext: Context, money: String) {
            val intent = Intent(mContext, ExchangeZSActivity::class.java)
            intent.putExtra("money", money)
            mContext.startActivity(intent)
        }
    }

    override fun getLayout(): Int {
        return R.layout.exchange_zs_layout
    }

    var money: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        money = intent.getStringExtra("money")
        setTitle("兑换钻石")
        setBackVisibily()
        initView()
        requestData()
    }

    private fun requestData() {
        val map = HashMap<String, String>()
        map.put("uid", SpUtils.getSp(mContext, "uid"))
        map.put("type", "1")
        val body = RetrofitUtil.createJsonRequest(map)
        NetWork.getService(ImpService::class.java)
                .diamond_dhym(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Action1<DiamondBean> {
                    override fun call(t: DiamondBean?) {
                        if (t!!.code == 0) {
                            tv_current_jb.setText(t.data.now_money)
                            tv_zs.setText(t.data.diamonds)
                            tv_ratio.setText("," + t.data.dh_desc)
                        }
                    }
                })

    }

    private fun initView() {
        tv_all_exchange.setOnClickListener {
            if (TextUtils.isEmpty(tv_current_jb.text.toString().trim())) {
                showToastMessage("当前没有金币")
                return@setOnClickListener
            }
            if (TextUtils.equals("0", tv_current_jb.text.toString().trim())) {
                showToastMessage("当前金币为零")
                return@setOnClickListener
            }
            ed_num.setText(tv_current_jb.text.toString().trim())
        }
        ed_num.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!TextUtils.isEmpty(s) && !"0".equals(s)) {
//                    val decimal = BigDecimal(s.toString())
//                    val dec = BigDecimal("10")
//                    val divide = decimal.divide(dec)
//                    tv_zs.setText(divide.toString())
                    tv_ok.isEnabled = true
                } else {
//                    tv_zs.setText("0")
                    tv_ok.isEnabled = false
                }
            }
        })
        tv_ok.setOnClickListener {
            exchangData()
        }
    }

    private fun exchangData() {

        val map = HashMap<String, String>()
        map.put("uid", SpUtils.getSp(mContext, "uid"))
        map.put("gold_nums", ed_num.text.toString().trim())
        map.put("type", "1")
        val body = RetrofitUtil.createJsonRequest(map)
        NetWork.getService(ImpService::class.java)
                .sub_diamond(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Action1<CommonBean> {
                    override fun call(t: CommonBean?) {
                        showToastMessage(t!!.msg)
                        if (t.code == 0) {
                            finish()
                        } else {
                            val bindingPhoneDialog = BindingPhoneDialog(mContext)
                            val tv_tip = bindingPhoneDialog.findViewById<TextView>(R.id.tv_tip)
                            val tv_go = bindingPhoneDialog.findViewById<TextView>(R.id.tv_go)
                            when (t.code) {
                                2 -> {//绑定手机号
                                    tv_tip.setText("请先绑定手机号")
                                    tv_go.setOnClickListener {
                                       startActivity(BindPhoneActivity::class.java)
                                    }
                                    bindingPhoneDialog.show()
                                }
                                3 -> {//去实名认证
                                    tv_tip.setText("请先实名认证")
                                    tv_go.setOnClickListener {
                                        startActivity(SmrzActivity::class.java)
                                    }
                                    bindingPhoneDialog.show()
                                }
                            }
                        }
                    }
                })
    }
}