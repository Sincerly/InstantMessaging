package com.ysxsoft.imtalk.view

import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.UserXyBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.NetWork
import kotlinx.android.synthetic.main.community_norms_layout.*
import org.jsoup.Jsoup
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *Create By 胡
 *on 2019/9/29 0029
 */
class UserRulesActivity:BaseActivity(){
    override fun getLayout(): Int {
        return R.layout.community_norms_layout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("用户协议")
        setBackVisibily()
        val webSettings = web_content.getSettings()
        webSettings.setJavaScriptEnabled(true)
        webSettings.setUseWideViewPort(true)
        webSettings.setLoadWithOverviewMode(true)
        webSettings.setTextSize(WebSettings.TextSize.LARGEST)

        webSettings.setBuiltInZoomControls(false);
        webSettings.setDefaultTextEncodingName("UTF-8") ;
        webSettings.setBlockNetworkImage(false);

        web_content.setWebViewClient(MyWebViewClient())

        requestData()
    }

    private fun requestData() {
        NetWork.getService(ImpService::class.java)
                .user_xy()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Observer<UserXyBean>{
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: UserXyBean?) {
                        if (t!!.code==0){
                            web_content.loadDataWithBaseURL(null,getNewContent(t.data), "text/html", "utf-8", null)
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

    fun getNewContent(htmltext: String): String {
        var doc = Jsoup.parse(htmltext);
        val elements = doc.getElementsByTag("img");
        for (element in elements) {
            element.attr("width", "100%").attr("height", "auto");
        }
        return doc.toString();
    }
}