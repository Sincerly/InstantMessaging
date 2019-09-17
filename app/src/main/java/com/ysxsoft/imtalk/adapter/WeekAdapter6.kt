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
 * 下周礼物
 */
class WeekAdapter6(mContext:Context) :ListBaseAdapter<WeekStarBean.DataBean.NextGiftBean>(mContext) {
    override val layoutId: Int
        get() = R.layout.item_weekstar_adapter1

    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val bean = mDataList.get(position)
        holder.getView<ImageView>(R.id.ivWeekStar1)?.displayUrlCyclo(bean.ws_pic)
        holder.getView<TextView>(R.id.tvWeekStar1)?.text = bean.ws_gold+"金币"
    }
}