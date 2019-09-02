package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.DressMallBean
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import org.w3c.dom.Text

/**
 *Create By 胡
 *on 2019/8/5 0005
 */
class GiftAdapter(mContext: Context) : ListBaseAdapter<DressMallBean.DataBean>(mContext) {
    override val layoutId: Int
        get() = R.layout.gift_item_layout


    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val bean = mDataList.get(position)
        ImageLoadUtil.GlideGoodsImageLoad(mContext, bean.pic, holder.getView<ImageView>(R.id.img_gift)!!)
        holder.getView<TextView>(R.id.tv_name)!!.setText(bean.name)
    }
}