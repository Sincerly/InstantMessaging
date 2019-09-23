package com.ysxsoft.imtalk.widget.dialog

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import com.ysxsoft.imtalk.R
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
import com.ysxsoft.imtalk.widget.ABSDialog
import io.rong.imlib.IRongCallback
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Message
import kotlinx.android.synthetic.main.online_dialog_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *Create By 胡
 *on 2019/9/21 0021
 */
class OnLineDialog(var mContext: Context) : ABSDialog(mContext) {
    var dataBean: UserInfoBean.DataBean? = null

    override fun initView() {
        this.setCanceledOnTouchOutside(false)
        this.setCancelable(false)
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

        tv_ok.setOnClickListener {
            var instance = ActivityPageManager.getInstance();
            instance!!.finishAllActivity();
            SpUtils.deleteSp(mContext)
            mContext.startActivity(Intent(mContext, LoginActivity::class.java))
        }
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
                                ToastUtils.showToast(this@OnLineDialog.mContext, t!!.msg)
                                if (t.code == 0) {
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
                        Log.d("tag=====","移除成功")
                        if (t!!.code == 0) {
//                            var instance = ActivityPageManager.getInstance();
//                            instance!!.finishAllActivity();
                            SpUtils.deleteSp(mContext)
//                            mContext.startActivity(Intent(mContext, LoginActivity::class.java))
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }

    override fun getLayoutResId(): Int {
        return R.layout.online_dialog_layout
    }
}