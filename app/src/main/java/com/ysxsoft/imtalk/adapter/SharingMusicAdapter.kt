package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.MusicListBean
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder
import com.ysxsoft.imtalk.utils.displayRes

/**
 *Create By 胡
 *on 2019/7/29 0029
 */
class SharingMusicAdapter(mContext: Context):ListBaseAdapter<MusicListBean.DataBean>(mContext){
    override val layoutId: Int
        get() = R.layout.item_music
    var click = -1
    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val bean = mDataList.get(position)
        holder.getView<TextView>(R.id.tvSongName)!!.setText(bean.music_name)
        holder.getView<TextView>(R.id.tvName)!!.setText(bean.author_name)

        holder.getView<RadioButton>(R.id.rbEnd)!!.setOnClickListener {
            if (onItemClickListener!=null){
                onItemClickListener!!.onClick(position)
            }
        }
//        if (click==position)
//            holder.getView<ImageView>(R.id.ivEnd)!!.displayRes(R.mipmap.icon_music_seleted)
//        else
//            holder.getView<ImageView>(R.id.ivEnd)!!.displayRes(R.mipmap.icon_music_add)
    }
    interface OnItemClickListener {
        fun onClick(position: Int)
    }
    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun setSelect(position: Int) {
        click = position
        notifyDataSetChanged()
    }

}