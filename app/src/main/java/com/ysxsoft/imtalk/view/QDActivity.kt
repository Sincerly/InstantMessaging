package com.ysxsoft.imtalk.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.QdSignListBean
import com.ysxsoft.imtalk.bean.SignDayBean
import com.ysxsoft.imtalk.chatroom.net.retrofit.RetrofitUtil
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.ImageLoadUtil
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import kotlinx.android.synthetic.main.qd_layout.*
import kotlinx.android.synthetic.main.w_translation_title_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *Create By 胡
 *on 2019/7/17 0017
 */
class QDActivity : BaseActivity() {

    override fun getLayout(): Int {
        return R.layout.qd_layout
    }

    var sign_id: String? = null
    var bean: QdSignListBean? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLightStatusBar(false)
        initStatusBar(topView)
        setBackVisibily()
        setTitle("签到")
        initView()
    }

    private fun initView() {
        tv_qd_gz.setOnClickListener {
            startActivity(SignRulesActivity::class.java)
        }
        tv_ok.setOnClickListener {

        }
        ll_1.setOnClickListener {

            when (bean!!.data.sign_list.get(0).qd) {
                0 -> {
//                    BQdata(bean!!.data.sign_list.get(0).sign_id, bean!!.data.sign_list.get(0).day)
                    val intent = Intent(mContext, JbWithDrawActivity::class.java)
                    intent.putExtra("sign_id",bean!!.data.sign_list.get(0).sign_id)
                    intent.putExtra("day_time",bean!!.data.sign_list.get(0).day)
                    startActivity(intent)
                }
                2 -> {
                    QDdata(bean!!.data.sign_list.get(0).sign_id)
                }
            }

        }
        ll_2.setOnClickListener {
            when (bean!!.data.sign_list.get(1).qd) {
                0 -> {
//                    BQdata(bean!!.data.sign_list.get(1).sign_id, bean!!.data.sign_list.get(1).day)
                    val intent = Intent(mContext, JbWithDrawActivity::class.java)
                    intent.putExtra("sign_id",bean!!.data.sign_list.get(1).sign_id)
                    intent.putExtra("day_time",bean!!.data.sign_list.get(1).day)
                    startActivity(intent)
                }
                2 -> {
                    QDdata(bean!!.data.sign_list.get(1).sign_id)
                }
            }

        }
        ll_3.setOnClickListener {
            when (bean!!.data.sign_list.get(2).qd) {
                0 -> {
//                    BQdata(bean!!.data.sign_list.get(2).sign_id, bean!!.data.sign_list.get(2).day)
                    val intent = Intent(mContext, JbWithDrawActivity::class.java)
                    intent.putExtra("sign_id",bean!!.data.sign_list.get(2).sign_id)
                    intent.putExtra("day_time",bean!!.data.sign_list.get(2).day)
                    startActivity(intent)
                }
                2 -> {
                    QDdata(bean!!.data.sign_list.get(2).sign_id)
                }
            }

        }
        ll_4.setOnClickListener {
            when (bean!!.data.sign_list.get(3).qd) {
                0 -> {
//                    BQdata(bean!!.data.sign_list.get(3).sign_id, bean!!.data.sign_list.get(3).day)
                    val intent = Intent(mContext, JbWithDrawActivity::class.java)
                    intent.putExtra("sign_id",bean!!.data.sign_list.get(3).sign_id)
                    intent.putExtra("day_time",bean!!.data.sign_list.get(3).day)
                    startActivity(intent)
                }
                2 -> {
                    QDdata(bean!!.data.sign_list.get(3).sign_id)
                }
            }

        }
        ll_5.setOnClickListener {
            when (bean!!.data.sign_list.get(4).qd) {
                0 -> {
//                    BQdata(bean!!.data.sign_list.get(4).sign_id, bean!!.data.sign_list.get(4).day)
                    val intent = Intent(mContext, JbWithDrawActivity::class.java)
                    intent.putExtra("sign_id",bean!!.data.sign_list.get(4).sign_id)
                    intent.putExtra("day_time",bean!!.data.sign_list.get(4).day)
                    startActivity(intent)
                }
                2 -> {
                    QDdata(bean!!.data.sign_list.get(4).sign_id)
                }
            }

        }
        ll_6.setOnClickListener {
            when (bean!!.data.sign_list.get(5).qd) {
                0 -> {
//                    BQdata(bean!!.data.sign_list.get(5).sign_id,bean!!.data.sign_list.get(5).day)
                    val intent = Intent(mContext, JbWithDrawActivity::class.java)
                    intent.putExtra("sign_id",bean!!.data.sign_list.get(5).sign_id)
                    intent.putExtra("day_time",bean!!.data.sign_list.get(5).day)
                    startActivity(intent)
                }
                2 -> {
                    QDdata(bean!!.data.sign_list.get(5).sign_id)
                }
            }

        }
        ll_7.setOnClickListener {
            when (bean!!.data.sign_list.get(6).qd) {
                0 -> {
//                    BQdata(bean!!.data.sign_list.get(6).sign_id, bean!!.data.sign_list.get(6).day)
                    val intent = Intent(mContext, JbWithDrawActivity::class.java)
                    intent.putExtra("sign_id",bean!!.data.sign_list.get(6).sign_id)
                    intent.putExtra("day_time",bean!!.data.sign_list.get(6).day)
                    startActivity(intent)
                }
                2 -> {
                    QDdata(bean!!.data.sign_list.get(6).sign_id)
                }
            }

        }


    }

    //补签
    private fun BQdata(sign_id: String?, day_name: String) {
        val map = HashMap<String, String>()
        map.put("uid", SpUtils.getSp(mContext, "uid"))
        map.put("sign_id", sign_id!!)
        map.put("day_time", day_name)
        val body = RetrofitUtil.createJsonRequest(map)
        NetWork.getService(ImpService::class.java)
                .help_sign(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Observer<SignDayBean>{
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: SignDayBean?) {
                        if (t!!.code == 0) {
                            when (t.data) {
                                "1" -> {
                                    fl_show1.visibility = View.VISIBLE
                                }
                                "2" -> {
                                    fl_show2.visibility = View.VISIBLE
                                }
                                "3" -> {
                                    fl_show3.visibility = View.VISIBLE
                                }
                                "4" -> {
                                    fl_show4.visibility = View.VISIBLE
                                }
                                "5" -> {
                                    fl_show5.visibility = View.VISIBLE
                                }
                                "6" -> {
                                    fl_show6.visibility = View.VISIBLE
                                }
                                "7" -> {
                                    fl_show7.visibility = View.VISIBLE
                                }
                            }
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }

    //签到
    private fun QDdata(sign_id: String?) {
        val map = HashMap<String, String>()
        map.put("uid", SpUtils.getSp(mContext, "uid"))
        map.put("sign_id", sign_id!!)
        val body = RetrofitUtil.createJsonRequest(map)
        NetWork.getService(ImpService::class.java)
                .sign_day(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<SignDayBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: SignDayBean?) {
                        if (t!!.code == 0) {
                            when (t.data) {
                                "1" -> {
                                    fl_show1.visibility = View.VISIBLE
                                }
                                "2" -> {
                                    fl_show2.visibility = View.VISIBLE
                                }
                                "3" -> {
                                    fl_show3.visibility = View.VISIBLE
                                }
                                "4" -> {
                                    fl_show4.visibility = View.VISIBLE
                                }
                                "5" -> {
                                    fl_show5.visibility = View.VISIBLE
                                }
                                "6" -> {
                                    fl_show6.visibility = View.VISIBLE
                                }
                                "7" -> {
                                    fl_show7.visibility = View.VISIBLE
                                }
                            }
                        }else{
                            showToastMessage(t.msg)
                        }
                    }

                    override fun onCompleted() {
                    }
                })

    }

    override fun onResume() {
        super.onResume()
        requestData()
    }

    private fun requestData() {
        NetWork.getService(ImpService::class.java)
                .SignList(SpUtils.getSp(mContext, "uid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<QdSignListBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: QdSignListBean?) {
                        if (t!!.code == 0) {
                            bean = t
                            val sign_day = t.data.sign_day
                            tv_ge.setText(sign_day)
                            tv_is_qd.setText(t.data.sign_descs)
                            /*     if (!TextUtils.isEmpty(sign_day) && sign_day != null) {
                                when (sign_day.length) {
                                    1 -> {
                                        tv_ge.setText(sign_day)
                                    }
                                    2 -> {
                                        for (str in sign_day.indices) {
                                            tv_shi.setText(sign_day[0].toString())
                                            tv_ge.setText(sign_day[1].toString())
                                        }
                                    }
                                    3 -> {
                                        for (str in sign_day.indices) {
                                            tv_bai.setText(sign_day[0].toString())
                                            tv_shi.setText(sign_day[1].toString())
                                            tv_ge.setText(sign_day[2].toString())
                                        }
                                    }
                                }
                            }*/
                            ImageLoadUtil.GlideHeadImageLoad(mContext, t.data.sign_list.get(0).lw_pic, img_tupian1)
                            ImageLoadUtil.GlideHeadImageLoad(mContext, t.data.sign_list.get(1).lw_pic, img_tupian2)
                            ImageLoadUtil.GlideHeadImageLoad(mContext, t.data.sign_list.get(2).lw_pic, img_tupian3)
                            ImageLoadUtil.GlideHeadImageLoad(mContext, t.data.sign_list.get(3).lw_pic, img_tupian4)
                            ImageLoadUtil.GlideHeadImageLoad(mContext, t.data.sign_list.get(4).lw_pic, img_tupian5)
                            ImageLoadUtil.GlideHeadImageLoad(mContext, t.data.sign_list.get(5).lw_pic, img_tupian6)
                            ImageLoadUtil.GlideHeadImageLoad(mContext, t.data.sign_list.get(6).lw_pic, img_tupian7)

                            when (t.data.sign_list.get(0).lw) {
                                1 -> {
                                    tv_jb1.setText(t.data.sign_list.get(0).sign_gold.toString() + "金币")
                                }
                                2 -> {
                                    tv_jb1.setText(t.data.sign_list.get(0).sign_gold.toString() + "座驾")
                                }
                                3 -> {
                                    tv_jb1.setText(t.data.sign_list.get(0).sign_gold.toString() + "头饰")
                                }
                            }

                            when (t.data.sign_list.get(1).lw) {
                                1 -> {
                                    tv_jb2.setText(t.data.sign_list.get(1).sign_gold.toString() + "金币")
                                }
                                2 -> {
                                    tv_jb2.setText(t.data.sign_list.get(1).sign_gold.toString() + "座驾")
                                }
                                3 -> {
                                    tv_jb2.setText(t.data.sign_list.get(1).sign_gold.toString() + "头饰")
                                }
                            }

                            when (t.data.sign_list.get(2).lw) {
                                1 -> {
                                    tv_jb3.setText(t.data.sign_list.get(2).sign_gold.toString() + "金币")
                                }
                                2 -> {
                                    if (t.data.sign_list.get(2).sign_gold==0){
                                        tv_jb3.setText("座驾")
                                    }else{
                                        tv_jb3.setText(t.data.sign_list.get(2).sign_gold.toString() + "座驾")
                                    }
                                }
                                3 -> {
                                    if (t.data.sign_list.get(2).sign_gold==0){
                                        tv_jb3.setText("头饰")
                                    }else{
                                        tv_jb3.setText(t.data.sign_list.get(2).sign_gold.toString() + "头饰")
                                    }
                                }
                            }


                            when (t.data.sign_list.get(3).lw) {
                                1 -> {
                                    tv_jb4.setText(t.data.sign_list.get(3).sign_gold.toString() + "金币")
                                }
                                2 -> {
                                    tv_jb4.setText(t.data.sign_list.get(3).sign_gold.toString() + "座驾")
                                }
                                3 -> {
                                    tv_jb4.setText(t.data.sign_list.get(3).sign_gold.toString() + "头饰")
                                }
                            }


                            when (t.data.sign_list.get(4).lw) {
                                1 -> {
                                    tv_jb5.setText(t.data.sign_list.get(4).sign_gold.toString() + "金币")
                                }
                                2 -> {
                                    tv_jb5.setText(t.data.sign_list.get(4).sign_gold.toString() + "座驾")
                                }
                                3 -> {
                                    tv_jb5.setText(t.data.sign_list.get(4).sign_gold.toString() + "头饰")
                                }
                            }


                            when (t.data.sign_list.get(5).lw) {
                                1 -> {
                                    tv_jb6.setText(t.data.sign_list.get(5).sign_gold.toString() + "金币")
                                }
                                2 -> {
                                    tv_jb6.setText(t.data.sign_list.get(5).sign_gold.toString() + "座驾")
                                }
                                3 -> {
                                    tv_jb6.setText(t.data.sign_list.get(5).sign_gold.toString() + "头饰")
                                }
                            }


                            when (t.data.sign_list.get(6).lw) {
                                1 -> {
                                    tv_jb7.setText(t.data.sign_list.get(6).sign_gold.toString() + "金币")
                                }
                                2 -> {
                                    if (t.data.sign_list.get(6).sign_gold==0){
                                        tv_jb7.setText("座驾")
                                    }else{
                                        tv_jb7.setText(t.data.sign_list.get(6).sign_gold.toString() + "座驾")
                                    }
                                }
                                3 -> {
                                    if (t.data.sign_list.get(6).sign_gold==0){
                                        tv_jb7.setText("头饰")
                                    }else{
                                        tv_jb7.setText(t.data.sign_list.get(6).sign_gold.toString() + "头饰")
                                    }
                                }
                            }



                            when (t.data.sign_list.get(0).qd) {
                                0 -> {
                                    tv_go_bq1.setText("补签")
                                }
                                1 -> {
                                    fl_show1.visibility = View.VISIBLE
                                    tv_go_bq1.setText("已签")
                                }
                                2 -> {
                                    tv_go_bq1.setText("签到")
                                }
                            }

                            when (t.data.sign_list.get(1).qd) {
                                0 -> {
                                    tv_go_bq2.setText("补签")
                                }
                                1 -> {
                                    tv_go_bq2.setText("已签")
                                    fl_show2.visibility = View.VISIBLE
                                }
                                2 -> {
                                    tv_go_bq2.setText("签到")
                                }
                            }

                            when (t.data.sign_list.get(2).qd) {
                                0 -> {
                                    tv_go_bq3.setText("补签")
                                }
                                1 -> {
                                    tv_go_bq3.setText("已签")
                                    fl_show3.visibility = View.VISIBLE
                                }
                                2 -> {
                                    tv_go_bq3.setText("签到")
                                }
                            }

                            when (t.data.sign_list.get(3).qd) {
                                0 -> {
                                    tv_go_bq4.setText("补签")
                                }
                                1 -> {
                                    tv_go_bq4.setText("已签")
                                    fl_show4.visibility = View.VISIBLE
                                }
                                2 -> {
                                    tv_go_bq4.setText("签到")
                                }
                            }

                            when (t.data.sign_list.get(4).qd) {
                                0 -> {
                                    tv_go_bq5.setText("补签")
                                }
                                1 -> {
                                    tv_go_bq5.setText("已签")
                                    fl_show5.visibility = View.VISIBLE
                                }
                                2 -> {
                                    tv_go_bq5.setText("签到")
                                }
                            }

                            when (t.data.sign_list.get(5).qd) {
                                0 -> {
                                    tv_go_bq6.setText("补签")
                                }
                                1 -> {
                                    tv_go_bq6.setText("已签")
                                    fl_show6.visibility = View.VISIBLE
                                }
                                2 -> {
                                    tv_go_bq6.setText("签到")
                                }
                            }

                            when (t.data.sign_list.get(6).qd) {
                                0 -> {
                                    tv_go_bq7.setText("补签")
                                }
                                1 -> {
                                    tv_go_bq7.setText("已签")
                                    fl_show7.visibility = View.VISIBLE
                                }
                                2 -> {
                                    tv_go_bq7.setText("签到")
                                }
                            }
                        }
                    }

                    override fun onCompleted() {
                    }
                })


    }
}