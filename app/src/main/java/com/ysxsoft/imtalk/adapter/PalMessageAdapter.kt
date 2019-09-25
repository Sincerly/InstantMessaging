package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.view.View
import android.widget.TextView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.AwardListDataBean
import com.ysxsoft.imtalk.bean.ContentBean
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import com.ysxsoft.imtalk.widget.CircleImageView
import io.rong.imlib.model.Message
import io.rong.message.TextMessage

/**
 *Create By jyg
 *on 2019/9/24 0026
 */
class PalMessageAdapter(mContext: Context) : ListBaseAdapter<Message>(mContext){

    private var isInit : Boolean = false

    override val layoutId: Int
        get() = R.layout.item_palmessage


    fun setLateData(list: Collection<Message>){
        setDataList(list)
        isInit = true
    }

    fun addData(msg : Message){
        mDataList.add(msg)
        notifyItemInserted(mDataList.size - 1)
    }

    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val item = mDataList[position]
        if (item.content is TextMessage){
            val content = item.content as TextMessage
        }
    }
}