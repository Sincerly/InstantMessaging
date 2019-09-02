package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.widget.TextView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.GiftTimesBean
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder

/**
 *Create By 胡
 *on 2019/8/6 0006
 */
class GiftTimesAdapter(mContext: Context):ListBaseAdapter<GiftTimesBean.DataBean>(mContext){
    override val layoutId: Int
        get() = R.layout.gift_time_layout

    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val bean = mDataList.get(position)
        holder.getView<TextView>(R.id.tv_name)!!.setText(bean.name)
        holder.getView<TextView>(R.id.tv_num)!!.setText("X   "+bean.times)
        holder.itemView.setOnClickListener {
            if (onGiftTimeListener!=null){
                onGiftTimeListener!!.onClick(position)
            }
        }
    }

    interface OnGiftTimeListener{
        fun onClick(position: Int)
    }
    private var onGiftTimeListener: OnGiftTimeListener?=null
    fun setOnGiftTimeListener(onGiftTimeListener: OnGiftTimeListener){
        this.onGiftTimeListener=onGiftTimeListener
    }

}