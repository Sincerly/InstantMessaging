package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.content.Intent
import android.widget.ImageView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.UserInfoBean
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import com.ysxsoft.imtalk.view.MyPhotoActivity

/**
 *Create By 胡
 *on 2019/7/23 0023
 */
class PhotosAdpater(mContext: Context) : ListBaseAdapter<UserInfoBean.DataBean.PictureBean>(mContext) {
    override val layoutId: Int
        get() = R.layout.photo_item_layout

    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val bean = mDataList.get(position)
        if (position == 0) {
            holder.getView<ImageView>(R.id.img_tupian)!!.setImageResource(R.mipmap.img_add)
            holder.getView<ImageView>(R.id.img_tupian)!!.setOnClickListener {
                mContext.startActivity(Intent(mContext,MyPhotoActivity::class.java))
            }
        } else {
            ImageLoadUtil.GlideGoodsImageLoad(mContext, bean.photo, holder.getView<ImageView>(R.id.img_tupian)!!)
        }
    }
}