package com.ysxsoft.imtalk.widget.dialog

import android.content.Context
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.AwardListDataBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import com.ysxsoft.imtalk.widget.ABSDialog
import kotlinx.android.synthetic.main.activity_rules_dialog_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *Create By 胡
 *on 2019/7/17 0017
 */
class ActivityRulesDialog: ABSDialog {

    constructor(mContext: Context) : super(mContext) {
        requestData(mContext)
    }

    private fun requestData(mContext: Context) {
        NetWork.getService(ImpService::class.java)
                .awardList(SpUtils.getSp(mContext,"uid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<AwardListDataBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: AwardListDataBean?) {
                        if (t!!.code==0){
                            web_content.loadDataWithBaseURL(null, t.data.act_rules, "text/html", "utf-8", null)
                        }
                    }

                    override fun onCompleted() {
                    }
                })

    }

    override fun initView() {
        img_close.setOnClickListener {
            dismiss()
        }
        web_content.setBackgroundColor(0)

        val webSettings =web_content .getSettings()
        webSettings.setJavaScriptEnabled(true)
        webSettings.setUseWideViewPort(true)
        webSettings.setLoadWithOverviewMode(true)
        webSettings.setTextSize(WebSettings.TextSize.LARGEST)
        web_content.setWebViewClient(MyWebViewClient())
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_rules_dialog_layout
    }
    private inner class MyWebViewClient : WebViewClient() {
        override// 在WebView中而不在默认浏览器中显示页面
        fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }
    }
}