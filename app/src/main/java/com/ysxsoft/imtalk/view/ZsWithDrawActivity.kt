package com.ysxsoft.imtalk.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.OrderBean
import com.ysxsoft.imtalk.bean.ZSAliBean
import com.ysxsoft.imtalk.bean.ZSBankBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.MoneyTextWatcher
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import com.ysxsoft.imtalk.widget.dialog.WithdrawDialog
import kotlinx.android.synthetic.main.title_layout.*
import kotlinx.android.synthetic.main.zs_withdraw_layout.*
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers

/**
 *Create By 胡
 *on 2019/7/16 0016
 */
class ZsWithDrawActivity : BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.zs_withdraw_layout
    }

    var type = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBackVisibily()
        tv_title_right.visibility=View.VISIBLE
        tv_title_right.setText("提现记录")
        setTitle("钻石提现")
        initView()
        ZsALiPay()
    }

    private fun initView() {
        tv_pay_type.setOnClickListener {
            val dialog = WithdrawDialog(mContext)
            dialog.setPayTypeClickListener(object : WithdrawDialog.PayTypeClickListener {
                @SuppressLint("ResourceAsColor")
                override fun payType(payType: Int) {
                    when (payType) {
                        1 -> {
                            type = 1
                            img_type.setImageResource(R.mipmap.img_alipay)
                            tv_pay_type.setText("支付宝账户")
                            tv_add_bank_card.visibility = View.GONE
                            tv_withdraw_acount.setHint("")
                            ZsALiPay()
                        }
                        2 -> {
                            type = 2
//                            BankPay()
                            img_type.setImageResource(R.mipmap.img_bank_card)
                            tv_pay_type.setText("银行卡账号")
                            tv_withdraw_acount.setHint("您还未添加银行卡，")
                            tv_add_bank_card.visibility = View.VISIBLE
                            tv_withdraw_acount.setHintTextColor(R.color.hint_text_color)
                        }
                    }
                }
            })
            dialog.show()
        }
        tv_add_bank_card.setOnClickListener {
            val intent = Intent(mContext, BankCardListActivity::class.java)
            intent.putExtra("is_finish","1")
            startActivityForResult(intent, 1426)
        }
        tv_all.setOnClickListener {
            ed_money.setText(tv_money.text.toString())
        }

        ed_money.addTextChangedListener(MoneyTextWatcher(ed_money))
        ed_money.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!TextUtils.isEmpty(s) && !"0".equals(s)) {
                    tv_ok.isEnabled = true
                } else {
                    tv_ok.isEnabled = false
                }
            }
        })
        tv_ok.setOnClickListener {
            if (TextUtils.isEmpty(ed_money.text.toString().trim())) {
                showToastMessage("提现金额不能为空")
                return@setOnClickListener
            }

            when (type) {
                1 -> {
                    AliData()
                }
                2 -> {
                    BankData()
                }
            }
        }
        tv_title_right.setOnClickListener {
            startActivity(WithDrawRecodeActivity::class.java)
        }
    }

    private fun BankPay() {
        NetWork.getService(ImpService::class.java)
                .zsTxAli(SpUtils.getSp(mContext, "uid"), "2")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Action1<ZSAliBean> {
                    @SuppressLint("ResourceAsColor")
                    override fun call(t: ZSAliBean?) {
                        if (t!!.code == 0) {

                        }else{

                        }
                    }
                })
    }

    private fun BankData() {
        NetWork.getService(ImpService::class.java)
                .TxBank(SpUtils.getSp(mContext,"uid"),ed_money.text.toString().trim(),"2",bank_id!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Action1<OrderBean>{
                    override fun call(t: OrderBean?) {
                        if (t!!.code==0){
                            startActivity(PaymentCompletionActivity::class.java)
                        }else{
                            showToastMessage(t.msg)
                        }
                    }

                })
    }

    private fun AliData() {
        NetWork.getService(ImpService::class.java)
                .TxAli(SpUtils.getSp(mContext, "uid"), ed_money.text.toString().trim(), "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Action1<OrderBean> {
                    override fun call(t: OrderBean?) {
                        if (t!!.code == 0) {
                            startActivity(PaymentCompletionActivity::class.java)
                        }else{
                            showToastMessage(t.msg)
                        }
                    }
                })

    }

    //银行卡
    private fun BankALiPay(bank_id: String) {
        NetWork.getService(ImpService::class.java)
                .zsTxBank(SpUtils.getSp(mContext, "uid"), "2", bank_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Action1<ZSBankBean> {
                    override fun call(t: ZSBankBean?) {
                        if (t!!.code == 0) {
                            tv_withdraw_acount.setText(t.data.bank_name)
                            tv_add_bank_card.visibility = View.GONE
                            tv_money.setText(t.data.money)
                        }
                    }
                })

    }

    //支付宝
    private fun ZsALiPay() {
        NetWork.getService(ImpService::class.java)
                .zsTxAli(SpUtils.getSp(mContext, "uid"), "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Action1<ZSAliBean> {
                    override fun call(t: ZSAliBean?) {
                        if (t!!.code == 0) {
                            tv_money.setText(t.data.money)
                            tv_withdraw_acount.setText(t.data.account_number)
                        }
                    }
                })

    }
    var bank_id:String?=null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1426 && resultCode == 1428) {
             bank_id = data!!.getStringExtra("bank_id")
            BankALiPay(bank_id!!)
        }
    }
}