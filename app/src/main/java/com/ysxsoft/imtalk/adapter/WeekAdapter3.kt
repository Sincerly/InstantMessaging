package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.widget.ImageView
import android.widget.LinearLayout
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
 * 本周明星头部礼物列表
 */
class WeekAdapter3(mContext: Context) : ListBaseAdapter<WeekStarBean.DataBean.WeekBean>(mContext) {

    private var selected: Int = 0
    private lateinit var listener : ItemClickListener

    fun setListener(listener : ItemClickListener){
        this.listener = listener
    }

    override val layoutId: Int
        get() = R.layout.item_weekstar_adapter3

    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val bean = mDataList[position]
        val linoutItem = holder.getView<LinearLayout>(R.id.linoutItem)
        if (selected == position) {
            linoutItem?.setBackgroundResource(R.drawable.shape_gradient_horizontal_180)
            holder.getView<TextView>(R.id.tvItem)?.setTextColor(ContextCompat.getColor(mContext, R.color.white))
        }else {
            linoutItem?.setBackgroundResource(R.drawable.shape_stroke_colorcc_oval)
            holder.getView<TextView>(R.id.tvItem)?.setTextColor(ContextCompat.getColor(mContext, R.color.textColor28))
        }
        //图像
        holder.getView<ImageView>(R.id.ivPic)?.displayUrl(bean.ws_pic)
        //名称
        holder.getView<TextView>(R.id.tvItem)?.text = bean.ws_name

        linoutItem?.setOnClickListener {
            selected = position
            notifyDataSetChanged()
            listener.itemClick(mDataList[selected])
        }
    }

    interface ItemClickListener{
        fun itemClick(item: WeekStarBean.DataBean.WeekBean)
    }

}