package com.ysxsoft.imtalk.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.R.id.tv_type
import com.ysxsoft.imtalk.bean.RefundListBean
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder

/**
 *Create By 胡
 *on 2019/8/12 0012
 */
class WithDrawRecodeAdapter(mContext: Context) : ListBaseAdapter<RefundListBean.DataBean>(mContext) {
    override val layoutId: Int
        get() = R.layout.wdr_item_layout

    @SuppressLint("ResourceAsColor")
    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val bean = mDataList.get(position)
        holder.getView<TextView>(R.id.tv_time)!!.setText(bean.add_time)
        when (bean.tx_status) {//状态：0 申请中；1 提现成功（通过）；2 提现失败（驳回）
            1 -> {
                holder.getView<TextView>(R.id.tv_sucess)!!.setText("提现成功")
                holder.getView<TextView>(R.id.tv_sucess)!!.setTextColor(R.color.btn_color)
                holder.getView<TextView>(R.id.tv_money)!!.setTextColor(R.color.btn_color)
                holder.getView<TextView>(R.id.tv_money)!!.setText("-"+bean.money)
                holder.getView<ImageView>(R.id.img_bh)!!.visibility=View.GONE
            }
            2 -> {
                holder.getView<ImageView>(R.id.img_bh)!!.visibility=View.VISIBLE
                holder.getView<TextView>(R.id.tv_sucess)!!.setText("提现驳回")
                holder.getView<TextView>(R.id.tv_sucess)!!.setTextColor(R.color.black)
                holder.getView<TextView>(R.id.tv_money)!!.setTextColor(R.color.black)
                holder.getView<TextView>(R.id.tv_money)!!.setText("-"+bean.money)
            }
        }
        when (bean.type) {//	1 支付宝提现； 2 银行卡提现
            1 -> {
                holder.getView<TextView>(R.id.tv_type)!!.setText("支付宝提现")
            }
            2 -> {
                holder.getView<TextView>(R.id.tv_type)!!.setText("银行卡提现")
            }

        }
    }
}