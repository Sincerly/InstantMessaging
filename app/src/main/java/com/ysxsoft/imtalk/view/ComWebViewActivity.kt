package com.ysxsoft.imtalk.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.utils.BaseActivity
import kotlinx.android.synthetic.main.community_norms_layout.*


/**
 *Create By 胡
 *on 2019/8/1 0001
 */
class ComWebViewActivity: BaseActivity() {
    companion object {
        fun starComWebViewActivity(mContext: Context, title: String, url: String) {
            val intent = Intent(mContext, ComWebViewActivity::class.java)
            intent.putExtra("title", title)
            intent.putExtra("url", url)
            mContext.startActivity(intent)
        }
    }

    override fun getLayout(): Int {
        return R.layout.community_norms_layout
    }

    var title1: String? = null
    var url: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title1 = intent.getStringExtra("title")
        url = intent.getStringExtra("url")
        setBackVisibily()
        setTitle(title1!!)
        val webSettings = web_content.getSettings()
        webSettings.setJavaScriptEnabled(true)
        webSettings.setUseWideViewPort(true)
        webSettings.setLoadWithOverviewMode(true)
        webSettings.setTextSize(WebSettings.TextSize.LARGEST)

        webSettings.setBuiltInZoomControls(false);
        webSettings.setDefaultTextEncodingName("UTF-8") ;
        webSettings.setBlockNetworkImage(false);

        web_content.setWebViewClient(MyWebViewClient())
        web_content.loadDataWithBaseURL(null,url, "text/html", "utf-8", null)
    }

    private inner class MyWebViewClient : WebViewClient() {
        override// 在WebView中而不在默认浏览器中显示页面
        fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }
    }
}
