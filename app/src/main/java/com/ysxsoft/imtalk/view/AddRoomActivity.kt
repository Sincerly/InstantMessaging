package com.ysxsoft.imtalk.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.R.string.room_name
import com.ysxsoft.imtalk.bean.CommonBean
import com.ysxsoft.imtalk.chatroom.im.message.RoomBgChangeMessage
import com.ysxsoft.imtalk.chatroom.im.message.RoomLableChangedMessage
import com.ysxsoft.imtalk.chatroom.im.message.RoomNameChangedMessage
import com.ysxsoft.imtalk.chatroom.net.retrofit.RetrofitUtil
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import com.ysxsoft.imtalk.widget.dialog.RoomNameDialog
import io.rong.imlib.IRongCallback
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Message
import kotlinx.android.synthetic.main.activity_add_room.*
import kotlinx.android.synthetic.main.room_tag_layout.*
import kotlinx.android.synthetic.main.title_layout.*
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers
import java.util.HashMap

class AddRoomActivity : BaseActivity() {

    companion object {
        fun starAddRoomActivity(mContext: Context, room_id: String, room_name: String) {
            val intent = Intent(mContext, AddRoomActivity::class.java)
            intent.putExtra("room_id", room_id)
            intent.putExtra("room_name", room_name)
            mContext.startActivity(intent)
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_add_room
    }

    var room_id: String? = null
    var room_name: String? = null
    var is_lock: Int? = 0
    var room_gift_tx: Int? = 0
    var room_is_fair: Int? = 0
    var room_pure: Int? = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        room_id = intent.getStringExtra("room_id")
        room_name = intent.getStringExtra("room_name")
        tv_title_right.visibility = View.VISIBLE
        tv_title_right.setText("完成")
        setLightStatusBar(true)
        initStatusBar(topView)
        initView()
    }

    private fun initView() {
        setBackVisibily()
        setTitle("房间设置")
        tv_fj_name.text = room_name
        //房间名
        ll_fj_name.setOnClickListener {
            val roomNameDialog = RoomNameDialog(mContext)
            roomNameDialog.setEdClickListener(object : RoomNameDialog.EdClickListener {
                override fun setData(string: String) {
                    tv_fj_name.text = string
                }
            })
            roomNameDialog.show()
        }

        //房间上锁
        switch_ss.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
            if (isChecked) is_lock = 1 else is_lock = 0
        }
        //房间标签
        ll_fj_bq.setOnClickListener {
            //            startActivity(RoomTagActivity::class.java)
            val intent = Intent(mContext, RoomTagActivity::class.java)
            startActivityForResult(intent, 0)
        }
        //管理员
        ll_fj_gly.setOnClickListener {
            ManagerActivity.starManagerActivity(mContext, "manage", room_id!!)
        }
        //黑名单
        tv_black_list.setOnClickListener {
            ManagerActivity.starManagerActivity(mContext, "black", room_id!!)
        }
        //房间背景
        tv_background.setOnClickListener {
            val intent = Intent(mContext, RoomBackGroundActivity::class.java)
            startActivityForResult(intent, 0)
        }

        //房间礼物特效
        switch_tx.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
            if (isChecked) room_gift_tx == 1 else room_gift_tx == 0
        }

        //房间公屏
        switch_gp.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
            if (isChecked) room_is_fair == 1 else room_is_fair == 0
        }
        //纯净模式
        switch_ms.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
            if (isChecked) room_pure == 1 else room_pure == 0
        }
        tv_title_right.setOnClickListener {
            savaData()
        }

    }

    private fun savaData() {
        val paramsMap = HashMap<String, String>()
        paramsMap.put("uid", SpUtils.getSp(mContext, "uid"))
        paramsMap.put("room_id", room_id!!)
        paramsMap.put("room_name", tv_fj_name.text.toString())
        paramsMap.put("lock_pwd", "")
        paramsMap.put("label_name", tv_fj_bq.text.toString())
        if(!TextUtils.isEmpty(img_id)) {
            paramsMap.put("room_bg", img_id!!)
        }
        paramsMap.put("room_gift_tx", room_gift_tx.toString())
        paramsMap.put("room_is_fair", room_is_fair.toString())
        paramsMap.put("room_pure", room_pure.toString())
        val body = RetrofitUtil.createJsonRequest(paramsMap)
        NetWork.getService(ImpService::class.java)
                .setRoomInfo(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Action1<CommonBean> {
                    override fun call(t: CommonBean?) {
                        showToastMessage(t!!.msg)
                        if (t.code == 0) {
                            if (!TextUtils.isEmpty(img_url)){
                                val intent = Intent("BGCHANG")
                                intent.putExtra("bgId",img_url)
                                sendBroadcast(intent)
                                val bgChangeMessage = RoomBgChangeMessage()
                                bgChangeMessage.bgId=img_url
                                val obtain = Message.obtain(room_id, Conversation.ConversationType.CHATROOM, bgChangeMessage)
                                RongIMClient.getInstance().sendMessage(obtain, null, null, object : IRongCallback.ISendMessageCallback {
                                    override fun onAttached(p0: Message?) {
                                        Log.d("tag",p0!!.content.toString())
                                    }

                                    override fun onSuccess(p0: Message?) {
                                        Log.d("tag",p0!!.content.toString())
                                    }

                                    override fun onError(p0: Message?, p1: RongIMClient.ErrorCode?) {
                                        Log.d("tag",p0!!.content.toString())
                                    }
                                });
                            }
                            if (!TextUtils.isEmpty(tagName)){
                                val intent = Intent("BGCHANG")
                                intent.putExtra("tagName",tv_fj_bq.text.toString())
                                sendBroadcast(intent)
                                val roomLableChangedMessage = RoomLableChangedMessage()
                                roomLableChangedMessage.roomLable=tv_fj_bq.text.toString()
                                val obtain = Message.obtain(room_id, Conversation.ConversationType.CHATROOM, roomLableChangedMessage)
                                RongIMClient.getInstance().sendMessage(obtain, null, null, object : IRongCallback.ISendMessageCallback {
                                    override fun onAttached(p0: Message?) {
                                        Log.d("tag",p0!!.content.toString())
                                    }

                                    override fun onSuccess(p0: Message?) {
                                        Log.d("tag",p0!!.content.toString())
                                    }

                                    override fun onError(p0: Message?, p1: RongIMClient.ErrorCode?) {
                                        Log.d("tag",p0!!.content.toString())
                                    }
                                });

                            }
                            if (!TextUtils.isEmpty(tv_fj_name.text.toString())){
                                val intent = Intent("BGCHANG")
                                intent.putExtra("roomName",tv_fj_name.text.toString())
                                sendBroadcast(intent)
                                val nameChangedMessage = RoomNameChangedMessage()
                                nameChangedMessage.roomName=tv_fj_name.text.toString()
                                val obtain = Message.obtain(room_id, Conversation.ConversationType.CHATROOM, nameChangedMessage)
                                RongIMClient.getInstance().sendMessage(obtain, null, null, object : IRongCallback.ISendMessageCallback {
                                    override fun onAttached(p0: Message?) {
                                        Log.d("tag",p0!!.content.toString())
                                    }

                                    override fun onSuccess(p0: Message?) {
                                        Log.d("tag",p0!!.content.toString())
                                    }

                                    override fun onError(p0: Message?, p1: RongIMClient.ErrorCode?) {
                                        Log.d("tag",p0!!.content.toString())
                                    }
                                });


                            }

                            finish()
                        }
                    }
                })
    }

    var img_id: String? = null
    var img_url: String? = null
    var tagName: String? = null
    var tabid: String? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0) {
            when (resultCode) {
                1 -> {
                    img_id = data!!.getStringExtra("img_id")
                    img_url = data!!.getStringExtra("img_url")
                }
                1854 -> {
                    tabid = data!!.getStringExtra("tabid")
                    tagName = data.getStringExtra("tagName")
                    tv_fj_bq.setText(tagName)
                }
            }
        }
    }
}
