package com.ysxsoft.imtalk.fragment

import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.utils.BaseFragment
import kotlinx.android.synthetic.main.fm_msg2.*

/**
 * 消息-联系人
 */
class Msg2Fragment:BaseFragment(){

    override fun getLayoutResId(): Int {
       return R.layout.fm_msg2
    }

    var id:String?=null
    private var tagP = "0"

    override fun onResume() {
        super.onResume()
        initView()
    }

    private fun initView() {
        replaceFragment()

        //好友
        tv1.setOnClickListener {
            if (tv1.isSelected) {
                return@setOnClickListener
            }

            tagP = "0"
            replaceFragment()

        }

        //关注
        tv2.setOnClickListener {
            if (tv2.isSelected) {
                return@setOnClickListener
            }
            tagP = "1"
            replaceFragment()
        }

        //粉丝
        tv3.setOnClickListener {
            if (tv3.isSelected) {
                return@setOnClickListener
            }
            tagP = "2"
            replaceFragment()
        }
    }

    private var currentFragment: BaseFragment? = null

    private fun replaceFragment() {
        tv1.isSelected = tagP.equals("0")
        tv2.isSelected = tagP.equals("1")
        tv3.isSelected = tagP.equals("2")

        if (currentFragment != null) {
            childFragmentManager.beginTransaction().hide(currentFragment!!).commit()
        }
        currentFragment = childFragmentManager.findFragmentByTag(tagP) as BaseFragment?
        if (currentFragment == null) {
            when (tagP) {
                "0" -> currentFragment = Msg21Fragment()
                "1" -> currentFragment = Msg22Fragment()
                "2" -> currentFragment = Msg23Fragment()
            }
            currentFragment?.let {childFragmentManager.beginTransaction().add(R.id.fm, it, tagP).commit() }
        } else {
            childFragmentManager.beginTransaction().show(currentFragment!!).commit()
        }
    }


}