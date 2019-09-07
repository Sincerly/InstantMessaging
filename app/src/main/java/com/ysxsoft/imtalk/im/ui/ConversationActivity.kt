package com.ysxsoft.imtalk.im.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.FragmentActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.SpUtils
import com.ysxsoft.imtalk.view.MyDataActivity
import io.rong.imkit.RongExtension
import io.rong.imkit.RongIM
import io.rong.imkit.fragment.ConversationFragment
import io.rong.imkit.widget.provider.FilePlugin
import io.rong.imlib.model.Conversation
import kotlinx.android.synthetic.main.conversation.*
import kotlinx.android.synthetic.main.fm_family_find.*
import kotlinx.android.synthetic.main.tab_item_layout.*
import kotlinx.android.synthetic.main.title_layout.*
import java.util.*


/**
 *Create By 胡
 *on 2019/8/11 0011
 */
class ConversationActivity : BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.conversation
    }

    var title1: String? = null
    /**
     * 对方id
     */
    private var targetId: String? = null
    /**
     * 会话类型
     */
    private var conversationType: Conversation.ConversationType? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
//        Theme.AppCompat.Light.DarkActionBar
        // 没有intent 的则直接返回
        val intent = intent
        if (intent == null || intent.data == null) {
            finish()
            return
        }
        targetId = intent.data!!.getQueryParameter("targetId")
        conversationType = Conversation.ConversationType.valueOf(intent.data!!.lastPathSegment.toUpperCase(Locale.US))
        title1 = intent.data!!.getQueryParameter("title")
        val fragmentManage = supportFragmentManager
        val fragement = fragmentManage.findFragmentById(R.id.conversation) as ConversationFragment
        val uri = Uri.parse("rong://" + applicationInfo.packageName).buildUpon()
                .appendPath("conversation")
                .appendPath(conversationType!!.getName().toLowerCase())
                .appendQueryParameter("targetId", targetId).build()
        fragement.uri = uri
    }

    override fun initUi() {
        super.initUi()
        initTitleBar(conversationType!!,targetId)
    }

    private fun initTitleBar(conversationType: Conversation.ConversationType, targetId: String?) {
        img_back.setImageResource(R.mipmap.img_black_back)
        img_back.isClickable=true;
        img_back.setOnClickListener(View.OnClickListener {
            finish()
        })
        img_back.visibility=View.VISIBLE
        img_right.setOnClickListener(View.OnClickListener {
            toDetailActivity(conversationType, targetId)
        })
        if (conversationType == Conversation.ConversationType.PRIVATE) {
            img_back.setImageResource(R.mipmap.icon_user_profile)
        }else if(conversationType == Conversation.ConversationType.GROUP){
            img_back.setImageResource(R.mipmap.icon_group_profile)
        }
        img_right.isClickable=true;
        img_right.visibility=View.VISIBLE
        tv_title.text="1111"
    }

    /**
     * 右侧资料按钮点击事件
     */
    private fun toDetailActivity(conversationType: Conversation.ConversationType, targetId: String?) {
        if (conversationType == Conversation.ConversationType.PUBLIC_SERVICE || conversationType == Conversation.ConversationType.APP_PUBLIC_SERVICE) {
            //RongIM.getInstance().startPublicServiceProfile(this, conversationType, targetId)
        } else if (conversationType == Conversation.ConversationType.PRIVATE) {
            if (targetId != null) {
                MyDataActivity.startMyDataActivity(mContext,targetId,"")
            };
           //单聊
        } else if (conversationType == Conversation.ConversationType.GROUP) {
           //群聊
        } else if (conversationType == Conversation.ConversationType.DISCUSSION) {
        }
    }

    private fun hintKbTwo() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
    }

}