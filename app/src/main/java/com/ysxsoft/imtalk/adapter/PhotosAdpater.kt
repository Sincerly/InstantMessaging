package com.ysxsoft.imtalk.adapter

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.XPopupImageLoader
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.UserInfoBean
import com.ysxsoft.imtalk.com.ListBaseAdapter
import com.ysxsoft.imtalk.com.SuperViewHolder
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import com.ysxsoft.imtalk.view.MyPhotoActivity
import java.io.File

/**
 *Create By 胡
 *on 2019/7/23 0023
 */
class PhotosAdpater(mContext: Context, var uid: String, var myself: String) : ListBaseAdapter<UserInfoBean.DataBean.PictureBean>(mContext) {
    override val layoutId: Int
        get() = R.layout.photo_item_layout

    override fun onBindItemHolder(holder: SuperViewHolder, position: Int) {
        val bean = mDataList.get(position)
        if (TextUtils.equals(myself, "myself")) {
            if (position == 0) {
                holder.getView<ImageView>(R.id.img_tupian)!!.visibility = View.VISIBLE
                holder.getView<ImageView>(R.id.img_tupian)!!.setImageResource(R.mipmap.img_add)
                holder.getView<ImageView>(R.id.img_tupian)!!.setOnClickListener {
                    val intent = Intent(mContext, MyPhotoActivity::class.java)
                    intent.putExtra("uid", uid)
                    mContext.startActivity(intent)
                }

            } else {
                ImageLoadUtil.GlideGoodsImageLoad(mContext, bean.photo, holder.getView<ImageView>(R.id.img_tupian)!!)
                holder.getView<ImageView>(R.id.img_tupian)!!.setOnClickListener {
                    XPopup.Builder(mContext).asImageViewer(holder.getView<ImageView>(R.id.img_tupian)!!, bean.photo, ImageLoader()).show();
                }
            }
        } else {
            ImageLoadUtil.GlideGoodsImageLoad(mContext, bean.photo, holder.getView<ImageView>(R.id.img_tupian)!!)
            holder.getView<ImageView>(R.id.img_tupian)!!.setOnClickListener {
                XPopup.Builder(mContext).asImageViewer(holder.getView<ImageView>(R.id.img_tupian)!!, bean.photo, ImageLoader()).show();
            }
        }


    }

    inner class ImageLoader : XPopupImageLoader {
        override fun loadImage(position: Int, url: Any, imageView: ImageView) {
            //必须指定Target.SIZE_ORIGINAL，否则无法拿到原图，就无法享用天衣无缝的动画
            Glide.with(imageView).load(url)/*.apply(RequestOptions().placeholder(R.mipmap.ic_launcher_round).override(Target.SIZE_ORIGINAL))*/.into(imageView)
        }

        override fun getImageFile(context: Context, uri: Any): File? {
            try {
                return Glide.with(context).downloadOnly().load(uri).submit().get()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        }
    }

}