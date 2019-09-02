package com.ysxsoft.imtalk.fragment

import android.support.v4.content.ContextCompat.startActivity
import android.util.Log
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.R.id.*
import com.ysxsoft.imtalk.bean.SysBean
import com.ysxsoft.imtalk.chatroom.net.retrofit.RetrofitUtil
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.BaseFragment
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import com.ysxsoft.imtalk.view.NoticeActivity
import com.ysxsoft.imtalk.view.QDActivity
import kotlinx.android.synthetic.main.fm_family_find.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * 家族-发现
 */
class FamilyFindFragment : BaseFragment() {

    override fun getLayoutResId(): Int {
        return R.layout.fm_family_find
    }

    //设置数据
    var list = ArrayList<String>()
    override fun onResume() {
        super.onResume()
        list.add("端午节米有兑换的小可爱要抓紧时间哦...");
        list.add("小可送给梨克油热气球");
        initView()
        requestData()
    }

    private fun requestData() {
        tv_marque_title.setDatas(list)
        tv_marque_title.startViewAnimator()
    }

    override fun onPause() {
        super.onPause()
        tv_marque_title.stopViewAnimator()
    }
    private fun initView() {
        //土豪榜
        tv1.setOnClickListener {

        }
        //巨星榜
        tv2.setOnClickListener {

        }
        //周星榜
        tv3.setOnClickListener {

        }
        //签到
        tv4.setOnClickListener {
            startActivity(QDActivity::class.java)
        }

        ll_notice.setOnClickListener {
           NoticeActivity.starNoticeActivity(mContext,"1")
        }
        tv_marque_title.setItemOnClickListener { data, position ->
            NoticeActivity.starNoticeActivity(mContext,"1")
        }
        //交友大厅
        tv_look.setOnClickListener {
            NoticeActivity.starNoticeActivity(mContext,"0")
        }
    }
}