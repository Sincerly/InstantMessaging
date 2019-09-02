package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.RoomBgBean
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder
import com.ysxsoft.imtalk.utils.ImageLoadUtil

/**
 *Create By 胡
 *on 2019/7/30 0030
 */
class RoomBackGroundAdapter(mContext: Context) : ListBaseAdapter<RoomBgBean.DataBean>(mContext) {
    override val layoutId: Int
        get() = R.layout.room_background_item_layout
    var click = -1
    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val bean = mDataList.get(position)
        ImageLoadUtil.GlideGoodsImageLoad(mContext, bean.bg_url, holder.getView<ImageView>(R.id.img_bg)!!)
        holder.getView<TextView>(R.id.tv_bgName)!!.setText(bean.bg_name)
        if (click == position) holder.getView<TextView>(R.id.tv_select)!!.isSelected = true else holder.getView<TextView>(R.id.tv_select)!!.isSelected = false
        holder.itemView.setOnClickListener {
            if (onItemClickListener != null) {
                onItemClickListener!!.OnClick(position)
            }
        }
    }


    interface OnItemClickListener {
        fun OnClick(position: Int)
    }

    private var onItemClickListener: OnItemClickListener? = null
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun setSelect(position: Int) {
        click = position
        notifyDataSetChanged()
    }
}