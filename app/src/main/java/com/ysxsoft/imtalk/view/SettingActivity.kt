package com.ysxsoft.imtalk.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.Settings
import android.support.v4.content.FileProvider
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.google.gson.JsonObject
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.appservice.PlayMusicService
import com.ysxsoft.imtalk.bean.CommonBean
import com.ysxsoft.imtalk.bean.UserInfoBean
import com.ysxsoft.imtalk.bean.VersionBean
import com.ysxsoft.imtalk.chatroom.im.IMClient
import com.ysxsoft.imtalk.chatroom.im.message.RoomMemberChangedMessage
import com.ysxsoft.imtalk.chatroom.net.retrofit.RetrofitUtil
import com.ysxsoft.imtalk.chatroom.rtc.RtcClient
import com.ysxsoft.imtalk.chatroom.task.AuthManager
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.*
import com.ysxsoft.imtalk.widget.dialog.LoginOutDialog
import com.ysxsoft.imtalk.widget.dialog.UpdataDialog
import io.rong.imlib.IRongCallback
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Message
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.title_layout.*
import kotlinx.android.synthetic.main.update_bar_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

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
            UpdataVersion()
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

    /**
     * 版本更新
     */
    private var dialog: UpdataDialog? = null
    private var proBar: ProgressBar? = null
    private fun UpdataVersion() {
        NetWork.getService(ImpService::class.java)
                .version("1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<VersionBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: VersionBean?) {
                        Log.e("tag==","onNext=="+t.toString())
                        if (t!!.code==0) {
                            val versionName = AppUtil.getVersionName(mContext)
                            val i = VersionUtils.compareVersion(versionName, t.data.version)
                            // 0代表相等，1代表version1大于version2，-1代表version1小于version2
                            if (i == -1) {
                                dialog = UpdataDialog(mContext)
                                val tv_update = dialog!!.findViewById<TextView>(R.id.tv_update)
                                proBar = dialog!!.findViewById<ProgressBar>(R.id.proBar)
                                tv_update.setOnClickListener(View.OnClickListener {
                                    proBar!!.setVisibility(View.VISIBLE)
                                    downloadAPK(t.data.path)
                                })
                                dialog!!.show()
                            }
                        }
                    }

                    override fun onCompleted() {
                    }
                })


    }

    //  进度
    private var mProgress: Int = 0
    //  文件保存路径
    private var mSavePath: String? = null
    //  判断是否停止
    private val mIsCancel = false

    private fun downloadAPK(path: String?) {
        Thread(Runnable {
            try {
                if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                    val sdPath = Environment.getExternalStorageDirectory().toString() + "/"
                    //                      文件保存路径
                    mSavePath = sdPath + "oil"
                    val dir = File(mSavePath)
                    if (!dir.exists()) {
                        dir.mkdir()
                    }
                    // 下载文件
                    val conn = URL(path).openConnection() as HttpURLConnection
                    conn.connect()
                    val isinput = conn.inputStream
                    val length = conn.contentLength

                    val apkFile = File(mSavePath, AppUtil.getVersionName(mContext))
                    val fos = FileOutputStream(apkFile)

                    var count = 0
                    val buffer = ByteArray(1024)
                    while (!mIsCancel) {
                        val numread = isinput.read(buffer)
                        count += numread
                        // 计算进度条的当前位置
                        mProgress = (count.toFloat() / length * 100).toInt()
                        // 更新进度条
                        mUpdateProgressHandler.sendEmptyMessage(1)

                        // 下载完成
                        if (numread < 0) {
                            mUpdateProgressHandler.sendEmptyMessage(2)
                            break
                        }
                        fos.write(buffer, 0, numread)
                    }
                    fos.close()
                    isinput.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }).start()
    }

    /**
     * 接收消息
     */
    @SuppressLint("HandlerLeak")
    private val mUpdateProgressHandler = object : Handler() {
        override fun handleMessage(msg: android.os.Message) {
            when (msg.what) {
                1 ->
                    // 设置进度条
                    proBar!!.setProgress(mProgress)
                2 -> {
                    // 隐藏当前下载对话框
                    dialog!!.dismiss()
                    // 安装 APK 文件
                    installAPK()
                }
            }
        }
    }

    private fun installAPK() {
        val apkFile = File(mSavePath, AppUtil.getVersionName(mContext))
        if (!apkFile.exists()) {
            return
        }
        val intent = Intent(Intent.ACTION_VIEW)
//      安装完成后，启动app（源码中少了这句话）

        if (null != apkFile) {
            try {
                //兼容7.0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    val contentUri = FileProvider.getUriForFile(mContext, mContext.packageName + ".fileProvider", apkFile)
                    intent.setDataAndType(contentUri, "application/vnd.android.package-archive")
                    //兼容8.0
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val hasInstallPermission = mContext.packageManager.canRequestPackageInstalls()
                        if (!hasInstallPermission) {
                            startInstallPermissionSettingActivity()
                            return
                        }
                    }
                } else {
                    intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive")
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                if (mContext.packageManager.queryIntentActivities(intent, 0).size > 0) {
                    mContext.startActivity(intent)
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    private fun startInstallPermissionSettingActivity() {
        //注意这个是8.0新API
        val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        mContext.startActivity(intent)

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
