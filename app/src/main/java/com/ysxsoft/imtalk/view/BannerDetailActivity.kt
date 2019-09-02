package com.ysxsoft.imtalk.view

import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.BannerDetailBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.NetWork
import kotlinx.android.synthetic.main.community_norms_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *Create By 胡
 *on 2019/7/23 0023
 */
class BannerDetailActivity:BaseActivity(){
    override fun getLayout(): Int {
        return R.layout.community_norms_layout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val banner_id = intent.getStringExtra("banner_id")
        setBackVisibily()
        setTitle("轮播详情")
        requestData(banner_id)
        val webSettings = web_content.getSettings()
        webSettings.setJavaScriptEnabled(true)
        webSettings.setUseWideViewPort(true)
        webSettings.setLoadWithOverviewMode(true)
        webSettings.setTextSize(WebSettings.TextSize.LARGEST)
        web_content.setWebViewClient(MyWebViewClient())
    }

    private fun requestData(banner_id: String?) {
        NetWork.getService(ImpService::class.java)
                .HomeBannerDetail(banner_id!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Observer<BannerDetailBean>{
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: BannerDetailBean?) {
                        if (t!!.code==0) {
                            web_content.loadDataWithBaseURL(null, t.data.get(0).content, "text/html", "utf-8", null)
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