package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.content.Intent
import android.widget.RelativeLayout
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.ContentBean
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder
import com.ysxsoft.imtalk.view.MyFamilyActivity
import com.ysxsoft.imtalk.view.MyFamilyGuideActivity

class FamilyJz2Adapter(mContext:Context) :ListBaseAdapter<ContentBean>(mContext) {
    override val layoutId: Int
        get() = R.layout.item_family_jz2

    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val bean = mDataList.get(position);
        holder.getView<RelativeLayout>(R.id.rl_jz)?.setOnClickListener {
           val intent : Intent = Intent(mContext, MyFamilyActivity::class.java)
            mContext.startActivity(intent)
        }

         holder.getView<RelativeLayout>(R.id.rl_jz_zn)?.setOnClickListener {
           val intent : Intent = Intent(mContext, MyFamilyGuideActivity::class.java)
            mContext.startActivity(intent)
        }


//        holder.getView<ImageView>(R.id.iv_jz_tx)
//        holder.getView<TextView>(R.id.tv_jz_name)!!.setText(bean.content)
//        holder.getView<TextView>(R.id.tv_jz_id)!!.setText(bean.content)
//        holder.getView<TextView>(R.id.tv_jz_cy)!!.setText(bean.content)

//        holder.getView<RelativeLayout>(R.id.rl_jz_kf)
//        holder.getView<ImageView>(R.id.iv_jz_kf)
//        holder.getView<TextView>(R.id.tv_jz_kf_name)!!.setText(bean.content)
//        holder.getView<TextView>(R.id.tv_jz_kf_id)!!.setText(bean.content)
//        holder.getView<TextView>(R.id.tv_jz_kf_content)!!.setText(bean.content)

//        holder.getView<RelativeLayout>(R.id.rl_jz_zn)
//        holder.getView<ImageView>(R.id.iv_jz_zn)
//        holder.getView<TextView>(R.id.tv_jz_zn_name)!!.setText(bean.content)
//        holder.getView<TextView>(R.id.tv_jz_zn_content)!!.setText(bean.content)

    }
}