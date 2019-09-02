package com.ysxsoft.imtalk.view

import android.os.Bundle
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.utils.AppUtil
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.widget.dialog.LoginOutDialog
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.title_layout.*

class SettingActivity : BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.activity_setting;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLightStatusBar(true)
        initView()
    }

    private fun initView() {
        initStatusBar(topView)
        setBackVisibily()
        setTitle("设置")
        tv_version.setText("V"+AppUtil.getVersionName(mContext))
        //绑定手机
        tv1.setOnClickListener {
            startActivity(BindPhoneActivity::class.java)
        }
        //绑定支付宝账号
        tv2.setOnClickListener {
            startActivity(BindZfbActivity::class.java)
        }
        //绑定银行卡
        tv12.setOnClickListener {
            startActivity(BankCardListActivity::class.java)
        }
        //登录密码
        tv3.setOnClickListener {
            startActivity(ForgetPswActivity::class.java)
        }
        //我要反馈
        tv4.setOnClickListener {
            startActivity(WyfkActivity::class.java)
        }
        //黑名单管理
        tv5.setOnClickListener {
            startActivity(BlackListActivity::class.java)
        }
        //社区规范
        tv6.setOnClickListener {
            startActivity(CommunityNormsActivity::class.java)
        }
        //联系官方
        tv7.setOnClickListener {
            startActivity(ContactOfficialsActivity::class.java)
        }
        //帮助
        tv8.setOnClickListener {
            startActivity(HelpActivity::class.java)
        }
        //关于平台
        tv9.setOnClickListener {
            startActivity(AboutPlatformActivity::class.java)
        }
        //检查版本
        rl10.setOnClickListener {
            showToastMessage("当前版本"+AppUtil.getVersionName(mContext))
        }
        //退出登录
        tv11.setOnClickListener {
            LoginOutDialog(mContext).show()
        }
    }
}
