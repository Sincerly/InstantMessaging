package com.ysxsoft.imtalk.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.CommonBean
import com.ysxsoft.imtalk.bean.DiamondBean
import com.ysxsoft.imtalk.chatroom.net.retrofit.RetrofitUtil
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import kotlinx.android.synthetic.main.exchange_jb_layout.*
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers
import java.math.BigDecimal

/**
 *Create By 胡
 *on 2019/7/16 0016
 */
class ExchangeJBActivity : BaseActivity() {

    companion object {
        fun starExchangeJBActivity(mContext: Context, money: String) {
            val intent = Intent(mContext, ExchangeJBActivity::class.java)
            intent.putExtra("money", money)
            mContext.startActivity(intent)
        }
    }

    override fun getLayout(): Int {
        return R.layout.exchange_jb_layout
    }

    var money: String? = null
    var thbl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //exchange_jb_item_layout
        money = intent.getStringExtra("money")
        setBackVisibily()
        setTitle("兑换金币")
        initView()
        requestData()
    }

    private fun requestData() {
        val map = HashMap<String, String>()
        map.put("uid", SpUtils.getSp(mContext, "uid"))
        map.put("type", "2")
        val body = RetrofitUtil.createJsonRequest(map)
        NetWork.getService(ImpService::class.java)
                .diamond_dhym(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Action1<DiamondBean> {
                    override fun call(t: DiamondBean?) {
                        if (t!!.code == 0) {
                            thbl = t.data.thbl
                            tv_current_jb.setText(t.data.now_money)
                            tv_zs.setText(t.data.diamonds)
                            tv_ratio.setText("," + t.data.dh_desc)
                        }
                    }
                })
    }

    private fun initView() {
//        tv_current_jb.setText(money)
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
        ed_num.setHint("请输入钻石数量")
        ed_num.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!TextUtils.isEmpty(s) && !"0".equals(s)) {
//                    val decimal = BigDecimal(s.toString())
//                    val dec = BigDecimal(thbl!!.split(".")[0])
//                    val divide = decimal.multiply(dec)
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
        map.put("type", "2")
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
                        }
                    }
                })
    }
}