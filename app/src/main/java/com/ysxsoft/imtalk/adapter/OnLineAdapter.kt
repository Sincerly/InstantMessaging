package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.RoomMemListBean
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import com.ysxsoft.imtalk.widget.CircleImageView

/**
 *Create By 胡
 *on 2019/8/1 0001
 */
class OnLineAdapter(mContext: Context) : ListBaseAdapter<RoomMemListBean.DataBean.RoomUserListBean>(mContext) {
    override val layoutId: Int
        get() = R.layout.online_item_layout

    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val bean = mDataList.get(position)
        ImageLoadUtil.GlideHeadImageLoad(mContext, bean.icon, holder.getView<CircleImageView>(R.id.img_head)!!)
        holder.getView<TextView>(R.id.tv_nikeName)!!.setText(bean.nickname)
        when (bean.sex) {
            1 -> {
                holder.getView<ImageView>(R.id.img_sex)!!.setImageResource(R.mipmap.img_boy)
            }
            2 -> {
                holder.getView<ImageView>(R.id.img_sex)!!.setImageResource(R.mipmap.img_girl)
            }
        }

        holder.itemView.setOnClickListener {
            if (onLineListener!=null){
                onLineListener!!.lineClick(position)
            }
        }
    }

    interface OnLineListener{
        fun lineClick(position: Int)
    }
    private var onLineListener: OnLineListener?=null
    fun setOnLineListener(onLineListener: OnLineListener){
        this.onLineListener=onLineListener
    }
}