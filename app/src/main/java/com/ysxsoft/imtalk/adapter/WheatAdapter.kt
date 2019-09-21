package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.chatroom.model.MicPositionsBean
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder

/**
 *Create By 胡
 *on 2019/9/6 0006
 */
class WheatAdapter(mContext: Context):ListBaseAdapter<MicPositionsBean>(mContext){
    override val layoutId: Int
        get() = R.layout.wheat_item_layout

    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {

        val bean = mDataList.get(position)
        if ("0".equals(bean.uid)) {
            holder.getView<ImageView>(R.id.chatroom_item_iv_mic_seat)!!.visibility = View.VISIBLE
            holder.getView<ImageView>(R.id.wheat_dui)!!.visibility = View.GONE
        }else{
            holder.getView<ImageView>(R.id.wheat_dui)!!.visibility = View.VISIBLE
            holder.getView<ImageView>(R.id.chatroom_item_iv_mic_seat)!!.visibility = View.GONE
        }
        holder.getView<TextView>(R.id.tv_mic)!!.setText(mDataList.size.toString()+"号麦")

        holder.itemView.setOnClickListener {
            if (onWheatListener!=null){
                onWheatListener!!.onWheat(position)
            }
        }

    }

    interface OnWheatListener{
        fun onWheat(position: Int)
    }

    private var onWheatListener: OnWheatListener?=null
    fun setOnWheatListener(onWheatListener: OnWheatListener){
        this.onWheatListener=onWheatListener
    }
}