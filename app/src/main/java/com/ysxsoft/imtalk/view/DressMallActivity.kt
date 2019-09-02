package com.ysxsoft.imtalk.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import android.view.View
import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.R.id.*
import com.ysxsoft.imtalk.adapter.DressMallAdapter
import com.ysxsoft.imtalk.bean.CommonBean
import com.ysxsoft.imtalk.bean.DressMallBean
import com.ysxsoft.imtalk.bean.UserInfoBean
import com.ysxsoft.imtalk.fragment.CarFragment
import com.ysxsoft.imtalk.fragment.HeadwearFragment
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.*
import kotlinx.android.synthetic.main.dress_mall_layout.*
import kotlinx.android.synthetic.main.w_translation_title_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers
import java.lang.ref.WeakReference

/**
 *Create By 胡
 *on 2019/7/15 0015
 */
class DressMallActivity : BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.dress_mall_layout
    }

    var currentFragment: BaseFragment? = null

    private var tagP = "0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//dress_mall_item_layout  theme_frame_bg
        setLightStatusBar(false)
        initStatusBar(topView)
        tv_title_right.visibility = View.VISIBLE
        tv_title_right.setText("我的装扮")
        setTitle("装扮商城")
        setBackVisibily()
        initView()
        PersonData()
    }

    private fun PersonData() {
        NetWork.getService(ImpService::class.java)
                .GetUserInfo(SpUtils.getSp(mContext, "uid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<UserInfoBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: UserInfoBean?) {
                        if (t!!.code == 0) {
                            ImageLoadUtil.GlideHeadImageLoad(mContext, t.data.icon, img_head)
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }

    private fun initView() {
        replaceFragment()
        tv_headwear.setOnClickListener {
            tagP = "0"
            if (tv_headwear.isSelected) {
                return@setOnClickListener
            }
            replaceFragment()
        }
        tv_car.setOnClickListener {
            tagP = "1"
            if (tv_car.isSelected) {
                return@setOnClickListener
            }
            replaceFragment()
        }
        tv_title_right.setOnClickListener {
            startActivity(MyDressActivity::class.java)
        }
    }
    private fun replaceFragment() {
        tv_headwear.isSelected = tagP.equals("0")
        tv_car.isSelected = tagP.equals("1")
        if (currentFragment != null) {
            supportFragmentManager.beginTransaction().hide(currentFragment!!).commit()
        }
        currentFragment = supportFragmentManager.findFragmentByTag(tagP) as BaseFragment?
        if (currentFragment == null) {
            when (tagP) {
                "0" -> currentFragment = HeadwearFragment()
                "1" -> currentFragment = CarFragment()
            }
            currentFragment?.let { supportFragmentManager.beginTransaction().add(R.id.fm, it, tagP).commit() }
        } else {
            supportFragmentManager.beginTransaction().show(currentFragment!!).commit()
        }

    }
}