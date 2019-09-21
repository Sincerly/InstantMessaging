package com.ysxsoft.imtalk.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.UserLevelBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import kotlinx.android.synthetic.main.title_layout2.*
import kotlinx.android.synthetic.main.user_levels_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class MyDjActivity : BaseActivity() {

    companion object {
        fun starMyDjActivity(mContext: Context, flag: String) {
            val intent = Intent(mContext, MyDjActivity::class.java)
            intent.putExtra("flag", flag)
            mContext.startActivity(intent)
        }
    }

    override fun getLayout(): Int {
        return R.layout.user_levels_layout
    }

    var flag: String? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        flag = intent.getStringExtra("flag")
        setLightStatusBar(false)
        initStatusBar(topView)
        setBackVisibily()
        setTitle("我的等级")
        rqeustData()
        initView()

        val webSettings = web_content.getSettings()
        webSettings.setJavaScriptEnabled(true)
        webSettings.setUseWideViewPort(true)
        webSettings.setLoadWithOverviewMode(true)
        webSettings.setTextSize(WebSettings.TextSize.LARGEST)
        web_content.setWebViewClient(MyWebViewClient())

    }

    private fun initView() {
        if ("2".equals(flag)) {
            tv2.isSelected = true
            tv_user_level_tip.setText("当前魅力等级：")
        } else {
            tv1.isSelected = true
            tv_user_level_tip.setText("当前用户等级：")
        }
        tv1.setOnClickListener {
            flag="1"
            tv1.isSelected = true
            tv2.isSelected = false
            rqeustData()
            tv_user_level_tip.setText("当前用户等级：")
        }
        tv2.setOnClickListener {
            flag="2"
            tv1.isSelected = false
            tv2.isSelected = true
            rqeustData()
            tv_user_level_tip.setText("当前魅力等级：")
        }

    }

    private fun rqeustData() {
        NetWork.getService(ImpService::class.java)
                .user_level(SpUtils.getSp(mContext, "uid"), flag.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<UserLevelBean> {
                    override fun onError(e: Throwable?) {
                        Log.d(this.javaClass.name, e!!.message.toString())
                    }

                    override fun onNext(t: UserLevelBean?) {
                        if (t!!.code == 0) {
                            tv_level.setText(t.data.user_level_name+t.data.now_level)
                            tv_user_level.setText(t.data.now_level)
                            tv_cjb.setText("差"+t.data.next_level_gold+"金币升级")
                            ImageLoadUtil.GlideHeadImageLoad(mContext,t.data.icon,img_head)
//                            web_content.reload()
                            web_content.loadDataWithBaseURL(null,t.data.sign_rule, "text/html", "utf-8", null)
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }
    private inner class MyWebViewClient : WebViewClient() {
        override// 在WebView中而不在默认浏览器中显示页面
        fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }
    }
}
