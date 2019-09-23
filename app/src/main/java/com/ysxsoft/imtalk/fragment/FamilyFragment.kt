package com.ysxsoft.imtalk.fragment

import android.view.View
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.utils.BaseFragment
import kotlinx.android.synthetic.main.money_title_layout.*

/**
 *Create By 胡
 *on 2019/7/3 0003
 */
class FamilyFragment:BaseFragment(){
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
        tv_left.visibility = View.GONE;
        tv_right.visibility = View.GONE;
        tv_left.text = "发现"
        tv_right.text = "家族"
        replaceFragment()

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


    private var currentFragment: BaseFragment? = null

    private fun replaceFragment() {
        tv_left.isSelected = tagP.equals("0")
        tv_right.isSelected = tagP.equals("1")

        if (currentFragment != null) {
            childFragmentManager.beginTransaction().hide(currentFragment!!).commit()
        }
        currentFragment = childFragmentManager.findFragmentByTag(tagP) as BaseFragment?
        if (currentFragment == null) {
            when (tagP) {
                "0" -> currentFragment = FamilyFindFragment()
                "1" -> currentFragment = FamilyJzFragment()
            }
            currentFragment?.let {childFragmentManager.beginTransaction().add(R.id.fm, it, tagP).commit() }
        } else {
            childFragmentManager.beginTransaction().show(currentFragment!!).commit()
        }
    }

}