package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.DressMallBean
import com.ysxsoft.imtalk.bean.SGiftBean
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder
import com.ysxsoft.imtalk.utils.ImageLoadUtil

/**
 *Create By 胡
 *on 2019/7/22 0022
 */
class MyDressAdapter(mContext: Context):ListBaseAdapter<SGiftBean.DataBean.ListInfoBean>(mContext){

    override val layoutId: Int
        get() = R.layout.my_dress_item_layout

    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val bean = mDataList.get(position)
        ImageLoadUtil.GlideHeadImageLoad(mContext,bean.pic,holder.getView<ImageView>(R.id.img_tupian)!!)
        holder.getView<TextView>(R.id.tv_gift_name)!!.setText(bean.name)
        holder.getView<TextView>(R.id.tv_time)!!.setText(bean.days)
        when(bean.is_use){
            0->{
                holder.getView<TextView>(R.id.tv_show)!!.setText("使用")
            }
            1->{
                holder.getView<TextView>(R.id.tv_show)!!.setText("已使用")
            }
        }
        holder.getView<TextView>(R.id.tv_show)!!.setOnClickListener {
            if (onUserItemListener!=null){
                onUserItemListener!!.userClick(position)
            }
        }
    }

    interface OnUserItemListener{
        fun userClick(position: Int)
    }
    private var onUserItemListener: OnUserItemListener?= null
    fun setOnUserItemListener(onUserItemListener: OnUserItemListener){
            this.onUserItemListener=onUserItemListener
    }
}