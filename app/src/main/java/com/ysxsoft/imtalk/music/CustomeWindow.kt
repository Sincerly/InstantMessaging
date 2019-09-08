package com.ysxsoft.imtalk.music

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
import android.net.Uri
import android.util.Log
import com.ysxsoft.imtalk.bean.UserInfoBean
import com.ysxsoft.imtalk.chatroom.im.message.RoomMemberChangedMessage
import com.ysxsoft.imtalk.chatroom.model.DetailRoomInfo
import com.ysxsoft.imtalk.chatroom.task.ResultCallback
import com.ysxsoft.imtalk.chatroom.task.RoomManager
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import com.ysxsoft.imtalk.view.ChatRoomActivity
import io.rong.imlib.IRongCallback
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Message
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Create By 胡
 * on 2019/9/8 0008
 */
class CustomeWindow(var context: Context, private val icon: String?) {
    private var windowManager: WindowManager? = null
    private var layoutParams: WindowManager.LayoutParams? = null
    private var displayView: View? = null
    private var mWindowWidth: Int = 0
    private var mWindowHeight: Int = 0
    private var imageView: CircleImageView? = null
    var mydatabean: UserInfoBean? = null
    init {
        requestMyData()
        initView()
    }
    private fun requestMyData() {
        NetWork.getService(ImpService::class.java)
                .GetUserInfo(SpUtils.getSp(context, "uid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<UserInfoBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: UserInfoBean?) {
                        if (t!!.code == 0) {
                            mydatabean = t
                        }
                    }

                    override fun onCompleted() {
                    }
                })

    }
    private fun initView() {
        windowManager = context.getSystemService(WINDOW_SERVICE) as WindowManager
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
        layoutParams!!.x = mWindowWidth
        layoutParams!!.y = mWindowHeight - 500
    }

    fun show() {
        val layoutInflater = LayoutInflater.from(context)
        displayView = layoutInflater.inflate(R.layout.floatwindow_layout, null)
        imageView = displayView!!.findViewById(R.id.img_head)
        displayView!!.setOnTouchListener(FloatingOnTouchListener())
        windowManager!!.addView(displayView, layoutParams)
        val viewById = displayView!!.findViewById<FrameLayout>(R.id.fl)
        viewById.setOnClickListener {
            ToastUtils.showToast("点击x号")
            dismiss()
        }
        imageView!!.setOnClickListener {
            joinChatRoom(mydatabean!!.data.now_roomId)
        }
        Glide.with(context).load(icon).into(imageView!!)
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
        if (windowManager != null && displayView != null) {
            windowManager!!.removeView(displayView)
        }
    }

    fun joinChatRoom(roomId: String) {
        RoomManager.getInstance().joinRoom(SpUtils.getSp(context, "uid"), roomId, object : ResultCallback<DetailRoomInfo> {
            override fun onSuccess(result: DetailRoomInfo?) {
                val intent = Intent(context, ChatRoomActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("room_id",roomId)
                intent.putExtra("nikeName",mydatabean!!.data.nickname)
                intent.putExtra("icon",mydatabean!!.data.icon)
                context.startActivity(intent)
                    dismiss()
            }

            override fun onFail(errorCode: Int) {

            }
        })
    }

}
