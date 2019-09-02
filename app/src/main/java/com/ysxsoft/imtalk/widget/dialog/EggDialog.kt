package com.ysxsoft.imtalk.widget.dialog

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.adapter.TimesAdapter
import com.ysxsoft.imtalk.bean.AwardListDataBean
import com.ysxsoft.imtalk.bean.CommonBean
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
import kotlin.text.Typography.times

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
                    gifDrawable!!.start(); //开始播放
                    gifDrawable!!.setLoopCount(1); //设置播放的次数，播放完了就自动停止
                    gifDrawable!!.reset()
                    ZdData()
                }

                "2" -> {
                    gifDrawable!!.start(); //开始播放
                    gifDrawable!!.setLoopCount(1); //设置播放的次数，播放完了就自动停止
                    gifDrawable!!.reset()
                    ZdData()
                }

                "3" -> {
                    gifDrawable!!.start(); //开始播放
                    gifDrawable!!.setLoopCount(1); //设置播放的次数，播放完了就自动停止
                    gifDrawable!!.reset()
                    ZdData()
                }
                "4" -> {
                    gifDrawable!!.start(); //开始播放
                    gifDrawable!!.reset()
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
                .subscribe(object : Action1<CommonBean> {
                    override fun call(t: CommonBean?) {
                        ToastUtils.showToast(this@EggDialog.context, t!!.msg)
                        if (t.code == 0) {
                            if ("4".equals(type)) {
                                if (isClick) {
                                    ZdData()
                                }
                            }
                        }
                    }
                })
    }

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

    override fun getLayoutResId(): Int {
        return R.layout.za_dialog_layout
    }


}