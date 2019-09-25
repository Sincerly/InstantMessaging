package com.ysxsoft.imtalk.view

import android.support.v4.app.Fragment
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.fragment.Msg1Fragment
import com.ysxsoft.imtalk.fragment.Msg21Fragment
import com.ysxsoft.imtalk.fragment.Msg22Fragment
import com.ysxsoft.imtalk.fragment.Msg23Fragment
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.BaseFragment
import kotlinx.android.synthetic.main.msg_layout.*

/**
 *Create By 胡
 *on 2019/9/17 0017
 */
class MsgActivity : BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.msg_layout
    }

    var id: String? = null
    private var tagP = "0"

    override fun initUi() {
        setBackVisibily()
        setTitle("消息")
        replaceFragment()

        //消息
        tv0.setOnClickListener {
            if (tv0.isSelected) {
                return@setOnClickListener
            }

            tagP = "0"
            replaceFragment()

        }
        //好友
        tv1.setOnClickListener {
            if (tv1.isSelected) {
                return@setOnClickListener
            }

            tagP = "1"
            replaceFragment()

        }

        //关注
        tv2.setOnClickListener {
            if (tv2.isSelected) {
                return@setOnClickListener
            }
            tagP = "2"
            replaceFragment()
        }

        //粉丝
        tv3.setOnClickListener {
            if (tv3.isSelected) {
                return@setOnClickListener
            }
            tagP = "3"
            replaceFragment()
        }
    }

    private var currentFragment: BaseFragment? = null

    private fun replaceFragment() {
        tv0.isSelected = tagP.equals("0")
        tv1.isSelected = tagP.equals("1")
        tv2.isSelected = tagP.equals("2")
        tv3.isSelected = tagP.equals("3")

        if (currentFragment != null) {
            supportFragmentManager.beginTransaction().hide(currentFragment!!).commit()
        }
        currentFragment = supportFragmentManager.findFragmentByTag(tagP) as BaseFragment?
        if (currentFragment == null) {
            when (tagP) {
                "0" -> currentFragment = Msg1Fragment()
                "1" -> currentFragment = Msg21Fragment()
                "2" -> currentFragment = Msg22Fragment()
                "3" -> currentFragment = Msg23Fragment()
            }
            currentFragment?.let { supportFragmentManager.beginTransaction().add(R.id.fm, it, tagP).commit() }
        } else {
            supportFragmentManager.beginTransaction().show(currentFragment!!).commit()
        }
    }

}