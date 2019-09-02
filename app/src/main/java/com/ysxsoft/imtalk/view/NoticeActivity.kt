package com.ysxsoft.imtalk.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.fragment.Notice1Fragment
import com.ysxsoft.imtalk.fragment.Notice2Fragment
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.BaseFragment
import com.ysxsoft.imtalk.widget.dialog.HelpPopuwindows
import kotlinx.android.synthetic.main.notice_title_layout.*

/**
 *Create By 胡
 *on 2019/7/27 0027
 */
class NoticeActivity : BaseActivity() {

    companion object {
        fun starNoticeActivity(mContext: Context, tagP: String) {
            val intent = Intent(mContext, NoticeActivity::class.java)
            intent.putExtra("tagP", tagP)
            mContext.startActivity(intent)
        }

    }

    override fun getLayout(): Int {
        return R.layout.notice_layout
    }

    private var tagP = "0"
    private var currentFragment: BaseFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tagP = intent.getStringExtra("tagP")
        setBackVisibily()
        initView()
    }

    private fun initView() {
        replaceFragment()
        tv_title.setOnClickListener {
            if (tv_title.isSelected) {
                return@setOnClickListener
            }
            tagP = "0"
            replaceFragment()
        }
        tv_title1.setOnClickListener {
            if (tv_title1.isSelected) {
                return@setOnClickListener
            }
            tagP = "1"
            replaceFragment()
        }
        img_right.setOnClickListener {
            HelpPopuwindows(mContext, R.layout.pop_layout, img_right)
        }
    }

    private fun replaceFragment() {
        tv_title.isSelected = tagP.equals("0")
        tv_title1.isSelected = tagP.equals("1")

        if (currentFragment != null) {
            supportFragmentManager.beginTransaction().hide(currentFragment!!).commit()
        }
        currentFragment = supportFragmentManager.findFragmentByTag(tagP) as BaseFragment?
        if (currentFragment == null) {
            when (tagP) {
                "0" -> currentFragment = Notice1Fragment()
                "1" -> currentFragment = Notice2Fragment()
            }
            currentFragment?.let { supportFragmentManager.beginTransaction().add(R.id.fm, it, tagP).commit() }
        } else {
            supportFragmentManager.beginTransaction().show(currentFragment!!).commit()
        }
    }
}



