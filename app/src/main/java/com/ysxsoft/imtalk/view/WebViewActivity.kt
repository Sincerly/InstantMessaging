package com.ysxsoft.imtalk.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_web_view.*

/**
 * 只有webview 的页面
 */
class WebViewActivity : BaseActivity() {



    override fun getLayout(): Int {
       return R.layout.activity_web_view
    }

    override fun initUi() {

        webView.setOnClickListener{


        }
    }


}
