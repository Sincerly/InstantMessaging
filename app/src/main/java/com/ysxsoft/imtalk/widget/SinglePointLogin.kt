package com.ysxsoft.imtalk.widget

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.R.id.imageView
import com.ysxsoft.imtalk.bean.CommonBean
import com.ysxsoft.imtalk.bean.UserInfoBean
import com.ysxsoft.imtalk.chatroom.im.IMClient
import com.ysxsoft.imtalk.chatroom.im.message.RoomMemberChangedMessage
import com.ysxsoft.imtalk.chatroom.net.retrofit.RetrofitUtil
import com.ysxsoft.imtalk.chatroom.rtc.RtcClient
import com.ysxsoft.imtalk.chatroom.task.AuthManager
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.*
import com.ysxsoft.imtalk.view.LoginActivity
import io.rong.imlib.IRongCallback
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Message
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *Create By 胡
 *on 2019/9/27 0027
 */
class SinglePointLogin {
    private var windowManager: WindowManager? = null
    private var layoutParams: WindowManager.LayoutParams? = null
    private var displayView: View? = null
    private var mWindowWidth: Int = 0
    private var mWindowHeight: Int = 0
    var dataBean: UserInfoBean.DataBean? = null

    constructor() : super() {
        initView()
        NetWork.getService(ImpService::class.java)
                .GetUserInfo(AuthManager.getInstance().currentUserId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<UserInfoBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: UserInfoBean?) {
                        if (t!!.code == 0) {
                            dataBean = t.data
                            if (!TextUtils.isEmpty(dataBean!!.now_roomId)) {
                                quiteRoom(AuthManager.getInstance().currentUserId, "1")
                            }
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }

    private fun initView() {
        windowManager = BaseApplication.mContext!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        layoutParams = WindowManager.LayoutParams()
        layoutParams!!.type = WindowManager.LayoutParams.TYPE_TOAST
        layoutParams!!.format = PixelFormat.RGBA_8888
        layoutParams!!.gravity = Gravity.CENTER
        layoutParams!!.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        layoutParams!!.width = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams!!.height = WindowManager.LayoutParams.WRAP_CONTENT
    }

    fun show() {
        val layoutInflater = LayoutInflater.from(BaseApplication.mContext!!)
        displayView = layoutInflater.inflate(R.layout.online_dialog_layout, null)
        val tv_ok = displayView!!.findViewById<TextView>(R.id.tv_ok)
        tv_ok.setOnClickListener {
            var instance = ActivityPageManager.getInstance();
            instance!!.finishAllActivity();
            SpUtils.deleteSp(BaseApplication.mContext!!)
            val intent = Intent(BaseApplication.mContext!!, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            BaseApplication.mContext!!.startActivity(intent)
            dismiss()
        }
        windowManager!!.addView(displayView, layoutParams)
    }

    private fun quiteRoom(uid: String, kick: String) {
        val message = RoomMemberChangedMessage()
        message.setCmd(2)//离开房间
        message.targetUserId = uid
        message.targetPosition = -1
        message.userInfo = io.rong.imlib.model.UserInfo(AuthManager.getInstance().currentUserId, dataBean!!.nickname, Uri.parse(dataBean!!.icon))
        val obtain = Message.obtain(dataBean!!.now_roomId, Conversation.ConversationType.CHATROOM, message)

        RongIMClient.getInstance().sendMessage(obtain, null, null, object : IRongCallback.ISendMessageCallback {
            override fun onAttached(p0: Message?) {
                Log.d("tag", p0!!.content.toString())
            }

            override fun onSuccess(p0: Message?) {
                NetWork.getService(ImpService::class.java)
                        .tCRoom(uid, kick, dataBean!!.now_roomId!!)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<CommonBean> {

                            override fun onError(e: Throwable?) {
                            }

                            override fun onNext(t: CommonBean?) {
                                if (t!!.code == 0) {
                                    IMClient.getInstance().quitChatRoom(dataBean!!.now_roomId, null)
                                    RtcClient.getInstance().quitRtcRoom(dataBean!!.now_roomId, null)
                                    removeUser(dataBean!!.now_roomId!!, AuthManager.getInstance().currentUserId)
                                }
                            }

                            override fun onCompleted() {
                            }
                        })
            }

            override fun onError(p0: Message?, p1: RongIMClient.ErrorCode?) {
                Log.d("tag", p0!!.content.toString())//23409
            }
        });
    }

    fun removeUser(roomId: String, uid: String) {
        val map = HashMap<String, String>()
        map.put("room_id", roomId)
        map.put("uid", uid)
        val body = RetrofitUtil.createJsonRequest(map)
        NetWork.getService(ImpService::class.java)
                .remove_user(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CommonBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: CommonBean?) {
                        Log.d("tag=====", "移除成功")
                        if (t!!.code == 0) {
                            SpUtils.deleteSp(BaseApplication.mContext!!)
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }


    fun dismiss() {
        if (windowManager != null && displayView != null) {
            windowManager!!.removeView(displayView)
        }
    }
}