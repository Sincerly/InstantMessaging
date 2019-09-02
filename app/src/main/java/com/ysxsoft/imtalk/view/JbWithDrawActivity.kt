package com.ysxsoft.imtalk.view

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.Log
import com.alipay.sdk.app.PayTask
import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter
import com.luck.picture.lib.dialog.CustomDialog
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.R.style.dialog
import com.ysxsoft.imtalk.adapter.JbWithDrawAdapter
import com.ysxsoft.imtalk.bean.*
import com.ysxsoft.imtalk.chatroom.net.retrofit.RetrofitUtil
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.*
import com.ysxsoft.imtalk.utils.alipay.PayResult
import kotlinx.android.synthetic.main.jb_withdraw_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers
import java.lang.ref.WeakReference

/**
 *Create By 胡
 *on 2019/7/16 0016
 */
class JbWithDrawActivity : BaseActivity() {

    companion object {

        fun starJbWithDrawActivity(mContext: Context) {
            val intent = Intent(mContext, JbWithDrawActivity::class.java)
            mContext.startActivity(intent)
        }
    }

    override fun getLayout(): Int {
        return R.layout.jb_withdraw_layout
    }

    var payType = 1
    var mDataAdapter: JbWithDrawAdapter? = null
    var mLuRecyclerViewAdapter: LuRecyclerViewAdapter? = null
    var gold_id: String? = null
    var day_time: String? = null
    var sign_id: String? = null
    private var api: IWXAPI? = null
    private var dialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //jb_withdraw_item_layout
        api = WXAPIFactory.createWXAPI(this, "wx9f167bc9812eb1dc")
        sign_id = intent.getStringExtra("sign_id")
        day_time = intent.getStringExtra("day_time")
        setTitle("金币充值")
        setBackVisibily()
        initView()
        MyData()
        getData()
    }

    private fun MyData() {
        NetWork.getService(ImpService::class.java)
                .GetUserInfo(SpUtils.getSp(mContext, "uid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<UserInfoBean> {
                    override fun onError(e: Throwable?) {

                    }

                    override fun onNext(t: UserInfoBean?) {
                        if (t!!.code == 0) {
                            ImageLoadUtil.GlideHeadImageLoad(mContext, t.data.icon, img_head)
                            tv_acount.setText("账号：" + t.data.nickname)
                            tv_money.setText(t.data.money)
                        }
                    }

                    override fun onCompleted() {
                    }

                })
    }

    private fun initView() {
        tv_alipay.isSelected = true
        tv_alipay.setOnClickListener {
            payType = 1
            tv_alipay.isSelected = true
            tv_wechat_pay.isSelected = false

        }
        tv_wechat_pay.setOnClickListener {
            payType = 2
            tv_alipay.isSelected = false
            tv_wechat_pay.isSelected = true
        }
        tv_ok.setOnClickListener {

            if (TextUtils.isEmpty(gold_id)) {
                showToastMessage("请选择充值金额")
                return@setOnClickListener
            }
            submintData()
        }
        mDataAdapter = JbWithDrawAdapter(mContext)
        mRecyclerView.layoutManager = GridLayoutManager(mContext, 3)
        mLuRecyclerViewAdapter = LuRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.adapter = mLuRecyclerViewAdapter
        val divider = LuDividerDecoration.Builder(mContext, mLuRecyclerViewAdapter)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.gray)
                .build()
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.addItemDecoration(divider)
        mLuRecyclerViewAdapter!!.setOnItemClickListener { view, position ->
            gold_id = mDataAdapter!!.dataList.get(position).id.toString()
            mDataAdapter!!.setSelect(position)
            notifyDataSetChanged()
        }
    }

    private fun submintData() {
        val map = HashMap<String, String>()
        map.put("uid", SpUtils.getSp(mContext, "uid"))
        map.put("gold_id", gold_id!!)
        if (TextUtils.isEmpty(sign_id)){
            map.put("type", "1")//1 其他充值；2 补签充值
        }else{
            map.put("type", "2")//1 其他充值；2 补签充值
            map.put("sign_id", sign_id!!)
            map.put("date_time",day_time!!)
        }
        val body = RetrofitUtil.createJsonRequest(map)
        NetWork.getService(ImpService::class.java)
                .recharge(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Action1<OrderBean> {
                    override fun call(t: OrderBean?) {
                        if (t!!.code == 0) {
                            when (payType) {
                                1 -> {
                                    AliPay(t.data)
                                }
                                2 -> {
                                    WxPay(t.data)
                                }
                            }
                        }
                    }
                })
    }

    private fun AliPay(data: String?) {
        NetWork.getService(ImpService::class.java)
                .AliPay(data!!, SpUtils.getSp(mContext, "uid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<AliPayBean> {
                    override fun onError(e: Throwable?) {
                        Log.d("tag", "ALiPay==" + e!!.message.toString())
                    }

                    override fun onNext(t: AliPayBean?) {
                        if (t!!.code == 0) {
                            payAli(t.data)
                        }
                    }

                    override fun onCompleted() {
                    }
                })

    }

    private val SDK_PAY_FLAG = 1
    @SuppressLint("HandlerLeak")
    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                SDK_PAY_FLAG -> {
                    val payResult = PayResult(msg.obj as Map<String, String>)
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    val resultInfo = payResult.getResult()// 同步返回需要验证的信息
                    val resultStatus = payResult.getResultStatus()
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        showToastMessage("支付成功")
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        showToastMessage("支付失败")
                    }
                }
            }
        }
    }

    private fun payAli(orderInfo: String?) {
        val payRunnable = Runnable {
            val alipay = PayTask(this)
            val result = alipay.payV2(orderInfo, true)
            val msg = Message()
            msg.what = SDK_PAY_FLAG
            msg.obj = result
            mHandler.sendMessage(msg)
        }
        // 必须异步调用
        val payThread = Thread(payRunnable)
        payThread.start()

    }

    private fun WxPay(data: String?) {
        dialog = ProgressDialog(this)
        dialog!!.show()
        NetWork.getService(ImpService::class.java)
                .WxPay(data!!, SpUtils.getSp(mContext, "uid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<WxPayBean> {
                    override fun onError(e: Throwable?) {
                        Log.d("tag", "wxPay==" + e!!.message.toString())
                    }

                    override fun onNext(t: WxPayBean?) {
                        if (t!!.code == 0) {
                            val req = PayReq()
                            req.appId = t.data.appid
                            req.prepayId = t.data.prepayid
                            req.partnerId = t.data.partnerid
                            req.nonceStr = t.data.noncestr
                            req.packageValue = t.data.packageX
                            req.sign = t.data.sign
                            req.timeStamp = t.data.timestamp.toString()
                            req.extData = "app data"
                            api!!.sendReq(req)
                            dialog!!.dismiss()
                        }
                    }

                    override fun onCompleted() {
                    }
                })

    }

    private fun getData() {
        NetWork.getService(ImpService::class.java)
                .GoldList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<GoldListBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: GoldListBean?) {
                        if (t!!.code == 0) {
                            mDataAdapter!!.addAll(t.data)
                            notifyDataSetChanged()
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }

    private fun notifyDataSetChanged() {
        mLuRecyclerViewAdapter!!.notifyDataSetChanged()
    }
}