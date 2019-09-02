package com.ysxsoft.imtalk.fragment

import android.content.Context
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.MFamilyBean
import com.ysxsoft.imtalk.bean.UserInfo
import com.ysxsoft.imtalk.bean.UserInfoBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.BaseFragment
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import com.ysxsoft.imtalk.view.*
import com.ysxsoft.imtalk.widget.dialog.DatePickerDialog
import kotlinx.android.synthetic.main.fm_my.*
import kotlinx.android.synthetic.main.include_my_top.*
import kotlinx.android.synthetic.main.title_layout2.*
import org.litepal.LitePal
import org.litepal.extension.findAll
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers

/**
 *Create By 胡
 *on 2019/7/3 0003
 */
class MyFragment : BaseFragment() {
    override fun getLayoutResId(): Int {
        return R.layout.fm_my
    }
    private var fm_id: String? = null
    private var is_fmy: String? = null
    override fun onResume() {
        super.onResume()
        initStatusBar(topView)
        img_back.setImageResource(R.mipmap.sign_white)
        setTitle("我的")
        initView()
        requestData()
    }

    private fun requestData() {
        NetWork.getService(ImpService::class.java)
                .GetUserInfo(SpUtils.getSp(mContext, "uid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<UserInfoBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: UserInfoBean?) {
                        if (t!!.code == 0) {
                            val info = UserInfo()
                            info.uid=t.data.uid
                            info.icon=t.data.icon
                            info.nikeName=t.data.nickname
                            info.sex=t.data.sex
                            info.zsl=t.data.user_level.toString()
                            val save = info.save()
                            ImageLoadUtil.GlideHeadImageLoad(mContext, t.data.icon, img_logo)
                            tv_nick.setText(t.data.nickname)
                            tv_id.setText("ID:" + t.data.tt_id)
                            tv_fans.setText(t.data.fans.toString())
                            tv_foucs_on.setText(t.data.gzrs.toString())
                            tv_level.setText(t.data.user_level.toString())
                            tv_mlz.setText(t.data.charm_level.toString())
                        }
                    }

                    override fun onCompleted() {
                    }
                })

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

    }

    private fun initView() {
        img_back.setOnClickListener {
            startActivity(QDActivity::class.java)
//            val dialog = DatePickerDialog(mContext)
//            dialog.setClickDatePicker(object : DatePickerDialog.ClickDatePicker{
//                override fun datePicker(date: String) {
//                    showToastMessage(date)
//                }
//            })
//            dialog.show()
        }

        img_logo.setOnClickListener {
//            startActivity(MyDataActivity::class.java)
            MyDataActivity.startMyDataActivity(mContext,SpUtils.getSp(mContext,"uid"),"myself")
        }
        //我的钱包
        tv1.setOnClickListener {
            startActivity(MoneyBagActivity::class.java)
        }
        //装扮商城
        tv2.setOnClickListener {
            startActivity(DressMallActivity::class.java)
        }
        //实名认证
        tv3.setOnClickListener {
            startActivity(SmrzActivity::class.java)
        }
        //我的家族
        tv4.setOnClickListener {
//            startActivity(MyFamilyActivity::class.java)
            JzData()
        }
        //我的等级
        tv5.setOnClickListener {
            startActivity(MyDjActivity::class.java)
        }
        //我的邀请
        tv6.setOnClickListener {
            startActivity(InviteFriendActivity::class.java)
        }
        //设置
        tv7.setOnClickListener {
            startActivity(SettingActivity::class.java)
        }
        ll_user_levles.setOnClickListener {
            MyDjActivity.starMyDjActivity(mContext,"1")
        }
        ll_ml_levels.setOnClickListener {
            MyDjActivity.starMyDjActivity(mContext,"2")
        }
    }
    private fun JzData() {
        NetWork.getService(ImpService::class.java)
                .mFamily(SpUtils.getSp(mContext,"uid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Action1<MFamilyBean> {
                    override fun call(t: MFamilyBean?) {
                        if (t!!.code==0){
                            fm_id = t.data.id
                            is_fmy = t.data.is_fmy
                            MyFamilyActivity.startMyFamilyActivity(mContext,fm_id!!,is_fmy!!)
                        }else{
                            showToastMessage("您暂未加入任何家族")
                        }
                    }
                })
    }
}