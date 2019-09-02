package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.ContentBean
import com.ysxsoft.imtalk.bean.UserBankListBean
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder
import com.ysxsoft.imtalk.view.BankCardEditActivity

class BankListAdapter(mContext:Context) :ListBaseAdapter<UserBankListBean.DataBean>(mContext) {
    override val layoutId: Int
        get() = R.layout.item_card

    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val bean = mDataList.get(position);
        //银行
        holder.getView<TextView>(R.id.tv_bank)!!.setText(bean.bank_name)
       // 名字
        holder.getView<TextView>(R.id.tv_name)!!.setText(bean.real_name)
       // 卡号
        holder.getView<TextView>(R.id.tv_no)!!.setText(bean.bank_number)

        holder.getView<ImageView>(R.id.img_ed)!!.setOnClickListener {
            val bank_id = bean.bank_id.toString()
            BankCardEditActivity.starBankCardEditActivity(mContext,bank_id,"1")
        }
    }
}