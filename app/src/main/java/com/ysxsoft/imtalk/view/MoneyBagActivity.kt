package com.ysxsoft.imtalk.view

import android.os.Bundle
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.fragment.MoneyJbFragment
import com.ysxsoft.imtalk.fragment.MoneyZsFragment
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.BaseFragment
import kotlinx.android.synthetic.main.money_title_layout.*

class MoneyBagActivity : BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.activity_money_bag
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLightStatusBar(true)
        initStatusBar(topView)
        initView()
    }

    private fun initView() { 
        setBackVisibily()
        replaceFragment("0")

        //钻石
        tv_left.setOnClickListener {
            if (tv_left.isSelected) {
                return@setOnClickListener
            }

            replaceFragment("0")

        }
        //金币
        tv_right.setOnClickListener {
            if (tv_right.isSelected) {
                return@setOnClickListener
            }

            replaceFragment("1")
        }
    }


    private var currentFragment: BaseFragment? = null

    private fun replaceFragment(tag: String) {
        tv_left.isSelected = tag.equals("0")
        tv_right.isSelected = tag.equals("1")

        if (currentFragment != null) {
            getSupportFragmentManager().beginTransaction().hide(currentFragment!!).commit()
        }
        currentFragment = getSupportFragmentManager().findFragmentByTag(tag) as BaseFragment?
        if (currentFragment == null) {
            when (tag) {
                "0" -> currentFragment = MoneyZsFragment()
                "1" -> currentFragment = MoneyJbFragment()
            }
            currentFragment?.let { getSupportFragmentManager().beginTransaction().add(R.id.fm, it, tag).commit() }
        } else {
            getSupportFragmentManager().beginTransaction().show(currentFragment!!).commit()
        }
    }
}
