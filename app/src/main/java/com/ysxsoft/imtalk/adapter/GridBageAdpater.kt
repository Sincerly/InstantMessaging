package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.BageListBean
import com.ysxsoft.imtalk.bean.DressMallBean
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder
import com.ysxsoft.imtalk.utils.ImageLoadUtil

/**
 *Create By 胡
 *on 2019/8/26 0026
 */
class GridBageAdpater(mContext: Context) : ListBaseAdapter<BageListBean.DataBean>(mContext) {
    override val layoutId: Int
        get() = R.layout.gift_item_layout

    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val bean = mDataList.get(position)
        ImageLoadUtil.GlideGoodsImageLoad(mContext, bean.aw_images, holder.getView<ImageView>(R.id.img_gift)!!)
        holder.getView<TextView>(R.id.tv_gold)!!.setText(bean.aw_name+"x"+bean.gift_num)
//        holder.getView<TextView>(R.id.tv_gold)!!.visibility=View.GONE
        if (bean.isSelect) {
            holder.getView<LinearLayout>(R.id.ll_bg)!!.setBackgroundResource(R.drawable.theme_fragme)
            holder.getView<TextView>(R.id.tv_gold)!!.setTextColor(Color.parseColor("#FF38C9"))
        } else {
            holder.getView<LinearLayout>(R.id.ll_bg)!!.setBackgroundResource(R.color.transparent)
            holder.getView<TextView>(R.id.tv_gold)!!.setTextColor(Color.parseColor("#686868"))
        }
        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onBage(position)
            }
        }
    }

    interface OnClickListener {
        fun onBage(position: Int)
    }

    private var onClickListener: OnClickListener? = null
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    fun setSelect(position: Int) {
        val bean = mDataList!!.get(position);
        bean!!.isSelect = !bean!!.isSelect;
        for (item in mDataList) {
            if (!item.id.toString().equals(bean.id.toString())) {
                item.isSelect=false;
            }
        }
        notifyDataSetChanged()
    }
}