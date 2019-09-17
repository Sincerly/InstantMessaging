package com.ysxsoft.imtalk.widget.dialog

import android.content.Context
import android.os.Looper
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.adapter.TimesAdapter
import com.ysxsoft.imtalk.bean.AwardListDataBean
import com.ysxsoft.imtalk.bean.EggBean
import com.ysxsoft.imtalk.chatroom.utils.DisplayUtils
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import com.ysxsoft.imtalk.utils.ToastUtils
import com.ysxsoft.imtalk.view.JbWithDrawActivity
import com.ysxsoft.imtalk.widget.ABSDialog
import kotlinx.android.synthetic.main.za_dialog_layout.*
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers

/**
 *Create By 胡
 *on 2019/7/17 0017
 */
class EggDialog(var mContext: Context) : ABSDialog(mContext) {

    var type: String? = null
    var gifImageView: GifImageView? = null
    var gifDrawable: GifDrawable? = null
    var isClick = true

    override fun initView() {
        this.setCanceledOnTouchOutside(false)
        this.setCancelable(false)
        requestData(mContext)
        gifImageView = findViewById<GifImageView>(R.id.img_egg)
        gifDrawable = gifImageView!!.drawable as GifDrawable
        gifDrawable!!.stop()
        img_close.setOnClickListener {
            isClick = false
            dismiss()
        }
        tv_winning_record.setOnClickListener {
            isClick = false
            dismiss()
            WiningRecordDialog(mContext).show()
        }
        tv_award_pool.setOnClickListener {
            isClick = false
            dismiss()
            AwardPoolDialog(mContext).show()
        }
        tv_activity_rules.setOnClickListener {
            isClick = false
            dismiss()
            ActivityRulesDialog(mContext).show()
        }
        tv_withdraw.setOnClickListener {
            isClick = false
            dismiss()
            JbWithDrawActivity.starJbWithDrawActivity(mContext)
        }

        img_break.setOnClickListener {
            if (TextUtils.isEmpty(type) || type == null) {
                ToastUtils.showToast(this@EggDialog.context, "请选择砸金蛋次数")
                return@setOnClickListener
            }

            when (type) {
                "1" -> {
                    ZdData()
                }

                "2" -> {
                    ZdData()
                }

                "3" -> {
                    ZdData()
                }
                "4" -> {
                    ZdData()
                }
            }
        }
    }

    /**
     * 砸蛋的数据请求
     */
    private fun ZdData() {
        NetWork.getService(ImpService::class.java)
                .beginAward(SpUtils.getSp(this.context, "uid"), type!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Action1<EggBean> {
                    override fun call(t: EggBean?) {
                        ToastUtils.showToast(this@EggDialog.context, t!!.msg)
                        if (t.code == 0) {
                            //刷新金币
                            getMoney(context)
                            if ("4".equals(type)) {
                                if (isClick) {
                                    boxViewLayout.removeAllViews()
                                    gifDrawable!!.start(); //开始播放
                                    gifDrawable!!.setLoopCount(1); //设置播放的次数，播放完了就自动停止
                                    gifDrawable!!.reset()
                                    Thread(object:Runnable{
                                        override fun run() {
                                            if(onEggOpenListener!=null){
                                                onEggOpenListener!!.onEggOpened(t!!.data!!)
                                            }
                                        }
                                    }).start()
                                    android.os.Handler(Looper.getMainLooper()).postDelayed({
                                        showAnim(t!!.data[0].aw_pic)
                                        ZdData()
                                    }, 3000)
                                }
                            } else if("1".equals(type)) {
                                boxViewLayout.removeAllViews()
                                gifDrawable!!.start(); //开始播放
                                gifDrawable!!.setLoopCount(1); //设置播放的次数，播放完了就自动停止
                                gifDrawable!!.reset()
                                Thread(object:Runnable{
                                    override fun run() {
                                        if(onEggOpenListener!=null){
                                            onEggOpenListener!!.onEggOpened(t!!.data!!)
                                        }
                                    }
                                }).start()
                                //砸1次
                                android.os.Handler(Looper.getMainLooper()).postDelayed({
                                    showAnim(t!!.data[0].aw_pic)
                                }, 3000)
                            }else if("2".equals(type)) {
                                boxViewLayout.removeAllViews()
                                gifDrawable!!.start(); //开始播放
                                gifDrawable!!.setLoopCount(1); //设置播放的次数，播放完了就自动停止
                                gifDrawable!!.reset()
                                Thread(object:Runnable{
                                    override fun run() {
                                        if(onEggOpenListener!=null){
                                            onEggOpenListener!!.onEggOpened(t!!.data!!)
                                        }
                                    }
                                }).start()
                                //砸10次
                                android.os.Handler(Looper.getMainLooper()).postDelayed({
                                    for (bean in t!!.data) {
                                        showAnim(bean!!.sg_pic)
                                    }
                                }, 3000)
                            }else{
                                boxViewLayout.removeAllViews()
                                gifDrawable!!.start(); //开始播放
                                gifDrawable!!.setLoopCount(1); //设置播放的次数，播放完了就自动停止
                                gifDrawable!!.reset()
                                Thread(object:Runnable{
                                    override fun run() {
                                        if(onEggOpenListener!=null){
                                            onEggOpenListener!!.onEggOpened(t!!.data!!)
                                        }
                                    }
                                }).start()
                                //砸蛋100次
                                android.os.Handler(Looper.getMainLooper()).postDelayed({
                                    //跳转到列表
                                }, 3000)
                            }
                        }
                    }
                })
    }

//    private fun showGif(picUrl:String){
//        Glide.with(context).asGif().listener(object : RequestListener<com.bumptech.glide.load.resource.gif.GifDrawable> {
//            override fun onLoadFailed(e: GlideException?, model: Any, target: Target<com.bumptech.glide.load.resource.gif.GifDrawable>, isFirstResource: Boolean): Boolean {
//                return false
//            }
//
//            override fun onResourceReady(resource: com.bumptech.glide.load.resource.gif.GifDrawable, model: Any, target: Target<com.bumptech.glide.load.resource.gif.GifDrawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
//                try {
//                    val gifStateField = com.bumptech.glide.load.resource.gif.GifDrawable::class.java.getDeclaredField("state")
//                    gifStateField.isAccessible = true
//                    val gifStateClass = Class.forName("com.bumptech.glide.load.resource.gif.GifDrawable\$GifState")
//                    val gifFrameLoaderField = gifStateClass.getDeclaredField("frameLoader")
//                    gifFrameLoaderField.isAccessible = true
//                    val gifFrameLoaderClass = Class.forName("com.bumptech.glide.load.resource.gif.GifFrameLoader")
//                    val gifDecoderField = gifFrameLoaderClass.getDeclaredField("gifDecoder")
//                    gifDecoderField.isAccessible = true
//                    val gifDecoderClass = Class.forName("com.bumptech.glide.gifdecoder.GifDecoder")
//                    val gifDecoder = gifDecoderField.get(gifFrameLoaderField.get(gifStateField.get(resource)))
//                    val getDelayMethod = gifDecoderClass.getDeclaredMethod("getDelay", Int::class.javaPrimitiveType!!)
//                    getDelayMethod.isAccessible = true
//                    //设置只播放一次
//                    resource.setLoopCount(1)
//                    //获得总帧数
//                    val count = resource.frameCount
//                    var delay = 0
//                    for (i in 0 until count) {
//                        //计算每一帧所需要的时间进行累加
//                        delay += getDelayMethod.invoke(gifDecoder, i) as Int
//                    }
//                    Log.e("tag","delay:"+delay)
//                } catch (e: NoSuchFieldException) {
//                    e.printStackTrace()
//                } catch (e: ClassNotFoundException) {
//                    e.printStackTrace()
//                } catch (e: IllegalAccessException) {
//                    e.printStackTrace()
//                } catch (e: NoSuchMethodException) {
//                    e.printStackTrace()
//                } catch (e: InvocationTargetException) {
//                    e.printStackTrace()
//                }
//                return false
//            }
//        }).diskCacheStrategy(DiskCacheStrategy.RESOURCE).load(R.mipmap.egg).into(img_egg)
//    }

    private fun requestData(mContext: Context) {
        NetWork.getService(ImpService::class.java)
                .awardList(SpUtils.getSp(this.context, "uid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<AwardListDataBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: AwardListDataBean?) {
                        if (t!!.code == 0) {
                            tv_1.setText(t.data.zjd_gold_num)

                            val adapter = TimesAdapter(this@EggDialog.context)
                            val manager = LinearLayoutManager(mContext)
                            manager.orientation = LinearLayoutManager.HORIZONTAL
                            recyclerView.layoutManager = manager
                            recyclerView.adapter = adapter
                            adapter.addAll(t.data.zjd_times)
                            adapter.setOnItemClickListener(object : TimesAdapter.OnItemClickListener {
                                override fun onClick(position: Int) {
                                    type = adapter.dataList.get(position).type
                                    adapter.setSelect(position)
                                }
                            })
                            tv_jb.setText(t.data.money)
                        }
                    }

                    override fun onCompleted() {
                    }
                })

    }

    private fun getMoney(mContext: Context) {
        NetWork.getService(ImpService::class.java)
                .awardList(SpUtils.getSp(this.context, "uid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<AwardListDataBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: AwardListDataBean?) {
                        if (t!!.code == 0) {
                            tv_jb.setText(t.data.money)
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }

    override fun getLayoutResId(): Int {
        return R.layout.za_dialog_layout
    }

    private fun showAnim(picUrl: String) {
        val v = View.inflate(mContext, R.layout.view_box, null)
        val boxView = v.findViewById<ImageView>(R.id.boxView)
        val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.gravity=Gravity.CENTER;
        v.layoutParams = layoutParams
        v.alpha = 0f
        v.translationX = 0f
        v.translationY = 0f
        Glide.with(context).load(picUrl).into(boxView)
        //var width = boxLayout.width /2- DisplayUtils.dp2px(context!!, 22)
        var width = boxLayout.width /2
        var height = boxLayout.height / 2//边界检测

        val x = floatArrayOf(0.1f, 0.2f, 0.3f,0.5f,0.6f,1.0f,1.1f,1.2f,1.3f,1.5f,1.6f)
        val xv = (Math.random() * x.size).toInt()
        var offsetX = (x[xv] * width).toInt()
        //var offsetY = -(Math.random() * height).toInt()
        val f = floatArrayOf(0.3f,0.4f,0.5f,0.6f, 0.72f, 0.73f)
        val yv = (Math.random() * f.size).toInt()
        var offsetY = -(f[yv] * height).toInt() + DisplayUtils.dp2px(context!!, 5)//偏移的高度
        if (offsetX < (boxLayout.width / 2)) {
            offsetX = -offsetX;
        }else{
            offsetX=offsetX-boxLayout.width/2;
        }
        boxViewLayout.addView(v)
        ViewCompat.animate(v).alpha(1f).translationXBy(offsetX.toFloat()).translationYBy(offsetY.toFloat()).setInterpolator(AccelerateDecelerateInterpolator()).setDuration(700).start()
    }

    interface OnEggOpenListener {
        fun onEggOpened(data:List<EggBean.DataBean>)
    }

    private var onEggOpenListener: OnEggOpenListener? = null
    fun setOnEggOpenListener(onEggOpenListener: OnEggOpenListener) {
        this.onEggOpenListener = onEggOpenListener
    }

}