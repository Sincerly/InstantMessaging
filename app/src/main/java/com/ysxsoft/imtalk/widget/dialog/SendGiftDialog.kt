package com.ysxsoft.imtalk.widget.dialog

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.Gravity
import android.view.ViewGroup
import com.gcssloop.widget.PagerGridLayoutManager
import com.gcssloop.widget.PagerGridSnapHelper
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.adapter.GridBageAdpater
import com.ysxsoft.imtalk.adapter.GridGiftAdpater
import com.ysxsoft.imtalk.adapter.MicPostionAdapter
import com.ysxsoft.imtalk.bean.*
import com.ysxsoft.imtalk.chatroom.model.DetailRoomInfo
import com.ysxsoft.imtalk.chatroom.net.retrofit.RetrofitUtil
import com.ysxsoft.imtalk.chatroom.task.AuthManager
import com.ysxsoft.imtalk.chatroom.task.ResultCallback
import com.ysxsoft.imtalk.chatroom.task.RoomManager
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.*
import com.ysxsoft.imtalk.view.JbWithDrawActivity
import com.ysxsoft.imtalk.widget.ABSDialog
import io.rong.callkit.util.SPUtils
import kotlinx.android.synthetic.main.dialog_send_gift.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers

/**
 *Create By 胡
 *on 2019/8/2 0002
 */
class SendGiftDialog : ABSDialog {

    var type = 3
    var gift_id: String? = null
    var gift_num: String? = null
    var targetId: String? = null
    var gifurl: String? = null
    var num = 0
    var giftbage = 1
    private var sectionNum = StringBuffer()
    override fun initView() {
        tv1.isSelected = true
        tv1.setOnClickListener {
            tv1.isSelected = true
            tv2.isSelected = false
            giftbage = 1
            GiftData()
        }
        tv2.setOnClickListener {
            tv1.isSelected = false
            tv2.isSelected = true
            giftbage = 2
            bageData()
        }

        tv_cz.setOnClickListener {
            JbWithDrawActivity.starJbWithDrawActivity(this@SendGiftDialog.context)
            dismiss()
        }

        tv_zs.setOnClickListener {
            if (TextUtils.isEmpty(targetId) || "0".equals(targetId)) {
                ToastUtils.showToast(this@SendGiftDialog.context, "赠送人不能为空")
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(giftid) && TextUtils.isEmpty(bageId)) {
                ToastUtils.showToast(this@SendGiftDialog.context, "所选礼物或座驾不能为空")
                return@setOnClickListener
            }

            val zsPopuwindows = ZSPopuwindows(this@SendGiftDialog.context, R.layout.zs_layout, tv_zs)
            zsPopuwindows.setOnGiftListener(object : ZSPopuwindows.OnGiftListener {
                override fun giftClick(times: String, id: String) {
                    dismiss()
                    gift_num = times
                    gift_id = id
                    tv_zs.setText("赠送 x " + times)
                    sendGift()
                }
            })
        }
    }

    private fun sendGift() {
        val map = HashMap<String, String>()
        map.put("type", giftbage.toString())
        map.put("gift_id", giftid.toString())
        map.put("gift_num", gift_num.toString())
        map.put("room_id", "")
        map.put("income_gift_uid", targetId!!)
        map.put("uid", SpUtils.getSp(this@SendGiftDialog.context, "uid"))
        val body = RetrofitUtil.createJsonRequest(map)
        NetWork.getService(ImpService::class.java)
                .send_gift(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CommonBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: CommonBean?) {
                        if (t!!.code == 0) {
                            if (listener != null) {
                                listener!!.onSendSuccess(SpUtils.getSp(this@SendGiftDialog.context, "uid"),targetId,giftid.toString(),gift_num.toString())
                            }
                        }else{
                            ToastUtils.showToast(this@SendGiftDialog.context,t.msg)
                        }
                    }

                    override fun onCompleted() {
                    }
                })


    }

    var bageId: String? = null
    /**
     * 获取背包数据
     */
    private fun bageData() {
        NetWork.getService(ImpService::class.java)
                .BageList(SpUtils.getSp(this@SendGiftDialog.context, "uid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Action1<BageListBean> {
                    override fun call(t: BageListBean?) {
                        if ("0".equals(t!!.code)) {
                            val data = t.data
                            val bageAdpater = GridBageAdpater(this@SendGiftDialog.context)
                            grid_view.adapter = bageAdpater
                            bageAdpater.addAll(data)

                            bageAdpater.setOnClickListener(object : GridBageAdpater.OnClickListener {
                                override fun onBage(position: Int) {
                                    giftid = bageAdpater.dataList.get(position).id.toString()
                                    gifurl = bageAdpater.dataList.get(position).aw_gif
                                    pic = bageAdpater.dataList.get(position).aw_images
                                    bageAdpater.setSelect(position)
                                }
                            })
                        }
                    }
                })
    }

    //金币数据
    private fun userInfo() {
        NetWork.getService(ImpService::class.java)
                .GetUserInfo(SpUtils.getSp(this@SendGiftDialog.context, "uid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<UserInfoBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: UserInfoBean?) {
                        if (t!!.code == 0) {
                            tv_gold.setText(t.data.money+"金币")
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }

    var giftid: String? = null
    var pic: String? = null

    //礼物数据
    private fun GiftData() {
        NetWork.getService(ImpService::class.java)
                .DressMall(type.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Action1<DressMallBean> {
                    override fun call(t: DressMallBean?) {
                        if (t!!.code == 0) {
                            val data = t.data
                            val giftAdpater = GridGiftAdpater(this@SendGiftDialog.context)
                            grid_view.adapter = giftAdpater
                            giftAdpater.addAll(data)
                            giftAdpater.setOnClickListener(object : GridGiftAdpater.OnClickListener {
                                override fun onGift(position: Int) {
                                    giftid = giftAdpater.dataList.get(position).id.toString()
                                    pic = giftAdpater.dataList.get(position).pic
                                    gifurl = giftAdpater.dataList.get(position).gif_pic
                                    giftAdpater.setSelect(position)
                                }
                            })
                        }
                    }
                })
    }

    override fun getLayoutResId(): Int {
        return R.layout.dialog_send_gift
    }

    constructor(mContext: Context, targetId: String) : super(mContext) {
        this.targetId = targetId
        val window = window
        window.setGravity(Gravity.BOTTOM)
        val params = window.attributes
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        window.attributes = params

        // 1.水平分页布局管理器
        val layoutManager = PagerGridLayoutManager(2, 4, PagerGridLayoutManager.HORIZONTAL)
        grid_view.setLayoutManager(layoutManager)

        // 2.设置滚动辅助工具
        val pageSnapHelper = PagerGridSnapHelper()
        pageSnapHelper.attachToRecyclerView(grid_view)

        GiftData()
        userInfo()
    }

    interface OnSendGiftListener {
        fun onSendSuccess(from: String,to: String?,giftId: String,giftNum: String)
    }

    private var listener: OnSendGiftListener? = null

    fun setOnSendGiftListener(onSendGiftListener: OnSendGiftListener) {
        this.listener = onSendGiftListener
    }
}