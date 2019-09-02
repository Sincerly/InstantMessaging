package com.ysxsoft.imtalk.widget.dialog

import android.content.Context
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
class GiftBagDialog : ABSDialog {

    var type = 3
    var gift_id: String? = null
    var gift_num: String? = null
    var roomId: String? = null
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
            JbWithDrawActivity.starJbWithDrawActivity(this@GiftBagDialog.context)
            dismiss()
        }

        tv_zs.setOnClickListener {
            //            for (bean in micPostionAdapter!!.dataList) {
//                if (bean.isChoosed) {
//                    sectionNum.append(bean.uid).append(",")
//                }
//            }

            if (TextUtils.isEmpty(mwuid) || "0".equals(mwuid)) {
                ToastUtils.showToast(this@GiftBagDialog.context, "赠送人不能为空")
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(giftid) && TextUtils.isEmpty(bageId)) {
                ToastUtils.showToast(this@GiftBagDialog.context, "所选礼物或座驾不能为空")
                return@setOnClickListener
            }

            val zsPopuwindows = ZSPopuwindows(this@GiftBagDialog.context, R.layout.zs_layout, tv_zs)
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
        map.put("room_id", roomId!!)
        map.put("income_gift_uid", mwuid!!)
        map.put("uid", SpUtils.getSp(this@GiftBagDialog.context, "uid"))
        val body = RetrofitUtil.createJsonRequest(map)
        NetWork.getService(ImpService::class.java)
                .send_gift(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CommonBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: CommonBean?) {
                       if (t!!.code==0){
                           GifDialog(this@GiftBagDialog.context,gifurl).show()
                       }else{
                           ToastUtils.showToast(this@GiftBagDialog.context,t.msg)
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
                .BageList(SpUtils.getSp(this@GiftBagDialog.context, "uid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Action1<BageListBean> {
                    override fun call(t: BageListBean?) {
                        if ("0".equals(t!!.code)) {
                            val data = t.data
                            val bageAdpater = GridBageAdpater(this@GiftBagDialog.context)
                            grid_view.adapter = bageAdpater
                            bageAdpater.addAll(data)

                            bageAdpater.setOnClickListener(object : GridBageAdpater.OnClickListener {
                                override fun onBage(position: Int) {
                                    giftid = bageAdpater.dataList.get(position).id.toString()
                                    gifurl = bageAdpater.dataList.get(position).aw_gif
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
                .GetUserInfo(SpUtils.getSp(this@GiftBagDialog.context, "uid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<UserInfoBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: UserInfoBean?) {
                        if (t!!.code == 0) {
                            tv_gold.setText(t.data.money)
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }

    var giftid: String? = null

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
                            val giftAdpater = GridGiftAdpater(this@GiftBagDialog.context)
                            grid_view.adapter = giftAdpater
                            giftAdpater.addAll(data)
                            giftAdpater.setOnClickListener(object : GridGiftAdpater.OnClickListener {
                                override fun onGift(position: Int) {
                                    giftid = giftAdpater.dataList.get(position).id.toString()
                                    gifurl = giftAdpater.dataList.get(position).gif_pic
                                    giftAdpater.setSelect(position)
                                }
                            })
                        }
                    }
                })
    }

    var mwuid: String? = null
    var micPostionAdapter: MicPostionAdapter? = null
    //麦位头像数据
    private fun MwData(room_id: String, mContext: Context) {
        NetWork.getService(ImpService::class.java)
                .RoomMicList(room_id, SpUtils.getSp(mContext, "uid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Action1<RoomMicListBean> {
                    override fun call(t: RoomMicListBean?) {
                        if (t!!.code == 0) {
                            micPostionAdapter = MicPostionAdapter(mContext)
                            val manager = LinearLayoutManager(mContext)
                            manager.orientation = LinearLayoutManager.HORIZONTAL
                            recyclerView.layoutManager = manager
                            recyclerView.adapter = micPostionAdapter
                            micPostionAdapter!!.addAll(t.data)
                            micPostionAdapter!!.setOnClickListener(object : MicPostionAdapter.OnClickListener {
                                override fun onClick(position: Int) {
                                    mwuid = micPostionAdapter!!.dataList.get(position).uid.toString()
                                    micPostionAdapter!!.setSelect(position)
                                }
                            })
                        }
                    }
                })

    }

    override fun getLayoutResId(): Int {
        return R.layout.gift_bag_dialog_layout
    }

    constructor(mContext: Context, room_id: String) : super(mContext) {
        roomId = room_id
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

        MwData(room_id, mContext)
        GiftData()
        userInfo()
    }

}