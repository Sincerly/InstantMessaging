package com.ysxsoft.imtalk.im.ui

import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.FragmentActivity
import android.view.View
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.SpUtils
import io.rong.imkit.RongExtension
import io.rong.imkit.fragment.ConversationFragment
import io.rong.imkit.widget.provider.FilePlugin
import io.rong.imlib.model.Conversation
import kotlinx.android.synthetic.main.conversation.*
import kotlinx.android.synthetic.main.fm_family_find.*
import kotlinx.android.synthetic.main.tab_item_layout.*
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
}