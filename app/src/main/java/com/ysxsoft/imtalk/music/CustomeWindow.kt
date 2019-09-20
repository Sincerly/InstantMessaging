package com.ysxsoft.imtalk.music

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout

import com.bumptech.glide.Glide
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.appservice.FloatingDisplayService
import com.ysxsoft.imtalk.chatroom.utils.MyApplication
import com.ysxsoft.imtalk.chatroom.utils.ToastUtils
import com.ysxsoft.imtalk.widget.CircleImageView

import android.content.Context.WINDOW_SERVICE
import android.content.IntentFilter
import android.net.Uri
import android.util.Log
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import com.umeng.socialize.utils.DeviceConfigInternal.context
import com.ysxsoft.imtalk.appservice.PlayMusicService
import com.ysxsoft.imtalk.bean.CommonBean
import com.ysxsoft.imtalk.bean.UserInfoBean
import com.ysxsoft.imtalk.chatroom.im.IMClient
import com.ysxsoft.imtalk.chatroom.im.message.RoomMemberChangedMessage
import com.ysxsoft.imtalk.chatroom.model.DetailRoomInfo
import com.ysxsoft.imtalk.chatroom.net.retrofit.RetrofitUtil
import com.ysxsoft.imtalk.chatroom.rtc.RtcClient
import com.ysxsoft.imtalk.chatroom.task.AuthManager
import com.ysxsoft.imtalk.chatroom.task.ResultCallback
import com.ysxsoft.imtalk.chatroom.task.RoomManager
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.BaseApplication
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import com.ysxsoft.imtalk.view.ChatRoomActivity
import io.rong.imlib.IRongCallback
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Message
import io.rong.push.RongPushClient.stopService
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Create By 胡
 * on 2019/9/8 0008
 */
class CustomeWindow {
    private var windowManager: WindowManager? = null
    private var layoutParams: WindowManager.LayoutParams? = null
    private var displayView: View? = null
    private var mWindowWidth: Int = 0
    private var mWindowHeight: Int = 0
    private var imageView: CircleImageView? = null
    var mydatabean: UserInfoBean? = null
    var isShowing: Boolean = false
    var icon: String? = null
    var myBroadCast: MyBroadCast? = null

    constructor(context: Context, icon: String?) : super() {
        this.icon = icon
        initView()
        myBroadCast = MyBroadCast()
        val filter = IntentFilter("WINDOW")
        if (IsShowing()) {
            BaseApplication.mContext!!.registerReceiver(myBroadCast, filter)
        }
    }

    inner class MyBroadCast : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if ("WINDOW".equals(intent!!.action)) {
                dismiss()
            }
        }
    }


    init {//初始化代码块
        requestMyData()
    }

    private fun requestMyData() {
        NetWork.getService(ImpService::class.java)
                .GetUserInfo(AuthManager.getInstance().currentUserId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<UserInfoBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: UserInfoBean?) {
                        if (t!!.code == 0) {
                            mydatabean = t
                            imageView!!.setOnClickListener {
                                joinChatRoom(mydatabean!!.data.now_roomId)
                            }
                        }
                    }

                    override fun onCompleted() {
                    }
                })

    }

    private fun initView() {
        isShowing = true
        windowManager = BaseApplication.mContext!!.getSystemService(WINDOW_SERVICE) as WindowManager
        layoutParams = WindowManager.LayoutParams()
        mWindowWidth = windowManager!!.defaultDisplay.width
        mWindowHeight = windowManager!!.defaultDisplay.height
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams!!.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            layoutParams!!.type = WindowManager.LayoutParams.TYPE_PHONE
        }
        layoutParams!!.format = PixelFormat.RGBA_8888
        layoutParams!!.gravity = Gravity.LEFT or Gravity.TOP
        layoutParams!!.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        layoutParams!!.width = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams!!.height = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams!!.x = mWindowWidth - 100
        layoutParams!!.y = mWindowHeight - 500
    }

    fun IsShowing(): Boolean {
        return isShowing
    }

    fun show() {
        requestMyData()
        isShowing = true
        val layoutInflater = LayoutInflater.from(BaseApplication.mContext!!)
        displayView = layoutInflater.inflate(R.layout.floatwindow_layout, null)
        imageView = displayView!!.findViewById(R.id.img_head)
        displayView!!.setOnTouchListener(FloatingOnTouchListener())
        windowManager!!.addView(displayView, layoutParams)
        val viewById = displayView!!.findViewById<FrameLayout>(R.id.fl)
        viewById.setOnClickListener {
            if (mydatabean != null) {
                if (mydatabean!!.data != null) {
                    quiteRoom("1")
                }
            }
        }
//        imageView!!.setOnClickListener {
//            joinChatRoom(mydatabean!!.data.now_roomId)
//        }
        Glide.with(BaseApplication.mContext!!).load(icon).into(imageView!!)

        var rotate = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        val lin = LinearInterpolator();
        rotate.setInterpolator(lin);
        rotate.setDuration(5000);//设置动画持续周期
        rotate.setRepeatCount(-1);//设置重复次数
        rotate.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        rotate.setStartOffset(10);//执行前的等待时间
        imageView!!.setAnimation(rotate);

    }

    private fun quiteRoom(kick: String) {
        val message = RoomMemberChangedMessage()
        message.setCmd(2)//离开房间
        message.targetUserId = AuthManager.getInstance().currentUserId
        message.targetPosition = -1
        message.userInfo = io.rong.imlib.model.UserInfo(AuthManager.getInstance().currentUserId, mydatabean!!.data.nickname, Uri.parse(icon))
        val obtain = Message.obtain(mydatabean!!.data.now_roomId, Conversation.ConversationType.CHATROOM, message)

        RongIMClient.getInstance().sendMessage(obtain, null, null, object : IRongCallback.ISendMessageCallback {
            override fun onAttached(p0: Message?) {
                Log.d("tag", p0!!.content.toString())
            }

            override fun onSuccess(p0: Message?) {
                NetWork.getService(ImpService::class.java)
                        .tCRoom(AuthManager.getInstance().currentUserId, kick, mydatabean!!.data.now_roomId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<CommonBean> {
                            override fun onError(e: Throwable?) {
                            }

                            override fun onNext(t: CommonBean?) {
                                ToastUtils.showToast(t!!.msg)
                                if (t.code == 0) {
                                    removeUser(AuthManager.getInstance().currentUserId, mydatabean!!.data.now_roomId)
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
                        if (t!!.code == 0) {
                            dismiss()
                            IMClient.getInstance().quitChatRoom(mydatabean!!.data.now_roomId, null)
                            RtcClient.getInstance().quitRtcRoom(mydatabean!!.data.now_roomId, null)
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }

    private inner class FloatingOnTouchListener : View.OnTouchListener {
        private var x: Int = 0
        private var y: Int = 0

        override fun onTouch(view: View, event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    x = event.rawX.toInt()
                    y = event.rawY.toInt()
                }
                MotionEvent.ACTION_MOVE -> {
                    val nowX = event.rawX.toInt()
                    val nowY = event.rawY.toInt()
                    val movedX = nowX - x
                    val movedY = nowY - y
                    x = nowX
                    y = nowY
                    layoutParams!!.x = layoutParams!!.x + movedX
                    layoutParams!!.y = layoutParams!!.y + movedY
                    windowManager!!.updateViewLayout(view, layoutParams)
                }

                MotionEvent.ACTION_UP -> {
                    layoutParams!!.x = mWindowWidth
                    windowManager!!.updateViewLayout(view, layoutParams)
                }
                else -> {
                }
            }
            return false
        }
    }


    fun dismiss() {
        BaseApplication.mContext!!.unregisterReceiver(myBroadCast)

        isShowing = false
        if (windowManager != null && displayView != null) {
            windowManager!!.removeView(displayView)
        }
        if (imageView != null) {
            imageView!!.clearAnimation()
        }
    }

    fun joinChatRoom(roomId: String) {
        RoomManager.getInstance().joinRoom(AuthManager.getInstance().currentUserId, roomId, "", object : ResultCallback<DetailRoomInfo> {
            override fun onSuccess(result: DetailRoomInfo?) {
                if (result != null) {
                    val intent = Intent(BaseApplication.mContext!!, ChatRoomActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.putExtra("room_id", roomId)
                    intent.putExtra("nikeName", mydatabean!!.data.nickname)
                    intent.putExtra("icon", mydatabean!!.data.icon)
                    BaseApplication.mContext!!.startActivity(intent)
                    dismiss()
                }
            }

            override fun onFail(errorCode: Int) {

            }
        })
    }

}
