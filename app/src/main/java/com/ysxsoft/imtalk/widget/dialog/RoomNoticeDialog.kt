package com.ysxsoft.imtalk.widget.dialog

import android.content.Context
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.chatroom.model.DetailRoomInfo
import com.ysxsoft.imtalk.chatroom.task.ResultCallback
import com.ysxsoft.imtalk.chatroom.task.RoomManager
import com.ysxsoft.imtalk.widget.ABSDialog
import kotlinx.android.synthetic.main.room_notice_dialog_layout.*

/**
 *Create By 胡
 *on 2019/9/9 0009
 */
class RoomNoticeDialog(mContext: Context,var room_id: String):ABSDialog(mContext){

    override fun initView() {
        this.setCanceledOnTouchOutside(false)
        this.setCancelable(false)
        RoomManager.getInstance().getRoomDetailInfo1(room_id,object :ResultCallback<DetailRoomInfo>{
            override fun onFail(errorCode: Int) {
            }

            override fun onSuccess(result: DetailRoomInfo?) {
              if (result!=null){
                  tv_room_talk_title.setText(result.roomInfo.room_desc)
                  tv_content.setText(result.roomInfo.room_content)
              }
            }
        })

        tv_close.setOnClickListener {
            dismiss()
        }
    }

    override fun getLayoutResId(): Int {
       return R.layout.room_notice_dialog_layout
    }
}