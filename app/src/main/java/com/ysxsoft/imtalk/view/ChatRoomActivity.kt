package com.ysxsoft.imtalk.view

import android.bluetooth.BluetoothHeadset
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.support.annotation.NonNull
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPropertyAnimatorListener
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.animation.AccelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.R.id.*
import com.ysxsoft.imtalk.appservice.FloatingDisplayService
import com.ysxsoft.imtalk.bean.CommonBean
import com.ysxsoft.imtalk.bean.HomeHLBean
import com.ysxsoft.imtalk.chatroom.adapter.RoomChatListAdapter
import com.ysxsoft.imtalk.chatroom.im.IMClient
import com.ysxsoft.imtalk.chatroom.im.message.MicPositionControlMessage
import com.ysxsoft.imtalk.chatroom.im.message.RoomEmjMessage
import com.ysxsoft.imtalk.chatroom.im.message.RoomMemberChangedMessage
import com.ysxsoft.imtalk.chatroom.model.DetailRoomInfo
import com.ysxsoft.imtalk.chatroom.model.MicBehaviorType
import com.ysxsoft.imtalk.chatroom.model.MicPositionsBean
import com.ysxsoft.imtalk.chatroom.rtc.RtcClient
import com.ysxsoft.imtalk.chatroom.task.AuthManager
import com.ysxsoft.imtalk.chatroom.task.ResultCallback
import com.ysxsoft.imtalk.chatroom.task.RoomEventListener
import com.ysxsoft.imtalk.chatroom.task.RoomManager
import com.ysxsoft.imtalk.chatroom.task.role.Role
import com.ysxsoft.imtalk.chatroom.utils.DisplayUtils
import com.ysxsoft.imtalk.chatroom.utils.HeadsetPlugReceiver
import com.ysxsoft.imtalk.chatroom.utils.HeadsetUtils
import com.ysxsoft.imtalk.chatroom.widget.MicSeatView
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.*
import com.ysxsoft.imtalk.widget.dialog.*
import io.rong.imkit.RongIM
import io.rong.imlib.IRongCallback
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Message
import kotlinx.android.synthetic.main.activity_chatroom.*
import pl.droidsonroids.gif.GifDecoder
import pl.droidsonroids.gif.GifImageView
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.util.ArrayList

/**
 * Create By 胡
 * on 2019/7/24 0024
 */
class ChatRoomActivity : BaseActivity(), RoomEventListener {
    override fun onRoomEmj(p: Int, url: String) {
        //房间表情
        showPositionEmj(p,url)
    }

    override fun onRoomGift(p: Int, toP: Int, giftUrl: String, staticUrl: String) {
        //房间动画
        showPositionGift(p,toP,giftUrl ,staticUrl)
    }

    override fun onRoomMemberChange(memberCount: Int) {
        showToastMessage("新成员")
        updateRoomInfo()
        updateRoomTitle(detailRoomInfo!!.roomInfo.room_name, detailRoomInfo!!.roomInfo.room_num, memberCount.toString())
    }

    override fun onMicUpdate(micPositionInfoList: MutableList<MicPositionsBean>?) {
        showToastMessage("麦位有更新")
        if (micPositionInfoList != null) {
            updateMicSeatState(micPositionInfoList)
        }
    }

    override fun onRoomMicSpeak(speakUserIdList: MutableList<String>?) {
        updateMicSpeakState(speakUserIdList)
    }

    override fun onMessageEvent(message: Message?) {
        if (chatListAdapter != null) {
            chatMessageList.add(message!!)
            chatListAdapter!!.notifyDataSetChanged()
            chatroom_list_chat.smoothScrollToPosition(chatListAdapter!!.getCount())
        }
    }

    override fun onSendMessageError(message: Message?, errorCode: Int) {

    }

    override fun onRoomBgChanged(bgId: String) {
        updateRoomBg(bgId)
    }

    override fun onRoleChanged(role: Role?) {
        if (role != null) {
            enableVoiceChat(role.hasVoiceChatPermission())
        }
    }

    override fun onKickOffRoom() {
        showToastMessage(R.string.toast_chatroom_kick_off_from_room)
        quiteRoom(SpUtils.getSp(mContext, "uid"), "1")
    }


    override fun onErrorLeaveRoom() {
        showToastMessage(R.string.toast_error_leave_room_because_error)
        // 提示后延迟退出房间
        handler!!.postDelayed(Runnable { quiteRoom(SpUtils.getSp(mContext, "uid"), "1") }, 3000)
    }

    override fun onRoomExistOverTimeLimit() {
        showToastMessage(R.string.toast_room_exist_over_time_limit)
        // 提示后延迟退出房间
        handler!!.postDelayed(Runnable { quiteRoom(SpUtils.getSp(mContext, "uid"), "1") }, 3000)
    }

    companion object {
        fun starChatRoomActivity(mContext: Context, room_id: String) {
            val intent = Intent(mContext, ChatRoomActivity::class.java)
            intent.putExtra("room_id", room_id)
            mContext.startActivity(intent)
        }
    }

    /**
     * 更新麦位说话状态
     *
     * @param speakUserList 当前正在发言的用户 id 列表
     */
    private fun updateMicSpeakState(speakUserList: List<String>?) {
        if (speakUserList == null || speakUserList.size == 0) return
        // 更新房主的说话状态
        val creatorUserId = detailRoomInfo!!.roomInfo.uid

        if (speakUserList.contains(creatorUserId)) {
            chatroom_rv_room_manager_ripple.enableRipple(true)
        }
        // 更新麦位上的说话状态
        for (micSeatView in micSeatViewList!!) {
            val userId = micSeatView.getMicInfo().uid.toString()
            if (!TextUtils.equals("0", userId) && speakUserList.contains(userId)) {
                micSeatView.startRipple()
            }
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_chatroom
    }

    private var chatListAdapter: RoomChatListAdapter? = null
    private val chatMessageList = ArrayList<Message>()
    private var audioManager: AudioManager? = null
    private var telephonyManager: TelephonyManager? = null
    private var roomPhoneStateListener: RoomPhoneStateListener? = null
    private var isMuteMic = false         //麦克风是否静音
    private var headsetPlugReceiver: HeadsetPlugReceiver? = null
    private var handler: Handler? = null
    var room_id: String? = null
    var roomManager: RoomManager? = null
    var detailRoomInfo: DetailRoomInfo? = null
    private var defaultMarginBottom: Int = 0
    var voiceDialog: SongVoiceDialog? = null

    /**
     * 监听来电状态进行房间的静音和禁麦操作
     */
    private inner class RoomPhoneStateListener : PhoneStateListener() {
        private var preMuteMicState = true
        private var preRoomVoiceState = true
        private var isInitCallState = true //用与判断注册监听后的立即回调

        override fun onCallStateChanged(state: Int, incomingNumber: String) {
            super.onCallStateChanged(state, incomingNumber)
            //注册监听时会立即回调状态，所以需要做判断，并不进行处理
            if (isInitCallState) {
                isInitCallState = false
                return
            }
            when (state) {
                TelephonyManager.CALL_STATE_RINGING // 响铃
                -> {
                }
                TelephonyManager.CALL_STATE_OFFHOOK // 接听
                -> {
                    // 记录接听电话前静音和房间声音的状态
                    preMuteMicState = isMuteMic
                    preRoomVoiceState = roomManager!!.isCurrentRoomVoiceEnable()
                    // 静音并关闭房间内的声音
                    setMuteMic(true)
                    enableRoomSound(false)
                }
                TelephonyManager.CALL_STATE_IDLE // 挂断
                -> {
                    // 还原回接听电话前静音及房间声音的状态
                    setMuteMic(preMuteMicState)
                    enableRoomSound(preRoomVoiceState)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        room_id = intent.getStringExtra("room_id")
        // 保持屏幕常亮
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        handler = Handler()
        setLightStatusBar(false)
        initStatusBar(topView)
        setTitle("这是哈哈哈的房间")
        initView()
        initRoom(room_id!!)
        initAudioOutputMode()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initView() {
        img_back.setOnClickListener {
            val homeSettingDialog = HomeSettingDialog(mContext)
            homeSettingDialog.setHomeSettingClickListener(object : HomeSettingDialog.HomeSettingClickListener {
                override fun clickHT() {
//                    ExitCurrentRoom()
                    RandomTalk()
                }

                override fun clickExit() {
                    quiteRoom(SpUtils.getSp(mContext, "uid"), "1")
                }

                override fun clickSmall() {
                    if (FloatingDisplayService.isStarted) {
                        return
                    }
                    if (!Settings.canDrawOverlays(mContext)) {
                        startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")), 1)
                    } else {
                        startService(Intent(this@ChatRoomActivity, FloatingDisplayService::class.java))
                    }
                }

                override fun clickReport() {
                    ReportActivity.starReportActivity(mContext, room_id!!)
                }
            })
            homeSettingDialog.show()
        }

        var a = 0;

        img_share.setOnClickListener {
            val friendDialog = ShareFriendDialog(mContext)
            friendDialog.setShareListener(object : ShareFriendDialog.ShareListener {
                override fun myself() {
//                    showPositionEmj(a, "")
//                    showPositionGift(0,a ,"http://chitchat.rhhhyy.com/uploads/images/20190826/728cb202b8af33ad401cf3984dffe74a.gif","http://chitchat.rhhhyy.com/uploads/images/20190826/f09aa72dd01f87a48fd20283e3570278.png")
//                    showPositionGift(7,a ,"http://chitchat.rhhhyy.com/uploads/images/20190826/1b8e43cd206cbbf8ef9c04cbd2cfc3dd.gif","http://chitchat.rhhhyy.com/uploads/images/20190826/5771f0008c39479aebe64c8b83c7ffb8.png")
                    showPositionGift(5,a ,"http://chitchat.rhhhyy.com/uploads/images/20190827/091c6b8e131a576a5aa81a93e5365236.gif","http://chitchat.rhhhyy.com/uploads/images/20190826/9cad09a166ffae62066263a0ea0a777d.png")
                    a++;
                    if (a == 9) {
                        a = 0;
                    }
                    showToastMessage("站内分享")
                }

                override fun Wechat() {
                    showToastMessage("微信")
                }

                override fun pyq() {
                    showToastMessage("朋友圈")
                }

                override fun qqZone() {
                    showToastMessage("QQ空间")
                }

                override fun QQ() {
                    showToastMessage("QQ")
                }
            })
            friendDialog.show()
        }

        img_head.setOnClickListener {
            //            GiveDialog(mContext, detailRoomInfo!!.roomInfo.uid, room_id!!).show()
        }

        img_right_img.setOnClickListener {
            val homeSettingDialog = HomeSettingDialog(mContext)
            homeSettingDialog.setHomeSettingClickListener(object : HomeSettingDialog.HomeSettingClickListener {
                override fun clickHT() {
                    RandomTalk()
                }

                override fun clickExit() {
                    quiteRoom(SpUtils.getSp(mContext, "uid"), "1")
                }

                override fun clickSmall() {
                    if (FloatingDisplayService.isStarted) {
                        return
                    }
                    if (!Settings.canDrawOverlays(mContext)) {
                        startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")), 1)
                    } else {
                        startService(Intent(this@ChatRoomActivity, FloatingDisplayService::class.java))
                    }
                }

                override fun clickReport() {
                    ReportActivity.starReportActivity(mContext, room_id!!)
                }
            })
            homeSettingDialog.show()
        }

        tv_room_level.setOnClickListener {
            RoomLevelsDialog(mContext, room_id).show()
        }

        tv_music.setOnClickListener {
            voiceDialog = SongVoiceDialog(mContext)
            voiceDialog!!.setSongVoiceListener(object : SongVoiceDialog.SongVoiceListener {
                override fun StopSong() {
                    showToastMessage("停止音乐")
                }

                override fun NextSong() {
                    showToastMessage("下一曲")
                }

                override fun SettingSong() {
//                    startActivity(MySongBookActivity::class.java)
                    MySongBookActivity.starMySongBookActivity(mContext, room_id!!)
                }
            })
            voiceDialog!!.show()
        }

        img_send_msg.setOnClickListener {
            ll_isShow.visibility = View.GONE
            chatroom_et_chat_input.visibility = View.VISIBLE
        }

        chatroom_et_chat_input.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                //业务代码
                val msg = chatroom_et_chat_input.text.toString().trim()
                if (!TextUtils.isEmpty(msg)) {
                    roomManager!!.sendChatRoomMessage(msg);
                }
                hideInputKeyboard();
                chatroom_et_chat_input.setText("");
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        chatroom_setting.setOnClickListener {
            AddRoomActivity.starAddRoomActivity(mContext, room_id!!, tv_title.text.toString())
        }

        chatroom_iv_sound_control.setOnClickListener {
            OnLineActivity.starOnLineActivity(mContext, room_id!!)
        }

        img_gold_egg.setOnClickListener {
            val eggDialog = EggDialog(mContext)
            eggDialog.show()
        }

        img_img_yy.setOnClickListener {
            setMuteMic(!isMuteMic)
        }

        img_simle.setOnClickListener {
            val dialog=SmilDialog(mContext);
            dialog.setOnDialogListener(object : SmilDialog.OnSmileDialogListener{
                override fun onClick(position: Int, url: String) {
                    val message = RoomEmjMessage()
                    message.position=0;//当前用户所在的麦位
                    message.imageUrl=url;
                    val obtain = Message.obtain(room_id, Conversation.ConversationType.CHATROOM, message)
                    RongIMClient.getInstance().sendMessage(obtain, null, null, object : IRongCallback.ISendMessageCallback {
                        override fun onAttached(p0: Message?) {

                        }

                        override fun onSuccess(p0: Message?) {
                        }

                        override fun onError(p0: Message?, p1: RongIMClient.ErrorCode?) {

                        }
                    });

                    //显示当前用户所在的麦位表情
                    showPositionEmj(0,url);
                }
            })
           dialog.show()
        }

        chatroom_iv_mic_control.setOnClickListener {
            enableRoomSound(!roomManager!!.isCurrentRoomVoiceEnable())
        }

        chatroom_gift.setOnClickListener {
            GiftBagDialog(mContext, room_id!!).show()
        }

        //聊天列表
        chatListAdapter = RoomChatListAdapter(this)
        chatroom_list_chat.setAdapter(chatListAdapter)
        //麦位
        setMicSeaViewList()
        // 默认麦克不可用
        enableUseMic(false)
        defaultMarginBottom = DisplayUtils.dp2px(this, 8)

        //启用软件弹出监听
        enableKeyboardStateListener(true)
    }

    //请求嗨聊的接口数据
    private fun RandomTalk() {
        NetWork.getService(ImpService::class.java)
                .homeroomId()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<HomeHLBean> {
                    override fun onError(e: Throwable?) {
                        showToastMessage("随机嗨聊==" + e!!.message.toString())
                    }

                    override fun onNext(t: HomeHLBean?) {
                        if (t!!.code == 0) {
                            room_id = t.data.room_id.toString()
                            joinChatRoom(room_id!!)
                        }
                    }

                    override fun onCompleted() {

                    }
                })

    }

    private fun enableRoomSound(enable: Boolean) {
        if (enable) {
            //启用房间声音
            roomManager!!.enableRoomChatVoice(true, object : ResultCallback<Boolean> {
                override fun onSuccess(aBoolean: Boolean?) {
                    // 每次启用声音时做音频输出切换
                    changeAudioOutputMode()
                    chatroom_iv_mic_control.setImageResource(R.mipmap.img_volume)
                }

                override fun onFail(errorCode: Int) {
                    showToastMessage("启用房间声音失败==" + errorCode)
                }
            })
        } else {
            // 关闭房间声音
            roomManager!!.enableRoomChatVoice(false, object : ResultCallback<Boolean> {
                override fun onSuccess(aBoolean: Boolean?) {
                    chatroom_iv_mic_control.setImageResource(R.mipmap.img_colose_volume)
                }

                override fun onFail(errorCode: Int) {
                    showToastMessage("关闭房间声音失败==" + errorCode)
                }
            })
        }

    }

    private fun initAudioOutputMode() {
        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        // 监听有线耳机连接和断开广播监听
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_HEADSET_PLUG)
        intentFilter.addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED)
        headsetPlugReceiver = HeadsetPlugReceiver()
        HeadsetPlugReceiver.setOnHeadsetPlugListener(object : HeadsetPlugReceiver.OnHeadsetPlugListener {
            override fun onNotifyHeadsetState(connected: Boolean, type: Int) {
                if (connected) {
                    // 蓝牙耳机连接时,且有线耳机没有连接时启用蓝牙耳机通讯
                    if (type == 0 && !HeadsetUtils.isWiredHeadsetOn(this@ChatRoomActivity)) { //蓝牙耳机连接时
                        // 部分蓝牙设备连接后需要一定延迟后才能保证连接
                        handler!!.postDelayed(Runnable {
                            audioManager!!.setBluetoothScoOn(true)
                            audioManager!!.startBluetoothSco()
                            audioManager!!.setSpeakerphoneOn(false)
                        }, 2000)

                    } else if (type == 1) {
                        // 有线耳机连接时关闭蓝牙耳机语音通讯
                        audioManager!!.setBluetoothScoOn(false)
                        audioManager!!.stopBluetoothSco()
                        audioManager!!.setSpeakerphoneOn(false)
                    }
                } else {
                    if (type == 0) { //蓝牙耳机断开时
                        // 关闭蓝牙耳机语音通讯
                        audioManager!!.setBluetoothScoOn(false)
                        audioManager!!.stopBluetoothSco()

                    } else if (type == 1 && HeadsetUtils.hasBluetoothHeadSetConnected()) {
                        // 当有线耳机拔出，蓝牙耳机还在连接时，开启蓝牙耳机通信
                        audioManager!!.setBluetoothScoOn(true)
                        audioManager!!.startBluetoothSco()
                    }

                    // 判断是否当有线耳机拔出或蓝牙耳机断开时，另一个耳机存在，如果存在则不改变输出模式
                    if (HeadsetUtils.isWiredHeadsetOn(this@ChatRoomActivity) || HeadsetUtils.hasBluetoothHeadSetConnected()) {
                        audioManager!!.setSpeakerphoneOn(false)
                    } else {
                        // 当没有耳机连接时，使用外放模式
                        audioManager!!.setSpeakerphoneOn(true)
                    }
                }
            }
        })
        registerReceiver(headsetPlugReceiver, intentFilter)
    }

    private fun initRoom(room_id: String) {
        roomManager = RoomManager.getInstance()
        detailRoomInfo = roomManager!!.getCurrentRoomInfo()

        if (detailRoomInfo == null) {
            finish()
            return
        }
        roomManager!!.setCurrentRoomEventListener(this)

        if ("1".equals(detailRoomInfo!!.roomInfo.room_pure)) {//纯净模式：0 关闭；1 开启
            tv_room_level.visibility = View.GONE
            tv_music.visibility = View.GONE
            img_gold_egg.visibility = View.GONE
            tv_room_manager.visibility = View.GONE
            UpdataTips(detailRoomInfo!!.getMicPositions(), false)
        } else {
            tv_room_level.visibility = View.VISIBLE
            tv_music.visibility = View.VISIBLE
            img_gold_egg.visibility = View.VISIBLE
            tv_room_manager.visibility = View.VISIBLE
            UpdataTips(detailRoomInfo!!.getMicPositions(), true)
        }

        //设置房间背景
        updateRoomBg(detailRoomInfo!!.roomInfo.room_bg)

        //获取进入房间前的消息
        val messageList = detailRoomInfo!!.getMessageList()

        //加入一条自己进入房间的消息
        val message = IMClient.getInstance().createLocalEnterRoomMessage(AuthManager.getInstance().currentUserId, room_id)
        chatMessageList.add(message)

        //设置聊天信息
        chatMessageList.addAll(messageList)
        chatListAdapter!!.setMessages(chatMessageList)
        chatListAdapter!!.notifyDataSetChanged()
        chatroom_list_chat.setSelection(chatListAdapter!!.getCount())

        chatroom_list_chat.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val userid = chatListAdapter!!.messageList.get(position).senderUserId.toString()
                if (TextUtils.isEmpty(userid)) {
                    return
                }
                val msgListDialog = MsgListDialog(mContext, userid, room_id)
                msgListDialog.setOnMsgListDialog(object : MsgListDialog.OnMsgListDialogListener {
                    override fun bMClick() {//闭麦

                    }

                    override fun xMClick() {//下麦旁听

                    }

                    override fun jMClick() {//解锁

                    }

                    override fun bTMClick() {//报Ta上麦
                        UpperWheatDialog(mContext, room_id).show()
                    }
                })
                for (bean in detailRoomInfo!!.roomUserList) {
                    if (SpUtils.getSp(mContext, "uid").equals(bean.uid) && ("1".equals(bean.role) || "2".equals(bean.role))) {
                        msgListDialog.show()
                    }
                }
            }
        })

        //设置标题
        updateRoomTitle(detailRoomInfo!!.roomInfo.room_name, detailRoomInfo!!.roomInfo.room_num, detailRoomInfo!!.roomInfo.memCount.toString())

        img_head.setOnClickListener {
            if (!SpUtils.getSp(mContext, "uid").equals(detailRoomInfo!!.roomInfo.uid)) {
                val giveDialog = GiveDialog(mContext, detailRoomInfo!!.roomInfo.uid, room_id)
                giveDialog.findViewById<LinearLayout>(R.id.ll_isShow).visibility = View.GONE
                giveDialog.setGiveClickListener(object : GiveDialog.GiveClickListener {
                    override fun clickGiveGift() {
                        GiftBagDialog(mContext, room_id).show()
                    }

                    override fun clickPrivateChat() {
                        RongIM.getInstance().startPrivateChat(mContext, detailRoomInfo!!.roomInfo.uid, "标题");
                    }

                    override fun clickGiveZb() {
                    }

                    override fun clickFoucsOn() {
                        FocusOnData(SpUtils.getSp(mContext, "uid"), detailRoomInfo!!.roomInfo.uid, "1")
                    }

                    override fun setManager() {

                    }

                    override fun removeManager() {

                    }

                    override fun setExit() {

                    }

                    override fun blackList() {

                    }
                })
                giveDialog.show()
            }
        }
        val role = roomManager!!.getCurrentRole()
        //更新麦位状态1
        updateMicSeatState(detailRoomInfo!!.getMicPositions())


        // 判断是否可以设置房间
        if (SpUtils.getSp(mContext, "uid").equals(detailRoomInfo!!.roomInfo.uid)) {
            chatroom_setting.setVisibility(View.VISIBLE)
            tv_room.setVisibility(View.VISIBLE)
        } else {
            chatroom_setting.setVisibility(View.GONE)
            tv_room.setVisibility(View.GONE)
        }
        // 初始化语音
        roomManager!!.initRoomVoice(object : ResultCallback<Boolean> {
            override fun onSuccess(result: Boolean?) {
                if (SpUtils.getSp(mContext, "uid").equals(detailRoomInfo!!.roomInfo.uid)) {
                    // 当房主时直接开启语音聊天
                    enableVoiceChat(true)  //话筒
                }
                // 开启房间语音声音
                enableRoomChatVoice(true) // 喇叭
                // 监听来电话状态
                roomPhoneStateListener = RoomPhoneStateListener()
                telephonyManager!!.listen(roomPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
            }

            override fun onFail(errorCode: Int) {
                showToastMessage("初始化语音失败" + errorCode)
            }
        })
    }

    private fun UpdataTips(micPositions: List<MicPositionsBean>, b: Boolean) {
        if (micPositions == null) return
        clearMicState()
        val seatSize = micSeatViewList!!.size

        for (micInfo in micPositions) {
            val position = micInfo.sort - 1
            if (position < seatSize) {
                val micSeatView = micSeatViewList!!.get(position)
                micSeatView.setHeartValue(micInfo.gifts, b)
            }
        }

    }

    /**
     * 设置启动房间聊天声音
     *
     * @param enable
     */
    private fun enableRoomChatVoice(enable: Boolean) {
        roomManager!!.enableRoomChatVoice(enable, object : ResultCallback<Boolean> {
            override fun onSuccess(aBoolean: Boolean?) {
                showToastMessage("设置启动房间聊天声音成功")
            }

            override fun onFail(errorCode: Int) {
                showToastMessage("设置启动房间聊天声音失败" + errorCode)
            }
        })
    }

    /**
     * 设置是否开启语音聊天
     *
     * @param enable
     */
    private fun enableVoiceChat(enable: Boolean) {
        if (enable) {
            roomManager!!.startVoiceChat(object : ResultCallback<Boolean> {
                override fun onSuccess(aBoolean: Boolean?) {
                    enableUseMic(true)
                }

                override fun onFail(errorCode: Int) {
                    showToastMessage("设置是否开启失败" + errorCode)
                }
            })
        } else {
            roomManager!!.stopVoiceChat(object : ResultCallback<Boolean> {
                override fun onSuccess(aBoolean: Boolean?) {
                    enableUseMic(false)
                }

                override fun onFail(errorCode: Int) {
                    showToastMessage("设置是否关闭失败" + errorCode)
                }
            })
        }
    }

    /**
     * 是否可使用麦克
     */
    private fun enableUseMic(isMicEnable: Boolean) {
        img_img_yy.setEnabled(isMicEnable)
        if (isMicEnable) {
            setMuteMic(isMuteMic)
        } else {
            img_img_yy.setImageResource(R.mipmap.img_colose_yy)
        }
    }

    /**
     * 切换当前的麦克状态
     */
    private fun setMuteMic(isMute: Boolean) {
        if (isMute) {
            // 关闭麦克
            RoomManager.getInstance().enableMic(false)
            img_img_yy.setImageResource(R.mipmap.img_colose_yy)
            isMuteMic = true
        } else {
            // 启用麦克
            RoomManager.getInstance().enableMic(true)
            img_img_yy.setImageResource(R.mipmap.img_yy)
            isMuteMic = false
        }
    }

    private fun updateMicSeatState(micPositions: List<MicPositionsBean>) {
        if (micPositions == null) return
        clearMicState()
        val seatSize = micSeatViewList!!.size

        for (micInfo in micPositions) {
            val position = micInfo.sort - 1
            if (position < seatSize) {
                val micSeatView = micSeatViewList!!.get(position)
                micSeatView.updateMicState(micInfo)
            }
        }
    }

    /**
     * 清除麦位状态
     */
    private fun clearMicState() {
        val size = micSeatViewList!!.size
        for (i in 0 until size) {
            val micSeatView = micSeatViewList!!.get(i)
            micSeatView.init(i)
        }
    }

    //社会房主的个人信息
    private fun updataRoomManager() {
        ImageLoadUtil.GlideHeadImageLoad(mContext, detailRoomInfo!!.roomInfo.icon, img_head)
        tv_name.setText(detailRoomInfo!!.roomInfo.nickname)
        when (detailRoomInfo!!.roomInfo.sex) {
            "1" -> {
                img_sex.setImageResource(R.mipmap.img_boy)
            }
            "2" -> {
                img_sex.setImageResource(R.mipmap.img_girl)
            }
        }
    }

    //设置房间标题
    private fun updateRoomTitle(room_name: String?, room_num: String?, memcount: String) {
        updataRoomManager()
        tv_title.setText(room_name)
        tv_lable.setText("#" + detailRoomInfo!!.roomInfo.room_label)
        tv_id.setText("ID：" + room_num + "  " + memcount + "人在线")
        tv_sys_notice.setText("系统公告：" + detailRoomInfo!!.roomInfo.room_notice)
    }

    //设置房间背景
    private fun updateRoomBg(room_bg: String?) {
        ImageLoadUtil.GlideGoodsImageLoad(mContext, room_bg!!, chatroom_layout)
    }

    /**
     * 改变音频输出模式
     */
    private fun changeAudioOutputMode() {
        if (audioManager == null) return
        audioManager!!.setMode(AudioManager.MODE_IN_COMMUNICATION)
        //检测是否有有线耳机或蓝牙耳机连接
        if (HeadsetUtils.hasBluetoothHeadSetConnected()) {
            audioManager!!.startBluetoothSco()
            audioManager!!.setBluetoothScoOn(true)
            audioManager!!.setSpeakerphoneOn(false)
        } else if (HeadsetUtils.isWiredHeadsetOn(this)) {
            audioManager!!.setSpeakerphoneOn(false)
        } else {
            // 设置为外放模式
            audioManager!!.setSpeakerphoneOn(true)
        }
    }

    var micSeatViewList: MutableList<MicSeatView>? = null

    private fun setMicSeaViewList() {
        micSeatViewList = ArrayList<MicSeatView>()
        var micSeatView: MicSeatView
        val micIds = intArrayOf(
                R.id.chatroom_mp_mic_1,
                R.id.chatroom_mp_mic_2,
                R.id.chatroom_mp_mic_3,
                R.id.chatroom_mp_mic_4,
                R.id.chatroom_mp_mic_5,
                R.id.chatroom_mp_mic_6,
                R.id.chatroom_mp_mic_7,
                R.id.chatroom_mp_mic_8)
        for (i in micIds.indices) {
            micSeatView = findViewById(micIds[i])
            micSeatViewList!!.add(micSeatView)
        }
        for (i in micSeatViewList!!.indices) {
            micSeatView = micSeatViewList!!.get(i)
            micSeatView.init(i)
            micSeatView.setOnImageClickListener(object : MicSeatView.OnImageClickListener {
                override fun onImageClick(view: View, position: Int) {
                    doActionToMicSeat(micSeatViewList!!.get(position).micInfo)
                }
            })
        }
    }

    var micPosition = -1
    private fun doActionToMicSeat(targetMicPositionInfo: MicPositionsBean) {
        if (detailRoomInfo == null) {
            //未加入到房间提示
            showToastMessage("未加入到房间")
            return
        }
        micPosition = targetMicPositionInfo.sort    //操作的麦位位置
        val userId = targetMicPositionInfo.uid.toString() //操作的麦位上的用户id
        val is_admin = targetMicPositionInfo.is_admin    //是否管理员：0 否；1 是
        val is_lock_wheat = targetMicPositionInfo.is_lock_wheat //是否锁麦：0 锁麦； 1 已解麦
        val is_wheat = targetMicPositionInfo.is_wheat //	是否上麦 0 下麦；1 上麦
        showToastMessage("麦位位置===" + micPosition)

        val behaviorList = ArrayList<MicBehaviorType>()   //可执行的麦位操作列表
        val behaviorNameList = ArrayList<String>()              //可执行的麦位操作的名称列表
        val currentRole = RoomManager.getInstance().currentRole
        behaviorList.addAll(currentRole.getBehaviorList(micPosition))

        // 显示根据当前可操作行为显示对话框进行操作选择
        for (behaviorType in behaviorList) {
            behaviorNameList.add(behaviorType.getName(BaseApplication.mContext))
        }

        if (detailRoomInfo!!.roomInfo.uid.equals(SpUtils.getSp(mContext, "uid"))) {//房主
            if ("0".equals(is_lock_wheat)) {//锁麦  显示解麦
                val seatMicDialog = UpperSeatMicDialog(mContext)
                seatMicDialog.findViewById<TextView>(R.id.tv_sm).visibility = View.GONE
                seatMicDialog.findViewById<TextView>(R.id.tv_btsm).visibility = View.GONE
                seatMicDialog.findViewById<TextView>(R.id.tv_close_mic).setText("解麦")
                seatMicDialog.setOnDialogClickListener(object : UpperSeatMicDialog.OnDialogClickListener {
                    override fun SMclick(view: TextView) {

                    }

                    override fun BTSMclick(view: TextView) {
                        val intent = Intent(mContext, OnLineActivity::class.java)
                        intent.putExtra("roomId", detailRoomInfo!!.roomInfo.room_id)
                        startActivityForResult(intent, 1033)
                    }

                    override fun CSMclick(view: TextView) {//锁麦

                    }

                    override fun BMclick(view: TextView) {//闭麦

                    }
                })
                seatMicDialog.show()
            } else { // 已接麦  显示锁麦
                if (TextUtils.equals("0", userId)) {//房主点击 空麦位
                    val seatMicDialog = UpperSeatMicDialog(mContext)
                    seatMicDialog.findViewById<TextView>(R.id.tv_sm).visibility = View.GONE
                    seatMicDialog.setOnDialogClickListener(object : UpperSeatMicDialog.OnDialogClickListener {
                        override fun SMclick(view: TextView) {

                        }

                        override fun BTSMclick(view: TextView) {
                            val intent = Intent(mContext, OnLineActivity::class.java)
                            intent.putExtra("roomId", detailRoomInfo!!.roomInfo.room_id)
                            startActivityForResult(intent, 1033)
                        }

                        override fun CSMclick(view: TextView) {//锁麦

                        }

                        override fun BMclick(view: TextView) {//闭麦

                        }
                    })
                    seatMicDialog.show()
                } else {//房主点击 麦位有人
                    val giveDialog = GiveDialog(mContext, userId, room_id!!)
                    val tv_setting_manager = giveDialog.findViewById<TextView>(R.id.tv_setting_manager)
                    val tv_move_manager = giveDialog.findViewById<TextView>(R.id.tv_move_manager)
                    val zw = giveDialog.findViewById<TextView>(R.id.zw)
                    if ("1".equals(is_admin)) {
                        tv_setting_manager.visibility = View.GONE
                        tv_move_manager.visibility = View.VISIBLE
                        zw.visibility = View.INVISIBLE
                    } else {
                        tv_setting_manager.visibility = View.VISIBLE
                        tv_move_manager.visibility = View.GONE
                        zw.visibility = View.INVISIBLE
                    }
                    giveDialog.setGiveClickListener(object : GiveDialog.GiveClickListener {
                        override fun removeManager() {
                            RemoveManager(userId, room_id!!, "2")
                        }

                        override fun setManager() {
                            BlackManager(userId, room_id!!, "2")
                        }

                        override fun setExit() {
                            ExitCurrentRoom(userId, "2", room_id!!)
                        }

                        override fun blackList() {
                            BlackManager(userId, room_id!!, "1")
                        }

                        override fun clickGiveGift() {
                            GiftBagDialog(mContext, room_id!!).show()
                        }

                        override fun clickPrivateChat() {
                            RongIM.getInstance().startPrivateChat(mContext, userId, "标题");
                        }

                        override fun clickGiveZb() {
                            showToastMessage("装扮")
                        }

                        override fun clickFoucsOn() {
                            FocusOnData(SpUtils.getSp(mContext, "uid"), userId, "1")
                        }
                    })
                    giveDialog.show()
                }
            }
        } else { //非房主  点击控麦位
            if ("0".equals(userId)) {
                val seatMicDialog = UpperSeatMicDialog(mContext)
                seatMicDialog.findViewById<TextView>(R.id.tv_btsm).visibility = View.GONE
                seatMicDialog.findViewById<TextView>(R.id.tv_close_mic).visibility = View.GONE
                seatMicDialog.findViewById<TextView>(R.id.tv_bm).visibility = View.GONE
                seatMicDialog.setOnDialogClickListener(object : UpperSeatMicDialog.OnDialogClickListener {
                    override fun SMclick(view: TextView) {
                        JoinMic(SpUtils.getSp(mContext, "uid"), micPosition)
                    }

                    override fun BTSMclick(view: TextView) {
                        OnLineActivity.starOnLineActivity(mContext, detailRoomInfo!!.roomInfo.room_id)
                    }

                    override fun CSMclick(view: TextView) {

                    }

                    override fun BMclick(view: TextView) {

                    }

                })
                seatMicDialog.show()
            } else { //非房主
                if ((SpUtils.getSp(mContext, "uid")).equals(userId)) { //麦位是自己
                    val seatMicDialog = UpperSeatMicDialog(mContext)
                    seatMicDialog.findViewById<TextView>(R.id.tv_btsm).visibility = View.GONE
                    seatMicDialog.findViewById<TextView>(R.id.tv_close_mic).visibility = View.GONE
                    seatMicDialog.findViewById<TextView>(R.id.tv_sm).setText("下麦")
                    seatMicDialog.setOnDialogClickListener(object : UpperSeatMicDialog.OnDialogClickListener {
                        override fun SMclick(view: TextView) {
                            ExitMic(userId, micPosition)
                        }

                        override fun BTSMclick(view: TextView) {
                            OnLineActivity.starOnLineActivity(mContext, detailRoomInfo!!.roomInfo.room_id)
                        }

                        override fun CSMclick(view: TextView) {

                        }

                        override fun BMclick(view: TextView) {//闭麦

                        }

                    })
                    seatMicDialog.show()
                } else { //麦位有人  切不是自己
                    if (!"0".equals(userId) && !(SpUtils.getSp(mContext, "uid")).equals(userId)) {
                        val giveDialog = GiveDialog(mContext, userId, room_id!!)
                        giveDialog.findViewById<LinearLayout>(R.id.ll_isShow).visibility = View.GONE
                        giveDialog.setGiveClickListener(object : GiveDialog.GiveClickListener {
                            override fun clickGiveGift() {
                                GiftBagDialog(mContext, room_id!!).show()
                            }

                            override fun clickPrivateChat() {
                                RongIM.getInstance().startPrivateChat(mContext, userId, "标题");
                            }

                            override fun clickGiveZb() {
                                showToastMessage("送装扮")
                            }

                            override fun clickFoucsOn() {
                                FocusOnData(SpUtils.getSp(mContext, "uid"), userId, "1")
                            }

                            override fun setManager() {
                            }

                            override fun removeManager() {
                            }

                            override fun setExit() {
                            }

                            override fun blackList() {
                            }
                        })
                        giveDialog.show()

                    }
                }
            }
        }
    }

    //踢出房间
    private fun ExitCurrentRoom(userId: String, leave: String, room_id: String) {
        NetWork.getService(ImpService::class.java)
                .tCRoom(userId, leave, room_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CommonBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: CommonBean?) {
                        showToastMessage(t!!.msg)
                        if (t.code == 0) {
                            updateRoomInfo()
                            val message = RoomMemberChangedMessage()
                            message.setCmd(3)//离开房间
                            message.targetUserId = userId
                            message.targetPosition = -1
                            val obtain = Message.obtain(room_id, Conversation.ConversationType.CHATROOM, message)
                            RongIMClient.getInstance().sendMessage(obtain, null, null, object : IRongCallback.ISendMessageCallback {
                                override fun onAttached(p0: Message?) {

                                }

                                override fun onSuccess(p0: Message?) {
                                }

                                override fun onError(p0: Message?, p1: RongIMClient.ErrorCode?) {

                                }
                            });

                        }
                    }

                    override fun onCompleted() {
                    }
                })

    }

    /**
     * 关注
     */
    private fun FocusOnData(sp: String, userId: String, s: String) {
        val map = HashMap<String, String>()
        map.put("uid", sp)
        map.put("fs_id", userId)
        map.put("flag", "1")
//        val body = RetrofitUtil.createJsonRequest(map)
        NetWork.getService(ImpService::class.java)
                .fans(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CommonBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: CommonBean?) {
                        showToastMessage(t!!.msg)
                        if (t.code == 0) {
                            updateRoomInfo()
                        }
                    }

                    override fun onCompleted() {
                    }
                })


    }

    /**
     * 移除管理员
     */
    private fun RemoveManager(userId: String, room_id: String, s2: String) {
        NetWork.getService(ImpService::class.java)
                .DelBlack1(userId, room_id, s2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CommonBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: CommonBean?) {
                        showToastMessage(t!!.msg)
                        if (t.code == 0) {
                            updateRoomInfo()
                        }
                    }

                    override fun onCompleted() {
                    }
                })


    }

    //设置管理员和添加黑名单
    private fun BlackManager(userId: String, room_id: String, type: String) {
        NetWork.getService(ImpService::class.java)
                .addBlack(userId, room_id, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CommonBean> {
                    override fun onError(e: Throwable?) {
                        Log.d("tag", "设置/黑名单" + e!!.message.toString())
                    }

                    override fun onNext(t: CommonBean?) {
                        showToastMessage(t!!.msg)
                        if (t.code == 0) {
                            updateRoomInfo()
                        }
                    }

                    override fun onCompleted() {
                    }
                })

    }

    /**
     * 更新房间信息
     */
    private fun updateRoomInfo() {
        roomManager!!.getRoomDetailInfo(room_id, object : ResultCallback<DetailRoomInfo> {
            override fun onSuccess(roomDetailInfo: DetailRoomInfo?) {
                if (roomDetailInfo != null) {
                    detailRoomInfo = roomDetailInfo
                    detailRoomInfo!!.roomInfo.setMemCount(roomDetailInfo.roomInfo.getMemCount())
                    updateMicSeatState(roomDetailInfo.micPositions)
                    updateRoomTitle(roomDetailInfo.roomInfo.room_name, roomDetailInfo.roomInfo.room_num, detailRoomInfo!!.roomInfo.memCount.toString())
                    //设置房主信息
//                    updataRoomManager()
//                    if ("1".equals(detailRoomInfo!!.roomInfo.room_pure)) {//纯净模式：0 关闭；1 开启
//                        UpdataTips(detailRoomInfo!!.getMicPositions(), false)
//                    } else {
//                        UpdataTips(detailRoomInfo!!.getMicPositions(), true)
//                    }
                }
            }

            override fun onFail(errorCode: Int) {
                showToastMessage("更新房间失败==" + errorCode)
            }
        })
    }

    /**
     * 退出房间
     */
    private fun quiteRoom(uid: String, kick: String) {
        NetWork.getService(ImpService::class.java)
                .tCRoom(uid, kick, room_id!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CommonBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: CommonBean?) {
                        showToastMessage(t!!.msg)
                        if (t.code == 0) {
//                            IMClient.getInstance().quitChatRoom(room_id, null)
                            RtcClient.getInstance().quitRtcRoom(room_id, null)
                            val message = RoomMemberChangedMessage()
                            message.setCmd(2)
                            message.targetUserId = uid
                            message.targetPosition = -1
                            val obtain = Message.obtain(room_id, Conversation.ConversationType.CHATROOM, message)

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

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            if (!Settings.canDrawOverlays(this)) {
                showToastMessage("授权失败")
            } else {
                startService(Intent(this@ChatRoomActivity, FloatingDisplayService::class.java))
                finish()
            }
        }
        if (requestCode == 1033 && resultCode == 1035) {
            val userId = data!!.getStringExtra("userId")
            JoinMic(userId, micPosition)
        }
    }

    /**
     * 加入麦位
     */
    private fun JoinMic(userId: String, mic: Int) {
        NetWork.getService(ImpService::class.java)
                .upDownWheat(userId, room_id!!, mic.toString(), "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CommonBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: CommonBean?) {
                        if (t!!.code == 0) {
                            updateRoomInfo()
                            enableVoiceChat(true)

                            val controlMessage = MicPositionControlMessage()
                            controlMessage.setCmd(6)//上麦
                            controlMessage.targetUserId = userId
                            controlMessage.micPositions = detailRoomInfo!!.micPositions
                            controlMessage.targetPosition = mic

                            val obtain = Message.obtain(room_id, Conversation.ConversationType.CHATROOM, controlMessage)

                            RongIMClient.getInstance().sendMessage(obtain, null, null, object : IRongCallback.ISendMessageCallback {
                                override fun onAttached(p0: Message?) {

                                }

                                override fun onSuccess(p0: Message?) {
                                }

                                override fun onError(p0: Message?, p1: RongIMClient.ErrorCode?) {

                                }
                            });
                        } else {
                            updateRoomInfo()
                            showToastMessage(t.msg)
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }

    /**
     * 下麦
     */
    private fun ExitMic(userId: String, mic: Int) {
        NetWork.getService(ImpService::class.java)
                .upDownWheat(userId, room_id!!, mic.toString(), "2")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CommonBean> {
                    override fun onError(e: Throwable?) {
                        showToastMessage("抱他上麦加入麦位失败==" + e!!.message.toString())
                    }

                    override fun onNext(t: CommonBean?) {
                        if (t!!.code == 0) {
                            updateRoomInfo()

                            val controlMessage = MicPositionControlMessage()
                            controlMessage.setCmd(7)//下麦
                            controlMessage.targetUserId = userId
                            controlMessage.micPositions = detailRoomInfo!!.micPositions
                            controlMessage.targetPosition = mic

                            val obtain = Message.obtain(room_id, Conversation.ConversationType.CHATROOM, controlMessage)

                            RongIMClient.getInstance().sendMessage(obtain, null, null, object : IRongCallback.ISendMessageCallback {
                                override fun onAttached(p0: Message?) {

                                }

                                override fun onSuccess(p0: Message?) {
                                }

                                override fun onError(p0: Message?, p1: RongIMClient.ErrorCode?) {

                                }
                            });

                        } else {
                            showToastMessage(t.msg)
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }


    private fun joinChatRoom(roomId: String) {
        RoomManager.getInstance().joinRoom(SpUtils.getSp(mContext, "uid"), roomId, object : ResultCallback<DetailRoomInfo> {
            override fun onSuccess(result: DetailRoomInfo?) {
                initRoom(room_id!!)
                initAudioOutputMode()
            }

            override fun onFail(errorCode: Int) {

            }
        })
    }

    /**
     * 隐藏键盘
     */
    fun hideInputKeyboard() {
        val currentFocus = currentFocus
        if (currentFocus != null) {
            ll_isShow.visibility = View.VISIBLE
            chatroom_et_chat_input.visibility = View.GONE

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }

    override fun onKeyboardStateChanged(isShown: Boolean, height: Int) {
        /*
         * 监听软键盘弹出，当有软键盘时使输入框加入软键盘等高的间距
         */
        val layoutParams = chatroom_ll_input_container.getLayoutParams() as RelativeLayout.LayoutParams

        if (isShown) {
            layoutParams.bottomMargin = height + defaultMarginBottom
        } else {
            layoutParams.bottomMargin = defaultMarginBottom
        }
        chatroom_ll_input_container.setLayoutParams(layoutParams)
    }

    override fun onBackPressed() {
        val homeSettingDialog = HomeSettingDialog(mContext)
        homeSettingDialog.setHomeSettingClickListener(object : HomeSettingDialog.HomeSettingClickListener {
            override fun clickHT() {
                RandomTalk()
            }

            override fun clickExit() {
                quiteRoom(SpUtils.getSp(mContext, "uid"), "1")
            }

            @RequiresApi(Build.VERSION_CODES.M)
            override fun clickSmall() {
                if (FloatingDisplayService.isStarted) {
                    return
                }
                if (!Settings.canDrawOverlays(mContext)) {
                    startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")), 1)
                } else {
                    startService(Intent(this@ChatRoomActivity, FloatingDisplayService::class.java))
                }
            }

            override fun clickReport() {
                ReportActivity.starReportActivity(mContext, room_id!!)
            }
        })
        homeSettingDialog.show()
    }

    override fun onResume() {
        super.onResume()
        /*
       * 当用户切出应用时可能会有其他应用更改音频的输出模式
       * 所以当应用切回前台时切换音频输出模式
       */
        changeAudioOutputMode()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(headsetPlugReceiver)

        if (audioManager != null) {
            audioManager!!.setMode(AudioManager.MODE_NORMAL)
        }
        if (telephonyManager != null && roomPhoneStateListener != null) {
            telephonyManager!!.listen(roomPhoneStateListener, TelephonyManager.PHONE_TYPE_NONE)
        }
    }

    fun showPositionEmj(position: Int, emjGifUrl: String) {
        var emjGifUrl = emjGifUrl
//        emjGifUrl = "http://chitchat.rhhhyy.com/uploads/images/20190903/f5d5ee9871ffbd2c422e4f436a72181e.gif"
        val viewMap = java.util.HashMap<Int, View>()

        val f = findViewById<FrameLayout>(android.R.id.content)
        if (viewMap.containsKey(position)) {
            //正在显示
            val view = viewMap[position]
            val showingView = f.findViewById<View>(view!!.id)
            if (showingView != null) {
                f.removeView(showingView)
            }
        } else {
            //结束显示
        }
        val w = chatroom_mp_mic_1.width;
        val h = chatroom_mp_mic_1.height;

        val imgW = DisplayUtils.dp2px(mContext, 44);
        val imgH = DisplayUtils.dp2px(mContext, 44);

        val offsetX = w / 2 - imgW / 2;
        val offsetY = h / 2 - imgH / 2;

        val location = getPosition(position);

        val locationX = location[0]
        val locationY = location[1]
        val x = locationX
        val y = locationY

        val v = View.inflate(mContext, R.layout.view_emj, null)
        val layoutParams = FrameLayout.LayoutParams(DisplayUtils.dp2px(mContext, 44), DisplayUtils.dp2px(mContext, 44))
        v.layoutParams = layoutParams

        v.id = position
        val imageView = v.findViewById<ImageView>(R.id.gifView)

        Glide.with(this).asGif().listener(object : RequestListener<GifDrawable> {
            override fun onLoadFailed(e: GlideException?, model: Any, target: Target<GifDrawable>, isFirstResource: Boolean): Boolean {
                return false
            }

            override fun onResourceReady(resource: GifDrawable, model: Any, target: Target<GifDrawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
//                resource.setLoopCount(2)
//                try {
//                    val duration = 0
//                    // 计算动画时长
//                    //GifDecoder decoder = gifDrawable.getDecoder();//4.0开始没有这个方法了
//                    val state = resource.constantState
//                    if (state != null) {
//                        //不能混淆GifFrameLoader和GifState类
//                        val gifFrameLoader = getValue(state, "frameLoader")
//                        if (gifFrameLoader != null) {
//                            val decoder = getValue(gifFrameLoader, "gifDecoder")
//                            if (decoder != null && decoder is GifDecoder) {
//                                val d = decoder.duration
//                                Handler(Looper.getMainLooper()).postDelayed({
//                                    //移除view
//                                    val f = findViewById<FrameLayout>(android.R.id.content)
//                                    if (viewMap.containsKey(position)) {
//                                        val view = viewMap[position]
//                                        val showingView = f.findViewById<View>(view!!.id)
//                                        if (showingView != null) {
//                                            f.removeView(showingView)
//                                            viewMap.remove(position)
//                                        }
//                                    }
//                                }, (d * 2 * 1000).toLong())
//                            }
//                        }
//                    }
//                } catch (e: Throwable) {
//                    Log.e("tag",e.message)
//                    e.printStackTrace()
//                }
//                return false
                try {
                    val gifStateField = GifDrawable::class.java.getDeclaredField("state")
                    gifStateField.isAccessible = true
                    val gifStateClass = Class.forName("com.bumptech.glide.load.resource.gif.GifDrawable\$GifState")
                    val gifFrameLoaderField = gifStateClass.getDeclaredField("frameLoader")
                    gifFrameLoaderField.isAccessible = true
                    val gifFrameLoaderClass = Class.forName("com.bumptech.glide.load.resource.gif.GifFrameLoader")
                    val gifDecoderField = gifFrameLoaderClass.getDeclaredField("gifDecoder")
                    gifDecoderField.isAccessible = true
                    val gifDecoderClass = Class.forName("com.bumptech.glide.gifdecoder.GifDecoder")
                    val gifDecoder = gifDecoderField.get(gifFrameLoaderField.get(gifStateField.get(resource)))
                    val getDelayMethod = gifDecoderClass.getDeclaredMethod("getDelay", Int::class.javaPrimitiveType!!)
                    getDelayMethod.isAccessible = true
                    //设置只播放一次
                    resource.setLoopCount(1)
                    //获得总帧数
                    val count = resource.frameCount
                    var delay = 0
                    for (i in 0 until count) {
                        //计算每一帧所需要的时间进行累加
                        delay += getDelayMethod.invoke(gifDecoder, i) as Int
                    }
                    imageView.postDelayed({
                        ViewCompat.animate(imageView).scaleX(0f).scaleY(0f).alpha(0f).setDuration(300).setListener(object : ViewPropertyAnimatorListener {
                            override fun onAnimationStart(view: View) {

                            }

                            override fun onAnimationEnd(view: View) {
                                //动画结束后隐藏
                                val f = findViewById<FrameLayout>(android.R.id.content)
                                if (viewMap.containsKey(position)) {
                                    val view = viewMap[position]
                                    val showingView = f.findViewById<View>(view!!.id)
                                    if (showingView != null) {
                                        f.removeView(showingView)
                                        viewMap.remove(position)
                                    }
                                }
                            }

                            override fun onAnimationCancel(view: View) {

                            }
                        })
                    }, 1 * delay.toLong())
                } catch (e: NoSuchFieldException) {
                    e.printStackTrace()
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                } catch (e: NoSuchMethodException) {
                    e.printStackTrace()
                } catch (e: InvocationTargetException) {
                    e.printStackTrace()
                }
                return false
            }
        }).diskCacheStrategy(DiskCacheStrategy.RESOURCE).load(emjGifUrl).into(imageView)
        v.x = x.toFloat() + offsetX;
        v.y = y.toFloat() + offsetY;
        f.addView(v)

        viewMap.put(position, v);
    }

    fun showPositionGift(position:Int,toPosition:Int,giftImgUrl: String,staticImgUrl: String){
        val f = findViewById<FrameLayout>(android.R.id.content)
        val v = View.inflate(mContext, R.layout.view_gift, null)
        val giftImageView = v.findViewById<ImageView>(R.id.giftView)

        val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        v.layoutParams = layoutParams
        f.addView(v)
        Glide.with(this).asGif().listener(object : RequestListener<GifDrawable> {
            override fun onLoadFailed(e: GlideException?, model: Any, target: Target<GifDrawable>, isFirstResource: Boolean): Boolean {
                return false
            }

            override fun onResourceReady(resource: GifDrawable, model: Any, target: Target<GifDrawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                try {
                    val gifStateField = GifDrawable::class.java.getDeclaredField("state")
                    gifStateField.isAccessible = true
                    val gifStateClass = Class.forName("com.bumptech.glide.load.resource.gif.GifDrawable\$GifState")
                    val gifFrameLoaderField = gifStateClass.getDeclaredField("frameLoader")
                    gifFrameLoaderField.isAccessible = true
                    val gifFrameLoaderClass = Class.forName("com.bumptech.glide.load.resource.gif.GifFrameLoader")
                    val gifDecoderField = gifFrameLoaderClass.getDeclaredField("gifDecoder")
                    gifDecoderField.isAccessible = true
                    val gifDecoderClass = Class.forName("com.bumptech.glide.gifdecoder.GifDecoder")
                    val gifDecoder = gifDecoderField.get(gifFrameLoaderField.get(gifStateField.get(resource)))
                    val getDelayMethod = gifDecoderClass.getDeclaredMethod("getDelay", Int::class.javaPrimitiveType!!)
                    getDelayMethod.isAccessible = true
                    //设置只播放一次
                    resource.setLoopCount(1)
                    //获得总帧数
                    val count = resource.frameCount
                    var delay = 0
                    for (i in 0 until count) {
                        //计算每一帧所需要的时间进行累加
                        delay += getDelayMethod.invoke(gifDecoder, i) as Int
                    }
                    giftImageView.postDelayed({
                        ViewCompat.animate(giftImageView).scaleX(0f).scaleY(0f).alpha(0f).setDuration(300).setListener(object : ViewPropertyAnimatorListener {
                            override fun onAnimationStart(view: View) {

                            }

                            override fun onAnimationEnd(view: View) {
                                //动画结束后删除大礼物图
                                f.removeView(v)
                                val w = chatroom_mp_mic_1.width;
                                val h = chatroom_mp_mic_1.height;
                                val imgW = DisplayUtils.dp2px(mContext, 44);
                                val imgH = DisplayUtils.dp2px(mContext, 44);
                                val imageOffsetX = w / 2 - imgW / 2;
                                val imageOffsetY = h / 2 - imgH / 2;

                                val startPosition=getPosition(position);
                                val endPosition=getPosition(toPosition);
                                val offsetX=endPosition[0]-startPosition[0];
                                val offsetY=endPosition[1]-startPosition[1];

                                val f = findViewById<FrameLayout>(android.R.id.content)
                                val childView = View.inflate(mContext, R.layout.view_emj, null)
                                val childImageView = childView.findViewById<ImageView>(R.id.gifView)
                                Glide.with(mContext).load(staticImgUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(childImageView)

                                childView.x = (startPosition[0] +imageOffsetX).toFloat();
                                childView.y = (startPosition[1] +imageOffsetY).toFloat();

                                val layoutParams = FrameLayout.LayoutParams(DisplayUtils.dp2px(mContext, 44), DisplayUtils.dp2px(mContext, 44))
                                childView.layoutParams = layoutParams
                                f.addView(childView)

                                Log.e("tag","x:"+offsetX.toFloat()+" y:"+offsetY.toFloat())
                                ViewCompat.animate(childView).translationXBy(offsetX.toFloat()).translationYBy(offsetY.toFloat()).setDuration(1000).setListener(object : ViewPropertyAnimatorListener {
                                    override fun onAnimationStart(view: View) {
                                    }

                                    override fun onAnimationEnd(view: View) {
                                        //动画结束后删除大礼物图
                                        f.removeView(childView)
                                    }

                                    override fun onAnimationCancel(view: View) {
                                    }
                                })
                            }

                            override fun onAnimationCancel(view: View) {

                            }
                        })
                    }, delay.toLong())
                } catch (e: NoSuchFieldException) {
                    e.printStackTrace()
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                } catch (e: NoSuchMethodException) {
                    e.printStackTrace()
                } catch (e: InvocationTargetException) {
                    e.printStackTrace()
                }
                return false
            }
        }).diskCacheStrategy(DiskCacheStrategy.RESOURCE).load(giftImgUrl).into(giftImageView)
    }
    /**
     * 获取坐标
     */
    fun getPosition(p: Int): IntArray {
        val pos = IntArray(2)
        when (p) {
            0 -> {chatroom_mp_mic_1!!.getLocationOnScreen(pos)}
            1 -> {chatroom_mp_mic_2.getLocationOnScreen(pos)}
            2 -> {chatroom_mp_mic_3.getLocationOnScreen(pos)}
            3 -> {chatroom_mp_mic_4.getLocationOnScreen(pos)}
            4 -> {chatroom_mp_mic_5.getLocationOnScreen(pos)}
            5 -> {chatroom_mp_mic_6.getLocationOnScreen(pos)}
            6 -> {chatroom_mp_mic_7.getLocationOnScreen(pos)}
            7 -> {chatroom_mp_mic_8.getLocationOnScreen(pos)}
            8 -> {img_head.getLocationOnScreen(pos)}
        }
        return pos
    }
}
