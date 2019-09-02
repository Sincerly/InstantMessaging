package com.ysxsoft.imtalk.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.GoldListBean
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder

/*
 *Create By 胡
 *on 2019/7/22 0022
 */
class JbWithDrawAdapter(mContext: Context) : ListBaseAdapter<GoldListBean.DataBean>(mContext) {

    var click =-1
    override val layoutId: Int
        get() = R.layout.jb_withdraw_item_layout


    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val bean = mDataList.get(position)
        holder.getView<TextView>(R.id.tv_gold)!!.setText("金币：" + bean.gd_gold_num)
        holder.getView<TextView>(R.id.tv_money)!!.setText("¥" + bean.gd_money)

        if (click == position) {
            holder.getView<TextView>(R.id.tv_gold)!!.setTextColor(mContext.resources.getColor(R.color.white))
            holder.getView<TextView>(R.id.tv_money)!!.setTextColor(mContext.resources.getColor(R.color.white))
            holder.getView<LinearLayout>(R.id.ll_bg)!!.setBackgroundResource(R.drawable.btn_radius10_shape)
        } else {
            holder.getView<TextView>(R.id.tv_gold)!!.setTextColor(mContext.resources.getColor(R.color.hint_text_color))
            holder.getView<TextView>(R.id.tv_money)!!.setTextColor(mContext.resources.getColor(R.color.hint_text_color))
            holder.getView<LinearLayout>(R.id.ll_bg)!!.setBackgroundResource(R.drawable.cccccc_frame_bg)
        }

    }

    fun setSelect(position: Int) {
        click = position
        notifyDataSetChanged()
    }
}