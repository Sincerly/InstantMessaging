package com.ysxsoft.imtalk.view

import android.os.Bundle
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.MyQrCodeBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import com.ysxsoft.imtalk.widget.dialog.ShareFriendDialog
import kotlinx.android.synthetic.main.activity_invite_friend.*
import kotlinx.android.synthetic.main.title_layout2.*
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers

class InviteFriendActivity : BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.activity_invite_friend
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLightStatusBar(false)
        initStatusBar(topView)
        ll_title.setBackgroundResource(R.mipmap.invite_top)
        initView()
        rquestData()
    }

    private fun rquestData() {
        NetWork.getService(ImpService::class.java)
                .myQrCode(SpUtils.getSp(mContext, "uid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Action1<MyQrCodeBean> {
                    override fun call(t: MyQrCodeBean?) {
                        if (t!!.code == 0) {
                            tv_jb_num.setText(t.data.total_gold.toString())
                            tv_people_num.setText(t.data.total_num.toString())
                            tv_invitation_code.setText("邀请码：" + t.data.invitation_code.toString())
                            ImageLoadUtil.GlideGoodsImageLoad(mContext, t.data.qr_code, img_qrcode)
                            tv_url.setText(t.data.share_url)
                        }
                    }
                })

    }

    private fun initView() {
        setBackVisibily()
        setTitle("邀请好友")
        img_right.setImageResource(R.mipmap.share_white)

        //分享
        img_right.setOnClickListener {
            val friendDialog = ShareFriendDialog(mContext)
            friendDialog.setShareListener(object : ShareFriendDialog.ShareListener {
                override fun myself() {
                    showToastMessage("站内分享")
                }

                override fun Wechat() {
                    showToastMessage("微信")
                }

                override fun pyq() {
                    showToastMessage("朋友圈")
                }

                override fun qqZone() {
                    showToastMessage("QQ空间")
                }

                override fun QQ() {
                    showToastMessage("QQ")
                }
            })
            friendDialog.show()
        }
        //查看邀请列表
        tv_look_list.setOnClickListener {
            startActivity(InviteListActivity::class.java)
        }


    }
}
