package com.ysxsoft.imtalk.fragment

import android.support.v4.app.Fragment
import android.view.View
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.utils.BaseFragment
import com.ysxsoft.imtalk.view.SearchActivity
import kotlinx.android.synthetic.main.money_title_layout.*

/**
 *Create By 胡
 *on 2019/7/3 0003
 */
class MsgFragment : BaseFragment() {

    override fun getLayoutResId(): Int {
        return R.layout.fm_family
    }

    private var tagP = "0"
    override fun onResume() {
        super.onResume()
        initView()
    }

    private fun initView() {
        initStatusBar(topView)
        img_back.visibility = View.GONE;
        tv_left.text = "消息"
        tv_right.text = "联系人"
        img_right.setImageResource(R.mipmap.search_right)
        replaceFragment()
        //搜索
        img_right.setOnClickListener {
            startActivity(SearchActivity::class.java)
        }

        //发现
        tv_left.setOnClickListener {
            if (tv_left.isSelected) {
                return@setOnClickListener
            }
            tagP = "0"
            replaceFragment()
        }
        //家族
        tv_right.setOnClickListener {
            if (tv_right.isSelected) {
                return@setOnClickListener
            }
            tagP = "1"
            replaceFragment()
        }
    }


    private var currentFragment: Fragment? = null

    private fun replaceFragment() {
        tv_left.isSelected = tagP.equals("0")
        tv_right.isSelected = tagP.equals("1")

        if (currentFragment != null) {
            childFragmentManager.beginTransaction().hide(currentFragment!!).commit()
        }
        currentFragment = childFragmentManager.findFragmentByTag(tagP)
        if (currentFragment == null) {
            when (tagP) {
                "0" -> currentFragment = Msg1Fragment()
                "1" -> currentFragment = Msg2Fragment()
            }
            currentFragment?.let { childFragmentManager.beginTransaction().add(R.id.fm, it, tagP).commit() }
        } else {
            childFragmentManager.beginTransaction().show(currentFragment!!).commit()
        }
    }

}