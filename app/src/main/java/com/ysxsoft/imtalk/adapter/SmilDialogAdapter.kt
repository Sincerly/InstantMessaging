package com.ysxsoft.imtalk.adapter

import android.content.ContentResolver
import android.content.Context
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.FaceListBean
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView

/**
 *Create By 胡
 *on 2019/8/5 0005
 */
class SmilDialogAdapter(mContext: Context) : ListBaseAdapter<FaceListBean.DataBean>(mContext) {
    override val layoutId: Int
        get() = R.layout.smile_dialog_item_layout

    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val bean = mDataList.get(position)
        Glide.with(mContext).load(bean.bq_pic).into(holder.getView<GifImageView>(R.id.gif)!!)
        holder.getView<TextView>(R.id.tv_name)!!.setText(bean.bq_name)
        holder.itemView.setOnClickListener {
            if (onSmileListener!=null){
                onSmileListener!!.onClick(position,bean.bq_gif)
            }
        }

    }

    interface OnSmileListener {
        fun onClick(position: Int,url:String)
    }

    private var onSmileListener: OnSmileListener? = null
    fun setOnSmileListener(onSmileListener: OnSmileListener) {
        this.onSmileListener = onSmileListener
    }

}