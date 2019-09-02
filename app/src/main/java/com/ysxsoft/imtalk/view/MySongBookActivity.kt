package com.ysxsoft.imtalk.view

import android.content.Context
import android.content.Intent
import android.support.design.widget.TabLayout
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.adapter.fgpageradapter.FgTableBean
import com.ysxsoft.imtalk.adapter.fgpageradapter.FgVpAdapter
import com.ysxsoft.imtalk.fragment.MySongBookFragment
import com.ysxsoft.imtalk.fragment.SharingMusicFragment
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.BaseFragment
import kotlinx.android.synthetic.main.activity_my_song_book.*

/**
 * 我的曲库/共享音乐
 */
class MySongBookActivity : BaseActivity() {
    companion object {

        fun starMySongBookActivity(mContext: Context,roomId:String){
            val intent = Intent(mContext, MySongBookActivity::class.java)
            intent.putExtra("roomId",roomId)
            mContext.startActivity(intent)
        }
    }


    private val titles = arrayOf("我的曲库", "共享音乐")

    override fun getLayout(): Int {
        return R.layout.activity_my_song_book
    }

    override fun initUi() {
        val roomId = intent.getStringExtra("roomId")
        setLightStatusBar(true)
        initToolBar(viewTop)
        setSupportActionBar(toolBar)
        toolBar.setNavigationOnClickListener { finish() }
        val informfragments = ArrayList<FgTableBean<BaseFragment>>()
        tabLayout.addTab(titles[0])
        tabLayout.addTab(titles[1])
        informfragments.add(FgTableBean(MySongBookFragment.newInstance(roomId), titles[0], 0))
        informfragments.add(FgTableBean(SharingMusicFragment.newInstance(roomId), titles[1], 1))
        val fgVpAdapter = FgVpAdapter(supportFragmentManager, informfragments)
        viewPager.adapter = fgVpAdapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout.getTabLayout()))
        tabLayout.setupWithViewPager(viewPager)
    }



}
