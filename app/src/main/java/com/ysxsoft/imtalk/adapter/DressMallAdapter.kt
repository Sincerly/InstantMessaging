package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.DressMallBean
import com.ysxsoft.imtalk.bean.SGiftBean
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder
import com.ysxsoft.imtalk.utils.BaseApplication.Companion.mContext
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import org.w3c.dom.Text

/**
 *Create By 胡
 *on 2019/7/22 0022
 */
class DressMallAdapter(mContext: Context) : ListBaseAdapter<DressMallBean.DataBean>(mContext) {
    override val layoutId: Int
        get() = R.layout.dress_mall_item_layout

    var click = -1

    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val bean = mDataList.get(position)
        ImageLoadUtil.GlideGoodsImageLoad(mContext, bean.pic, holder.getView<ImageView>(R.id.img_tupian)!!)
        holder.getView<TextView>(R.id.tv_name)!!.setText(bean.name)
        holder.getView<TextView>(R.id.tv_money)!!.setText(bean.gold + "金币")
        holder.getView<TextView>(R.id.tv_day)!!.setText("/" + bean.days.toString() + "天")
        if (click == position) {
            holder.getView<LinearLayout>(R.id.ll_bg)!!.setBackgroundResource(R.drawable.theme_frame_bg)
        } else {
            holder.getView<LinearLayout>(R.id.ll_bg)!!.setBackgroundResource(R.drawable.gray_shape)
        }
    }

    fun setSelect(position: Int) {
        click = position
        notifyDataSetChanged()
    }

}