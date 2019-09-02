package com.ysxsoft.imtalk

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.luck.picture.lib.immersive.LightStatusBarUtils.setLightStatusBar
import com.luck.picture.lib.permissions.RxPermissions
import com.ysxsoft.imtalk.chatroom.utils.log.SLog
import com.ysxsoft.imtalk.fragment.*
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.SpUtils
import com.ysxsoft.imtalk.widget.dialog.QDDialog
import io.reactivex.functions.Consumer
import io.rong.imlib.RongIMClient
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import android.os.Build.VERSION.SDK_INT
import android.support.v4.app.ActivityCompat
import android.text.TextUtils
import org.litepal.LitePal
import org.litepal.tablemanager.Connector


class MainActivity : BaseActivity() {

    var fragments = ArrayList<Fragment>()
    override fun getLayout(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LitePal.getDatabase()
        QDDialog(mContext).show()
        RxPermissions(this).request(Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_LOGS,
                Manifest.permission.SET_DEBUG_APP,
                Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.GET_ACCOUNTS,
                Manifest.permission.WRITE_APN_SETTINGS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(object : Consumer<Boolean> {
                    override fun accept(t: Boolean?) {
                        if (t!!) {
                            //申请的权限全部允许
                        } else {
                            //只要有一个权限被拒绝，就会执行
//                            showToastMessage("未授权权限，部分功能不能使用")
                        }
                    }
                })
        if (!TextUtils.isEmpty(SpUtils.getSp(mContext,"chat_token"))){
            loginToIM(SpUtils.getSp(mContext,"chat_token"))
        }
        setLightStatusBar(true)
        initView()
    }

    private fun initView() {
        fragments.add(HomeFragment())
        fragments.add(HouseFragment())
        fragments.add(MsgFragment())
        fragments.add(FamilyFragment())
        fragments.add(MyFragment())
        val adapter = MyViewPagerAdapter(supportFragmentManager)
        vp_content.adapter = adapter
        vp_content.offscreenPageLimit=5
        rg_home.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_home -> {
                    vp_content.currentItem = 0;
                }

                R.id.rb_house -> {
                    vp_content.currentItem = 1
                }

                R.id.rb_msg -> {
                    vp_content.currentItem = 2
                }

                R.id.rb_family -> {
                    vp_content.currentItem = 3
                }

                R.id.rb_my -> {
                    vp_content.currentItem = 4
                }
            }
        }
        rg_home.check(R.id.rb_home)
    }

    inner class MyViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(i: Int): Fragment {
            return fragments[i]
        }

        override fun getCount(): Int {
            return fragments.size
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode!=123) {
//            showToastMessage("权限没开通，部分功能不能使用")
        }
    }
}
