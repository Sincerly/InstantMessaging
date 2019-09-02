package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import java.io.File

/**
 *Create By 胡
 *on 2019/8/2 0002
 */
class ReportPhotoAdapter(mContext: Context) : ListBaseAdapter<File>(mContext) {
    override val layoutId: Int
        get() = R.layout.report_photo_itme_layout

    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val file = mDataList.get(position)
//        if (TextUtils.isEmpty(file.toString())) {
//            holder.getView<ImageView>(R.id.img_1)!!.setImageResource(R.mipmap.img_add_report)
//        } else {
//            ImageLoadUtil.GlideGoodsImageLoad(mContext, file.toString(), holder.getView<ImageView>(R.id.img_1)!!)
//        }
//        if (position==9) {
//            holder.getView<ImageView>(R.id.img_1)!!.visibility = View.GONE
//        }else{
//            holder.getView<ImageView>(R.id.img_1)!!.visibility = View.VISIBLE
//        }
        if (position==0){
            holder.getView<ImageView>(R.id.img_1)!!.setImageResource(R.mipmap.img_add_report)
        }else {
            ImageLoadUtil.GlideGoodsImageLoad(mContext, file.toString(), holder.getView<ImageView>(R.id.img_1)!!)
        }

//        holder.getView<ImageView>(R.id.img_1)!!.setOnClickListener {
//            if (onClickPhonoListener!=null){
//                onClickPhonoListener!!.onClick(position)
//            }
//        }
        holder.itemView.setOnClickListener {
            if (onClickPhonoListener!=null){
                onClickPhonoListener!!.onClick(position)
            }
        }

    }

    interface  OnClickPhonoListener{
        fun onClick(position: Int)
    }
    private var onClickPhonoListener: OnClickPhonoListener?=null
     fun setOnClickPhonoListener(onClickPhonoListener: OnClickPhonoListener){
         this.onClickPhonoListener=onClickPhonoListener
     }

}