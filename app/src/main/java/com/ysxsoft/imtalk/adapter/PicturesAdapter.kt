package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import com.luck.picture.lib.config.PictureConfig.TYPE_CAMERA
import com.luck.picture.lib.config.PictureConfig.TYPE_PICTURE
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.R.id.img_tupian
import com.ysxsoft.imtalk.bean.UserInfoBean
import com.ysxsoft.imtalk.utils.BaseApplication.Companion.mContext
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import com.ysxsoft.imtalk.utils.ToastUtils

/**
 *Create By 胡
 *on 2019/7/23 0023
 */
class PicturesAdapter(var mContext: Context, var pictures: MutableList<UserInfoBean.DataBean.PictureBean>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var TYPE_CAMERA = 1
    var TYPE_PICTURE = 2
    override fun getItemViewType(position: Int): Int {
        return if (isShowAddItem(position)) {
            TYPE_CAMERA
        } else {
            TYPE_PICTURE
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val view = getView(R.layout.picture_f_layout)
        return NormalHolder(view)
    }

    override fun getItemCount(): Int {
        return  pictures.size + 1

    }

    private fun isShowAddItem(position: Int): Boolean {
        val size = if (pictures.size == 0) 0 else pictures.size
        return position == size
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, position: Int) {

        if (getItemViewType(position) == TYPE_CAMERA) {
            p0.itemView.findViewById<ImageView>(R.id.img_tupian).setImageResource(R.mipmap.img_add)
            p0.itemView.setOnClickListener {
                if (itemClickListener != null) {
                    itemClickListener!!.onClick(position)
                }
            }
        } else {
            ImageLoadUtil.GlideGoodsImageLoad(mContext, pictures.get(position).photo, p0.itemView.findViewById(R.id.img_tupian))
        }
        val bean = pictures.get(position-1)
        if (bean.isChoosed()) {
            p0.itemView.findViewById<CheckBox>(R.id.cb_box)!!.setChecked(true)
        } else {
            p0.itemView.findViewById<CheckBox>(R.id.cb_box)!!.setChecked(false)
        }
        p0.itemView.findViewById<CheckBox>(R.id.cb_box)!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                bean.setChoosed((v as CheckBox).isChecked);
                if (checkInterface != null) {
                    checkInterface!!.checkGroup(position, (v as CheckBox).isChecked)
                }
            }
        })
        if (b) {
            if (position == 0) {
                p0.itemView.findViewById<CheckBox>(R.id.cb_box)!!.visibility = View.GONE
                p0.itemView.findViewById<ImageView>(R.id.img_tupian)!!.setOnClickListener {
                    if (itemClickListener != null) {
                        itemClickListener!!.onClick(position)
                    }
                }
            } else {
                p0.itemView.findViewById<CheckBox>(R.id.cb_box)!!.visibility = View.VISIBLE
            }
        } else {
            p0.itemView.findViewById<CheckBox>(R.id.cb_box)!!.visibility = View.GONE
        }
    }

    class PictureHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    class NormalHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    /**
     * 用来引入布局的方法
     */
    private fun getView(view: Int): View {
        return View.inflate(mContext, view, null)
    }

    class PicturesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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
        fun onClick(position: Int)
    }

    private var itemClickListener: ItemClickListener? = null
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }
//    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
//        val layoutManager = recyclerView.layoutManager
//        if (layoutManager is GridLayoutManager) {
//            val gridLayoutManager = layoutManager as GridLayoutManager?
//            gridLayoutManager!!.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//                override fun getSpanSize(position: Int): Int {
//                    return if (position == 0 || position == 1 ) {
//                        gridLayoutManager.spanCount
//                    } else 2
//                }
//            }
//            gridLayoutManager.spanCount = gridLayoutManager.spanCount
//        }
//    }
}