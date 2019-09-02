package com.ysxsoft.imtalk.adapter.fgpageradapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class FgVpAdapter<T : Fragment>(fm: FragmentManager, mList: List<FgTableBean<T>>) : FragmentPagerAdapter(fm) {

    private val mList: List<FgTableBean<T>>? = mList

    override fun getItem(position: Int): T? {
        return mList?.get(position)?.getFragment()
    }

    override fun getCount(): Int {
        if (mList != null) {
            return mList.size
        }
        return 0
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mList?.get(position)?.getTitle()
    }

}