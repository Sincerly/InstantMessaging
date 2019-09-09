package com.ysxsoft.imtalk.widget.dialog

import android.content.Context
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.RoomnewBean
import com.ysxsoft.imtalk.chatroom.model.DetailRoomInfo
import com.ysxsoft.imtalk.chatroom.task.ResultCallback
import com.ysxsoft.imtalk.chatroom.task.RoomManager
import com.ysxsoft.imtalk.chatroom.utils.ToastUtils
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.widget.ABSDialog
import kotlinx.android.synthetic.main.room_notice_dialog_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *Create By 胡
 *on 2019/9/9 0009
 */
class RoomNoticeDialog:ABSDialog{

    constructor(mContext: Context,room_id: String):super(mContext){
        requestData(room_id)
    }

    override fun initView() {
        this.setCanceledOnTouchOutside(false)
        this.setCancelable(false)
        tv_close.setOnClickListener {
            dismiss()
        }
    }

    private fun requestData(room_id:String) {
        NetWork.getService(ImpService::class.java)
                .Roomnew(room_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Observer<RoomnewBean>{
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: RoomnewBean?) {
                        if (t!!.code==0){
                            tv_room_talk_title.setText(t.data.result.roomInfo.room_desc)
                            tv_content.setText(t.data.result.roomInfo.room_content)
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }

    override fun getLayoutResId(): Int {
       return R.layout.room_notice_dialog_layout
    }
}