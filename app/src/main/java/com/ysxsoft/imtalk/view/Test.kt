//package cn.rongcloud.im.ui.activity
//
//import android.content.Context
//import android.content.Intent
//import android.os.Bundle
//import android.text.TextUtils
//import android.util.Log
//import android.view.KeyEvent
//import android.view.View
//import android.view.inputmethod.InputMethodManager
//
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentManager
//import androidx.fragment.app.FragmentTransaction
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.Observer
//import androidx.lifecycle.ViewModelProviders
//
//import java.util.Locale
//
//import cn.rongcloud.im.R
//import cn.rongcloud.im.common.IntentExtra
//import cn.rongcloud.im.common.ThreadManager
//import cn.rongcloud.im.im.message.SealGroupConNtfMessage
//import cn.rongcloud.im.model.Resource
//import cn.rongcloud.im.model.ScreenCaptureResult
//import cn.rongcloud.im.model.Status
//import cn.rongcloud.im.model.TypingInfo
//import cn.rongcloud.im.ui.fragment.ConversationFragmentEx
//import cn.rongcloud.im.ui.view.AnnouceView
//import cn.rongcloud.im.ui.view.SealTitleBar
//import cn.rongcloud.im.utils.ScreenCaptureUtil
//import cn.rongcloud.im.utils.log.SLog
//import cn.rongcloud.im.viewmodel.ConversationViewModel
//import io.rong.callkit.util.SPUtils
//import io.rong.imkit.RongContext
//import io.rong.imkit.RongIM
//import io.rong.imkit.RongKitIntent
//import io.rong.imlib.IRongCallback
//import io.rong.imlib.RongIMClient
//import io.rong.imlib.model.Conversation
//import io.rong.imlib.model.Message
//
///**
// * 会话页面
// */
//class ConversationActivity : TitleBaseActivity() {
//
//    private val TAG = ConversationActivity::class.java.simpleName
//    private var fragment: ConversationFragmentEx? = null
//    private var annouceView: AnnouceView? = null
//    private var conversationViewModel: ConversationViewModel? = null
//    private var title: String? = null
//    private val screenCaptureStatus: Int = 0
//    /**
//     * 对方id
//     */
//    private var targetId: String? = null
//    /**
//     * 会话类型
//     */
//    private var conversationType: Conversation.ConversationType? = null
//    private var screenCaptureUtil: ScreenCaptureUtil? = null
//
//    protected fun onCreate(savedInstanceState: Bundle) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.conversation_activity_conversation)
//
//        // 没有intent 的则直接返回
//        val intent = getIntent()
//        if (intent == null || intent!!.getData() == null) {
//            finish()
//            return
//        }
//
//        targetId = intent!!.getData()!!.getQueryParameter("targetId")
//        conversationType = Conversation.ConversationType.valueOf(intent!!.getData()!!
//                .getLastPathSegment()!!.toUpperCase(Locale.US))
//        title = intent!!.getData()!!.getQueryParameter("title")
//
//        initView()
//        initViewModel()
//    }
//
//    protected fun onResume() {
//        super.onResume()
//        getTitleStr(targetId, conversationType, title)
//        refreshScreenCaptureStatus()
//    }
//
//    protected fun onPause() {
//        super.onPause()
//        if (screenCaptureUtil != null) {
//            screenCaptureUtil!!.unRegister()
//        }
//    }
//
//    private fun refreshScreenCaptureStatus() {
//        //私聊或者群聊才开启功能
//        if (conversationType == Conversation.ConversationType.PRIVATE || conversationType == Conversation.ConversationType.GROUP) {
//            val cacheType = SPUtils.get(this, "ScreenCaptureStatus", 0) as Int
//            //1为开启，0为关闭
//            if (cacheType == 1) {
//                if (screenCaptureUtil == null) {
//                    initScreenShotListener()
//                } else {
//                    screenCaptureUtil!!.register()
//                }
//            } else if (cacheType == 0) {
//                if (screenCaptureUtil != null) {
//                    screenCaptureUtil!!.unRegister()
//                }
//            }
//        }
//    }
//
//    private fun initScreenShotListener() {
//        screenCaptureUtil = ScreenCaptureUtil(this)
//        screenCaptureUtil!!.setScreenShotListener(object : ScreenCaptureUtil.ScreenShotListener() {
//            fun onScreenShotComplete(data: String, dateTaken: Long) {
//                SLog.d(TAG, "onScreenShotComplete===")
//                ThreadManager.getInstance().runOnUIThread(Runnable {
//                    //在主线程注册 observeForever 因为截屏时候可能使得 activity 处于 pause 状态，无法发送消息
//                    val result = conversationViewModel!!.sendScreenShotMsg(conversationType!!.value, targetId)
//                    result.observeForever(object : Observer<Resource<Void>>() {
//                        fun onChanged(voidResource: Resource<Void>) {
//                            if (voidResource.status === Status.SUCCESS) {
//                                result.removeObserver(this)
//                                SLog.d(TAG, "sendScreenShotMsg===Success")
//                            } else if (voidResource.status === Status.ERROR) {
//                                result.removeObserver(this)
//                                SLog.d(TAG, "sendScreenShotMsg===Error")
//                            }
//                        }
//                    })
//                })
//            }
//        })
//        screenCaptureUtil!!.register()
//    }
//
//    private fun initViewModel() {
//        conversationViewModel = ViewModelProviders.of(this, ConversationViewModel.Factory(targetId, conversationType, title, this.getApplication())).get(ConversationViewModel::class.java)
//
//        conversationViewModel!!.getTitleStr().observe(this, object : Observer<String>() {
//            fun onChanged(title: String) {
//                if (TextUtils.isEmpty(title)) {
//                    if (conversationType == null) {
//                        return
//                    }
//                    val titleResId: Int
//                    if (conversationType == Conversation.ConversationType.DISCUSSION) {
//                        titleResId = R.string.seal_conversation_title_discussion_group
//                    } else if (conversationType == Conversation.ConversationType.SYSTEM) {
//                        titleResId = R.string.seal_conversation_title_system
//                    } else if (conversationType == Conversation.ConversationType.CUSTOMER_SERVICE) {
//                        titleResId = R.string.seal_conversation_title_feedback
//                    } else {
//                        titleResId = R.string.seal_conversation_title_defult
//                    }
//                    getTitleBar().setTitle(titleResId)
//
//                } else {
//                    getTitleBar().setTitle(title)
//                }
//            }
//        })
//
//        // 正在输入状态
//        conversationViewModel!!.getTypingStatusInfo().observe(this, object : Observer<TypingInfo>() {
//            fun onChanged(typingInfo: TypingInfo?) {
//                if (typingInfo == null) {
//                    return
//                }
//
//                if (typingInfo!!.conversationType === conversationType && typingInfo!!.targetId.equals(targetId)) {
//                    if (typingInfo!!.typingList == null) {
//                        getTitleBar().setType(SealTitleBar.Type.NORMAL)
//                    } else {
//                        val typing = typingInfo!!.typingList.get(typingInfo!!.typingList.size() - 1)
//                        getTitleBar().setType(SealTitleBar.Type.TYPING)
//                        if (typing.type === TypingInfo.Typing.Type.text) {
//                            getTitleBar().setTyping(R.string.seal_conversation_remote_side_is_typing)
//                        } else if (typing.type === TypingInfo.Typing.Type.voice) {
//                            getTitleBar().setTyping(R.string.seal_conversation_remote_side_speaking)
//                        }
//                    }
//                }
//            }
//        })
//
//        // 群 @ 跳转
//        conversationViewModel!!.getGroupAt().observe(this, object : Observer<String>() {
//            fun onChanged(s: String) {
//                // 跳转选择界面
//                val intent = Intent(RongContext.getInstance(), MemberMentionedExActivity::class.java)
//                intent.putExtra("conversationType", conversationType!!.value)
//                intent.putExtra("targetId", targetId)
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                startActivity(intent)
//            }
//        })
//
//        conversationViewModel!!.getScreenCaptureStatus(conversationType!!.value, targetId).observe(this, object : Observer<Resource<ScreenCaptureResult>>() {
//            fun onChanged(screenCaptureResultResource: Resource<ScreenCaptureResult>) {
//                if (screenCaptureResultResource.status === Status.SUCCESS) {
//                    // 0 关闭 1 打开
//                    refreshScreenCaptureStatus()
//                }
//            }
//        })
//    }
//
//    /**
//     * 获取 title
//     *
//     * @param targetId
//     * @param conversationType
//     * @param title
//     */
//    private fun getTitleStr(targetId: String?, conversationType: Conversation.ConversationType?, title: String?) {
//        if (conversationViewModel != null) {
//            conversationViewModel!!.getTitleByConversation(targetId, conversationType, title)
//        }
//    }
//
//
//    private fun initView() {
//        initTitleBar(conversationType!!, targetId)
//        initAnnouceView()
//        initConversationFragment()
//    }
//
//    private fun initConversationFragment() {
//        /**
//         * 加载会话界面 。 ConversationFragmentEx 继承自 ConversationFragment
//         */
//        val fragmentManager = getSupportFragmentManager()
//        val existFragment = fragmentManager.findFragmentByTag(ConversationFragmentEx::class.java!!.getCanonicalName())
//        if (existFragment != null) {
//            fragment = existFragment as ConversationFragmentEx
//            val transaction = fragmentManager.beginTransaction()
//            transaction.show(fragment)
//            transaction.commitAllowingStateLoss()
//        } else {
//            fragment = ConversationFragmentEx()
//            // 自定义的服务才会有通知监听
//            if (conversationType == Conversation.ConversationType.CUSTOMER_SERVICE) {
//                // 设置通知监听
//                fragment!!.setOnShowAnnounceBarListener(object : ConversationFragmentEx.OnShowAnnounceListener() {
//                    fun onShowAnnounceView(announceMsg: String, announceUrl: String) {
//                        annouceView!!.setVisibility(View.VISIBLE)
//                        annouceView!!.setAnnounce(announceMsg, announceUrl)
//                    }
//                })
//            }
//
//            val transaction = fragmentManager.beginTransaction()
//            transaction.add(R.id.rong_content, fragment, ConversationFragmentEx::class.java!!.getCanonicalName())
//            transaction.commitAllowingStateLoss()
//        }
//
//    }
//
//    /**
//     * 通知布局
//     */
//    private fun initAnnouceView() {
//        // 初始化通知布局
//        annouceView = findViewById(R.id.view_annouce)
//        annouceView!!.setVisibility(View.GONE)
//        annouceView!!.setOnAnnounceClickListener(object : AnnouceView.OnAnnounceClickListener() {
//            fun onClick(v: View, url: String) {
//                var str = url.toLowerCase()
//                if (!TextUtils.isEmpty(str)) {
//                    if (!str.startsWith("http") && !str.startsWith("https")) {
//                        str = "http://$str"
//                    }
//                    val intent = Intent(RongKitIntent.RONG_INTENT_ACTION_WEBVIEW)
//                    intent.setPackage(v.context.packageName)
//                    intent.putExtra("url", str)
//                    v.context.startActivity(intent)
//                }
//            }
//        })
//    }
//
//    /**
//     * 初始化 title
//     *
//     * @param conversationType
//     * @param targetId
//     */
//    private fun initTitleBar(conversationType: Conversation.ConversationType, targetId: String?) {
//        // title 布局设置
//        // 左边返回按钮
//        getTitleBar().setOnBtnLeftClickListener(View.OnClickListener {
//            if (fragment != null && !fragment!!.onBackPressed()) {
//                if (fragment!!.isLocationSharing()) {
//                    fragment!!.showQuitLocationSharingDialog(this@ConversationActivity)
//                    return@OnClickListener
//                }
//                hintKbTwo()
//            }
//            finish()
//        })
//
//
//        getTitleBar().getBtnRight().setOnClickListener(View.OnClickListener { toDetailActivity(conversationType, targetId) })
//
//        if (conversationType == Conversation.ConversationType.GROUP) {
//            getTitleBar().getBtnRight().setImageDrawable(getResources().getDrawable(R.drawable.seal_detail_group))
//        } else if (conversationType == Conversation.ConversationType.PRIVATE
//                || conversationType == Conversation.ConversationType.PUBLIC_SERVICE
//                || conversationType == Conversation.ConversationType.APP_PUBLIC_SERVICE
//                || conversationType == Conversation.ConversationType.DISCUSSION) {
//            getTitleBar().getBtnRight().setImageDrawable(getResources().getDrawable(R.drawable.seal_detail_single))
//        } else {
//            getTitleBar().getBtnRight().setVisibility(View.GONE)
//        }
//    }
//
//    /**
//     * 根据 targetid 和 ConversationType 进入到设置页面
//     */
//    private fun toDetailActivity(conversationType: Conversation.ConversationType, targetId: String?) {
//
//        if (conversationType == Conversation.ConversationType.PUBLIC_SERVICE || conversationType == Conversation.ConversationType.APP_PUBLIC_SERVICE) {
//
//            RongIM.getInstance().startPublicServiceProfile(this, conversationType, targetId)
//        } else if (conversationType == Conversation.ConversationType.PRIVATE) {
//            val intent = Intent(this, PrivateChatSettingActivity::class.java)
//            intent.putExtra(IntentExtra.STR_TARGET_ID, targetId)
//            intent.putExtra(IntentExtra.SERIA_CONVERSATION_TYPE, Conversation.ConversationType.PRIVATE)
//            startActivity(intent)
//        } else if (conversationType == Conversation.ConversationType.GROUP) {
//            val intent = Intent(this, GroupDetailActivity::class.java)
//            intent.putExtra(IntentExtra.STR_TARGET_ID, targetId)
//            intent.putExtra(IntentExtra.SERIA_CONVERSATION_TYPE, Conversation.ConversationType.GROUP)
//            startActivity(intent)
//        } else if (conversationType == Conversation.ConversationType.DISCUSSION) {
//
//        }
//    }
//
//    fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
//        if (KeyEvent.KEYCODE_BACK == event.keyCode) {
//            if (fragment != null && !fragment!!.onBackPressed()) {
//                if (fragment!!.isLocationSharing()) {
//                    fragment!!.showQuitLocationSharingDialog(this)
//                    return true
//                }
//                finish()
//            }
//        }
//        return false
//    }
//
//    private fun hintKbTwo() {
//        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        if (imm.isActive && getCurrentFocus() != null) {
//            if (getCurrentFocus().getWindowToken() != null) {
//                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)
//            }
//        }
//    }
//
//    fun clearAllFragmentExistBeforeCreate() {}
//}
