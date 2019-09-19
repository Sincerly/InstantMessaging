package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.ContentBean
import com.ysxsoft.imtalk.bean.UserBankListBean
import com.ysxsoft.imtalk.bean.WeekStarBean
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder
import com.ysxsoft.imtalk.utils.displayResCyclo
import com.ysxsoft.imtalk.utils.displayUrl
import com.ysxsoft.imtalk.utils.displayUrlCyclo
import com.ysxsoft.imtalk.view.BankCardEditActivity

/**
 * 本周礼物
 */
class WeekAdapter2(mContext:Context) :ListBaseAdapter<WeekStarBean.DataBean.LastStarBean>(mContext) {


    private lateinit var listener : OnItemClickListener

    override val layoutId: Int
        get() = R.layout.item_weekstar_adapter2

    fun setOnclickListener(listener : OnItemClickListener){
        this.listener = listener
    }

    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val bean = mDataList[position]
        holder.getView<ImageView>(R.id.ivLastWeekGift1)?.displayUrlCyclo(bean.icon)
        holder.getView<TextView>(R.id.tvLastWeekGift1)?.text = bean.nickname
        holder.getView<RelativeLayout>(R.id.linoutItem)?.setOnClickListener {
                listener.onItemClick(it, position, bean)
        }
    }

    interface OnItemClickListener{
        fun onItemClick( itemView : View, position: Int, item : WeekStarBean.DataBean.LastStarBean)
    }

}