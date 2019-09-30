package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.graphics.Typeface
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
 * 周星奖励
 */
class WeekAdapter5(mContext:Context) :ListBaseAdapter<WeekStarBean.DataBean.LastStar1Bean>(mContext) {


    override val layoutId: Int
        get() = R.layout.item_weekstar_adapter5


    override fun getItemCount(): Int {
        return super.getItemCount()+1
    }

    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        if (mDataList.isNotEmpty()) {
            val bean = mDataList[if (position - 1 >= 0) position - 1 else position]
            val tvTitle = holder.getView<TextView>(R.id.tvTitle)
            val tvContent = holder.getView<TextView>(R.id.tvContent)
            if (position == 0) {
                tvTitle?.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD)
                tvContent?.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD)
                //礼物名
                tvTitle?.text = "礼物名"
                //奖励详情
                tvContent?.text = "奖励详情"
            }else{
                tvTitle?.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL)
                tvContent?.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL)
                //礼物名
                tvTitle?.text = bean.award_name
                //奖励详情
                tvContent?.text = bean.award_gold
            }
        }
    }
}