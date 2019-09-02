package com.ysxsoft.imtalk.adapter

import android.content.Context
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.ContentBean
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder

/**
 *Create By 胡
 *on 2019/7/23 0023
 */
class MyDataFamilyAdapter(mContext: Context):ListBaseAdapter<ContentBean>(mContext){
    override val layoutId: Int
        get() = R.layout.my_data_fragment_item_layout

    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {

    }
}