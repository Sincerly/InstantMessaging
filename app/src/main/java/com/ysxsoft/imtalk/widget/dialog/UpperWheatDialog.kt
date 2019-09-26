package com.ysxsoft.imtalk.widget.dialog

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.R.id.*
import com.ysxsoft.imtalk.adapter.WheatAdapter
import com.ysxsoft.imtalk.bean.RoomMicListBean
import com.ysxsoft.imtalk.bean.RoomMwUserBean
import com.ysxsoft.imtalk.chatroom.model.DetailRoomInfo
import com.ysxsoft.imtalk.chatroom.task.ResultCallback
import com.ysxsoft.imtalk.chatroom.task.RoomManager
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.GradeIconUtils
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.widget.ABSDialog
import kotlinx.android.synthetic.main.upper_wheat_dialog_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers

/**
 *Create By 胡
 *on 2019/7/17 0017
 * 上麦
 */
class UpperWheatDialog:ABSDialog{

    constructor(mContext: Context,userId:String,roomId: String):super(mContext){
        requestData(userId, roomId)
        MwData(roomId, mContext)
    }

    private fun MwData(roomId: String, mContext: Context) {
        RoomManager.getInstance().getRoomDetailInfo1(roomId,object : ResultCallback<DetailRoomInfo>{
            override fun onSuccess(result: DetailRoomInfo?) {
                if (result!=null){
                    recyclerView.layoutManager=GridLayoutManager(mContext,4)
                    val wheatAdapter = WheatAdapter(mContext)
                    recyclerView.adapter=wheatAdapter
                    wheatAdapter.addAll(result.micPositions)
                    wheatAdapter.setOnWheatListener(object :WheatAdapter.OnWheatListener{
                        override fun onWheat(position: Int) {
                            dismiss()
                            val uid = wheatAdapter.dataList.get(position).uid
                            val sort = wheatAdapter.dataList.get(position).sort
                            if (onWheatClickListener!=null){
                                onWheatClickListener!!.onClickWheat(sort)
                            }
                        }
                    })
                }
            }

            override fun onFail(errorCode: Int) {
            }
        })


    }

    private fun requestData(userId: String, roomId: String) {
        NetWork.getService(ImpService::class.java)
                .roomMwUser(userId, roomId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<RoomMwUserBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: RoomMwUserBean?) {
                        if (t!!.code == 0) {
                            ImageLoadUtil.GlideHeadImageLoad(this@UpperWheatDialog.context,t.data.icon,img_head)
                            tv_nikeName.setText(t.data.nickname)
                            tv_id.setText("ID："+t.data.tt_id)
                            tv_familly.setText("所在家族："+t.data.fmy_name)
//                            tv_tuhao.setText(t.data.user_level)
//                            tv_mei.setText("魅 "+t.data.ml_level)
//                            tv_zs.setText(t.data.user_level)

                            val charmIcon = GradeIconUtils.charmIcon(t.data.ml_level.toInt())
                            img_mei.setImageResource(charmIcon[0])
                            tv_mei.setText(t.data.ml_level)
                            tv_mei.setTextColor(charmIcon[1])
                            val ints = GradeIconUtils.gradeIcon(t.data.user_level.toInt())
                            tv_zs.setText(t.data.user_level)
                            tv_zs.setTextColor(ints[1])
                            img_zs.setImageResource(ints[0])
                        }
                    }

                    override fun onCompleted() {
                    }
                })


    }

    override fun initView() {
        img_cancle.setOnClickListener {
            dismiss()
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.upper_wheat_dialog_layout
    }

    interface  OnWheatClickListener{
        fun onClickWheat(position:Int)
    }
    private var onWheatClickListener: OnWheatClickListener?=null
    fun setOnWheatClickListener(onWheatClickListener: OnWheatClickListener){
        this.onWheatClickListener=onWheatClickListener
    }

}