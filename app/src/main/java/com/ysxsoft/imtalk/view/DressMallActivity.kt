package com.ysxsoft.imtalk.view

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import com.ysxsoft.imtalk.R.mipmap.myself
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

    companion object {
        fun startDressMallActivity(mContext: Context, uid: String,myself: String,nikeName:String) {
            val intent = Intent(mContext, DressMallActivity::class.java)
            intent.putExtra("uid", uid)
            intent.putExtra("myself", myself)
            intent.putExtra("nikeName", nikeName)
            mContext.startActivity(intent)
        }
    }

    override fun getLayout(): Int {
        return R.layout.dress_mall_layout
    }

    var currentFragment: BaseFragment? = null
    var uid: String? = null
    var myself: String? = null
    var nikeName: String? = null
    var myBroadCast: MyBroadCast? = null
    var headwearFragment=HeadwearFragment()
    var carFragment=CarFragment()
    private var tagP = "0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uid = intent.getStringExtra("uid")
        myself = intent.getStringExtra("myself")
        nikeName = intent.getStringExtra("nikeName")
//dress_mall_item_layout  theme_frame_bg
        setLightStatusBar(false)
        initStatusBar(topView)
        tv_title_right.visibility = View.VISIBLE
        tv_title_right.setText("我的装扮")
        setTitle("装扮商城")
        setBackVisibily()
        initView()
        PersonData()
        myBroadCast = MyBroadCast()
        val filter = IntentFilter("HEADWEAR")
        registerReceiver(myBroadCast, filter)

    }

    inner class MyBroadCast : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if ("HEADWEAR".equals(intent!!.action) && !TextUtils.isEmpty("headwarurl")) {
                val headwarurl = intent.getStringExtra("headwarurl")
                ImageLoadUtil.GlideHeadImageLoad(mContext,headwarurl,img_head_wear)
            }
        }
    }

    private fun PersonData() {
        NetWork.getService(ImpService::class.java)
                .GetUserInfo(uid!!)
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
        val bundle = Bundle();
        bundle.putString("myself", myself);
        bundle.putString("uid", uid);
        bundle.putString("nikeName", nikeName);
        headwearFragment!!.setArguments(bundle);//数据传递到fragment中

        tv_headwear.setOnClickListener {
            tagP = "0"
            if (tv_headwear.isSelected) {
                return@setOnClickListener
            }
            replaceFragment()
            val bundle = Bundle();
            bundle.putString("myself", myself);
            bundle.putString("uid", uid);
            bundle.putString("nikeName", nikeName);
            headwearFragment!!.setArguments(bundle);//数据传递到fragment中
        }
        tv_car.setOnClickListener {
            tagP = "1"
            if (tv_car.isSelected) {
                return@setOnClickListener
            }
            replaceFragment()
            val bundle = Bundle();
            bundle.putString("myself", myself);
            bundle.putString("uid", uid);
            bundle.putString("nikeName", nikeName);
            carFragment!!.setArguments(bundle);//数据传递到fragment中
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
                "0" -> currentFragment = headwearFragment
                "1" -> currentFragment = carFragment
            }
            currentFragment?.let { supportFragmentManager.beginTransaction().add(R.id.fm, it, tagP).commit() }
        } else {
            supportFragmentManager.beginTransaction().show(currentFragment!!).commit()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(myBroadCast)
    }
}