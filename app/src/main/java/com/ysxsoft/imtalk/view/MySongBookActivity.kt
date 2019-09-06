package com.ysxsoft.imtalk.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.View
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.adapter.fgpageradapter.FgTableBean
import com.ysxsoft.imtalk.adapter.fgpageradapter.FgVpAdapter
import com.ysxsoft.imtalk.fragment.Msg1Fragment
import com.ysxsoft.imtalk.fragment.Msg2Fragment
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
        fun starMySongBookActivity(mContext: Context, roomId: String) {
            val intent = Intent(mContext, MySongBookActivity::class.java)
            intent.putExtra("roomId", roomId)
            mContext.startActivity(intent)
        }
    }


    private val titles = arrayOf("我的曲库", "共享音乐")

    override fun getLayout(): Int {
        return R.layout.activity_my_song_book
    }

    var mySongBroadCast: MySongBroadCast? = null
    var roomId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mySongBroadCast = MySongBroadCast()
        val intentFilter = IntentFilter("CHANGEFRAGMENT")
        registerReceiver(mySongBroadCast, intentFilter);

    }

    inner class MySongBroadCast : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            tabLayout.getTabAt(1)!!.select()
            switchFragment(SharingMusicFragment.newInstance(roomId!!)).commit()
        }
    }

    override fun initUi() {
        roomId = intent.getStringExtra("roomId")
        setLightStatusBar(true)
        initToolBar(viewTop)
        setSupportActionBar(toolBar)
        toolBar.setNavigationOnClickListener { finish() }
        tabLayout.addTab(titles[0])
        tabLayout.addTab(titles[1])

        switchFragment(MySongBookFragment.newInstance(roomId!!)).commit()
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                when (p0!!.position) {
                    0 -> {
                        switchFragment(MySongBookFragment.newInstance(roomId!!)).commit()
                    }
                    1 -> {
                        switchFragment(SharingMusicFragment.newInstance(roomId!!)).commit()
                    }
                }
            }
        })
    }

    var currentFragment: Fragment? = null
    fun switchFragment(targetFragment: Fragment): FragmentTransaction {
        val transaction = supportFragmentManager.beginTransaction()
        if (!targetFragment.isAdded()) {
            //第一次使用switchFragment()时currentFragment为null，所以要判断一下
            if (currentFragment != null) {
                transaction.hide(currentFragment!!);
            }
            transaction.add(R.id.fm, targetFragment, targetFragment.javaClass.name);
        } else {
            transaction.hide(currentFragment!!).show(targetFragment);
        }
        currentFragment = targetFragment;
        return transaction;
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mySongBroadCast)
    }
}
