package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import com.luck.picture.lib.config.PictureConfig.TYPE_CAMERA
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.UserInfoBean
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import com.ysxsoft.imtalk.utils.ToastUtils
import kotlinx.android.synthetic.main.picture_f_layout.view.*

/**
 *Create By 胡
 *on 2019/7/23 0023
 */
class MyPictureAdapter(mContext: Context) : ListBaseAdapter<UserInfoBean.DataBean.PictureBean>(mContext) {

    override val layoutId: Int
        get() = R.layout.picture_f_layout

    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val bean = mDataList.get(position)
        if (position == 0) {
            holder.getView<ImageView>(R.id.img_tupian)!!.setImageResource(R.mipmap.img_add)
            holder.getView<ImageView>(R.id.img_tupian)!!.setOnClickListener {
                if (itemClickListener != null) {
                    itemClickListener!!.onItemClick(position)
                }
            }
        } else {
            ImageLoadUtil.GlideGoodsImageLoad(mContext, bean.photo, holder.getView<ImageView>(R.id.img_tupian)!!)
        }
        if (bean.isChoosed()) {
            holder.getView<CheckBox>(R.id.cb_box)!!.setChecked(true)
        } else {
            holder.getView<CheckBox>(R.id.cb_box)!!.setChecked(false)
        }
        holder.getView<CheckBox>(R.id.cb_box)!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                bean.setChoosed((v as CheckBox).isChecked);
                if (checkInterface != null) {
                    checkInterface!!.checkGroup(position, (v as CheckBox).isChecked)
                }
            }
        })
        if (b) {
            if (position == 0) {
                holder.getView<CheckBox>(R.id.cb_box)!!.visibility = View.GONE
                holder.getView<ImageView>(R.id.img_tupian)!!.setOnClickListener {
                    if (itemClickListener != null) {
                        itemClickListener!!.onItemClick(position)
                    }
                }
            } else {
                holder.getView<CheckBox>(R.id.cb_box)!!.visibility = View.VISIBLE
            }
        } else {
            holder.getView<CheckBox>(R.id.cb_box)!!.visibility = View.GONE
        }

    }

    private var checkInterface: CheckInterface? = null
    /**
     * 单选接口
     *
     * @param checkInterface
     */
    fun setCheckInterface(checkInterface: CheckInterface) {
        this.checkInterface = checkInterface
    }

    var b: Boolean = false
    fun isShow(b: Boolean) {
        this.b = b
        notifyDataSetChanged()
    }

    interface CheckInterface {
        fun checkGroup(position: Int, isChecked: Boolean)
    }

    interface ItemClickListener {
        fun onItemClick(position: Int)
    }

    private var itemClickListener: ItemClickListener? = null
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}