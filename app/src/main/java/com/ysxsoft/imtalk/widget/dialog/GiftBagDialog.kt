package com.ysxsoft.imtalk.widget.dialog

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
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
class GiftBagDialog : ABSDialog {

    var type = 3
    var gift_id: String? = null
    var gift_num: String? = null
    var roomId: String? = null
    var gifname: String? = null
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
            if ("".equals(mwJson)||"[]".equals(mwJson)) {
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

    val uidList = ArrayList<String>()
    private fun sendGift() {
        if(mwJson==null){
            ToastUtils.showToast(context,"请选择赠送的人")
            return
        }
        val map = HashMap<String, String>()
        map.put("type", giftbage.toString())
        map.put("gift_id", giftid.toString())
        map.put("gift_num", gift_num.toString())
        map.put("room_id", roomId!!)
        map.put("income_gift_uid", mwJson!!)
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
                        if (t!!.code == 0) {
                            uidList.clear()
                            for (bean in micPositons!!) {
                                uidList.add(bean.uid.toString())
                            }
                            if (onGiftListener != null) {
                                val data= ArrayList<Int>()
                                var fromPosition=-1;//默认不在麦位上
                                for (bean in micPositons!!) {
                                    val sort = bean.sort
                                    if(!bean.isChoosed){
                                        if (AuthManager.getInstance().currentUserId.equals(bean.uid.toString())) {
                                            if(sort==0){
                                                //自己是房主
                                                fromPosition=8;
                                            }else{
                                                fromPosition=sort-1;
                                            }
                                        }
                                    }
                                    data.clear()

                                    for (bean in micPositons!!) {
                                        if(bean.isChoosed){
                                            val sort = bean.sort
                                            if (AuthManager.getInstance().currentUserId.equals(bean.uid.toString())) {
                                            }else{
                                                if(sort==0){
                                                    //加入房主
                                                    data.add(8)
                                                }else{
                                                    //加入其它用户
                                                    data.add(sort-1)
                                                }
                                            }
                                        }
                                    }
//                                    if (AuthManager.getInstance().currentUserId.equals(bean.uid.toString())) {//自己在麦位
//                                        val sort = bean.sort
//                                        if (sort == 0 && toPosition != 0) {//自己是房主    送其他人
//                                            onGiftListener!!.onClck(8, toPosition - 1, pic!!, gifurl!!)
//                                        } else if (sort == 0 && toPosition == 0) { //房主送房主
//                                            onGiftListener!!.onClck(8, 8, pic!!, gifurl!!)
//                                        } else {
//                                            if (toPosition == 0) {//送房主
//                                                onGiftListener!!.onClck(sort - 1, 8, pic!!, gifurl!!)
//                                            } else {
//                                                onGiftListener!!.onClck(sort - 1, toPosition - 1, pic!!, gifurl!!)
//                                            }
//                                        }
//                                    }
                                }
//                                if (!uidList.contains(AuthManager.getInstance().currentUserId)) {//自己没在麦位上
//                                    if (toPosition == 0) {//送房主
//                                        onGiftListener!!.onClck(-1, 8, pic!!, gifurl!!)
//                                    } else {//送其他人
//                                        onGiftListener!!.onClck(-1, toPosition - 1, pic!!, gifurl!!)
//                                    }
//                                }
                                onGiftListener!!.onClck(fromPosition,data,pic!!,micPositons!!,gifurl!!,gifname!!,gift_num!!)
                            }
                        } else {
                            ToastUtils.showToast(this@GiftBagDialog.context, t.msg)
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
                                    gifname = bageAdpater.dataList.get(position).aw_name
                                    pic = bageAdpater.dataList.get(position).aw_images
                                    //TODO: Sincerly ：缺少数量？
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
                            tv_user_gold.setText(t.data.money)
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
                            val giftAdpater = GridGiftAdpater(this@GiftBagDialog.context)
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

    var mwJson:String?=null
    var toPosition = -2
    var micPositons: List<RoomMicListBean.DataBean>? = null
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
                            micPositons = t.data
                            micPostionAdapter = MicPostionAdapter(mContext)
                            val manager = LinearLayoutManager(mContext)
                            manager.orientation = LinearLayoutManager.HORIZONTAL
                            recyclerView.layoutManager = manager
                            recyclerView.adapter = micPostionAdapter
                            micPostionAdapter!!.addAll(t.data)
                            micPostionAdapter!!.setOnClickListener(object : MicPostionAdapter.OnClickListener {
                                override fun onClick(position: Int) {
                                    //循环获取
                                    val uid=AuthManager.getInstance().currentUserId;
                                    val data=micPostionAdapter!!.dataList.get(position);
                                    if(data!!.uid.toString().equals(uid)){
                                        ToastUtils.showToast(context,"不能送礼物给自己!")
                                        return
                                    }
                                    if(data!!.uid==0){
                                        ToastUtils.showToast(context,"选择麦位无人!")
                                        return
                                    }

                                    //mwuid = micPostionAdapter!!.dataList.get(position).uid.toString()
                                    //toPosition = micPostionAdapter!!.dataList.get(position).sort
                                    val bean = micPostionAdapter!!.dataList.get(position)
                                    bean.isChoosed = !bean.isChoosed;

                                    val micData= ArrayList<String>()
                                    for (bean in micPositons!!) {
                                        if(bean.isChoosed){
                                            micData.add(bean.uid.toString())
                                        }
                                    }
                                    val gson=Gson();
                                    mwJson=gson.toJson(micData);//选择的麦位用户id数组
                                    micPostionAdapter!!.notifyDataSetChanged()
                                }
                            })
//                            micPostionAdapter!!.setCheckInterface(object :MicPostionAdapter.CheckInterface{
//                                override fun checkGroup(position: Int, isChecked: Boolean) {
//                                    val bean = micPostionAdapter!!.dataList.get(position)
//                                    bean.isChoosed = isChecked;
//                                    micPostionAdapter!!.notifyDataSetChanged()
//                                }
//                            })
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

    interface OnGiftListener {
        fun onClck(targetPosition: Int, toPosition: List<Int>, pic: String,dataList:List<RoomMicListBean.DataBean>, gifPic: String, gifName: String, gifNum: String)
    }

    private var onGiftListener: OnGiftListener? = null

    fun setonGiftListener(onGiftListener: OnGiftListener) {
        this.onGiftListener = onGiftListener
    }
}