package com.ysxsoft.imtalk.adapter

import android.content.Context
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder
import io.rong.imlib.model.Conversation

class PalLobbyAdapter(mContext: Context) : ListBaseAdapter<Conversation>(mContext) {


    override val layoutId: Int
        get() = R.layout.item_palmessage


    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        var item = dataList[position]


    }


}