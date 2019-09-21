package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.AwardRecodeBean
import com.ysxsoft.imtalk.bean.ContentBean
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import com.ysxsoft.imtalk.widget.CircleImageView

/**
 *Create By 胡
 *on 2019/7/26 0026
 */
class WiningRecordAdapter(mContext: Context) :ListBaseAdapter<AwardRecodeBean.DataBean>(mContext){
    override val layoutId: Int
        get() = R.layout.wining_record_item_layout

    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val bean = mDataList.get(position)
        val c=holder.getView<CircleImageView>(R.id.img_head2)!!
        Glide.with(mContext).load(bean.aw_images).into(c)
        holder.getView<TextView>(R.id.tv_nikeName)!!.setText(bean.aw_name)
        holder.getView<TextView>(R.id.tv_time)!!.setText(bean.add_time)
        holder.getView<TextView>(R.id.tv_money)!!.setText(bean.aw_gold+"金币")
    }
}