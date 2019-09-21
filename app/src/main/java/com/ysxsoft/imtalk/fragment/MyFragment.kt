package com.ysxsoft.imtalk.fragment

import android.content.Context
import android.text.TextUtils
import android.view.View
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.GetRealInfoBean
import com.ysxsoft.imtalk.bean.MFamilyBean
import com.ysxsoft.imtalk.bean.UserInfo
import com.ysxsoft.imtalk.bean.UserInfoBean
import com.ysxsoft.imtalk.chatroom.net.retrofit.RetrofitUtil
import com.ysxsoft.imtalk.chatroom.task.AuthManager
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.*
import com.ysxsoft.imtalk.view.*
import com.ysxsoft.imtalk.widget.dialog.DatePickerDialog
import io.rong.callkit.util.SPUtils
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
    private var bean: GetRealInfoBean.DataBean? = null
    private var dataBean:UserInfoBean.DataBean?=null
    override fun onResume() {
        super.onResume()
        initStatusBar(topView)
        img_back.visibility=View.GONE
        img_back.setImageResource(R.mipmap.sign_white)
        setTitle("我的")
        initView()
        requestData()
        getData()
    }
    private fun getData() {
        val map = HashMap<String, String>()
        map.put("uid",AuthManager.getInstance().currentUserId)
        val body = RetrofitUtil.createJsonRequest(map)
        NetWork.getService(ImpService::class.java)
                .get_real_info(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Observer<GetRealInfoBean>{
                    override fun onError(e: Throwable?) {

                    }

                    override fun onNext(t: GetRealInfoBean?) {
                        if("0".equals(t!!.code)){
                            bean = t.data
                        }
                    }

                    override fun onCompleted() {
                    }
                })

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
                            dataBean = t.data
                            val info = UserInfo()
                            info.uid = t.data.uid
                            info.icon = t.data.icon
                            info.nikeName = t.data.nickname
                            info.sex = t.data.sex
                            info.zsl = t.data.user_level.toString()
                            val save = info.save()
                            ImageLoadUtil.GlideHeadImageLoad(mContext, t.data.icon, img_logo)
                            tv_nick.setText(t.data.nickname)
                            tv_id.setText("ID:" + t.data.tt_id)
                            tv_fans.setText(t.data.fans.toString())
                            tv_foucs_on.setText(t.data.gzrs.toString())
                            tv_level.setText(t.data.user_level.toString())
                            tv_mlz.setText(t.data.charm_level.toString())
                            if (t.data.sex.equals("1")){
                                img_sex.setImageResource(R.mipmap.img_boy)
                            }else{
                                img_sex.setImageResource(R.mipmap.img_girl)
                            }

                            SharedPreferencesUtils.saveCarName(mContext,dataBean!!.user_zj_name);//保存座驾名称
                            SharedPreferencesUtils.saveCarPic(mContext,dataBean!!.user_zj_pic)//保存座驾图片
                        }
                    }

                    override fun onCompleted() {
                    }
                })

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
        ll_fans.setOnClickListener {
//            startActivity(FansActivity::class.java)
            FansActivity.startFansActivity(mContext,AuthManager.getInstance().currentUserId)
        }
        tv_foucs_on.setOnClickListener {
//            startActivity(FouceOnActivity::class.java)
            FouceOnActivity.startFouceOnActivity(mContext,AuthManager.getInstance().currentUserId)
        }

        img_logo.setOnClickListener {
            //            startActivity(MyDataActivity::class.java)
            MyDataActivity.startMyDataActivity(mContext, SpUtils.getSp(mContext, "uid"), "myself")
        }
        //我的钱包
        tv1.setOnClickListener {
            startActivity(MoneyBagActivity::class.java)
        }
        //装扮商城
        tv2.setOnClickListener {
//            startActivity(DressMallActivity::class.java)
            DressMallActivity.startDressMallActivity(mContext,AuthManager.getInstance().currentUserId,"myself",dataBean!!.nickname)
        }
        //实名认证
        tv3.setOnClickListener {
            if (bean!!.is_real==0){
                startActivity(SmrzActivity::class.java)
            }else{
                showToastMessage("已认证")
            }
        }
        //我的家族
        tv4.setOnClickListener {
            //            startActivity(MyFamilyActivity::class.java)
            JzData()
        }
        //我的等级
        tv5.setOnClickListener {
//            startActivity(MyDjActivity::class.java)
            MyDjActivity.starMyDjActivity(mContext, "1")
        }
        tv6.visibility=View.GONE
        //我的邀请
        tv6.setOnClickListener {
            startActivity(InviteFriendActivity::class.java)
        }
        //设置
        tv7.setOnClickListener {
            if (dataBean!=null&&!TextUtils.isEmpty(dataBean!!.nickname)) {
                SettingActivity.startSettingActivity(mContext, dataBean!!.now_roomId, dataBean!!.nickname, dataBean!!.icon)
            }
//            startActivity(SettingActivity::class.java)
        }
        ll_user_levles.setOnClickListener {
            MyDjActivity.starMyDjActivity(mContext, "1")
        }
        ll_ml_levels.setOnClickListener {
            MyDjActivity.starMyDjActivity(mContext, "2")
        }
    }

    private fun JzData() {
        NetWork.getService(ImpService::class.java)
                .mFamily(SpUtils.getSp(mContext, "uid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Action1<MFamilyBean> {
                    override fun call(t: MFamilyBean?) {
                        if (t!!.code == 0) {
                            fm_id = t.data.id
                            is_fmy = t.data.is_fmy
                            MyFamilyActivity.startMyFamilyActivity(mContext, fm_id!!, is_fmy!!)
                        } else {
                            showToastMessage("您暂未加入任何家族")
                        }
                    }
                })
    }
}