package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.widget.ImageView
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
    override val layoutId: Int
        get() = R.layout.item_weekstar_adapter2

    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val bean = mDataList.get(position)
        holder.getView<ImageView>(R.id.ivLastWeekGift1)?.displayUrlCyclo(bean.icon)
        holder.getView<TextView>(R.id.tvLastWeekGift1)?.text = bean.nickname
    }
}