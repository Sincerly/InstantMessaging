package com.ysxsoft.imtalk.utils

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Process
import android.support.multidex.MultiDex
import android.util.Log
import cat.ereza.customactivityoncrash.CustomActivityOnCrash
import com.umeng.commonsdk.UMConfigure
import com.umeng.socialize.PlatformConfig
import com.ysxsoft.imtalk.bean.PalMessageBus
import com.ysxsoft.imtalk.chatroom.im.IMClient
import com.ysxsoft.imtalk.chatroom.net.HttpClient
import com.ysxsoft.imtalk.chatroom.task.AuthManager
import com.ysxsoft.imtalk.chatroom.task.ThreadManager
import com.ysxsoft.imtalk.chatroom.utils.MyApplication
import com.ysxsoft.imtalk.chatroom.utils.ToastUtils
import com.ysxsoft.imtalk.chatroom.utils.log.SLog
import com.ysxsoft.imtalk.im.message.LobbyTextMessage
import com.ysxsoft.imtalk.im.message.PrivateCarMessage
import com.ysxsoft.imtalk.im.message.PrivateGiftMessage
import com.ysxsoft.imtalk.im.message.PrivateHeaderMessage
import com.ysxsoft.imtalk.im.provider.*
import com.ysxsoft.imtalk.rong.MyExtensionModule
import com.ysxsoft.imtalk.widget.dialog.OnLineDialog
import io.rong.imkit.DefaultExtensionModule
import io.rong.imkit.IExtensionModule
import io.rong.imkit.RongExtensionManager
import io.rong.imkit.RongIM
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Group
import io.rong.imlib.model.Message
import io.rong.imlib.model.UserInfo
import io.rong.message.ImageMessage
import io.rong.message.RichContentMessage
import io.rong.message.TextMessage
import io.rong.message.VoiceMessage
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import org.litepal.LitePal
import org.litepal.extension.find


@SuppressLint("Registered")
class BaseApplication : MyApplication() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        var mContext: Context? = null
    }

    var instance: BaseApplication? = null
    override fun onCreate() {
        super.onCreate()
        mContext = applicationContext
        CustomActivityOnCrash.install(this);
        UMConfigure.init(this, "5d650e274ca3578df70008cb", "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");//58edcfeb310c93091c000be2 5965ee00734be40b580001a0

        LitePal.initialize(this)//初始化LitePal数据库
//        RongIMClient.init(this, "lmxuhwagl5dcd", false);
        SLog.init(this)
        RongIM.init(this)

        /*
         * 以上部分在所有进程中会执行
         */
        if (!applicationInfo.packageName.equals(getCurProcessName(applicationContext))) {
            return
        }
        /*
         * 以下部分仅在主进程中进行执行
         */
        // 初始化网络请求
        HttpClient.getInstance().init(this)
        // 初始化后清除掉请求认证缓存，保证每次登录都使用不用的用户
        HttpClient.getInstance().clearRequestCache()
        // 初始化线程管理
        ThreadManager.getInstance().init(this)
        // 初始化 IM
        IMClient.getInstance().init(this)


        PlatformConfig.setWeixin("wx9f167bc9812eb1dc", "efa8793c110273a2af04f92d53acde45");//s:  appId   s1: AppSecret
        PlatformConfig.setQQZone("101762831", "9b5d79670b6aa08e27d23b00861522f3");//qq 的s:APP ID   s1：APP Key

        IMClient.getInstance().addMessageReceiveListener(MyReceiveMessageListener())

        RongIM.getInstance().setSendMessageListener(MySendMessageListener())
        setMyExtensionModule()

        RongIM.setUserInfoProvider({ userId ->//个人信息消息提供者
            Log.e("provider", "chat")
            //   //根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
            val beans = LitePal.where("uid=?", userId).find<com.ysxsoft.imtalk.bean.UserInfo>()
            if (beans.isNotEmpty()) {
                val bean = beans[0]
                UserInfo(bean.uid, bean.nikeName, Uri.parse(bean.icon))
            } else {
                UserInfo(userId, "", Uri.parse(""))
            }
        }, true)

        initMessageAndTemplate()
    }

    fun initMessageAndTemplate() {
        RongIM.registerMessageType(PrivateGiftMessage::class.java)
        RongIM.registerMessageTemplate(PrivateGiftProvider())
        RongIM.registerMessageType(PrivateCarMessage::class.java)
        RongIM.registerMessageTemplate(PrivateCarProvider())
        RongIM.registerMessageType(PrivateHeaderMessage::class.java)
        RongIM.registerMessageTemplate(PrivateHeaderProvider())
        //根据PalLobbyFragment注释看需求
        RongIM.registerMessageType(LobbyTextMessage::class.java)
        RongIM.registerMessageTemplate(LobbyTextMessageProvider())
////        为TextMessage注册自定义模板,根据PalLobbyFragment注释选择需求
//        RongIM.registerMessageTemplate(LobbyMessageProvider())

    }

    fun setMyExtensionModule() {
        val moduleList = RongExtensionManager.getInstance().extensionModules
        var defaultModule: IExtensionModule? = null
        if (moduleList != null) {
            for (module in moduleList) {
                if (module is DefaultExtensionModule) {
                    defaultModule = module
                    break
                }
            }
            if (defaultModule != null) {
                RongExtensionManager.getInstance().unregisterExtensionModule(defaultModule)
                RongExtensionManager.getInstance().registerExtensionModule(MyExtensionModule())
            }
        }
    }

    inner class MyReceiveMessageListener : RongIMClient.OnReceiveMessageListener {
        /**
         * 收到消息的处理。
         *
         * @param message 收到的消息实体。
         * @param left    剩余未拉取消息数目。
         * @return 收到消息是否处理完成，true 表示自己处理铃声和后台通知，false 走融云默认处理方式。
         */
        @Override
        override fun onReceived(p0: Message?, p1: Int): Boolean {
            Log.e("---->", p0.toString())
            if (p0 != null) {
                try {
                    saveUserInfo(p0)
                } catch (e: Exception) {
                }
            }
            val intent = Intent("RECEIVEMESSAGE")
            sendBroadcast(intent)
            EventBus.getDefault().post(PalMessageBus(p0))
            return false
        }
    }

    /**
     * 处理消息扩展
     */
    fun saveUserInfo(message: Message) {
        var userInfo: JSONObject? = null
        val messageContent = message!!.getContent()
        if (messageContent is TextMessage) {//文本消息
            val intent = Intent("TEXTMESSAGE")
            sendBroadcast(intent)
            val textMessage = messageContent as TextMessage
            userInfo = JSONObject(textMessage!!.extra)
        } else if (messageContent is ImageMessage) {//图片消息
            val imageMessage = messageContent as ImageMessage
            userInfo = JSONObject(imageMessage!!.extra)
        } else if (messageContent is VoiceMessage) {//语音消息
            val voiceMessage = messageContent as VoiceMessage
            userInfo = JSONObject(voiceMessage!!.extra)
        } else if (messageContent is RichContentMessage) {//图文消息
            val richMessage = messageContent as RichContentMessage
            userInfo = JSONObject(richMessage!!.extra)
        } else if (messageContent is LobbyTextMessage){//交友大厅消息
            val richMessage = messageContent as LobbyTextMessage
            userInfo = JSONObject(richMessage.extra)
        }else{
            Log.d("tag", "onSent-其他消息，自己来判断处理")
        }
        //从扩展字段获取用户信息
        if (userInfo != null) {
            var tempUser = com.ysxsoft.imtalk.bean.UserInfo()
            tempUser.uid = userInfo.optString("uid")              //UID
            tempUser.nikeName = userInfo.optString("nikeName")   //昵称
            tempUser.icon = userInfo.optString("icon")          //头像
            tempUser.sex = userInfo.optString("sex")            //性别
            tempUser.zsl = userInfo.optString("zsl")            //钻数量
            tempUser.save()
            //刷新用户缓存数据(否则聊天内部头像不更新)
            RongIM.getInstance().refreshUserInfoCache(UserInfo(tempUser.uid, tempUser.nikeName, Uri.parse(tempUser.icon)))
        }
    }

    inner class MySendMessageListener : RongIM.OnSendMessageListener {

        /**
         * 消息发送前监听器处理接口（是否发送成功可以从 SentStatus 属性获取）。
         *
         * @param message 发送的消息实例。
         * @return 处理后的消息实例。
         */
        override fun onSend(message: Message?): Message {
            //开发者根据自己需求自行处理逻辑
            val intent = Intent("RECEIVEMESSAGE")
            sendBroadcast(intent)
            val beans = LitePal.where("uid=?", AuthManager.getInstance().currentUserId).find<com.ysxsoft.imtalk.bean.UserInfo>()
            if (beans!!.size > 0) {
                val bean = beans.get(0)
                //将用户信息添加到扩展字段
                val messageContent = message!!.getContent()
                val tempUser = JSONObject()
                tempUser.put("uid", bean.uid)
                tempUser.put("nikeName", bean.nikeName)
                tempUser.put("icon", bean.icon)
                tempUser.put("sex", bean.sex)
                tempUser.put("zsl", bean.zsl)
                if (messageContent is TextMessage) {//文本消息
                    messageContent.extra = tempUser.toString()
                } else if (messageContent is ImageMessage) {//图片消息
                    messageContent.extra = tempUser.toString()
                } else if (messageContent is VoiceMessage) {//语音消息
                    messageContent.extra = tempUser.toString()
                } else if (messageContent is RichContentMessage) {//图文消息
                    messageContent.extra = tempUser.toString()
                } else {
                    Log.d("tag", "onSent-其他消息，自己来判断处理")
                }
            }
            Log.e("---->", message.toString())
            return message!!
        }

        /**
         * 消息在 UI 展示后执行/自己的消息发出后执行,无论成功或失败。
         *
         * @param message              消息实例。
         * @param sentMessageErrorCode 发送消息失败的状态码，消息发送成功 SentMessageErrorCode 为 null。
         * @return true 表示走自己的处理方式，false 走融云默认处理方式。
         */
        override fun onSent(message: Message?, sentMessageErrorCode: RongIM.SentMessageErrorCode?): Boolean {
            Log.e("---->11", message.toString())
            if (message!!.getSentStatus() === Message.SentStatus.FAILED) {
                if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_CHATROOM) {
                    //不在聊天室
                } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_DISCUSSION) {
                    //不在讨论组
                } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_GROUP) {
                    //不在群组
                } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.REJECTED_BY_BLACKLIST) {
                    //你在他的黑名单中
                }
            }

            val messageContent = message!!.getContent()

            if (messageContent is TextMessage) {//文本消息
                val textMessage = messageContent as TextMessage
                Log.d("tag", "onSent-TextMessage:" + textMessage.content)
            } else if (messageContent is ImageMessage) {//图片消息
                val imageMessage = messageContent as ImageMessage
                Log.d("tag", "onSent-ImageMessage:" + imageMessage.remoteUri)
            } else if (messageContent is VoiceMessage) {//语音消息
                val voiceMessage = messageContent as VoiceMessage
                Log.d("tag", "onSent-voiceMessage:" + voiceMessage.uri.toString())
            } else if (messageContent is RichContentMessage) {//图文消息
                val richContentMessage = messageContent as RichContentMessage
                Log.d("tag", "onSent-RichContentMessage:" + richContentMessage.content)
            } else {
                Log.d("tag", "onSent-其他消息，自己来判断处理")
            }
            val intent = Intent("RECEIVEMESSAGE")
            sendBroadcast(intent)
            return false
        }
    }

    /**
     * 获取当前进程的名称
     *
     * @param context
     * @return
     */
    fun getCurProcessName(context: Context): String? {
        val pid = Process.myPid()
        val mActivityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningAppProcessInfoList = mActivityManager.runningAppProcesses
        if (runningAppProcessInfoList == null) {
            return null
        } else {
            val processInfoIterator = runningAppProcessInfoList.iterator()

            var appProcess: ActivityManager.RunningAppProcessInfo
            do {
                if (!processInfoIterator.hasNext()) {
                    return null
                }

                appProcess = processInfoIterator.next() as ActivityManager.RunningAppProcessInfo
            } while (appProcess.pid != pid)

            return appProcess.processName
        }
    }

    /**
     * 获取当前 Application 实例
     *
     * @return
     */
    fun getApplication(): BaseApplication? {
        return instance
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }
}
