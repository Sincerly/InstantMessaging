package com.ysxsoft.imtalk.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.app.Activity
import android.bluetooth.BluetoothHeadset
import android.content.*
import android.graphics.Color
import android.graphics.PixelFormat
import android.media.AudioManager
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPropertyAnimatorListener
import android.support.v7.widget.AppCompatSeekBar
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.opensource.svgaplayer.SVGAImageView
import com.opensource.svgaplayer.SVGAParser
import com.opensource.svgaplayer.SVGAVideoEntity
import com.umeng.socialize.ShareAction
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.media.UMImage
import com.umeng.socialize.media.UMWeb
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.appservice.PlayMusicService
import com.ysxsoft.imtalk.bean.*
import com.ysxsoft.imtalk.chatroom.adapter.RoomChatListAdapter
import com.ysxsoft.imtalk.chatroom.adapter.RoomChatListAdapter.VIEW_TYPE_EGG
import com.ysxsoft.imtalk.chatroom.adapter.RoomChatListAdapter.VIEW_TYPE_GIFT
import com.ysxsoft.imtalk.chatroom.im.IMClient
import com.ysxsoft.imtalk.chatroom.im.message.*
import com.ysxsoft.imtalk.chatroom.model.DetailRoomInfo
import com.ysxsoft.imtalk.chatroom.model.MicPositionsBean
import com.ysxsoft.imtalk.chatroom.net.retrofit.RetrofitUtil
import com.ysxsoft.imtalk.chatroom.rtc.RtcClient
import com.ysxsoft.imtalk.chatroom.task.AuthManager
import com.ysxsoft.imtalk.chatroom.task.ResultCallback
import com.ysxsoft.imtalk.chatroom.task.RoomEventListener
import com.ysxsoft.imtalk.chatroom.task.RoomManager
import com.ysxsoft.imtalk.chatroom.task.role.Linker
import com.ysxsoft.imtalk.chatroom.task.role.Listener
import com.ysxsoft.imtalk.chatroom.task.role.Role
import com.ysxsoft.imtalk.chatroom.utils.DisplayUtils
import com.ysxsoft.imtalk.chatroom.utils.HeadsetPlugReceiver
import com.ysxsoft.imtalk.chatroom.utils.HeadsetUtils
import com.ysxsoft.imtalk.chatroom.widget.MicSeatView
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.music.AudioUtils
import com.ysxsoft.imtalk.music.CustomeWindow
import com.ysxsoft.imtalk.utils.*
import com.ysxsoft.imtalk.widget.dialog.*
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.FileCallBack
import io.rong.imkit.RongIM
import io.rong.imlib.IRongCallback
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Message
import kotlinx.android.synthetic.main.activity_chatroom.*
import kotlinx.android.synthetic.main.view_input_num.*
import okhttp3.Call
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.w3c.dom.Text
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.lang.reflect.InvocationTargetException
import kotlin.collections.HashMap
import java.net.URL
import java.util.ArrayList

/**
 * Create By 胡
 * on 2019/7/24 0024
 */
class ChatRoomActivity : BaseActivity(), RoomEventListener {


    override fun setManager(uid: String?, cmd: String?) {
        Log.d("》》》》", "设置管理员成功=====" + uid)
        AdminData()
        updateRoomInfo()
    }

    override fun onGoldMessage(nickname: String?, giftName: String?, goldNum: String?) {
        if (giftEggManager!! != null) {
            val d = NotifyManager.Data()
            d.nickName = nickname;
            d.giftName = giftName;
            d.goldNum = goldNum;
            giftEggManager!!.addData(d)
            giftEggManager!!.start()
        }
    }

    override fun onGiftMessage(roomPublicGiftMessageBean: RoomPublicGiftMessageBean?) {
        //送礼物超过一定公屏消息
        if (giftNotifyManager!! != null) {
            giftNotifyManager!!.addData(roomPublicGiftMessageBean)
            giftNotifyManager!!.start()
        }
        roomManager!!.getRoomDetailInfo1(room_id,object :ResultCallback<DetailRoomInfo>{
            override fun onSuccess(result: DetailRoomInfo?) {
               if (result!=null){
                   if ("0".equals(result.roomInfo.room_gift_tx)){
                       UpdataTips(result.micPositions!!, false)
                   }else{
                       tv_room_manager.setText(result.roomInfo.gifts)
                       UpdataTips(result.micPositions!!, true)
                   }
               }
            }

            override fun onFail(errorCode: Int) {

            }
        })

    }

    override fun onIsLock(isLock: String?, isFair: String?, isPure: String?) {
        fair = isFair
        //房间是否加锁  是否纯净模式  是否开启公屏
        isLockFair(isLock, isFair, isPure)
    }

    override fun onGiftValue(cmd: Int, micPositionInfoList: MutableList<MicPositionsBean>?, houseOwnerValue: String?) {
        //礼物值
        if (cmd == 0) {//关闭
            tv_room_manager.visibility = View.GONE
            UpdataTips(micPositionInfoList!!, false)
        } else {
            tv_room_manager.visibility = View.VISIBLE
            tv_room_manager.setText(houseOwnerValue)
            UpdataTips(micPositionInfoList!!, true)
        }
    }

    override fun onRoomName(room_name: String?) {
        //房间名称
        updataTitle(room_name)
    }

    override fun onRoomLable(room_lable: String?) {
        // 房间标签
        updatRoomLable(room_lable)
    }

    override fun onRoomNotice(room_desc: String?) {
        //房间通知的title
        updataRoomTalk(room_desc)
    }

    override fun onRoomEmj(p: Int, url: String) {
        //房间表情
        showPositionEmj(p, url)
    }

    override fun onRoomGift(p: Int, toP: List<Int>, giftUrl: String, staticUrl: String) {
        //房间动画
        showPositionGift(p, toP, giftUrl, staticUrl)
    }

    /**
     * 被踢房间
     */
    override fun onRoomMemberKickChange(uid: String) {
        if (AuthManager.getInstance().currentUserId.equals(uid)) {
            IMClient.getInstance().quitChatRoom(room_id, null)
            RtcClient.getInstance().quitRtcRoom(room_id, null)
            sendBroadcast(Intent("WINDOW"))
            showToastMessage("你已被踢出房间")
//            finish()
            removeUser(room_id!!, uid)
        }
    }

    override fun onRoomMemberChange(memberCount: Int) {
        updateRoomInfo()
        updateRoomTitle(detailRoomInfo!!.roomInfo.room_name, detailRoomInfo!!.roomInfo.room_num, memberCount.toString())
    }

    /**
     * 携带座驾进入
     */
    override fun onRoomMemberChangeWithCar(memberCount: Int, carPic: String) {
        updateRoomInfo()
        updateRoomTitle(detailRoomInfo!!.roomInfo.room_name, detailRoomInfo!!.roomInfo.room_num, memberCount.toString())
        if (carPic != null && !"".equals(carPic)) {
            CarUtils.playCar(this, carPic);
        }
    }

    override fun onMicUpdate(micPositionInfoList: MutableList<MicPositionsBean>?) {
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
            val hasVoiceChatPermission = role.hasVoiceChatPermission()
            enableVoiceChat(role.hasVoiceChatPermission())
        }
    }

    override fun onKickOffRoom() {
        showToastMessage(R.string.toast_chatroom_kick_off_from_room)
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
        fun starChatRoomActivity(mContext: Context, room_id: String, nikeName: String, icon: String) {
            val intent = Intent(mContext, ChatRoomActivity::class.java)
            intent.putExtra("room_id", room_id)
            intent.putExtra("nikeName", nikeName)
            intent.putExtra("icon", icon)
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
            val userId = micSeatView.getMicInfo().uid
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
    var nikeName: String? = null
    var icon: String? = null
    var roomManager: RoomManager? = null
    var detailRoomInfo: DetailRoomInfo? = null
    private var defaultMarginBottom: Int = 0
    var voiceDialog: SongVoiceDialog? = null
    var mydatabean: UserInfoBean? = null
    var shareBean: ShareUserBean.DataBean? = null
    var bgChangBroadCast: BgChangBroadCast? = null
    var giftEggManager: NotifyManager? = null
    var giftNotifyManager: GiftNotifyManager? = null

    var amdinType: Int? = -1

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
        nikeName = intent.getStringExtra("nikeName")
        icon = intent.getStringExtra("icon")
        AdminData()
        // 保持屏幕常亮
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        handler = Handler()
        EventBus.getDefault().register(this);
        setLightStatusBar(false)
        initStatusBar(topView)
        setTitle("这是哈哈哈的房间")
        requestMyData()
        initView()
//        initRoom(room_id!!)
        initroomManager()
        initAudioOutputMode()
        bgChangBroadCast = BgChangBroadCast()
        val intentFilter = IntentFilter("BGCHANG")
        registerReceiver(bgChangBroadCast, intentFilter)
        ShareData()
        giftEggManager = NotifyManager(mContext as Activity?)
        giftNotifyManager = GiftNotifyManager(mContext as Activity?)
    }

    private fun initroomManager() {
        roomManager = RoomManager.getInstance()
        detailRoomInfo = roomManager!!.getCurrentRoomInfo()

        if (detailRoomInfo == null) {
            finish()
            return
        }
        roomManager!!.setCurrentRoomEventListener(this)
        //获取进入房间前的消息
        val messageList = detailRoomInfo!!.getMessageList()

        //加入一条自己进入房间的消息
        val carName = SharedPreferencesUtils.getCarName(mContext)
        val carPic = SharedPreferencesUtils.getCarPic(mContext)

        val message = IMClient.getInstance().createLocalEnterRoomMessage(AuthManager.getInstance().currentUserId, room_id, nikeName!!, icon!!, carName, carPic)
        chatMessageList.add(message)

        //播放自己的座驾
        if (carPic != null && !"".equals(carPic)) {
            CarUtils.playCar(this, carPic)
        }

        //设置聊天信息
        chatMessageList.addAll(messageList)
        chatListAdapter!!.setMessages(chatMessageList)
        chatListAdapter!!.notifyDataSetChanged()
        chatroom_list_chat.setSelection(chatListAdapter!!.getCount())
    }

    private fun AdminData() {
        val map = HashMap<String, String>()
        map.put("room_id", room_id!!)
        map.put("uid", AuthManager.getInstance().currentUserId)
        val body = RetrofitUtil.createJsonRequest(map)
        NetWork.getService(ImpService::class.java)
                .isAdmin(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<IsAdminBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: IsAdminBean?) {
                        if (t!!.code == 0) {
                            amdinType = t.data
                            initRoom(room_id!!)
                        }
                    }

                    override fun onCompleted() {
                    }
                })


    }

    private fun isLockFair(lock: String?, fair: String?, pure: String?) {
        if ("0".equals(lock)) {
            img_w_lock.visibility = View.GONE
        } else {
            img_w_lock.visibility = View.VISIBLE
        }
        if ("0".equals(fair)) {//是否开启公屏
            chatroom_list_chat.visibility = View.VISIBLE
        } else {
            chatroom_list_chat.visibility = View.GONE
        }
        if ("0".equals(pure)) {//纯净模式
            img_gold_egg.visibility = View.VISIBLE
        } else {
            img_gold_egg.visibility = View.GONE
        }
    }

    private fun ShareData() {
        val map = HashMap<String, String>()
        map.put("uid", AuthManager.getInstance().currentUserId)
        map.put("room_id", room_id!!)
        val body = RetrofitUtil.createJsonRequest(map)
        NetWork.getService(ImpService::class.java)
                .share_user(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ShareUserBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: ShareUserBean?) {
                        if (t!!.code == 0) {
                            shareBean = t.data
                        }
                    }

                    override fun onCompleted() {
                    }
                })

    }

    inner class BgChangBroadCast : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if ("BGCHANG".equals(intent!!.action)) {
                val bgId = intent.getStringExtra("bgId")
                val tagName = intent.getStringExtra("tagName")
                val roomName = intent.getStringExtra("roomName")
                if (!TextUtils.isEmpty(bgId)) {
                    updateRoomBg(bgId)
                }
                if (!TextUtils.isEmpty(tagName)) {
                    updatRoomLable(tagName)
                }
                if (!TextUtils.isEmpty(roomName)) {
                    updataTitle(roomName)
                }
            }
        }
    }

    private fun requestMyData() {
        NetWork.getService(ImpService::class.java)
                .GetUserInfo(SpUtils.getSp(mContext, "uid"))
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

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initView() {
        img_back.setOnClickListener {
            val homeSettingDialog = HomeSettingDialog(mContext)
            homeSettingDialog.setHomeSettingClickListener(object : HomeSettingDialog.HomeSettingClickListener {
                override fun clickHT() {
                    RandomTalk()
                }

                override fun clickExit() {
                    quiteRoom(SpUtils.getSp(mContext, "uid"), "1")
                }

                override fun clickSmall() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if (!Settings.canDrawOverlays(mContext)) {
                            startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")), 1)
                        } else {
                            val customeWindow = CustomeWindow(BaseApplication.mContext!!, detailRoomInfo!!.roomInfo.icon)
                            customeWindow.show()
                            finish()
                        }
                    } else {
                        val customeWindow = CustomeWindow(BaseApplication.mContext!!, detailRoomInfo!!.roomInfo.icon)
                        customeWindow.show()
                        finish()
                    }
                }

                override fun clickReport() {
                    ReportActivity.starReportActivity(mContext, room_id!!)
                }
            })
            homeSettingDialog.show()
        }

        img_share.setOnClickListener {
            val friendDialog = ShareFriendDialog(mContext)
            friendDialog.setShareListener(object : ShareFriendDialog.ShareListener {
                override fun myself() {
                    showToastMessage("站内分享")
                }

                override fun Wechat() {
                    ShareUrl(shareBean!!.nickname, shareBean!!.desc, shareBean!!.icon, shareBean!!.url, SHARE_MEDIA.WEIXIN)
                }

                override fun pyq() {
                    ShareUrl(shareBean!!.nickname, shareBean!!.desc, shareBean!!.icon, shareBean!!.url, SHARE_MEDIA.WEIXIN_CIRCLE)
                }

                override fun qqZone() {
                    ShareUrl(shareBean!!.nickname, shareBean!!.desc, shareBean!!.icon, shareBean!!.url, SHARE_MEDIA.QZONE)
                }

                override fun QQ() {
                    ShareUrl(shareBean!!.nickname, shareBean!!.desc, shareBean!!.icon, shareBean!!.url, SHARE_MEDIA.QQ)
                }
            })
            friendDialog.show()
        }

        tv_online.setOnClickListener {
            OnlineListActivity.starOnlineListActivity(mContext, room_id!!)
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
                    if (playMusicService != null) {
                        playMusicService!!.stop()
                    }
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                        if (!Settings.canDrawOverlays(mContext)) {
                            startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")), 1)
                        } else {
                            val customeWindow = CustomeWindow(BaseApplication.mContext!!, detailRoomInfo!!.roomInfo.icon)
                            customeWindow.show()
                            finish()
                        }
                    } else {
                        val customeWindow = CustomeWindow(BaseApplication.mContext!!, detailRoomInfo!!.roomInfo.icon)
                        customeWindow.show()
                        finish()
                    }
                }

                override fun clickReport() {
                    ReportActivity.starReportActivity(mContext, room_id!!)
                }
            })
            homeSettingDialog.show()
        }

        tv_room_level.setOnClickListener {
            //            RoomLevelsDialog(mContext, room_id).show()
            RoomLevelDialog(mContext, room_id!!)
                    .show()
        }

        tv_music.setOnClickListener {
            voiceDialog = SongVoiceDialog(mContext)
            val tv_song_name1 = voiceDialog!!.findViewById<TextView>(R.id.tv_song_name1)
            if (musicbean != null) {
                tv_song_name1.setText(musicbean!!.list.get(musicbean!!.position).music_name)
            }
            val seekBar = voiceDialog!!.findViewById<AppCompatSeekBar>(R.id.seekBar)
            seekBar.progress = AudioUtils.getCurrentAudioVolume(mContext)
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    AudioUtils.setAudioVolume(mContext, progress)
                }
            })
            voiceDialog!!.setSongVoiceListener(object : SongVoiceDialog.SongVoiceListener {
                override fun StopSong() {
                    if (playMusicService != null) {
                        playMusicService!!.pause()
                    }
                }

                override fun NextSong() {
                    if (playMusicService != null) {
                       var position = musicbean!!.position
                        playMusicService!!.next()
                        position++
                        if (position>musicbean!!.list.size){
                            position==0
                            tv_song_name1.setText(musicbean!!.list.get(musicbean!!.position).music_name)
                        }else{
                            tv_song_name1.setText(musicbean!!.list.get(position).music_name)
                        }
                    }
                }

                override fun SettingSong() {
                    MySongBookActivity.starMySongBookActivity(mContext, room_id!!)
                }
            })
            voiceDialog!!.show()
        }

        img_send_msg.setOnClickListener {
            ll_isShow.visibility = View.GONE
            ll_send.visibility = View.VISIBLE
            AppUtil.openKeyboard(mContext)

        }

        et_chat.setOnClickListener {
            ll_isShow.visibility = View.GONE
            ll_send.visibility = View.VISIBLE
        }

        tv_send.setOnClickListener {
            if (!"0".equals(fair)) {
                showToastMessage("当前为公屏模式")
                return@setOnClickListener
            }

            val msg = chatroom_et_chat_input.text.toString().trim()
            if (!TextUtils.isEmpty(msg)) {
                roomManager!!.sendChatRoomMessage(msg, SpUtils.getSp(mContext, "uid"), mydatabean!!.data.nickname, mydatabean!!.data.icon);
            }
            hideInputKeyboard();
            chatroom_et_chat_input.setText("");

        }

        chatroom_setting.setOnClickListener {
            AddRoomActivity.starAddRoomActivity(mContext, room_id!!, tv_title.text.toString())
        }

        chatroom_iv_sound_control.setOnClickListener {
            startActivity(MsgActivity::class.java)
        }

        img_gold_egg.setOnClickListener {
            val eggDialog = EggDialog(mContext)
            eggDialog.setOnEggOpenListener(object : EggDialog.OnEggOpenListener {
                override fun onEggOpened(data: List<EggBean.DataBean>) {
                    //创建消息
                    for (item in data) {
                        val msg = EggChatMessage()
                        msg.giftName = item.sg_name
                        msg.giftPrice = "0"//TODO: Sincerly 是否需要价格？
                        msg.name = mydatabean!!.data!!.nickname
                        msg.giftSendUserId = AuthManager.getInstance().currentUserId
                        val message = Message.obtain(room_id, Conversation.ConversationType.CHATROOM, msg)
                        RongIMClient.getInstance().sendMessage(message, null, null, object : IRongCallback.ISendMessageCallback {
                            override fun onAttached(p0: Message?) {
                                Log.d("tag", p0!!.content.toString())
                            }

                            override fun onSuccess(p0: Message?) {
                                Log.d("tag", p0!!.content.toString())
                            }

                            override fun onError(p0: Message?, p1: RongIMClient.ErrorCode?) {
                                Log.d("tag", p0!!.content.toString())
                            }
                        });
                        if (chatListAdapter != null) {
                            chatMessageList.add(message!!)
                        }
                    }
                    runOnUiThread {
                        chatListAdapter!!.notifyDataSetChanged()
                        chatroom_list_chat.smoothScrollToPosition(chatListAdapter!!.count)
                    }
                }
            })
            eggDialog.show()
        }

        img_img_yy.setOnClickListener {
            setMuteMic(!isMuteMic)
        }

        img_simle.setOnClickListener {
            val dialog = SmilDialog(mContext);
            dialog.setOnDialogListener(object : SmilDialog.OnSmileDialogListener {
                override fun onClick(position: Int, url: String) {
                    Log.e("tag", "send");
                    val message = RoomEmjMessage()
                    if (AuthManager.getInstance().currentUserId.equals(detailRoomInfo!!.roomInfo.uid)) {
                        message.position = 8;//当前用户所在的麦位
                    } else {
                        for (bean in detailRoomInfo!!.micPositions) {
                            if (AuthManager.getInstance().currentUserId.equals(bean.uid)) {
                                message.position = bean.sort - 1;//当前用户所在的麦位
                            }
                        }
                    }
                    message.imageUrl = url;
                    val obtain = Message.obtain(room_id, Conversation.ConversationType.CHATROOM, message)
                    RongIMClient.getInstance().sendMessage(obtain, null, null, object : IRongCallback.ISendMessageCallback {
                        override fun onAttached(p0: Message?) {
                            Log.d("tag", p0!!.content.toString())
                        }

                        override fun onSuccess(p0: Message?) {
                            Log.d("tag", p0!!.content.toString())
                        }

                        override fun onError(p0: Message?, p1: RongIMClient.ErrorCode?) {
                            Log.d("tag", p0!!.content.toString())
                        }
                    });

                    if (AuthManager.getInstance().currentUserId.equals(detailRoomInfo!!.roomInfo.uid)) {
                        showPositionEmj(8, url);
                    } else {
                        for (bean in detailRoomInfo!!.micPositions) {
                            if (AuthManager.getInstance().currentUserId.equals(bean.uid)) {
                                //显示当前用户所在的麦位表情
                                showPositionEmj(bean.sort - 1, url);
                            }
                        }
                    }
                }
            })
            dialog.show()
        }

        chatroom_iv_mic_control.setOnClickListener {
            enableRoomSound(!roomManager!!.isCurrentRoomVoiceEnable())
        }

        chatroom_gift.setOnClickListener {
            showGiftBagDialog("", "")
        }

        //聊天列表
        chatListAdapter = RoomChatListAdapter(this)
        chatroom_list_chat.setAdapter(chatListAdapter)
        chatListAdapter!!.setOnGiftEggItemClickListener(object : RoomChatListAdapter.OnGiftEggItemClickListener {
            override fun onClickUser(userId: String?) {
                showMessageUserInfoDialog(userId.toString(), room_id!!)

            }
        })
        //麦位
        setMicSeaViewList()
        // 默认麦克不可用
        enableUseMic(false)
        defaultMarginBottom = DisplayUtils.dp2px(this, 10)

        //启用软件弹出监听
        enableKeyboardStateListener(true)
    }

    private fun showGiftBagDialog(targetUid: String, targetNickName: String) {
        val giftBagDialog = GiftBagDialog(mContext, room_id!!, targetUid, targetNickName)
        giftBagDialog.setonGiftListener(object : GiftBagDialog.OnGiftListener {
            override fun needInputed() {
                //需要显示输入数量
                var d = InputDialog(mContext, R.style.BottomStyle)
                d.setListener(object : InputDialog.OnDialogClickListener {
                    override fun input(giftNum: String?) {
                        giftBagDialog.setGiftNum(giftNum.toString())
                    }
                })
                d.showDialog()
            }

            override fun onClck(targetPosition: Int, toPosition: List<Int>, pic: String, dataList: List<RoomMicListBean.DataBean>, gifPic: String, gifName: String, gifNum: String, targetUserId: String, targetUserName: String) {
                showPositionGift(targetPosition, toPosition, gifPic, pic)
                if ("".equals(targetUserId)) {
                    //发送小屏消息
                    for (bean in dataList) {
                        if (bean.isChoosed) {
                            val giftChatMessage = GiftChatMessage()
                            giftChatMessage.name = mydatabean!!.data!!.nickname//发送人
                            giftChatMessage.toName = bean.nickname//接收人
                            giftChatMessage.giftName = gifName//礼物名称
                            giftChatMessage.giftPic = gifPic//礼物图片
                            giftChatMessage.giftNum = gifNum//礼物数量
                            giftChatMessage.fromUid = AuthManager.getInstance().currentUserId.toString()
                            giftChatMessage.toUid = bean.uid.toString()
                            //在本地保存一条数据
                            val o = Message.obtain(room_id, Conversation.ConversationType.CHATROOM, giftChatMessage)
                            if (chatListAdapter != null) {
                                chatMessageList.add(o!!)
                            }
                            RongIMClient.getInstance().sendMessage(o, null, null, object : IRongCallback.ISendMessageCallback {
                                override fun onAttached(p0: Message?) {
                                    Log.d("tag", "attached:" + p0!!.content.toString())
                                }

                                override fun onSuccess(p0: Message?) {
                                    Log.d("tag", "onSuccess:" + p0!!.content.toString())
                                }

                                override fun onError(p0: Message?, p1: RongIMClient.ErrorCode?) {
                                    Log.d("tag", "onError:" + p0!!.content.toString())
                                }
                            });
                        }
                    }
                    chatListAdapter!!.notifyDataSetChanged()
                    chatroom_list_chat.smoothScrollToPosition(chatListAdapter!!.count)

                    //发送礼物消息
                    val roomGiftMessage = RoomGiftMessage()
                    roomGiftMessage.position = targetPosition
                    roomGiftMessage.toPosition = toPosition
                    roomGiftMessage.staticUrl = pic
                    roomGiftMessage.giftUrl = gifPic
                    val obtain = Message.obtain(room_id, Conversation.ConversationType.CHATROOM, roomGiftMessage)
                    RongIMClient.getInstance().sendMessage(obtain, null, null, object : IRongCallback.ISendMessageCallback {
                        override fun onAttached(p0: Message?) {
                            Log.d("tag", "attached:" + p0!!.content.toString())
                        }

                        override fun onSuccess(p0: Message?) {
                            Log.d("tag", "onSuccess:" + p0!!.content.toString())
                        }

                        override fun onError(p0: Message?, p1: RongIMClient.ErrorCode?) {
                            Log.d("tag", "onError:" + p0!!.content.toString())
                        }
                    });
                } else {
                    //赠送的是别人发送小屏消息
                    val giftChatMessage = GiftChatMessage()
                    giftChatMessage.name = mydatabean!!.data!!.nickname//发送人
                    giftChatMessage.toName = targetUserName//接收人
                    giftChatMessage.giftName = gifName//礼物名称
                    giftChatMessage.giftPic = gifPic//礼物图片
                    giftChatMessage.giftNum = gifNum//礼物数量
                    giftChatMessage.fromUid = AuthManager.getInstance().currentUserId.toString()
                    giftChatMessage.toUid = targetUid.toString()
                    //在本地保存一条数据
                    val o = Message.obtain(room_id, Conversation.ConversationType.CHATROOM, giftChatMessage)
                    if (chatListAdapter != null) {
                        chatMessageList.add(o!!)
                    }
                    RongIMClient.getInstance().sendMessage(o, null, null, object : IRongCallback.ISendMessageCallback {
                        override fun onAttached(p0: Message?) {
                            Log.d("tag", "attached:" + p0!!.content.toString())
                        }

                        override fun onSuccess(p0: Message?) {
                            Log.d("tag", "onSuccess:" + p0!!.content.toString())
                        }

                        override fun onError(p0: Message?, p1: RongIMClient.ErrorCode?) {
                            Log.d("tag", "onError:" + p0!!.content.toString())
                        }
                    });
                    chatListAdapter!!.notifyDataSetChanged()
                    chatroom_list_chat.smoothScrollToPosition(chatListAdapter!!.count)

                    //发送礼物消息
                    val roomGiftMessage = RoomGiftMessage()
                    roomGiftMessage.position = targetPosition
                    roomGiftMessage.toPosition = toPosition
                    roomGiftMessage.staticUrl = pic
                    roomGiftMessage.giftUrl = gifPic
                    val obtain = Message.obtain(room_id, Conversation.ConversationType.CHATROOM, roomGiftMessage)
                    RongIMClient.getInstance().sendMessage(obtain, null, null, object : IRongCallback.ISendMessageCallback {
                        override fun onAttached(p0: Message?) {
                            Log.d("tag", "attached:" + p0!!.content.toString())
                        }

                        override fun onSuccess(p0: Message?) {
                            Log.d("tag", "onSuccess:" + p0!!.content.toString())
                        }

                        override fun onError(p0: Message?, p1: RongIMClient.ErrorCode?) {
                            Log.d("tag", "onError:" + p0!!.content.toString())
                        }
                    });
                }
            }
        })
        giftBagDialog.show()
    }

    private fun ShareUrl(nickname: String, desc: String, icon: String, url: String, platform: SHARE_MEDIA) {
        val umWeb = UMWeb(url)
        umWeb.title = nickname
        umWeb.setThumb(UMImage(mContext, icon))
        umWeb.description = desc
        ShareAction(this@ChatRoomActivity)
                .withMedia(umWeb)
                .setPlatform(platform)
                .setCallback(shareListener)
                .share()
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
                            if (playMusicService != null) {
                                playMusicService!!.stop()
                            }
                            RandomQuiteRoom(SpUtils.getSp(mContext, "uid"), "1", t.data.room_id.toString())
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
                    Log.d("tag","启用房间声音失败==" + errorCode)
                }
            })
        } else {
            // 关闭房间声音
            roomManager!!.enableRoomChatVoice(false, object : ResultCallback<Boolean> {
                override fun onSuccess(aBoolean: Boolean?) {
                    chatroom_iv_mic_control.setImageResource(R.mipmap.img_colose_volume)
                }

                override fun onFail(errorCode: Int) {
                    Log.d("tag","关闭房间声音失败==" + errorCode)
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
        if (!TextUtils.isEmpty(detailRoomInfo!!.roomInfo.user_ts)) {
            ImageLoadUtil.GlideHeadImageLoad(mContext, detailRoomInfo!!.roomInfo.user_ts, img_head_wear)
        }

        if ("0".equals(detailRoomInfo!!.roomInfo.room_gift_tx)) {//礼物值：0 关闭；1 开启
            tv_room_manager.visibility = View.GONE
            UpdataTips(detailRoomInfo!!.getMicPositions(), false)
        } else {
            tv_room_manager.visibility = View.VISIBLE
            tv_room_manager.setText(detailRoomInfo!!.roomInfo.gifts)
            UpdataTips(detailRoomInfo!!.getMicPositions(), true)
        }
        if ("0".equals(detailRoomInfo!!.roomInfo.is_lock)) {
            img_w_lock.visibility = View.GONE
        } else {
            img_w_lock.visibility = View.VISIBLE
        }
        //判断房主是否离开
        UpdateOwner(detailRoomInfo!!)
        //设置房间背景
        updateRoomBg(detailRoomInfo!!.roomInfo.room_bg)
        //设置房间话题
        updataRoomTalk(detailRoomInfo!!.roomInfo.room_desc)
        //设置房间标签
        updatRoomLable(detailRoomInfo!!.roomInfo.room_label)
        //设置房间标题
        updataTitle(detailRoomInfo!!.roomInfo.room_name)

        chatroom_list_chat.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (chatListAdapter!!.messageList.get(position) != null && chatListAdapter!!.messageList.size > 0 && !TextUtils.isEmpty(chatListAdapter!!.messageList.get(position).senderUserId)) {
                    val viewType = chatListAdapter!!.getItemViewType(position);
                    if (viewType == VIEW_TYPE_GIFT || viewType == VIEW_TYPE_EGG) {
                        //送礼物消息和砸金蛋房间内消息移至 OnGiftEggListener
                        return
                    }

                    val userid = chatListAdapter!!.messageList.get(position).senderUserId.toString()
                    if (TextUtils.isEmpty(userid)) {
                        return
                    }
                    showMessageUserInfoDialog(userid, room_id)
                }
            }
        })

        //设置标题
        updateRoomTitle(detailRoomInfo!!.roomInfo.room_name, detailRoomInfo!!.roomInfo.room_num, detailRoomInfo!!.roomInfo.memCount.toString())

        img_head.setOnClickListener {
            if (!SpUtils.getSp(mContext, "uid").equals(detailRoomInfo!!.roomInfo.uid)) {
                val giveDialog = GiveDialog(mContext, detailRoomInfo!!.roomInfo.uid, room_id)
                var findNikeName = giveDialog.findViewById<TextView>(R.id.tv_nikeName)
                giveDialog.findViewById<LinearLayout>(R.id.ll_isShow).visibility = View.GONE
                giveDialog.findViewById<LinearLayout>(R.id.ll_bbs).visibility = View.GONE
                giveDialog.setGiveClickListener(object : GiveDialog.GiveClickListener {
                    override fun BmClick() {

                    }

                    override fun BtxmClick() {

                    }

                    override fun ScmClick() {

                    }

                    override fun clickGiveGift(uid: String, nickname: String) {
                        showGiftBagDialog(detailRoomInfo!!.roomInfo.uid, nickname)
                    }

                    override fun clickPrivateChat() {
                        RongIM.getInstance().startPrivateChat(mContext, detailRoomInfo!!.roomInfo.uid, findNikeName.text.toString());
                    }

                    override fun clickGiveZb() {
                        DressMallActivity.startDressMallActivity(mContext, detailRoomInfo!!.roomInfo.uid,"",detailRoomInfo!!.roomInfo.nickname)
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
            } else {
                MyDataActivity.startMyDataActivity(mContext, detailRoomInfo!!.roomInfo.uid, "myself")
            }
        }
        //更新麦位状态1
        updateMicSeatState(detailRoomInfo!!.getMicPositions())

        // 判断是否可以设置房间
        if (SpUtils.getSp(mContext, "uid").equals(detailRoomInfo!!.roomInfo.uid) || amdinType == 1) {
            chatroom_setting.setVisibility(View.VISIBLE)
            img_simle.visibility = View.VISIBLE
            tv_music.visibility = View.VISIBLE

            ll_send.visibility = View.GONE
            et_chat.visibility = View.GONE
            ll_isShow.visibility = View.VISIBLE
            img_send_msg.visibility = View.VISIBLE

            tv_room.setOnClickListener {
                val intent = Intent(mContext, RoomNoticeActivity::class.java)
                intent.putExtra("roomId", room_id)
                startActivityForResult(intent, 1999)
//                RoomNoticeActivity.startRoomNoticeActivity(mContext,room_id)
            }
        } else {
            img_simle.visibility = View.GONE
            tv_music.visibility = View.GONE

            chatroom_setting.setVisibility(View.GONE)
            ll_send.visibility = View.GONE
            img_send_msg.visibility = View.GONE
            et_chat.visibility = View.VISIBLE
            ll_isShow.visibility = View.VISIBLE

            tv_room.setOnClickListener {
                RoomNoticeDialog(mContext, room_id).show();
            }
            if (!TextUtils.isEmpty(detailRoomInfo!!.roomInfo.room_content)) {
                RoomNoticeDialog(mContext, room_id).show();
            }
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

    private fun showMessageUserInfoDialog(userid: String, room_id: String) {
        val msgListDialog = MsgListDialog(mContext, userid, room_id)
        msgListDialog.setOnMsgListDialog(object : MsgListDialog.OnMsgListDialogListener {
            override fun bMClick() {//闭麦

            }

            override fun xMClick() {//下麦旁听

            }

            override fun jMClick() {//解锁

            }

            override fun bTMClick() {//报Ta上麦
                val upperWheatDialog = UpperWheatDialog(mContext, userid, room_id)
                upperWheatDialog.setOnWheatClickListener(object : UpperWheatDialog.OnWheatClickListener {
                    override fun onClickWheat(position: Int) {
                        JoinMic(userid, position)
                    }
                })
                upperWheatDialog.show()
            }
        })

        if (SpUtils.getSp(mContext, "uid").equals(detailRoomInfo!!.roomInfo.uid) || amdinType == 1) {
            msgListDialog.show()
        } else {
            val giveDialog = GiveDialog(mContext, userid, room_id)
            val tv_nikeName = giveDialog.findViewById<TextView>(R.id.tv_nikeName)
            giveDialog.findViewById<LinearLayout>(R.id.ll_isShow).visibility = View.GONE
            giveDialog.findViewById<LinearLayout>(R.id.ll_bbs).visibility = View.GONE
            giveDialog.setGiveClickListener(object : GiveDialog.GiveClickListener {
                override fun BmClick() {

                }

                override fun BtxmClick() {

                }

                override fun ScmClick() {

                }

                override fun clickGiveGift(uid: String, nickname: String) {
                    showGiftBagDialog(uid, nickname)
                    //GiftBagDialog(mContext, room_id).show()
                }

                override fun clickPrivateChat() {
                    RongIM.getInstance().startPrivateChat(mContext, detailRoomInfo!!.roomInfo.uid, tv_nikeName.text.toString());
                }

                override fun clickGiveZb() {
                    DressMallActivity.startDressMallActivity(mContext, detailRoomInfo!!.roomInfo.uid,"",micNickname!!)
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

    private fun UpdateOwner(detailRoomInfo: DetailRoomInfo) {
        if ("0".equals(detailRoomInfo.roomInfo.is_wheat)) {
            img_leave.visibility = View.VISIBLE
        } else {
            img_leave.visibility = View.GONE
        }
    }

    private fun updataRoomTalk(room_desc: String?) {
        if (AuthManager.getInstance().currentUserId.equals(detailRoomInfo!!.roomInfo.uid)) {
            if (TextUtils.isEmpty(room_desc)) {
                tv_room.setText("点击编辑房间话题")
            } else {
                tv_room.setText(room_desc)
            }
        } else {
            if (TextUtils.isEmpty(room_desc)) {
                tv_room.setText("")
            } else {
                tv_room.setText(room_desc)
            }
        }
    }

    private fun updatRoomLable(room_lable: String?) {
        tv_lable.setText(room_lable)
    }

    private fun updataTitle(room_name: String?) {
        tv_title.setText(room_name)
    }

    private fun UpdataTips(micPositions: List<MicPositionsBean>, b: Boolean) {
        if (micPositions == null) return
        for (micInfo in micPositions) {
            val position = micInfo.sort - 1
            val micSeatView = micSeatViewList!!.get(position)
            micSeatView.setHeartValue(micInfo.gifts, b)
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
                Log.d("tag","设置启动房间聊天声音成功")
            }

            override fun onFail(errorCode: Int) {
                Log.d("tag","设置启动房间聊天声音失败" + errorCode)
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
                    Log.d("tag","设置是否开启失败" + errorCode)
                }
            })
        } else {
            roomManager!!.stopVoiceChat(object : ResultCallback<Boolean> {
                override fun onSuccess(aBoolean: Boolean?) {
                    enableUseMic(false)
                }

                override fun onFail(errorCode: Int) {
                    Log.d("tag","设置是否关闭失败" + errorCode)
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

        for (position in micPositions.indices) {
            if (position < seatSize) {
                val micSeatView = micSeatViewList!!.get(position)
                micSeatView.updateMicState(micPositions.get(position))
            }
        }

//        for (micInfo in micPositions) {
//            val position = micInfo.sort - 1
//            if (position < seatSize) {
//                val micSeatView = micSeatViewList!!.get(position)
//                micSeatView.updateMicState(micInfo)
//            }
//        }
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

    //设置房主的个人信息
    private fun updataRoomManager() {
        ImageLoadUtil.GlideHeadImageLoad(BaseApplication.mContext!!, detailRoomInfo!!.roomInfo.icon, img_head)
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
        tv_id.setText("ID：" + room_num)
        tv_online.setText(memcount + "人在线")
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
    var micNickname: String? = null
    var micIcon: String? = null
    private fun doActionToMicSeat(targetMicPositionInfo: MicPositionsBean) {
        if (detailRoomInfo == null) {
            //未加入到房间提示
            showToastMessage("未加入到房间")
            return
        }
        micPosition = targetMicPositionInfo.sort    //操作的麦位位置
        micNickname = targetMicPositionInfo.nickname
        micIcon = targetMicPositionInfo.icon
        val userId = targetMicPositionInfo.uid.toString() //操作的麦位上的用户id
        val is_admin = targetMicPositionInfo.is_admin    //是否管理员：0 否；1 是
        val is_lock_wheat = targetMicPositionInfo.is_lock_wheat //是否锁麦：0 锁麦； 1 已解麦
        val is_wheat = targetMicPositionInfo.is_wheat //	是否上麦 0 下麦；1 上麦
        val is_oc_wheat = targetMicPositionInfo.is_oc_wheat //		是否闭麦：0 开麦；1 闭麦

        if (detailRoomInfo!!.roomInfo.uid.equals(SpUtils.getSp(mContext, "uid"))) {//房主
            if ("0".equals(userId)) {//判断点击麦位是否为空
                val seatMicDialog = UpperSeatMicDialog(mContext)
                seatMicDialog.findViewById<TextView>(R.id.tv_sm).visibility = View.GONE
                if ("0".equals(is_lock_wheat)) {
                    seatMicDialog.findViewById<TextView>(R.id.tv_close_mic).setText("解麦")
                } else {
                    seatMicDialog.findViewById<TextView>(R.id.tv_close_mic).setText("锁麦")
                }
                if ("0".equals(is_oc_wheat)) {
                    seatMicDialog.findViewById<TextView>(R.id.tv_bm).setText("闭麦")
                } else {
                    seatMicDialog.findViewById<TextView>(R.id.tv_bm).setText("开麦")
                }

                seatMicDialog.setOnDialogClickListener(object : UpperSeatMicDialog.OnDialogClickListener {
                    override fun SMclick(view: TextView) {

                    }

                    override fun BTSMclick(view: TextView) {
                        val intent = Intent(mContext, OnLineActivity::class.java)
                        intent.putExtra("roomId", detailRoomInfo!!.roomInfo.room_id)
                        startActivityForResult(intent, 1033)
                    }

                    override fun CSMclick(view: TextView) {
                        if ("0".equals(is_lock_wheat)) {
                            LockWheat(room_id, micPosition, "2", userId)//	是否锁麦：1 锁麦； 2 已解麦
                        } else {
                            LockWheat(room_id, micPosition, "1", userId)//	是否锁麦：1 锁麦； 2 已解麦
                        }
                    }

                    override fun BMclick(view: TextView) {
                        if ("0".equals(is_oc_wheat)) {
                            OcWheat(room_id, micPosition, "2", userId)
                        } else {
                            OcWheat(room_id, micPosition, "1", userId)
                        }
                    }
                })
                seatMicDialog.show()
            } else {// 点击有人麦位
                val giveDialog = GiveDialog(mContext, userId, room_id!!)
                val tv_setting_manager = giveDialog.findViewById<TextView>(R.id.tv_setting_manager)
                val tv_move_manager = giveDialog.findViewById<TextView>(R.id.tv_move_manager)
                val zw = giveDialog.findViewById<TextView>(R.id.zw)
                val tv_btxm = giveDialog.findViewById<TextView>(R.id.tv_btxm)
                if ("0".equals(is_lock_wheat)) {
                    giveDialog.findViewById<TextView>(R.id.tv_scm).setText("解麦")
                } else {
                    giveDialog.findViewById<TextView>(R.id.tv_scm).setText("锁麦")
                }
                if ("0".equals(is_oc_wheat)) {
                    giveDialog.findViewById<TextView>(R.id.tv_bm).setText("闭麦")
                } else {
                    giveDialog.findViewById<TextView>(R.id.tv_bm).setText("开麦")
                }
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

                    override fun BmClick() {//闭麦
                        if ("0".equals(is_oc_wheat)) {
                            OcWheat(room_id, micPosition, "2", userId)
                        } else {
                            OcWheat(room_id, micPosition, "1", userId)
                        }
                    }

                    override fun BtxmClick() {//抱TA下麦
                        ExitMic(userId, micPosition)
                    }

                    override fun ScmClick() {//锁麦
                        if (!TextUtils.equals("0", userId)) {
                            showToastMessage("麦位有人")
                            return
                        }

                        if ("0".equals(is_lock_wheat)) {
                            LockWheat(room_id, micPosition, "2", userId)//	是否锁麦：1 锁麦； 2 已解麦
                        } else {
                            LockWheat(room_id, micPosition, "1", userId)//	是否锁麦：1 锁麦； 2 已解麦
                        }
                    }

                    override fun removeManager() {
                        RemoveManager(userId, room_id!!, "2")
                    }

                    override fun setManager() {
                        BlackManager(userId, room_id!!, "2", "", "")
                    }

                    override fun setExit() {
                        ExitCurrentRoom(userId, "2", room_id!!, micNickname!!, micIcon!!)
                    }

                    override fun blackList() {
                        BlackManager(userId, room_id!!, "1", micNickname!!, micIcon!!)
                        ExitCurrentRoom(userId, "2", room_id!!, micNickname!!, micIcon!!)
                    }

                    override fun clickGiveGift(uid: String, nikeName: String) {
                        showGiftBagDialog(uid, nikeName)
                    }

                    override fun clickPrivateChat() {
                        RongIM.getInstance().startPrivateChat(mContext, userId, micNickname);
                    }

                    override fun clickGiveZb() {
                        DressMallActivity.startDressMallActivity(mContext, userId,"",nikeName!!)
                    }

                    override fun clickFoucsOn() {
                        FocusOnData(SpUtils.getSp(mContext, "uid"), userId, "1")
                    }
                })
                giveDialog.show()
            }
        } else if (amdinType == 1) {//如果我是管理员
            if ("0".equals(userId)) {//判断点击麦位是否为空
                val currentRole = roomManager!!.currentRole
                if (currentRole is Listener) {
                    JoinMic(AuthManager.getInstance().currentUserId, micPosition)
                } else if ((currentRole is Linker) && (preMicPosition != -1)) {
                    JumpMic(AuthManager.getInstance().currentUserId, preMicPosition, micPosition)
                }
            } else {//有人
                if ("0".equals(is_admin)) {//不是管理员
                    val giveDialog = GiveDialog(mContext, userId, room_id!!)
                    val tv_setting_manager = giveDialog.findViewById<TextView>(R.id.tv_setting_manager)
                    val tv_move_manager = giveDialog.findViewById<TextView>(R.id.tv_move_manager)
                    val zw = giveDialog.findViewById<TextView>(R.id.zw)
                    val tv_btxm = giveDialog.findViewById<TextView>(R.id.tv_btxm)
                    if ("0".equals(is_lock_wheat)) {
                        giveDialog.findViewById<TextView>(R.id.tv_scm).setText("解麦")
                    } else {
                        giveDialog.findViewById<TextView>(R.id.tv_scm).setText("锁麦")
                    }
                    if ("0".equals(is_oc_wheat)) {
                        giveDialog.findViewById<TextView>(R.id.tv_bm).setText("闭麦")
                    } else {
                        giveDialog.findViewById<TextView>(R.id.tv_bm).setText("开麦")
                    }
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

                        override fun BmClick() {//闭麦
                            if ("0".equals(is_oc_wheat)) {
                                OcWheat(room_id, micPosition, "2", userId)
                            } else {
                                OcWheat(room_id, micPosition, "1", userId)
                            }
                        }

                        override fun BtxmClick() {//抱TA下麦
                            ExitMic(userId, micPosition)
                        }

                        override fun ScmClick() {//锁麦
                            if (!TextUtils.equals("0", userId)) {
                                showToastMessage("麦位有人")
                                return
                            }
                            if ("0".equals(is_lock_wheat)) {
                                LockWheat(room_id, micPosition, "2", userId)//	是否锁麦：1 锁麦； 2 已解麦
                            } else {
                                LockWheat(room_id, micPosition, "1", userId)//	是否锁麦：1 锁麦； 2 已解麦
                            }
                        }

                        override fun removeManager() {
                            RemoveManager(userId, room_id!!, "2")
                        }

                        override fun setManager() {
                            BlackManager(userId, room_id!!, "2", "", "")
                        }

                        override fun setExit() {
                            ExitCurrentRoom(userId, "2", room_id!!, micNickname!!, micIcon!!)
                        }

                        override fun blackList() {
                            BlackManager(userId, room_id!!, "1", micNickname!!, micIcon!!)
                            ExitCurrentRoom(userId, "2", room_id!!, micNickname!!, micIcon!!)
                        }

                        override fun clickGiveGift(uid: String, nickname: String) {
                            showGiftBagDialog(userId, nickname)
                        }

                        override fun clickPrivateChat() {
                            RongIM.getInstance().startPrivateChat(mContext, userId, micNickname);
                        }

                        override fun clickGiveZb() {
                            DressMallActivity.startDressMallActivity(mContext, userId,"",micNickname!!)
                        }

                        override fun clickFoucsOn() {
                            FocusOnData(SpUtils.getSp(mContext, "uid"), userId, "1")
                        }
                    })
                    giveDialog.show()
                } else {//是管理员
                    if (AuthManager.getInstance().currentUserId.equals(userId)) {//是自己
                        val seatMicDialog = UpperSeatMicDialog(mContext)
                        seatMicDialog.findViewById<TextView>(R.id.tv_btsm).visibility = View.GONE
                        seatMicDialog.findViewById<TextView>(R.id.tv_close_mic).visibility = View.GONE
                        seatMicDialog.findViewById<TextView>(R.id.tv_bm).visibility = View.GONE
                        seatMicDialog.findViewById<TextView>(R.id.tv_sm).setText("下麦")
                        if ("0".equals(is_oc_wheat)) {
                            seatMicDialog.findViewById<TextView>(R.id.tv_bm).setText("闭麦")
                        } else {
                            seatMicDialog.findViewById<TextView>(R.id.tv_bm).setText("开麦")
                        }
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
                                if ("0".equals(is_oc_wheat)) {
                                    OcWheat(room_id, micPosition, "2", userId)
                                } else {
                                    OcWheat(room_id, micPosition, "1", userId)
                                }
                            }
                        })
                        seatMicDialog.show()

                    } else {
                        showToastMessage("无权限操作")
                    }
                }
            }
        } else { //非房主  点击控麦位
            if ("0".equals(userId)) {
                val currentRole = roomManager!!.currentRole

                if (currentRole is Listener) {
                    JoinMic(AuthManager.getInstance().currentUserId, micPosition)
                } else if ((currentRole is Linker) && (preMicPosition != -1)) {
                    JumpMic(AuthManager.getInstance().currentUserId, preMicPosition, micPosition)
                }
            } else { //非房主
                if ((SpUtils.getSp(mContext, "uid")).equals(userId)) { //麦位是自己
                    val seatMicDialog = UpperSeatMicDialog(mContext)
                    seatMicDialog.findViewById<TextView>(R.id.tv_btsm).visibility = View.GONE
                    seatMicDialog.findViewById<TextView>(R.id.tv_close_mic).visibility = View.GONE
                    seatMicDialog.findViewById<TextView>(R.id.tv_bm).visibility = View.GONE
                    seatMicDialog.findViewById<TextView>(R.id.tv_sm).setText("下麦")
                    if ("0".equals(is_oc_wheat)) {
                        seatMicDialog.findViewById<TextView>(R.id.tv_bm).setText("闭麦")
                    } else {
                        seatMicDialog.findViewById<TextView>(R.id.tv_bm).setText("开麦")
                    }
                    seatMicDialog.setOnDialogClickListener(object : UpperSeatMicDialog.OnDialogClickListener {
                        override fun SMclick(view: TextView) {
                            ExitMic(userId, micPosition)
                        }

                        override fun BTSMclick(view: TextView) {
                            OnLineActivity.starOnLineActivity(mContext, detailRoomInfo!!.roomInfo.room_id)
//                            OnlineListActivity.starOnlineListActivity(mContext, detailRoomInfo!!.roomInfo.room_id)
                        }

                        override fun CSMclick(view: TextView) {

                        }

                        override fun BMclick(view: TextView) {//闭麦
                            if ("0".equals(is_oc_wheat)) {
                                OcWheat(room_id, micPosition, "2", userId)
                            } else {
                                OcWheat(room_id, micPosition, "1", userId)
                            }
                        }
                    })
                    seatMicDialog.show()
                } else { //麦位有人  切不是自己
                    if (!"0".equals(userId) && !(SpUtils.getSp(mContext, "uid")).equals(userId)) {
                        val giveDialog = GiveDialog(mContext, userId, room_id!!)
                        giveDialog.findViewById<LinearLayout>(R.id.ll_isShow).visibility = View.GONE
                        giveDialog.findViewById<LinearLayout>(R.id.ll_bbs).visibility = View.GONE
                        giveDialog.setGiveClickListener(object : GiveDialog.GiveClickListener {

                            override fun BmClick() {

                            }

                            override fun BtxmClick() {

                            }

                            override fun ScmClick() {

                            }

                            override fun clickGiveGift(uid: String, nickname: String) {
                                showGiftBagDialog(userId, nickname)
                            }

                            override fun clickPrivateChat() {
                                RongIM.getInstance().startPrivateChat(mContext, userId, micNickname);
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

    private fun JumpMic(userId: String?, mic: Int, tomicPosition: Int) {
        NetWork.getService(ImpService::class.java)
                .upDownWheat(userId!!, room_id!!, mic.toString(), "2")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CommonBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: CommonBean?) {
                        if (t!!.code == 0) {
                            roomManager!!.getRoomDetailInfo(room_id, object : ResultCallback<DetailRoomInfo> {
                                override fun onSuccess(roomDetailInfo: DetailRoomInfo?) {
                                    if (roomDetailInfo != null) {
                                        detailRoomInfo = roomDetailInfo
                                        for (bean in roomDetailInfo.micPositions) {
                                            if (!AuthManager.getInstance().currentUserId.equals(bean.uid)) {
                                                img_simle.visibility = View.GONE
                                            }
                                        }
                                        updateMicSeatState(roomDetailInfo.micPositions)
                                        val controlMessage = MicPositionControlMessage()
                                        controlMessage.setCmd(7)//下麦
                                        controlMessage.targetUserId = userId
                                        controlMessage.micPositions = roomDetailInfo.micPositions
                                        controlMessage.targetPosition = mic

                                        val obtain = Message.obtain(room_id, Conversation.ConversationType.CHATROOM, controlMessage)

                                        RongIMClient.getInstance().sendMessage(obtain, null, null, object : IRongCallback.ISendMessageCallback {
                                            override fun onAttached(p0: Message?) {
                                                Log.d("tag===", p0!!.content.toString())
                                            }

                                            override fun onSuccess(p0: Message?) {
                                                JoinMic(userId, tomicPosition)
                                            }

                                            override fun onError(p0: Message?, p1: RongIMClient.ErrorCode?) {
                                                Log.d("tag===", p0!!.content.toString())
                                            }
                                        });
                                    }
                                }

                                override fun onFail(errorCode: Int) {
                                }
                            })


                        }
                    }

                    override fun onCompleted() {
                    }
                })

    }

    /**
     * 闭麦 - 开麦
     */
    private fun OcWheat(room_id: String?, micPosition: Int, s: String, userId: String) {
        val map = HashMap<String, String>()
        map.put("room_id", room_id!!)
        map.put("sort_id", micPosition.toString())
        map.put("type", s)
        val body = RetrofitUtil.createJsonRequest(map)
        NetWork.getService(ImpService::class.java)
                .oc_wheat(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CommonBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: CommonBean?) {
                        if (t!!.code == 0) {
                            roomManager!!.getRoomDetailInfo(room_id, object : ResultCallback<DetailRoomInfo> {
                                override fun onSuccess(roomDetailInfo: DetailRoomInfo?) {
                                    if (roomDetailInfo != null) {
                                        updateMicSeatState(roomDetailInfo.micPositions)
                                        val controlMessage = MicPositionControlMessage()
                                        if ("1".equals(s)) {//闭麦
                                            controlMessage.setCmd(3)//禁麦
                                        } else {//开麦
                                            controlMessage.setCmd(4)//开麦
                                        }
                                        controlMessage.targetUserId = userId
                                        controlMessage.micPositions = roomDetailInfo.micPositions
                                        controlMessage.targetPosition = micPosition
                                        val obtain = Message.obtain(room_id, Conversation.ConversationType.CHATROOM, controlMessage)
                                        RongIMClient.getInstance().sendMessage(obtain, null, null, object : IRongCallback.ISendMessageCallback {
                                            override fun onAttached(p0: Message?) {

                                            }

                                            override fun onSuccess(p0: Message?) {
                                                Log.d("SM:MPCtrlMsg=BM=", p0!!.content.toString())
                                            }

                                            override fun onError(p0: Message?, p1: RongIMClient.ErrorCode?) {

                                            }
                                        });

                                    }
                                }

                                override fun onFail(errorCode: Int) {
                                }
                            })
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }

    /**
     * 锁麦 - 解麦
     */
    private fun LockWheat(room_id: String?, micPosition: Int, s: String, userId: String) {
        val map = HashMap<String, String>()
        map.put("room_id", room_id!!)
        map.put("sort_id", micPosition.toString())
        map.put("type", s)
        val body = RetrofitUtil.createJsonRequest(map)
        NetWork.getService(ImpService::class.java)
                .lock_wheat(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CommonBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: CommonBean?) {
                        if (t!!.code == 0) {
                            roomManager!!.getRoomDetailInfo(room_id, object : ResultCallback<DetailRoomInfo> {
                                override fun onSuccess(roomDetailInfo: DetailRoomInfo?) {
                                    if (roomDetailInfo != null) {
//                                        detailRoomInfo = roomDetailInfo
                                        updateMicSeatState(roomDetailInfo.micPositions)
                                        val controlMessage = MicPositionControlMessage()
                                        if ("1".equals(s)) {//锁麦
                                            controlMessage.setCmd(1)//锁麦
                                        } else {//开麦
                                            controlMessage.setCmd(2)//开麦
                                        }
                                        controlMessage.targetUserId = userId
                                        controlMessage.micPositions = roomDetailInfo.micPositions
                                        controlMessage.targetPosition = micPosition

                                        val obtain = Message.obtain(room_id, Conversation.ConversationType.CHATROOM, controlMessage)
                                        RongIMClient.getInstance().sendMessage(obtain, null, null, object : IRongCallback.ISendMessageCallback {
                                            override fun onAttached(p0: Message?) {
                                                Log.d("tag", p0!!.content.toString())
                                            }

                                            override fun onSuccess(p0: Message?) {
                                                Log.d("SM:MPCtrlMsg=SCM=", p0!!.content.toString())
                                            }

                                            override fun onError(p0: Message?, p1: RongIMClient.ErrorCode?) {
                                                Log.d("tag", p1!!.message)
                                            }
                                        });
                                    }
                                }

                                override fun onFail(errorCode: Int) {
                                }
                            })

                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }

    //踢出房间
    private fun ExitCurrentRoom(userId: String, leave: String, room_id: String, micNickname: String, micIcon: String) {
        val message = RoomMemberChangedMessage()
        message.setCmd(3)//踢出房间
        message.targetUserId = userId
        message.targetPosition = -1
        message.userInfo = io.rong.imlib.model.UserInfo(SpUtils.getSp(mContext, "uid"), micNickname, Uri.parse(micIcon))
        val obtain = Message.obtain(room_id, Conversation.ConversationType.CHATROOM, message)
        RongIMClient.getInstance().sendMessage(obtain, null, null, object : IRongCallback.ISendMessageCallback {
            override fun onAttached(p0: Message?) {

            }

            override fun onSuccess(p0: Message?) {
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
                                }
                            }

                            override fun onCompleted() {
                            }
                        })

            }

            override fun onError(p0: Message?, p1: RongIMClient.ErrorCode?) {

            }
        });
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
                            val adminChangeMessage = RoomAdminChangeMessage()
                            adminChangeMessage.cmd = "2"
                            adminChangeMessage.isAdmin = userId
                            val obtain = Message.obtain(room_id, Conversation.ConversationType.CHATROOM, adminChangeMessage)

                            RongIMClient.getInstance().sendMessage(obtain, null, null, object : IRongCallback.ISendMessageCallback {
                                override fun onAttached(p0: Message?) {
                                    Log.d("tag", p0!!.content.toString())
                                }

                                override fun onSuccess(p0: Message?) {
                                    roomManager!!.getRoomDetailInfo(room_id, object : ResultCallback<DetailRoomInfo> {
                                        override fun onSuccess(roomDetailInfo: DetailRoomInfo?) {
                                            if (roomDetailInfo != null) {
                                                updateMicSeatState(roomDetailInfo.micPositions)
                                                detailRoomInfo = roomDetailInfo
                                            }
                                        }

                                        override fun onFail(errorCode: Int) {
                                        }
                                    })

                                }

                                override fun onError(p0: Message?, p1: RongIMClient.ErrorCode?) {
                                    Log.d("tag", p0!!.content.toString())//23409
                                }
                            });

                            updateRoomInfo()
                        }
                    }

                    override fun onCompleted() {
                    }
                })


    }

    //设置管理员和添加黑名单
    private fun BlackManager(userId: String, room_id: String, type: String, micNickname: String, micIcon: String) {
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
                            if ("1".equals(type)) {
                                ExitCurrentRoom(userId, "2", room_id, micNickname, micIcon)
                            } else {//设置管理员  需要发自定义消息
                                val adminChangeMessage = RoomAdminChangeMessage()
                                adminChangeMessage.cmd = "1"
                                adminChangeMessage.isAdmin = userId
                                val obtain = Message.obtain(room_id, Conversation.ConversationType.CHATROOM, adminChangeMessage)

                                RongIMClient.getInstance().sendMessage(obtain, null, null, object : IRongCallback.ISendMessageCallback {
                                    override fun onAttached(p0: Message?) {
                                        Log.d("tag", p0!!.content.toString())
                                    }

                                    override fun onSuccess(p0: Message?) {
                                        roomManager!!.getRoomDetailInfo(room_id, object : ResultCallback<DetailRoomInfo> {
                                            override fun onSuccess(roomDetailInfo: DetailRoomInfo?) {
                                                if (roomDetailInfo != null) {
                                                    updateMicSeatState(roomDetailInfo.micPositions)
                                                    detailRoomInfo = roomDetailInfo
                                                }
                                            }

                                            override fun onFail(errorCode: Int) {
                                            }
                                        })

                                    }

                                    override fun onError(p0: Message?, p1: RongIMClient.ErrorCode?) {
                                        Log.d("tag", p0!!.content.toString())//23409
                                    }
                                });

                            }
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
                    UpdateOwner(roomDetailInfo)
                    detailRoomInfo = roomDetailInfo
                    detailRoomInfo!!.roomInfo.setMemCount(roomDetailInfo.roomInfo.getMemCount())
                    updateMicSeatState(roomDetailInfo.micPositions)
                    updateRoomTitle(roomDetailInfo.roomInfo.room_name, roomDetailInfo.roomInfo.room_num, detailRoomInfo!!.roomInfo.memCount.toString())
                    //设置房主信息
//                    updataRoomManager()
                    if ("1".equals(roomDetailInfo!!.roomInfo.room_pure)) {//纯净模式：0 关闭；1 开启
                        tv_room_manager.visibility = View.VISIBLE
                        tv_room_manager.setText(roomDetailInfo.roomInfo.gifts)
                        UpdataTips(roomDetailInfo!!.getMicPositions(), false)
                    } else {
                        tv_room_manager.visibility = View.GONE
                        UpdataTips(roomDetailInfo!!.getMicPositions(), true)
                    }
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
        val message = RoomMemberChangedMessage()
        message.setCmd(2)//离开房间
        message.targetUserId = uid
        message.targetPosition = -1
        message.userInfo = io.rong.imlib.model.UserInfo(SpUtils.getSp(mContext, "uid"), nikeName, Uri.parse(icon))
        val obtain = Message.obtain(room_id, Conversation.ConversationType.CHATROOM, message)

        RongIMClient.getInstance().sendMessage(obtain, null, null, object : IRongCallback.ISendMessageCallback {
            override fun onAttached(p0: Message?) {
                Log.d("tag", p0!!.content.toString())
            }

            override fun onSuccess(p0: Message?) {
                NetWork.getService(ImpService::class.java)
                        .tCRoom(uid, kick, room_id!!)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<CommonBean> {
                            override fun onError(e: Throwable?) {
                                showToastMessage(e!!.message.toString())
                            }

                            override fun onNext(t: CommonBean?) {
                                showToastMessage(t!!.msg)
                                if (t.code == 0) {
                                    IMClient.getInstance().quitChatRoom(room_id, null)
                                    RtcClient.getInstance().quitRtcRoom(room_id, null)
                                    removeUser(room_id!!, uid)

//                                    val intent = Intent(mContext, PlayMusicService::class.java)
//                                    stopService(intent)
//                                    unbindService(connection)

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

    private fun RandomQuiteRoom(uid: String, kick: String, toString: String) {
        val message = RoomMemberChangedMessage()
        message.setCmd(2)//离开房间
        message.targetUserId = uid
        message.targetPosition = -1
        message.userInfo = io.rong.imlib.model.UserInfo(SpUtils.getSp(mContext, "uid"), nikeName, Uri.parse(icon))
        val obtain = Message.obtain(room_id, Conversation.ConversationType.CHATROOM, message)

        RongIMClient.getInstance().sendMessage(obtain, null, null, object : IRongCallback.ISendMessageCallback {
            override fun onAttached(p0: Message?) {
                Log.d("tag", p0!!.content.toString())
            }

            override fun onSuccess(p0: Message?) {
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
                                    IMClient.getInstance().quitChatRoom(room_id, null)
                                    RtcClient.getInstance().quitRtcRoom(room_id, null)
                                    val map = HashMap<String, String>()
                                    map.put("room_id", room_id!!)
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
                                                        joinChatRoom(toString)
                                                        finish()
                                                    }
                                                }

                                                override fun onCompleted() {
                                                }
                                            })


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

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            if (!Settings.canDrawOverlays(this)) {
                showToastMessage("授权失败")
            } else {
                val customeWindow = CustomeWindow(BaseApplication.mContext!!, detailRoomInfo!!.roomInfo.icon)
                customeWindow.show()
                finish()
            }
        }
        if (requestCode == 1033 && resultCode == 1035) {
            val userId = data!!.getStringExtra("userId")
            JoinMic(userId, micPosition)
        }
        if (requestCode == 1999 && resultCode == 1000) {
            val room_desc = data!!.getStringExtra("room_desc")
            updataRoomTalk(room_desc)
        }
    }

    var preMicPosition = -1
    /**
     * 加入麦位
     */
    private fun JoinMic(userId: String, mic: Int) {
        preMicPosition = mic
        NetWork.getService(ImpService::class.java)
                .upDownWheat(userId, room_id!!, mic.toString(), "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CommonBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: CommonBean?) {
                        if (t!!.code == 0) {
                            roomManager!!.getRoomDetailInfo(room_id, object : ResultCallback<DetailRoomInfo> {
                                override fun onSuccess(roomDetailInfo: DetailRoomInfo?) {
                                    if (roomDetailInfo != null) {
                                        updateMicSeatState(roomDetailInfo.micPositions)
                                        detailRoomInfo = roomDetailInfo
                                        for (bean in roomDetailInfo.micPositions) {
                                            if (AuthManager.getInstance().currentUserId.equals(bean.uid)) {
                                                img_simle.visibility = View.VISIBLE
                                                tv_music.visibility = View.VISIBLE
                                            }
                                        }

                                        updateMicSeatState(roomDetailInfo.micPositions)
                                        val controlMessage = MicPositionControlMessage()
                                        controlMessage.setCmd(6)//上麦
                                        controlMessage.targetUserId = userId
                                        controlMessage.micPositions = roomDetailInfo.micPositions
                                        controlMessage.targetPosition = mic

                                        val obtain = Message.obtain(room_id, Conversation.ConversationType.CHATROOM, controlMessage)

                                        RongIMClient.getInstance().sendMessage(obtain, null, null, object : IRongCallback.ISendMessageCallback {
                                            override fun onAttached(p0: Message?) {
                                                Log.d("tag", p0!!.content.toString())
                                            }

                                            override fun onSuccess(p0: Message?) {
                                                Log.d("tag", p0!!.content.toString())
                                            }

                                            override fun onError(p0: Message?, p1: RongIMClient.ErrorCode?) {
                                                Log.d("tag", p1!!.message)
                                            }
                                        });
                                    }
                                }

                                override fun onFail(errorCode: Int) {
                                    showToastMessage("加入麦位失败==")
                                }
                            })

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
                    }

                    override fun onNext(t: CommonBean?) {
                        if (t!!.code == 0) {
                            roomManager!!.getRoomDetailInfo(room_id, object : ResultCallback<DetailRoomInfo> {
                                override fun onSuccess(roomDetailInfo: DetailRoomInfo?) {
                                    if (roomDetailInfo != null) {
                                        updateMicSeatState(roomDetailInfo.micPositions)
                                        detailRoomInfo = roomDetailInfo
                                        for (bean in roomDetailInfo.micPositions) {
                                            if (!AuthManager.getInstance().currentUserId.equals(bean.uid)) {
                                                img_simle.visibility = View.GONE
                                                tv_music.visibility = View.GONE
                                            }
                                        }
                                        updateMicSeatState(roomDetailInfo.micPositions)
                                        val controlMessage = MicPositionControlMessage()
                                        controlMessage.setCmd(7)//下麦
                                        controlMessage.targetUserId = userId
                                        controlMessage.micPositions = roomDetailInfo.micPositions
                                        controlMessage.targetPosition = mic

                                        val obtain = Message.obtain(room_id, Conversation.ConversationType.CHATROOM, controlMessage)

                                        RongIMClient.getInstance().sendMessage(obtain, null, null, object : IRongCallback.ISendMessageCallback {
                                            override fun onAttached(p0: Message?) {
                                                Log.d("tag===", p0!!.content.toString())
                                            }

                                            override fun onSuccess(p0: Message?) {
                                                Log.d("tag===", p0!!.content.toString())
                                            }

                                            override fun onError(p0: Message?, p1: RongIMClient.ErrorCode?) {
                                                Log.d("tag===", p0!!.content.toString())
                                            }
                                        });
                                    }
                                }

                                override fun onFail(errorCode: Int) {
                                }
                            })
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }

    private fun joinChatRoom(roomId: String) {
        RoomManager.getInstance().joinRoom(SpUtils.getSp(mContext, "uid"), roomId, "", object : ResultCallback<DetailRoomInfo> {
            override fun onSuccess(result: DetailRoomInfo?) {

                val message = RoomMemberChangedMessage()
                message.setCmd(1)
                message.targetUserId = SpUtils.getSp(mContext, "uid")
                val carName = SharedPreferencesUtils.getCarName(mContext)
                val carPic = SharedPreferencesUtils.getCarPic(mContext)
                message.carName = carName//座驾名称
                message.carPic = carPic//座驾图片
                message.targetPosition = -1
                message.userInfo = io.rong.imlib.model.UserInfo(SpUtils.getSp(mContext, "uid"), mydatabean!!.data.nickname, Uri.parse(mydatabean!!.data.icon))
                val obtain = Message.obtain(result!!.roomInfo.room_id, Conversation.ConversationType.CHATROOM, message)
                RongIMClient.getInstance().sendMessage(obtain, null, null, object : IRongCallback.ISendMessageCallback {
                    override fun onAttached(p0: Message?) {
                        Log.d("tag", p0!!.content.toString())
                    }

                    override fun onSuccess(p0: Message?) {
                        Log.d("tag", p0!!.content.toString())
                        finish()
                        ChatRoomActivity.starChatRoomActivity(mContext, roomId, mydatabean!!.data.nickname, mydatabean!!.data.icon)
                    }

                    override fun onError(p0: Message?, p1: RongIMClient.ErrorCode?) {
                        Log.d("tag", p0!!.content.toString())
                    }
                });
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
//            ll_isShow.visibility = View.VISIBLE
//            ll_send.visibility = View.GONE
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
            chatroom_et_chat_input.setFocusable(true);
            chatroom_et_chat_input.setFocusableInTouchMode(true);
            chatroom_et_chat_input.requestFocus();//获取焦点 光标出现
            ll_isShow.visibility = View.GONE
            ll_send.visibility = View.VISIBLE
            layoutParams.bottomMargin = height + defaultMarginBottom
        } else {
            if (SpUtils.getSp(mContext, "uid").equals(detailRoomInfo!!.roomInfo.uid) || amdinType == 1) {
                ll_isShow.visibility = View.VISIBLE
                ll_send.visibility = View.GONE
                et_chat.visibility = View.GONE

            } else {
                ll_send.visibility = View.GONE
                img_send_msg.visibility = View.GONE
                chatroom_setting.visibility = View.GONE
                ll_isShow.visibility = View.VISIBLE
            }

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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (!Settings.canDrawOverlays(mContext)) {
                        startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")), 1)
                    } else {
                        val customeWindow = CustomeWindow(BaseApplication.mContext!!, detailRoomInfo!!.roomInfo.icon)
                        customeWindow.show()
                        finish()
                    }
                } else {
                    val customeWindow = CustomeWindow(BaseApplication.mContext!!, detailRoomInfo!!.roomInfo.icon)
                    customeWindow.show()
                    finish()
                }
            }

            override fun clickReport() {
                ReportActivity.starReportActivity(mContext, room_id!!)
            }
        })
        homeSettingDialog.show()
    }

    var fair: String? = null

    override fun onResume() {
        super.onResume()
        /*
       * 当用户切出应用时可能会有其他应用更改音频的输出模式
       * 所以当应用切回前台时切换音频输出模式
       */
        changeAudioOutputMode()

        roomManager!!.getRoomDetailInfo1(room_id, object : ResultCallback<DetailRoomInfo> {
            override fun onSuccess(result: DetailRoomInfo?) {
                if (result != null) {
                    updateMicSeatState(result.micPositions)

                    val room_gift_tx = result.roomInfo.room_gift_tx
                    fair = result.roomInfo.room_is_fair
                    val is_lock = result.roomInfo.is_lock
                    val pure = result.roomInfo.room_pure

                    if ("0".equals(room_gift_tx)) {//关闭
                        tv_room_manager.visibility = View.GONE
                        UpdataTips(detailRoomInfo!!.getMicPositions(), false)
                    } else {//开启
                        tv_room_manager.visibility = View.VISIBLE
                        tv_room_manager.setText(result.roomInfo.gifts)
                        UpdataTips(detailRoomInfo!!.getMicPositions(), true)
                    }

                    if ("0".equals(is_lock)) {
                        img_w_lock.visibility = View.GONE
                    } else {
                        img_w_lock.visibility = View.VISIBLE
                    }
                    if ("0".equals(fair)) {//是否开启公屏
                        chatroom_list_chat.visibility = View.VISIBLE
                    } else {
                        chatroom_list_chat.visibility = View.GONE
                    }
                    if ("0".equals(pure)) {//纯净模式
                        img_gold_egg.visibility = View.VISIBLE
                    } else {
                        img_gold_egg.visibility = View.GONE
                    }
                }
            }

            override fun onFail(errorCode: Int) {
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this);
        unregisterReceiver(headsetPlugReceiver)
        unregisterReceiver(bgChangBroadCast)
        if (audioManager != null) {
            audioManager!!.setMode(AudioManager.MODE_NORMAL)
        }
        if (telephonyManager != null && roomPhoneStateListener != null) {
            telephonyManager!!.listen(roomPhoneStateListener, TelephonyManager.PHONE_TYPE_NONE)
        }
        if (playMusicService != null) {
            playMusicService!!.stop()
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

    fun showPositionGiftWindowManager(position: Int, toPosition: Int, giftImgUrl: String, staticImgUrl: String) {
        val manager = mContext!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val params = WindowManager.LayoutParams()
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.MATCH_PARENT
        params.format = PixelFormat.TRANSPARENT

        val svgaImageView = SVGAImageView(this)
        svgaImageView.setBackgroundColor(Color.TRANSPARENT)
        val parser = SVGAParser(this)
        val layoutParams = WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        svgaImageView.layoutParams = layoutParams
        manager.addView(svgaImageView, params)
        //        parser.decodeFromAssets("hj.svga", object : SVGAParser.ParseCompletion {
//            override fun onComplete(videoItem: SVGAVideoEntity) {
//                svgaImageView.setVideoItem(videoItem)
//                svgaImageView.stepToFrame(0, true)
//            }
//
//            override fun onError() {
//            }
//        })
    }

    fun showPositionGift(position: Int, toPosition: List<Int>, giftImgUrl: String, staticImgUrl: String) {
        val f = findViewById<FrameLayout>(android.R.id.content)
        if (giftImgUrl.endsWith(".svga")) {
            //val giftImgUrl = "http://chitchat.rhhhyy.com/mengjing.svga"
            //显示View

            //加载svga
            val SDPATH = Environment.getExternalStorageDirectory().absolutePath + "/" + AppUtil.getCurrentPageName(mContext) + "/svags/"
            val file = File(SDPATH)
            if (!file.exists()) {
                file.mkdirs()
            }
            val index = giftImgUrl!!.lastIndexOf("/")
            val destFileName = giftImgUrl.substring(index, giftImgUrl.length)
            val downloadFile = File(SDPATH + destFileName)
            if (!downloadFile.exists()) {
                Log.e("tag", "动画不存在,进行下载！downloadFile:" + downloadFile)
                //文件不存在进行下载
                downloadGift(giftImgUrl, object : OnDownLoadComplteListener {
                    override fun complete(path: String?) {
                        runOnUiThread(Runnable {
                            //直接显示
                            try {
                                //移除之后创建分开View
                                val halfWidth = (AppUtil.getScreenWidth(mContext) / 2 - DisplayUtils.dp2px(mContext, 40)).toFloat()
                                val halfHeight = (AppUtil.getScreenHeight(mContext) / 2).toFloat()
                                for (i in toPosition) {
                                    val childImageView = ImageView(mContext)
                                    Glide.with(mContext).load(staticImgUrl).into(childImageView)
                                    val layoutParams = FrameLayout.LayoutParams(DisplayUtils.dp2px(mContext, 80), DisplayUtils.dp2px(mContext, 80))
                                    childImageView.layoutParams = layoutParams;
                                    childImageView.x = halfWidth
                                    childImageView.y = halfHeight
                                    f.addView(childImageView)

                                    val startPosition = getPosition(position)
                                    val endPosition = getPosition(i)
                                    val childOffsetX = endPosition[0] - halfWidth + DisplayUtils.dp2px(mContext, 10)
                                    val childOffsetY = endPosition[1] - halfHeight + DisplayUtils.dp2px(mContext, 5)

                                    ViewCompat.animate(childImageView)
                                            .translationXBy(childOffsetX.toFloat())
                                            .translationYBy(childOffsetY.toFloat())
                                            .setDuration(800)
                                            .setListener(object : ViewPropertyAnimatorListener {
                                                override fun onAnimationStart(view: View) {
                                                }

                                                override fun onAnimationEnd(view: View) {
                                                    //动画结束后删除大礼物图
                                                    f.removeView(childImageView)
                                                }

                                                override fun onAnimationCancel(view: View) {
                                                }
                                            })
                                }

                                Handler(Looper.getMainLooper()).postDelayed({
                                    //分发动画结束之后 显示特效
                                    val svgaImageView = SVGAImageView(mContext)
                                    val parser = SVGAParser(mContext)
                                    f.addView(svgaImageView)
                                    val inputStream = FileInputStream(downloadFile)
                                    parser.decodeFromInputStream(inputStream, destFileName, object : SVGAParser.ParseCompletion {
                                        override fun onComplete(videoItem: SVGAVideoEntity) {
                                            val s = videoItem.frames / videoItem.FPS
                                            Log.e("tag", "数据时长:" + s);
                                            svgaImageView.setVideoItem(videoItem)
                                            svgaImageView.stepToFrame(0, true)
                                            Handler(Looper.getMainLooper()).postDelayed({
                                                //移除特效动画
                                                f.removeView(svgaImageView)
                                            }, (s * 1000).toLong())
                                        }

                                        override fun onError() {
                                        }
                                    }, true)
                                }, (800).toLong())
                            } catch (e: FileNotFoundException) {
                                e.printStackTrace()
                            }
                        })
                    }
                })
                return
            } else {
                Log.e("tag", "动画存在,直接显示！downloadFile:" + downloadFile)
                //直接显示
                try {
                    //移除之后创建分开View
                    val halfWidth = (AppUtil.getScreenWidth(mContext) / 2 - DisplayUtils.dp2px(mContext, 40)).toFloat()
                    val halfHeight = (AppUtil.getScreenHeight(mContext) / 2).toFloat()
                    for (i in toPosition) {
                        val childImageView = ImageView(mContext)
                        Glide.with(mContext).load(staticImgUrl).into(childImageView)
                        val layoutParams = FrameLayout.LayoutParams(DisplayUtils.dp2px(mContext, 80), DisplayUtils.dp2px(mContext, 80))
                        childImageView.layoutParams = layoutParams;
                        childImageView.x = halfWidth
                        childImageView.y = halfHeight
                        f.addView(childImageView)

                        val startPosition = getPosition(position)
                        val endPosition = getPosition(i)
                        val childOffsetX = endPosition[0] - halfWidth + DisplayUtils.dp2px(mContext, 10)
                        val childOffsetY = endPosition[1] - halfHeight + DisplayUtils.dp2px(mContext, 5)

                        ViewCompat.animate(childImageView)
                                .translationXBy(childOffsetX.toFloat())
                                .translationYBy(childOffsetY.toFloat())
                                .setDuration(800)
                                .setListener(object : ViewPropertyAnimatorListener {
                                    override fun onAnimationStart(view: View) {
                                    }

                                    override fun onAnimationEnd(view: View) {
                                        //动画结束后删除大礼物图
                                        f.removeView(childImageView)
                                    }

                                    override fun onAnimationCancel(view: View) {
                                    }
                                })
                    }

                    Handler(Looper.getMainLooper()).postDelayed({
                        //分发动画结束之后 显示特效
                        val svgaImageView = SVGAImageView(this)
                        val parser = SVGAParser(this)
                        f.addView(svgaImageView)
                        val inputStream = FileInputStream(downloadFile)
                        parser.decodeFromInputStream(inputStream, destFileName, object : SVGAParser.ParseCompletion {
                            override fun onComplete(videoItem: SVGAVideoEntity) {
                                val s = videoItem.frames / videoItem.FPS
                                Log.e("tag", "数据时长:" + s);
                                svgaImageView.setVideoItem(videoItem)
                                svgaImageView.stepToFrame(0, true)
                                Handler(Looper.getMainLooper()).postDelayed({
                                    //移除特效动画
                                    f.removeView(svgaImageView)
                                }, (s * 1000).toLong())
                            }

                            override fun onError() {
                            }
                        }, true)
                    }, (800).toLong())
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
        } else {
            //静态图
            val imageView = ImageView(this)
            val layoutParams = FrameLayout.LayoutParams(DisplayUtils.dp2px(mContext, 80), DisplayUtils.dp2px(mContext, 80))
            imageView.layoutParams = layoutParams;
            Glide.with(mContext).load(giftImgUrl).into(imageView)
            f.addView(imageView)
            val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
            val w = (AppUtil.getScreenWidth(mContext) / 2 - DisplayUtils.dp2px(mContext, 40)).toFloat()
            val h = (AppUtil.getScreenHeight(mContext) / 2).toFloat()
            valueAnimator.addUpdateListener { animation ->
                val value = animation.animatedValue as Float
                if (position == -1) {
                    //不在麦位上 从顶部到中间 再分到对应的人
                    imageView.x = w
                    imageView.y = h * value
                } else {
                    //在麦上 从麦位到中间 再分到对应的人
                    val startPosition = getPosition(position)
                    val offsetX = (w - startPosition[0]) * value;
                    val offsetY = (h - startPosition[1]) * value;

                    imageView.x = startPosition[0] + offsetX;
                    imageView.y = startPosition[1] + offsetY;
                }
            }
            valueAnimator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    f.removeView(imageView)
                    //移除之后创建分开View
                    val halfWidth = (AppUtil.getScreenWidth(mContext) / 2 - DisplayUtils.dp2px(mContext, 40)).toFloat()
                    val halfHeight = (AppUtil.getScreenHeight(mContext) / 2).toFloat()

                    for (i in toPosition) {
                        val childImageView = ImageView(mContext)
                        Glide.with(mContext).load(staticImgUrl).into(childImageView)
                        val layoutParams = FrameLayout.LayoutParams(DisplayUtils.dp2px(mContext, 80), DisplayUtils.dp2px(mContext, 80))
                        childImageView.layoutParams = layoutParams;
                        childImageView.x = halfWidth
                        childImageView.y = halfHeight
                        f.addView(childImageView)

                        val startPosition = getPosition(position)
                        val endPosition = getPosition(i)
                        val childOffsetX = endPosition[0] - halfWidth + DisplayUtils.dp2px(mContext, 10)
                        val childOffsetY = endPosition[1] - halfHeight + DisplayUtils.dp2px(mContext, 5)

                        Log.e("tag", "halfWidth:" + halfWidth + "i:" + i + "position:" + position + "endPosition[0]:" + endPosition[0] + "endPosition[1]:" + endPosition[1] + " startPosition[0]:" + startPosition[0] + "startPosition[1]:" + startPosition[1] + "offsetX:" + childOffsetX + "offsetY:" + childOffsetY);
                        ViewCompat.animate(childImageView)
                                .translationXBy(childOffsetX.toFloat())
                                .translationYBy(childOffsetY.toFloat())
                                .setDuration(800)
                                .setListener(object : ViewPropertyAnimatorListener {
                                    override fun onAnimationStart(view: View) {
                                    }

                                    override fun onAnimationEnd(view: View) {
                                        //动画结束后删除大礼物图
                                        f.removeView(childImageView)
                                    }

                                    override fun onAnimationCancel(view: View) {
                                    }
                                })
                    }
                }
            })
            valueAnimator.repeatCount = 0
            valueAnimator.duration = 800
            valueAnimator.start()
        }
//        if (giftImgUrl.endsWith(".gif")) {
//            //显示动态图
//            Glide.with(this).asGif().listener(object : RequestListener<GifDrawable> {
//                override fun onLoadFailed(e: GlideException?, model: Any, target: Target<GifDrawable>, isFirstResource: Boolean): Boolean {
//                    return false
//                }
//
//                override fun onResourceReady(resource: GifDrawable, model: Any, target: Target<GifDrawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
//                    try {
//                        val gifStateField = GifDrawable::class.java.getDeclaredField("state")
//                        gifStateField.isAccessible = true
//                        val gifStateClass = Class.forName("com.bumptech.glide.load.resource.gif.GifDrawable\$GifState")
//                        val gifFrameLoaderField = gifStateClass.getDeclaredField("frameLoader")
//                        gifFrameLoaderField.isAccessible = true
//                        val gifFrameLoaderClass = Class.forName("com.bumptech.glide.load.resource.gif.GifFrameLoader")
//                        val gifDecoderField = gifFrameLoaderClass.getDeclaredField("gifDecoder")
//                        gifDecoderField.isAccessible = true
//                        val gifDecoderClass = Class.forName("com.bumptech.glide.gifdecoder.GifDecoder")
//                        val gifDecoder = gifDecoderField.get(gifFrameLoaderField.get(gifStateField.get(resource)))
//                        val getDelayMethod = gifDecoderClass.getDeclaredMethod("getDelay", Int::class.javaPrimitiveType!!)
//                        getDelayMethod.isAccessible = true
//                        //设置只播放一次
//                        resource.setLoopCount(1)
//                        //获得总帧数
//                        val count = resource.frameCount
//                        var delay = 0
//                        for (i in 0 until count) {
//                            //计算每一帧所需要的时间进行累加
//                            delay += getDelayMethod.invoke(gifDecoder, i) as Int
//                        }
//                        giftImageView.postDelayed({
//                            ViewCompat.animate(giftImageView).scaleX(0f).scaleY(0f).alpha(0f).setDuration(300).setListener(object : ViewPropertyAnimatorListener {
//                                override fun onAnimationStart(view: View) {
//
//                                }
//
//                                override fun onAnimationEnd(view: View) {
//                                    //动画结束后删除大礼物图
//                                    f.removeView(v)
//                                    val w = chatroom_mp_mic_1.width;
//                                    val h = chatroom_mp_mic_1.height;
//                                    val imgW = DisplayUtils.dp2px(mContext, 44);
//                                    val imgH = DisplayUtils.dp2px(mContext, 44);
//                                    val imageOffsetX = w / 2 - imgW / 2;
//                                    val imageOffsetY = h / 2 - imgH / 2;
//
//                                    val startPosition = getPosition(position);
//                                    val endPosition = getPosition(toPosition);
//                                    val offsetX = endPosition[0] - startPosition[0];
//                                    val offsetY = endPosition[1] - startPosition[1];
//
//                                    val f = findViewById<FrameLayout>(android.R.id.content)
//                                    val childView = View.inflate(mContext, R.layout.view_emj, null)
//                                    val childImageView = childView.findViewById<ImageView>(R.id.gifView)
//                                    Glide.with(mContext).load(staticImgUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(childImageView)
//
//                                    childView.x = (startPosition[0] + imageOffsetX).toFloat();
//                                    childView.y = (startPosition[1] + imageOffsetY).toFloat();
//
//                                    val layoutParams = FrameLayout.LayoutParams(DisplayUtils.dp2px(mContext, 44), DisplayUtils.dp2px(mContext, 44))
//                                    childView.layoutParams = layoutParams
//                                    f.addView(childView)
//
//                                    Log.e("tag", "x:" + offsetX.toFloat() + " y:" + offsetY.toFloat())
//                                    ViewCompat.animate(childView).translationXBy(offsetX.toFloat()).translationYBy(offsetY.toFloat()).setDuration(1000).setListener(object : ViewPropertyAnimatorListener {
//                                        override fun onAnimationStart(view: View) {
//                                        }
//
//                                        override fun onAnimationEnd(view: View) {
//                                            //动画结束后删除大礼物图
//                                            f.removeView(childView)
//                                        }
//
//                                        override fun onAnimationCancel(view: View) {
//                                        }
//                                    })
//                                }
//
//                                override fun onAnimationCancel(view: View) {
//
//                                }
//                            })
//                        }, delay.toLong())
//                    } catch (e: NoSuchFieldException) {
//                        e.printStackTrace()
//                    } catch (e: ClassNotFoundException) {
//                        e.printStackTrace()
//                    } catch (e: IllegalAccessException) {
//                        e.printStackTrace()
//                    } catch (e: NoSuchMethodException) {
//                        e.printStackTrace()
//                    } catch (e: InvocationTargetException) {
//                        e.printStackTrace()
//                    }
//                    return false
//                }
//            }).diskCacheStrategy(DiskCacheStrategy.RESOURCE).load(giftImgUrl).into(giftImageView)
//        } else {
//            //显示静态图
//            Glide.with(this).load(giftImgUrl).into(giftImageView);
//            giftImageView.postDelayed({
//                ViewCompat.animate(giftImageView).scaleX(0f).scaleY(0f).alpha(0f).setDuration(300).setListener(object : ViewPropertyAnimatorListener {
//                    override fun onAnimationStart(view: View) {
//                    }
//
//                    override fun onAnimationEnd(view: View) {
//                        //动画结束后删除大礼物图
//                        f.removeView(v)
//                        val w = chatroom_mp_mic_1.width;
//                        val h = chatroom_mp_mic_1.height;
//                        val imgW = DisplayUtils.dp2px(mContext, 44);
//                        val imgH = DisplayUtils.dp2px(mContext, 44);
//                        val imageOffsetX = w / 2 - imgW / 2;
//                        val imageOffsetY = h / 2 - imgH / 2;
//
//                        val startPosition = getPosition(position);
//                        val endPosition = getPosition(toPosition);
//                        val offsetX = endPosition[0] - startPosition[0];
//                        val offsetY = endPosition[1] - startPosition[1];
//
//                        val f = findViewById<FrameLayout>(android.R.id.content)
//                        val childView = View.inflate(mContext, R.layout.view_emj, null)
//                        val childImageView = childView.findViewById<ImageView>(R.id.gifView)
//                        Glide.with(mContext).load(staticImgUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(childImageView)
//
//                        childView.x = (startPosition[0] + imageOffsetX).toFloat();
//                        childView.y = (startPosition[1] + imageOffsetY).toFloat();
//
//                        val layoutParams = FrameLayout.LayoutParams(DisplayUtils.dp2px(mContext, 44), DisplayUtils.dp2px(mContext, 44))
//                        childView.layoutParams = layoutParams
//                        f.addView(childView)
//
//                        Log.e("tag", "x:" + offsetX.toFloat() + " y:" + offsetY.toFloat())
//                        ViewCompat.animate(childView).translationXBy(offsetX.toFloat()).translationYBy(offsetY.toFloat()).setDuration(1000).setListener(object : ViewPropertyAnimatorListener {
//                            override fun onAnimationStart(view: View) {
//                            }
//
//                            override fun onAnimationEnd(view: View) {
//                                //动画结束后删除大礼物图
//                                f.removeView(childView)
//                            }
//
//                            override fun onAnimationCancel(view: View) {
//                            }
//                        })
//                    }
//
//                    override fun onAnimationCancel(view: View) {
//
//                    }
//                })
//            }, 2000)
//        }
    }

    private fun downloadGift(url: String?, listener: OnDownLoadComplteListener) {
        val SDPATH = Environment.getExternalStorageDirectory().absolutePath + "/" + AppUtil.getCurrentPageName(mContext) + "/svags/"
        val index = url!!.lastIndexOf("/")
        val destFileName = url.substring(index, url.length)
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(object : FileCallBack(SDPATH, destFileName) {
                    override fun onError(call: Call, e: Exception, id: Int) {
                    }

                    override fun inProgress(progress: Float, total: Long, id: Int) {}

                    override fun onResponse(file: File, id: Int) {
                        if (listener != null) {
                            listener.complete(file.path);
                        }
                    }
                })
    }

    interface OnDownLoadComplteListener {
        fun complete(path: String?);
    }

    /**
     * 获取坐标
     */
    fun getPosition(p: Int): IntArray {
        val pos = IntArray(2)
        when (p) {
            -1 -> {
                pos[0] = AppUtil.getScreenWidth(mContext) / 2 - DisplayUtils.dp2px(mContext, 32);
                pos[1] = AppUtil.getScreenHeight(mContext) / 2
            }
            0 -> {
                chatroom_mp_mic_1!!.getLocationOnScreen(pos)
            }
            1 -> {
                chatroom_mp_mic_2.getLocationOnScreen(pos)
            }
            2 -> {
                chatroom_mp_mic_3.getLocationOnScreen(pos)
            }
            3 -> {
                chatroom_mp_mic_4.getLocationOnScreen(pos)
            }
            4 -> {
                chatroom_mp_mic_5.getLocationOnScreen(pos)
            }
            5 -> {
                chatroom_mp_mic_6.getLocationOnScreen(pos)
            }
            6 -> {
                chatroom_mp_mic_7.getLocationOnScreen(pos)
            }
            7 -> {
                chatroom_mp_mic_8.getLocationOnScreen(pos)
            }
            8 -> {
                img_head.getLocationOnScreen(pos)
            }
        }
        return pos
    }

    var shareListener = object : UMShareListener {
        override fun onResult(p0: SHARE_MEDIA?) {
            showToastMessage("分享成功")
        }

        override fun onCancel(p0: SHARE_MEDIA?) {
            showToastMessage("分享取消了")
        }

        override fun onError(p0: SHARE_MEDIA?, p1: Throwable?) {
        }

        override fun onStart(p0: SHARE_MEDIA?) {
        }
    }

    /**
     * 显示砸蛋通知
     * @param name 砸蛋人
     * @param num  中奖数量
     * @param gold 中奖内容  金币/礼物名称
     */
    fun showEggNotifyCation(name: String, num: String, gold: String) {
//        val imageView = ImageView(this)
//        val layoutParams = FrameLayout.LayoutParams(DisplayUtils.dp2px(mContext, 80), DisplayUtils.dp2px(mContext, 80))
//        imageView.layoutParams = layoutParams;
//        Glide.with(mContext).load(giftImgUrl).into(imageView)
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
                            finish()
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }

    var musicbean: EventBusBean? = null
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent1(bean: EventBusBean) {
        musicbean = bean
        startService()
    }

    private fun startService() {
        val intent = Intent(mContext, PlayMusicService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    var playMusicService: PlayMusicService? = null
    var connection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val myBind = service as (PlayMusicService.MyBind)
            playMusicService = myBind.service
            playMusicService!!.setData(musicbean!!.list, musicbean!!.position)
        }
    }
}
