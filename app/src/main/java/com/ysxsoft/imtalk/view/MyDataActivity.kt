package com.ysxsoft.imtalk.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.R.mipmap.myself
import com.ysxsoft.imtalk.bean.UserInfoBean
import com.ysxsoft.imtalk.fragment.*
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.*
import kotlinx.android.synthetic.main.my_data_layout.*
import kotlinx.android.synthetic.main.w_translation_title_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *Create By 胡
 *on 2019/7/15 0015
 */
class MyDataActivity : BaseActivity() {

    companion object {
        fun startMyDataActivity(mContext: Context, uid: String, myself: String) {
            val intent = Intent(mContext, MyDataActivity::class.java)
            intent.putExtra("uid", uid)
            intent.putExtra("myself", myself)
            mContext.startActivity(intent)
        }
    }

    override fun getLayout(): Int {
        return R.layout.my_data_layout
    }

    private var tagP = "0"
    var uid: String? = null
    var myself: String? = null
    var mydatagiftfragment = MyDataGiftFragment()
    var mydatacarfragment = MyDataCarFragment()
    var mydatafragment = MyDataFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uid = intent.getStringExtra("uid")
        myself = intent.getStringExtra("myself")
        if ("myself".equals(myself)) {
            tv_za_ta.visibility = View.GONE
            tv_t_room.visibility = View.GONE
            img_right.visibility = View.VISIBLE
            setTitle("我的资料")
        } else {
            tv_za_ta.visibility = View.VISIBLE
            tv_t_room.visibility = View.VISIBLE
            img_right.visibility = View.GONE
            setTitle("TA的资料")
        }

        img_right.setImageResource(R.mipmap.img_edittext)

        setBackVisibily()
        initStatusBar(topView)
        setLightStatusBar(false)
        initView()

    }

    override fun onResume() {
        super.onResume()
        requestData()
    }

    private fun requestData() {
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
                            tv_nikeName.setText(t.data.nickname)
                            tv_id.setText("ID:" + t.data.tt_id)
                            tv_fance.setText(t.data.fans.toString())
                            tv_foucs.setText(t.data.gzrs.toString())
                            tv_zs.setText(t.data.user_level.toString())
                            tv_m.setText("魅" + t.data.charm_level.toString())
                            when (t.data.sex) {
                                "1" -> {
                                    img_sex.setImageResource(R.mipmap.img_boy)
                                }
                                "2" -> {
                                    img_sex.setImageResource(R.mipmap.img_girl)
                                }
                            }
                        }
                    }

                    override fun onCompleted() {
                    }
                })

    }

    private fun initView() {
        img_right.setOnClickListener {
            startActivity(PersonDataActivity::class.java)
        }
        val bundle = Bundle();
        bundle.putString("uid", uid);
        bundle.putString("myself", myself);
        mydatafragment.setArguments(bundle);//数据传递到fragment中
        replaceFragment()
        tv_data.setOnClickListener {
            if (tv_data.isSelected) {
                return@setOnClickListener
            }
            val bundle = Bundle();
            bundle.putString("uid", uid);
            bundle.putString("myself", myself);
            mydatafragment.setArguments(bundle);//数据传递到fragment中
            tagP = "0"
            replaceFragment()
        }

        tv_gift.setOnClickListener {
            if (tv_gift.isSelected) {
                return@setOnClickListener
            }
            val bundle = Bundle();
            bundle.putString("uid", uid);
            bundle.putString("myself", myself);
            mydatagiftfragment.setArguments(bundle);//数据传递到fragment中

            tagP = "1"
            replaceFragment()
        }

        tv_car.setOnClickListener {
            if (tv_car.isSelected) {
                return@setOnClickListener
            }
            val bundle = Bundle();
            bundle.putString("uid", uid);
            bundle.putString("myself", myself);
            mydatacarfragment.setArguments(bundle);//数据传递到fragment中
            tagP = "2"
            replaceFragment()
        }

    }

    private var currentFragment: BaseFragment? = null

    private fun replaceFragment() {
        tv_data.isSelected = tagP.equals("0")
        tv_gift.isSelected = tagP.equals("1")
        tv_car.isSelected = tagP.equals("2")

        if (currentFragment != null) {
            supportFragmentManager.beginTransaction().hide(currentFragment!!).commit()
        }
        currentFragment = supportFragmentManager.findFragmentByTag(tagP) as BaseFragment?
        if (currentFragment == null) {
            when (tagP) {
                "0" -> currentFragment = mydatafragment
                "1" -> currentFragment = mydatagiftfragment
                "2" -> currentFragment = mydatacarfragment
            }
            currentFragment?.let { supportFragmentManager.beginTransaction().add(R.id.fm, it, tagP).commit() }
        } else {
            supportFragmentManager.beginTransaction().show(currentFragment!!).commit()
        }

    }
}