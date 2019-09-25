package com.ysxsoft.imtalk.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.appservice.PlayMusicService
import com.ysxsoft.imtalk.bean.CommonBean
import com.ysxsoft.imtalk.bean.UserInfoBean
import com.ysxsoft.imtalk.chatroom.im.IMClient
import com.ysxsoft.imtalk.chatroom.im.message.RoomMemberChangedMessage
import com.ysxsoft.imtalk.chatroom.net.retrofit.RetrofitUtil
import com.ysxsoft.imtalk.chatroom.rtc.RtcClient
import com.ysxsoft.imtalk.chatroom.task.AuthManager
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.*
import com.ysxsoft.imtalk.widget.dialog.LoginOutDialog
import io.rong.imlib.IRongCallback
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Message
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.title_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class SettingActivity : BaseActivity() {

    companion object {
        fun startSettingActivity(mContext: Context, roomId: String, nikeName: String, icon: String) {
            val intent = Intent(mContext, SettingActivity::class.java)
            intent.putExtra("roomId", roomId)
            intent.putExtra("nikeName", nikeName)
            intent.putExtra("icon", icon)
            mContext.startActivity(intent)
        }
    }

    //    var roomId: String? = null
    var nikeName: String? = null
    var icon: String? = null
    override fun getLayout(): Int {
        return R.layout.activity_setting;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        roomId = intent.getStringExtra("roomId")
        nikeName = intent.getStringExtra("nikeName")
        icon = intent.getStringExtra("icon")
        setLightStatusBar(true)
        initView()
    }

    private fun initView() {
        initStatusBar(topView)
        setBackVisibily()
        setTitle("设置")
        tv_version.setText("V" + AppUtil.getVersionName(mContext))
        //绑定手机
        tv1.setOnClickListener {
            startActivity(BindPhoneActivity::class.java)
        }
        //绑定支付宝账号
        tv2.setOnClickListener {
            startActivity(BindZfbActivity::class.java)
        }
        //绑定银行卡
        tv12.setOnClickListener {
            startActivity(BankCardListActivity::class.java)
        }
        //登录密码
        tv3.setOnClickListener {
            startActivity(ForgetPswActivity::class.java)
        }
        //我要反馈
        tv4.setOnClickListener {
            startActivity(WyfkActivity::class.java)
        }
        //黑名单管理
        tv5.setOnClickListener {
            startActivity(BlackListActivity::class.java)
        }
        //社区规范
        tv6.setOnClickListener {
            startActivity(CommunityNormsActivity::class.java)
        }
        //联系官方
        tv7.setOnClickListener {
            startActivity(ContactOfficialsActivity::class.java)
        }
        //帮助
        tv8.setOnClickListener {
            startActivity(HelpActivity::class.java)
        }
        //关于平台
        tv9.setOnClickListener {
            startActivity(AboutPlatformActivity::class.java)
        }
        //检查版本
        rl10.setOnClickListener {
            showToastMessage("当前版本" + AppUtil.getVersionName(mContext))
        }
        //退出登录
        tv11.setOnClickListener {
            val outDialog = LoginOutDialog(mContext)
            val tv_ok = outDialog.findViewById<TextView>(R.id.tv_ok)
            tv_ok.setOnClickListener {
                sendBroadcast(Intent("WINDOW"))
                NetWork.getService(ImpService::class.java)
                        .GetUserInfo(SpUtils.getSp(mContext, "uid"))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<UserInfoBean> {
                            override fun onError(e: Throwable?) {
                            }

                            override fun onNext(t: UserInfoBean?) {
                                if (t!!.code == 0) {
                                    val data = t.data
                                    if (!TextUtils.isEmpty(data.now_roomId)) {
                                        quiteRoom(AuthManager.getInstance().currentUserId, "1", data.now_roomId)
                                    } else {
                                        var instance = ActivityPageManager.getInstance();
                                        instance!!.finishAllActivity();
                                        SpUtils.deleteSp(mContext)
                                        mContext.startActivity(Intent(mContext, LoginActivity::class.java))
                                    }
                                    outDialog.dismiss()
                                }
                            }

                            override fun onCompleted() {
                            }
                        })
            }
            outDialog.show()
        }
    }

    private fun quiteRoom(uid: String, kick: String, roomId: String) {
        val message = RoomMemberChangedMessage()
        message.setCmd(2)//离开房间
        message.targetUserId = uid
        message.targetPosition = -1
        message.userInfo = io.rong.imlib.model.UserInfo(SpUtils.getSp(mContext, "uid"), nikeName, Uri.parse(icon))
        val obtain = Message.obtain(roomId, Conversation.ConversationType.CHATROOM, message)

        RongIMClient.getInstance().sendMessage(obtain, null, null, object : IRongCallback.ISendMessageCallback {
            override fun onAttached(p0: Message?) {
                Log.d("tag", p0!!.content.toString())
            }

            override fun onSuccess(p0: Message?) {
                NetWork.getService(ImpService::class.java)
                        .tCRoom(uid, kick, roomId!!)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<CommonBean> {
                            override fun onError(e: Throwable?) {
                            }

                            override fun onNext(t: CommonBean?) {
                                showToastMessage(t!!.msg)
                                if (t.code == 0) {
                                    IMClient.getInstance().quitChatRoom(roomId, null)
                                    RtcClient.getInstance().quitRtcRoom(roomId, null)
                                    removeUser(roomId!!, AuthManager.getInstance().currentUserId)
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
                            var instance = ActivityPageManager.getInstance();
                            instance!!.finishAllActivity();
                            SpUtils.deleteSp(mContext)
                            mContext.startActivity(Intent(mContext, LoginActivity::class.java))
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }
}
