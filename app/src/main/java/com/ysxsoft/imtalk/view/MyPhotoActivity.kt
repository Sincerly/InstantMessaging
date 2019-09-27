package com.ysxsoft.imtalk.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.View
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.permissions.RxPermissions
import com.luck.picture.lib.tools.PictureFileUtils
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.R.id.tv_title_right
import com.ysxsoft.imtalk.adapter.MyPictureAdapter
import com.ysxsoft.imtalk.adapter.PicturesAdapter
import com.ysxsoft.imtalk.bean.CommonBean
import com.ysxsoft.imtalk.bean.UploadFileBean
import com.ysxsoft.imtalk.bean.UserInfoBean
import com.ysxsoft.imtalk.com.FullyGridLayoutManager
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.*
import com.ysxsoft.imtalk.utils.BaseApplication.Companion.mContext
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.my_photo_layout.*
import kotlinx.android.synthetic.main.title_layout.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers
import java.io.File

/**
 *Create By 胡
 *on 2019/7/16 0016
 */
class MyPhotoActivity : BaseActivity(), MyPictureAdapter.CheckInterface {


    override fun checkGroup(position: Int, isChecked: Boolean) {
        mDataAdapter!!.dataList.get(position).setChoosed(isChecked)
        mDataAdapter!!.notifyDataSetChanged()
    }

    override fun getLayout(): Int {
        return R.layout.my_photo_layout
    }

    var ismanager: Boolean = true
    var uid: String? = null
    var mDataAdapter: MyPictureAdapter? = null
    var sectionDelete = StringBuffer()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         uid = intent.getStringExtra("uid")
        tv_title_right.visibility = View.VISIBLE
        tv_title_right.setText("管理")
        setBackVisibily()
        setTitle("我的相册")
        initView()
        requestData()
    }

    private fun requestData() {
        NetWork.getService(ImpService::class.java)
                .GetUserInfo(uid!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<UserInfoBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: UserInfoBean?) {
                        if (t!!.code == 0) {
                            val pictures = t.data.picture
                            pictures.add(0, UserInfoBean.DataBean.PictureBean())
                            mDataAdapter = MyPictureAdapter(mContext)
                            recyclerView.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
                            recyclerView.adapter = mDataAdapter
                            mDataAdapter!!.addAll(pictures)
                            mDataAdapter!!.setItemClickListener(object : MyPictureAdapter.ItemClickListener {
                                override fun onItemClick(position: Int) {
                                    RxPermissions(this@MyPhotoActivity).request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe { aBoolean ->
                                        if (aBoolean!!) {
                                            //申请的权限全部允许
                                            openGallery()
                                        } else {
                                            //只要有一个权限被拒绝，就会执行
                                            showToastMessage("未授权权限，部分功能不能使用")
                                        }
                                    }
                                }
                            })
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }

    fun initView() {
        tv_title_right.setOnClickListener {
            ismanager = !ismanager
            if (ismanager) {
                tv_title_right.setText("管理")
                mDataAdapter!!.isShow(false)
                tv_del.visibility = View.GONE
            } else {
                tv_title_right.setText("删除")
                tv_del.visibility = View.VISIBLE
                mDataAdapter!!.isShow(true)
            }
        }
        tv_del.setOnClickListener {

            if (sectionDelete.length != 0) {
                sectionDelete.setLength(0)
            }
            for (dataBean in mDataAdapter!!.dataList) {
                val choosed = dataBean.isChoosed
                if (choosed) {
                    sectionDelete.append(dataBean.id.toString()).append(",")
                }
            }
            if (sectionDelete.length == 0) {
                showToastMessage("照片删除不能为空")
                return@setOnClickListener
            }
            var delId = sectionDelete.deleteCharAt(sectionDelete.length - 1).toString();
            delData(delId)

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    val localMedia = PictureSelector.obtainMultipleResult(data)
                    val file = File(localMedia[0].path)
                    val requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
                    val body = MultipartBody.Part.createFormData("file", file.name, requestBody)
                    NetWork.getService(ImpService::class.java)
                            .UploadFile(body)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(object : Observer<UploadFileBean> {
                                override fun onError(e: Throwable?) {
                                    showToastMessage(e!!.message.toString())
                                }

                                override fun onNext(t: UploadFileBean?) {
                                    ClearCache()
                                    if (t!!.code == 0) {
                                        savaPhoto(t.data.id)
                                    }
                                }

                                override fun onCompleted() {
                                }
                            })


                }
            }
        }
    }

    private fun savaPhoto(data: String?) {
        NetWork.getService(ImpService::class.java)
                .Picture(SpUtils.getSp(mContext, "uid"), data!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CommonBean> {
                    override fun onError(e: Throwable?) {
                        Log.d(this@MyPhotoActivity.localClassName, e!!.message.toString())
                    }

                    override fun onNext(t: CommonBean?) {
                        showToastMessage(t!!.msg)
                        if (t.code == 0) {
                            requestData()
                            mDataAdapter!!.notifyDataSetChanged()
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }


    private fun openGallery() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofAll())
                .maxSelectNum(1)// 最大图片选择数量
                .selectionMode(PictureConfig.SINGLE)
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .enableCrop(false)// 是否裁剪
                .compress(true)// 是否压缩
                .compressSavePath(FileUtils.getSDCardPath(mContext))//压缩图片保存地址
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .forResult(PictureConfig.CHOOSE_REQUEST)//结果回调onActivityResult code
    }

    private fun delData(delId: String) {
        NetWork.getService(ImpService::class.java)
                .Delphoto(SpUtils.getSp(mContext, "uid"), delId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Action1<CommonBean> {
                    override fun call(t: CommonBean?) {
                        showToastMessage(t!!.msg)
                        if (t.code == 0) {
                            requestData()
                            mDataAdapter!!.notifyDataSetChanged()
                            tv_title_right.setText("管理")
                            tv_del.visibility = View.GONE
                        }
                    }
                })

    }

    private fun ClearCache() {
        PictureFileUtils.deleteCacheDirFile(this);
        RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(object : io.reactivex.Observer<Boolean> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onNext(t: Boolean) {
                        if (t) {
                            PictureFileUtils.deleteCacheDirFile(mContext);
                        } else {
                            showToastMessage(getString(R.string.picture_jurisdiction));
                        }
                    }
                });
    }
}