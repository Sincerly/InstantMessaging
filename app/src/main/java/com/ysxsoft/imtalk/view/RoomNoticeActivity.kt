package com.ysxsoft.imtalk.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.CommonBean
import com.ysxsoft.imtalk.chatroom.im.message.RoomNoticeChangedMessage
import com.ysxsoft.imtalk.chatroom.net.retrofit.RetrofitUtil
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import io.rong.imlib.IRongCallback
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Message
import kotlinx.android.synthetic.main.room_notice_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.HashMap

/**
 *Create By 胡
 *on 2019/8/1 0001
 */
class RoomNoticeActivity : BaseActivity() {

    companion object {
        fun startRoomNoticeActivity(mContext: Context, roomId: String) {
            val intent = Intent(mContext, RoomNoticeActivity::class.java)
            intent.putExtra("roomId", roomId)
            mContext.startActivity(intent)
        }
    }

    override fun getLayout(): Int {
        return R.layout.room_notice_layout
    }

    var roomId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        roomId = intent.getStringExtra("roomId")
        setTitle("房间公告")
        setBackVisibily()
        initView()
    }

    private fun initView() {
        ed_room_title.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tv_0.setText((15 - s!!.length).toString())
            }
        })

        ed_room_content.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tv1.setText((300 - s!!.length).toString())
            }
        })
        tv_submint.setOnClickListener {
            if (TextUtils.isEmpty(ed_room_title.text.toString().trim())) {
                showToastMessage("房间公告标题不能为空")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(ed_room_content.text.toString().trim())) {
                showToastMessage("房间公告内容不能为空")
                return@setOnClickListener
            }
            savaData()
        }
    }

    private fun savaData() {
        val paramsMap = HashMap<String, String>()
        paramsMap.put("uid", SpUtils.getSp(mContext, "uid"))
        paramsMap.put("room_id", roomId!!)
        paramsMap.put("room_desc", ed_room_title.text.toString().trim())
        paramsMap.put("room_content", ed_room_content.text.toString().trim())
        val body = RetrofitUtil.createJsonRequest(paramsMap)
        NetWork.getService(ImpService::class.java)
                .setRoomInfo(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Observer<CommonBean>{
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: CommonBean?) {
                        showToastMessage(t!!.msg)
                        if (t.code==0){
                            val intent = Intent()
                            intent.putExtra("room_desc",ed_room_title.text.toString().trim())
                            setResult(1000,intent)

                            val noticeChangedMessage = RoomNoticeChangedMessage()
                            noticeChangedMessage.roomDesc=ed_room_title.text.toString().trim()
                            val obtain = Message.obtain(roomId, Conversation.ConversationType.CHATROOM, noticeChangedMessage)
                            RongIMClient.getInstance().sendMessage(obtain, null, null, object : IRongCallback.ISendMessageCallback {
                                override fun onAttached(p0: Message?) {
                                }

                                override fun onSuccess(p0: Message?) {
                                }

                                override fun onError(p0: Message?, p1: RongIMClient.ErrorCode?) {
                                }
                            });
                            finish()
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }
}