package com.ysxsoft.imtalk.widget.dialog

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.gcssloop.widget.PagerGridLayoutManager
import com.gcssloop.widget.PagerGridSnapHelper
import com.google.gson.Gson
import com.google.gson.JsonArray
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
import kotlinx.android.synthetic.main.gift_bag_dialog_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers

/**
 *Create By 胡
 *on 2019/8/2 0002
 */
class SendGiftDialog : ABSDialog {

    var zsPopuwindows:ZSPopuwindows?=null
    var type = 3
    var gift_num: String? = null
    var gifname: String? = null
    var gifurl: String? = null
    var giftbage = 1
    var mwJson:String?=""
    var targetUserId = "";
    var targetUserName = "";

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
            if (TextUtils.isEmpty(giftid) && TextUtils.isEmpty(bageId)) {
                ToastUtils.showToast(this@SendGiftDialog.context, "所选礼物或座驾不能为空")
                return@setOnClickListener
            }
            if (gift_num == null) {
                gift_num = "1"
            }
            if("".equals(toUserName.text.toString())){
                ToastUtils.showToast(this@SendGiftDialog.context, "正在获取对方信息，请稍后重试！")
                return@setOnClickListener
            }
            sendGift()
        }

        inputNumberLayout.setOnClickListener {
            zsPopuwindows = ZSPopuwindows(this@SendGiftDialog.context, R.layout.zs_layout, tv_zs)
            zsPopuwindows!!.setOnGiftListener(object : ZSPopuwindows.OnGiftListener {
                override fun giftClick(times: String, id: String) {
                    if ("-1".equals(times)) {
                        //选择了其他数量
                        onGiftListener!!.needInputed()
                    } else {
                        gift_num = times
                        tv_zs.setText("赠送 x " + times)
                        inputNumber.setText(times)
                    }
                }
            })
        }
    }

    /**
     * 设置礼物数量
     */
    public fun setGiftNum(num: String) {
        gift_num = num
        tv_zs.setText("赠送 x " + num)
        inputNumber.setText(num)
        if(zsPopuwindows!!!=null&&zsPopuwindows!!.isShowing){
            zsPopuwindows!!.dismiss()
        }
    }

    private fun sendGift() {
        val map = HashMap<String, String>()
        map.put("type", giftbage.toString())
        map.put("gift_id", giftid.toString())
        map.put("gift_num", gift_num.toString())
        map.put("room_id", "")
        map.put("income_gift_uid", mwJson!!)
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
                            dismiss()
                            if (onGiftListener != null) {
                                val data = ArrayList<Int>()
                                var fromPosition = -1;//默认不在麦位上

                                onGiftListener!!.onClck(fromPosition, data, pic!!, emptyList(), gifurl!!, gifname!!, gift_num!!,targetUserId,toUserName.text.toString())
                            }
                        } else {
                            ToastUtils.showToast(this@SendGiftDialog.context, t.msg)
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
                                    gifname = bageAdpater.dataList.get(position).aw_name
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
                            tv_user_gold.setText(t.data.money+"金币")
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }


    /**
     * 获取好友资料以及信息
     */
    private fun getUserInfo(uid: String) {
        NetWork.getService(ImpService::class.java)
                .GetUserInfo(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<UserInfoBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: UserInfoBean?) {
                        if (t!!.code == 0) {
                            toUserName.setText(t.data.nickname)
                            Glide.with(context).load(t.data.icon).into(toIcon)
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
                                    gifname = giftAdpater.dataList.get(position).name
                                    giftAdpater.setSelect(position)
                                }
                            })
                        }
                    }
                })
    }

    override fun getLayoutResId(): Int {
        return R.layout.gift_bag_dialog_layout
    }

    var layoutManager: PagerGridLayoutManager;

    constructor(mContext: Context, targetUserId: String) : super(mContext) {
        val window = window
        window.setGravity(Gravity.BOTTOM)
        val params = window.attributes
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        window.attributes = params
        // 1.水平分页布局管理器
        layoutManager = PagerGridLayoutManager(2, 4, PagerGridLayoutManager.HORIZONTAL)
        grid_view.setLayoutManager(layoutManager)
        layoutManager.setPageListener(object : PagerGridLayoutManager.PageListener {
            override fun onPageSelect(pageIndex: Int) {
                giftIndicator.setCurrent(pageIndex)
            }

            override fun onPageSizeChanged(pageSize: Int) {
                giftIndicator.setMax(pageSize)
            }
        })
        // 2.设置滚动辅助工具
        val pageSnapHelper = PagerGridSnapHelper()
        pageSnapHelper.attachToRecyclerView(grid_view)
        infoLayout.visibility = View.VISIBLE
        val micData = ArrayList<String>()
        micData.add(targetUserId)
        val gson = Gson();
        mwJson = gson.toJson(micData);
        this.targetUserId = targetUserId;
        this.targetUserName = targetUserName;

        getUserInfo(targetUserId)//获取别人信息
        GiftData()//获取礼物数据
        userInfo()//获取自己信息
    }

    interface OnGiftListener {
        fun onClck(targetPosition: Int, toPosition: List<Int>, pic: String, dataList: List<RoomMicListBean.DataBean>, gifPic: String, gifName: String, gifNum: String,targetUserId:String,toUserName:String)
        fun needInputed();
    }

    private var onGiftListener: OnGiftListener? = null

    fun setonGiftListener(onGiftListener: OnGiftListener) {
        this.onGiftListener = onGiftListener
    }
}