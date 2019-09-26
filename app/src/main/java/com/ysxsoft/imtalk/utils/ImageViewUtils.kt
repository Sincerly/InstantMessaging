package com.ysxsoft.imtalk.utils

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ysxsoft.imtalk.R


/**
 * 加载string类型
 */
fun Glide.loadUrl(iv: ImageView, url: String?, def: Int) {
    Glide.with(context)
            .load(url)
            .apply(RequestOptions()
                    .placeholder(def)
                    .error(R.mipmap.icon_pic_error))
            .into(iv)
}
//圆形
fun Glide.loadUrlCyclo(iv: ImageView, url: String?, def: Int) {
    val mRequestOptions = RequestOptions
            .circleCropTransform()
            .error(R.mipmap.icon_pic_error)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)//磁盘缓存
            .skipMemoryCache(true)//不做内存缓存
    Glide.with(context).load(url).apply(mRequestOptions).into(iv)
}
fun Glide.loadUrlCyclo(iv: ImageView, url: Int?, def: Int) {
    val mRequestOptions = RequestOptions
            .circleCropTransform()
            .error(R.mipmap.icon_pic_error)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)//磁盘缓存
            .skipMemoryCache(true)//不做内存缓存
    Glide.with(context).load(url).apply(mRequestOptions).into(iv)
}
//圆角
fun Glide.loadUrlRoundCorner(iv: ImageView, url: String?, def: Int, cornersSize: Float) {
    //设置图片圆角角度
        val glideRoundTransform = CenterCropRoundCornerTransform(cornersSize)
    //通过RequestOptions扩展功能
    val options = RequestOptions
            .bitmapTransform(glideRoundTransform)
            .error(R.mipmap.icon_pic_error)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)//自动磁盘缓存
            .skipMemoryCache(true)//不做内存缓存 缓存到外存
            .override(300, 300)
    Glide.with(context).load(url).apply(options).into(iv)
}

/**
 * 加载本地资源
 */
fun Glide.loadRes(iv: ImageView, resId: Int?, def: Int) {
    Glide.with(context)
            .load(getDrawableURi(resId))
            .apply(RequestOptions()
                    .placeholder(def)
                    .error(R.mipmap.icon_pic_error))
            .into(iv)
}
//圆形
fun Glide.loadResCyclo(iv: ImageView, resId: Int?, def: Int) {
    val mRequestOptions = RequestOptions
            .circleCropTransform()
            .error(R.mipmap.icon_pic_error)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)//不做磁盘缓存
            .skipMemoryCache(true)//不做内存缓存
    Glide.with(context).load(resId).apply(mRequestOptions).into(iv)
}
//圆角
fun Glide.loadResRoundCorner(iv: ImageView, resId: Int, def: Int, cornersSize: Float) {
    //设置图片圆角角度
    var glideRoundTransform = CenterCropRoundCornerTransform(cornersSize)
    //通过RequestOptions扩展功能
    val options = RequestOptions
            .bitmapTransform(glideRoundTransform)
            .error(R.mipmap.icon_pic_error)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)//自动磁盘缓存
            .skipMemoryCache(true)//不做内存缓存 缓存到外存
            .override(300, 300)
    Glide.with(context).load(resId).apply(options).into(iv)
}

/**
 * 获取本地资源转uri
 */
fun getDrawableURi(id: Int?): Uri? {
    return Uri.parse("android.resource://" + (BaseApplication.mContext?.getPackageName()
            ?: "") + "/" + id)
}

/**
 * 加载网络图片
 */
fun ImageView.displayUrl(url: String?) {
    Glide.get(context).loadUrl(this, url, R.mipmap.icon_def)
}
fun ImageView.displayUrl(url: String?, def: Int) {
    Glide.get(context).loadUrl(this, url, def)
}
//圆形
fun ImageView.displayUrlCyclo(url: String?) {
    Glide.get(context).loadUrlCyclo(this, url, R.mipmap.icon_def)
}
fun ImageView.displayUrlCyclo(url: String?, def: Int) {
    Glide.get(context).loadUrlCyclo(this, url, def)
}
//圆角
fun ImageView.displayUrlRoundCorner(url: String?, size : Float) {
    Glide.get(context).loadUrlRoundCorner(this, url, R.mipmap.icon_def, size)
}
fun ImageView.displayUrlRoundCorner(url: String?, def: Int, size : Float) {
    Glide.get(context).loadUrlRoundCorner(this, url, def, size)
}
/**
 * 加载本地资源
 */
fun ImageView.displayRes(path: Int?) {
    Glide.get(context).loadRes(this, path, R.mipmap.icon_def)
}
fun ImageView.displayRes(path: Int?, def: Int) {
    Glide.get(context).loadRes(this, path, def)
}
//圆形
fun ImageView.displayResCyclo(url: Int?) {
    Glide.get(context).loadResCyclo(this, url, R.mipmap.icon_def)
}
fun ImageView.displayResCyclo(url: Int?, def: Int) {
    Glide.get(context).loadResCyclo(this, url, def)
}
//圆角
fun ImageView.displayResRoundCorner(resId: Int, size : Float) {
    Glide.get(context).loadResRoundCorner(this, resId, R.mipmap.icon_def, size)
}
fun ImageView.displayResRoundCorner(resId: Int, def: Int, size : Float) {
    Glide.get(context).loadResRoundCorner(this, resId, def, size)
}

