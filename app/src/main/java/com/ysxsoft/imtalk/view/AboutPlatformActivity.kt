package com.ysxsoft.imtalk.view

import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.CommunityBean
import com.ysxsoft.imtalk.bean.ContactBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.NetWork
import kotlinx.android.synthetic.main.community_norms_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *Create By 胡
 *on 2019/7/12 0012
 */
class AboutPlatformActivity : BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.community_norms_layout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBackVisibily()
        setTitle("关于平台")
        requestData()
        val webSettings =web_content.getSettings()
        webSettings.setJavaScriptEnabled(true)
        webSettings.setUseWideViewPort(true)
        webSettings.setLoadWithOverviewMode(true)
        webSettings.setTextSize(WebSettings.TextSize.LARGEST)
        web_content.setWebViewClient(MyWebViewClient())
    }
    private fun requestData() {
        NetWork.getService(ImpService::class.java)
                .BaseSetAbout("5")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CommunityBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: CommunityBean?) {
                        if (t!!.code==0) {
                            web_content.loadDataWithBaseURL(null, t.data, "text/html", "utf-8", null)
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