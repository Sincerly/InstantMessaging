package com.ysxsoft.imtalk.view

import android.os.Bundle
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.utils.BaseActivity

/**
 *Create By 胡
 *on 2019/8/1 0001
 */
class UserLevelsActivity:BaseActivity(){
    override fun getLayout(): Int {
        return R.layout.user_levels_layout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBackVisibily()
        setTitle("我的等级")
    }
}