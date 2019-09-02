package com.ysxsoft.imtalk.view

import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.AwardBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.NetWork
import kotlinx.android.synthetic.main.family_guide_layout.*
import kotlinx.android.synthetic.main.w_translation_title_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers


/**
 *Create By 胡
 *on 2019/7/27 0027
 */
class MyFamilyGuideActivity : BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.family_guide_layout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLightStatusBar(false)
        initStatusBar(topView)
        setTitle("家族指南")
        setBackVisibily()

        val webSettings = web_content.getSettings()

        webSettings.setJavaScriptEnabled(true)
        webSettings.setUseWideViewPort(true)
        webSettings.setLoadWithOverviewMode(true)
        webSettings.setTextSize(WebSettings.TextSize.LARGEST)
        web_content.setWebViewClient(MyWebViewClient())

        val webSettings1 = web_content1.getSettings()

        webSettings1.setJavaScriptEnabled(true)
        webSettings1.setUseWideViewPort(true)
        webSettings1.setLoadWithOverviewMode(true)
        webSettings1.setTextSize(WebSettings.TextSize.LARGEST)
        web_content1.setWebViewClient(MyWebViewClient())

        requestData()
    }

    private inner class MyWebViewClient : WebViewClient() {
        override// 在WebView中而不在默认浏览器中显示页面
        fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }
    }

    private fun requestData() {
        NetWork.getService(ImpService::class.java)
                .FamilyGuide()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<AwardBean>{
                    override fun onError(e: Throwable?) {
                        showToastMessage(e!!.message.toString())
                    }

                    override fun onNext(t: AwardBean?) {
                        if (t!!.code == 0) {
                            web_content.loadDataWithBaseURL(null,t.data.jzdesc, "text/html", "utf-8", null)
                            web_content1.loadDataWithBaseURL(null,t.data.cjwt, "text/html", "utf-8", null)
                        }
                    }

                    override fun onCompleted() {
                    }
                })

    }


}