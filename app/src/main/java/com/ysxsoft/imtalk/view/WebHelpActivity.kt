package com.ysxsoft.imtalk.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.CommunityBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.BaseApplication.Companion.mContext
import com.ysxsoft.imtalk.utils.NetWork
import kotlinx.android.synthetic.main.community_norms_layout.*
import org.jsoup.Jsoup
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers

/**
 *Create By 胡
 *on 2019/7/12 0012
 */
class WebHelpActivity : BaseActivity() {

    companion object {
        fun starWebHelpActivity(mContext: Context,title: String, id: String) {
            val intent = Intent(mContext, WebHelpActivity::class.java)
            intent.putExtra("title", title)
            intent.putExtra("id", id)
            mContext.startActivity(intent)
        }
    }

    override fun getLayout(): Int {
        return R.layout.community_norms_layout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val title = intent.getStringExtra("title")
        val id = intent.getStringExtra("id")
        setBackVisibily()
        setTitle(title)
        requesetData(id)
        val webSettings =web_content.getSettings()
        webSettings.setJavaScriptEnabled(true)
        webSettings.setUseWideViewPort(true)
        webSettings.setLoadWithOverviewMode(true)
        webSettings.setTextSize(WebSettings.TextSize.LARGEST)
        web_content.setWebViewClient(MyWebViewClient())
    }

    private fun requesetData(id: String?) {
        NetWork.getService(ImpService::class.java)
                .HelpDetail(id!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Action1<CommunityBean>{
                    override fun call(t: CommunityBean?) {
                        if (t!!.code==0) {
                            web_content.loadDataWithBaseURL(null,getNewContent(t.data), "text/html", "utf-8", null)
                        }
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