package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.BannerBean
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import com.ysxsoft.imtalk.view.BannerDetailActivity

/**
 * Create By 胡
 * on 2019/7/23 0023
 */
class BannerAdapter(var mContext: Context, var data: MutableList<BannerBean.DataBean>) : RecyclerView.Adapter<BannerAdapter.BannerHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): BannerHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.banner_item_layout, p0, false)
        return BannerHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(p0: BannerHolder, p1: Int) {
        val bean = data.get(p1)
        ImageLoadUtil.GlideGoodsImageLoad(mContext,bean.pic,p0.itemView.findViewById<ImageView>(R.id.img_tupian)!!)
        p0.itemView.setOnClickListener {
            val banner_id = bean.id.toString()
            val intent = Intent(mContext, BannerDetailActivity::class.java)
            intent.putExtra("banner_id", banner_id)
            mContext.startActivity(intent)

        }
    }

    class BannerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
}
