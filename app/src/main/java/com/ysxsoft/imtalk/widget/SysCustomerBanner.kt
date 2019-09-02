package com.ysxsoft.imtalk.widget

import android.content.Context
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.SysMessageBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import kotlinx.android.synthetic.main.sys_customer_layout.view.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *Create By 胡
 *on 2019/8/23 0023
 */
class SysCustomerBanner : AbsLinearLayout {

    var mContext: Context? = null

    override val layoutResId: Int
        get() = R.layout.sys_customer_layout

    override fun initView() {
//        QBadgeView(mContext).bindTarget(tv_point).hide(false)
//        QBadgeView(mContext).bindTarget(tv_point)
////                    .setBadgeBackgroundColor(R.color.btn_color)
////                    .setBadgeTextColor(R.color.white)
//                .setBadgeGravity(Gravity.CENTER or Gravity.END)
//                .setBadgeNumber(99)
    }

    constructor(mContext: Context) : super(mContext) {
        this.mContext = mContext
        requestData(mContext)
    }

    private fun requestData(mContext: Context) {
        NetWork.getService(ImpService::class.java)
                .sysmessage(SpUtils.getSp(mContext, "uid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<SysMessageBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: SysMessageBean?) {
                        if (t!!.code == 0) {
                            ImageLoadUtil.GlideHeadImageLoad(mContext,t.data.userInfo.get(0).icon,img_head)
                            tv_custome.setText(t.data.userInfo.get(0).nickname)
//                            tv_custome.setText(t.data.userInfo.get(0).nickname)
                            ImageLoadUtil.GlideHeadImageLoad(mContext,t.data.userInfo.get(1).icon,img_sys_head)
                            tv_sys.setText(t.data.userInfo.get(1).nickname)
//                            tv_custome.setText(t.data.userInfo.get(0).nickname)
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }
}