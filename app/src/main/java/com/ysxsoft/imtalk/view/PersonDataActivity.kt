package com.ysxsoft.imtalk.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.permissions.RxPermissions
import com.luck.picture.lib.tools.PictureFileUtils
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.bean.CommonBean
import com.ysxsoft.imtalk.bean.UploadFileBean
import com.ysxsoft.imtalk.bean.UserInfoBean
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.*
import com.ysxsoft.imtalk.widget.dialog.NickNameDialog
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.person_data_layout.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 *Create By 胡
 *on 2019/7/16 0016
 */
class PersonDataActivity : BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.person_data_layout
    }

    var fielid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("个人资料")
        setBackVisibily()
        initView()
        requestData()
    }

    private fun requestData() {
        NetWork.getService(ImpService::class.java)
                .GetUserInfo(SpUtils.getSp(mContext, "uid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<UserInfoBean> {
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: UserInfoBean?) {
                        if (t!!.code == 0) {
                            ImageLoadUtil.GlideHeadImageLoad(mContext, t.data.icon, img_head)
                            ed_nikeName.setText(t.data.nickname)
                            tv_birth.setText(t.data.date_birth)
                        }
                    }

                    override fun onCompleted() {
                    }
                })


    }

    private fun initView() {
        img_head.setOnClickListener {
            RxPermissions(this@PersonDataActivity)
                    .request(Manifest.permission.READ_EXTERNAL_STORAGE,
                             Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe { aBoolean ->
                if (aBoolean!!) {
                    //申请的权限全部允许
                    openGallery()
                } else {
                    //只要有一个权限被拒绝，就会执行
                    showToastMessage("未授权权限，部分功能不能使用")
                }
            }
        }
        tv_birth.setOnClickListener {
            AppUtil.colsePhoneKeyboard(this)
            val pvTime = TimePickerBuilder(mContext, object : OnTimeSelectListener {
                override fun onTimeSelect(date: Date?, v: View?) {
                    val format = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
                    tv_birth.setText(format.format(date).toString())
                    savaBirth(format.format(date).toString())
                }
            }).build()
            pvTime.show()
        }
        tv_photo.setOnClickListener {
            startActivity(MyPhotoActivity::class.java)
        }

        tv_introduce.setOnClickListener {
            startActivity(PersonIntroduceActivity::class.java)
        }

        ed_nikeName.setOnClickListener {
            val nameDialog = NickNameDialog(mContext)
            nameDialog.setNickNameClickListener(object :NickNameDialog.NickNameClickListener{
                override fun nickName(name: String) {
                    ed_nikeName.setText(name)
                    saveNickName(name)
                }
            })
            nameDialog.show()
        }
    }

    private fun savaBirth(birth: String) {
        NetWork.getService(ImpService::class.java)
                .Brith(SpUtils.getSp(mContext,"uid"),birth)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Action1<CommonBean>{
                    override fun call(t: CommonBean?) {
                        showToastMessage(t!!.msg)
                    }
                })

    }

    private fun saveNickName(name: String) {
        NetWork.getService(ImpService::class.java)
                .NikeName(SpUtils.getSp(mContext,"uid"),name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Action1<CommonBean>{
                    override fun call(t: CommonBean?) {
                        showToastMessage(t!!.msg)
                    }
                })

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
                            .subscribe(object : Observer<UploadFileBean>{
                                override fun onError(e: Throwable?) {
                                    showToastMessage(e!!.message.toString())
                                }

                                override fun onNext(t: UploadFileBean?) {
                                    ClearCache()
                                    if (t!!.code == 0) {
                                        fielid = t.data.id
                                        ImageLoadUtil.GlideGoodsImageLoad(mContext, file.toString(), img_head)
                                        saveHead(fielid)
                                    }
                                }

                                override fun onCompleted() {
                                }
                            })


                }
            }
        }
    }

    private fun saveHead(fielid: String?) {
        NetWork.getService(ImpService::class.java)
                .Head(SpUtils.getSp(mContext,"uid"),fielid!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Observer<CommonBean>{
                    override fun onError(e: Throwable?) {
                    }

                    override fun onNext(t: CommonBean?) {
                        showToastMessage(t!!.msg)
                    }

                    override fun onCompleted() {
                    }
                })
    }

    private fun openGallery() {
        PictureSelector.create(this@PersonDataActivity)
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

    private fun ClearCache() {
        PictureFileUtils.deleteCacheDirFile(this);
        RxPermissions(this@PersonDataActivity)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(object : io.reactivex.Observer<Boolean> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(t: Boolean) {
                        if (t) {
                            PictureFileUtils.deleteCacheDirFile(mContext);
                        } else {
                            showToastMessage(getString(R.string.picture_jurisdiction));
                        }
                    }

                    override fun onError(e: Throwable) {
                    }

                    override fun onComplete() {
                    }
                });
    }
}