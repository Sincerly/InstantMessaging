package com.ysxsoft.imtalk.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.View
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.adapter.fgpageradapter.FgTableBean
import com.ysxsoft.imtalk.adapter.fgpageradapter.FgVpAdapter
import com.ysxsoft.imtalk.chatroom.utils.MyApplication
import com.ysxsoft.imtalk.fragment.HouseItemFragment
import com.ysxsoft.imtalk.fragment.Notice2Fragment
import com.ysxsoft.imtalk.fragment.PalLobbyFragment
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.BaseFragment
import com.ysxsoft.imtalk.widget.dialog.HelpPopuwindows
import io.rong.imlib.model.Conversation
import kotlinx.android.synthetic.main.activity_pal_lobby.*
import kotlinx.android.synthetic.main.fragment_house.*
import kotlinx.android.synthetic.main.fragment_house.img_right
import kotlinx.android.synthetic.main.notice_title_layout.*

private const val ARG_GROUPID = "arg_groupid"
private const val TYPE = "type"

class PalLobbyActivity : BaseActivity() {

    companion object{
        fun intentPalLobbyActivity( groupid : String,type:Int){
            val bundle = Bundle()
            bundle.putString(ARG_GROUPID, groupid)
            bundle.putInt(TYPE, type)
            val intent = Intent(MyApplication.mcontext, PalLobbyActivity::class.java)
            intent.putExtras(bundle)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            MyApplication.mcontext.startActivity(intent)
        }
    }

    private lateinit var groupId : String
    private  var type =0
    private var titles = arrayListOf("交友大厅", "系统公告")

    override fun getLayout(): Int {
        return R.layout.activity_pal_lobby
    }

    override fun initUi() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR//黑色
        groupId = intent.extras?.getString(ARG_GROUPID, "")!!
        type = intent.extras?.getInt(TYPE, 0)!!
        toolBar.setNavigationOnClickListener { finish() }
        ivHelp.setOnClickListener {
            HelpPopuwindows(mContext, R.layout.pop_layout, ivHelp)
        }
        initAdapter()
    }

    private fun initAdapter() {
        val informfragments = ArrayList<FgTableBean<Fragment>>()
            tabTop.addTab(titles[0])
            informfragments.add(FgTableBean(getConverationFragment(Conversation.ConversationType.GROUP), titles[0], 0))
            tabTop.addTab(titles[1])
            informfragments.add(FgTableBean(Notice2Fragment(), titles[1], 1))
        val fgVpAdapter = FgVpAdapter(supportFragmentManager, informfragments)
        viewPager.adapter = fgVpAdapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabTop.getTabLayout()))
        viewPager.currentItem=type
        tabTop.setupWithViewPager(viewPager)
    }


    /**
     * 获取聊天页面
     */
    private fun getConverationFragment(mConversationType : Conversation.ConversationType): PalLobbyFragment{
        val fragment = PalLobbyFragment()
        val uri = Uri.parse("rong://" + applicationInfo.packageName).buildUpon()
                .appendPath("conversation")
                .appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", groupId)
                .build()
        fragment.uri = uri
        return fragment
    }
}
