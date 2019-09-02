package com.ysxsoft.imtalk.fragment

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_local_tyrant_list.*
import kotlinx.android.synthetic.main.fragment_house_item.*
import kotlinx.android.synthetic.main.include_toolbar.*

/**
 * 土豪榜
 */
class LocalTyrantListActivity : BaseActivity() {


    override fun getLayout(): Int {
       return R.layout.activity_local_tyrant_list
    }


    override fun initUi() {
        tvTitle.text = "土豪榜"
        setSupportActionBar(toolBar)
        toolBar.setNavigationOnClickListener {
            finish()
        }

    }


}
