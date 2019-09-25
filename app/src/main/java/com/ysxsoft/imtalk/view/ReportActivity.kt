package com.ysxsoft.imtalk.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.Log
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.permissions.RxPermissions
import com.luck.picture.lib.tools.PictureFileUtils
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.adapter.GridImageAdapter
import com.ysxsoft.imtalk.adapter.ReportAdapter
import com.ysxsoft.imtalk.bean.CommonBean
import com.ysxsoft.imtalk.bean.ReportListBean
import com.ysxsoft.imtalk.bean.UploadFileBean
import com.ysxsoft.imtalk.chatroom.net.retrofit.RetrofitUtil
import com.ysxsoft.imtalk.chatroom.task.AuthManager
import com.ysxsoft.imtalk.com.FullyGridLayoutManager
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.*
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.report_layout.*
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
 *on 2019/7/17 0017
 */
class ReportActivity : BaseActivity(), ReportAdapter.CheckInterface {

    companion object {
        fun starReportActivity(mContext: Context, room_id: String) {
            val intent = Intent(mContext, ReportActivity::class.java)
            intent.putExtra("room_id", room_id)
            mContext.startActivity(intent)
        }
    }


    override fun checkGroup(position: Int, isChecked: Boolean) {
        adapter!!.dataList.get(position).setChoosed(isChecked)
        adapter!!.notifyDataSetChanged()
    }

    override fun getLayout(): Int {
        return R.layout.report_layout
    }

    var adapter: ReportAdapter? = null
    var sectionDelete = StringBuffer()
    var sectionPic= StringBuffer()
    var room_id: String? = null
    var be_uid: String? = null
    var Ids = ArrayList<String>()
    var photoAdapter: GridImageAdapter? = null
     var selectList = ArrayList<LocalMedia>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        room_id = intent.getStringExtra("room_id")
        be_uid = intent.getStringExtra("be_uid")
        setTitle("举报")
        setBackVisibily()
        initView()
        requestData()
    }

    private fun requestData() {
        NetWork.getService(ImpService::class.java)
                .ReportList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Action1<ReportListBean> {
                    override fun call(t: ReportListBean?) {
                        if (t!!.code == 0) {
                            adapter = ReportAdapter(mContext)
                            recyclerView.layoutManager = LinearLayoutManager(mContext)
                            recyclerView.adapter = adapter
                            adapter!!.addAll(t.data)
                        }
                    }
                })
    }

    private fun initView() {

        val manager = FullyGridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false)
        photoRV.layoutManager=manager
        photoRV.isNestedScrollingEnabled = false
        photoAdapter = GridImageAdapter(mContext, onAddPicClickListener)
        photoAdapter!!.setSelectMax(9)
        photoRV.adapter = photoAdapter

        tv_submit.setOnClickListener {
            if (sectionDelete.length != 0) {
                sectionDelete.setLength(0)
            }
            for (dataBean in adapter!!.dataList) {
                val choosed = dataBean.isChoosed
                if (choosed) {
                    sectionDelete.append(dataBean.id.toString()).append(",")
                }
            }
            if (sectionDelete.length == 0) {
                showToastMessage("举报内容不能为空")
                return@setOnClickListener
            }
            if (selectList.size<=0){
                showToastMessage("举报图片不能为空")
                return@setOnClickListener
            }
            for (str in Ids){
                sectionPic.append(str).append(",")
            }
            var info_id = sectionDelete.deleteCharAt(sectionDelete.length - 1).toString();
            var pic_id = sectionPic.deleteCharAt(sectionPic.length - 1).toString();
            if (!TextUtils.isEmpty(be_uid)){
                reportFriend(be_uid,info_id,pic_id)
            }else{
                saveData(info_id,pic_id)
            }
        }
    }

    private fun reportFriend(be_uid: String?, info_id: String, pic_id: String) {
        val map = HashMap<String, String>()
        map.put("be_uid",be_uid!!)
        map.put("uid",AuthManager.getInstance().currentUserId)
        map.put("info_id",info_id)
        map.put("pic_id","["+pic_id+"]")
        val body = RetrofitUtil.createJsonRequest(map)
        NetWork.getService(ImpService::class.java)
                .reportUser(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Observer<CommonBean>{
                    override fun onError(e: Throwable?) {
                        Log.d("tag举报好友",e!!.message.toString())
                    }

                    override fun onNext(t: CommonBean?) {
                        showToastMessage(t!!.msg)
                        if (t.code == 0) {
                            finish()
                        }
                    }

                    override fun onCompleted() {
                    }
                })

    }

    private fun saveData(info_id: String, pic_id: String) {
        NetWork.getService(ImpService::class.java)
                .reportRoom(room_id!!, SpUtils.getSp(mContext, "uid"), info_id, "["+pic_id+"]")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Observer<CommonBean>{
                    override fun onError(e: Throwable?) {
                        Log.d("tag举报",e!!.message.toString())
                    }

                    override fun onNext(t: CommonBean?) {
                        showToastMessage(t!!.msg)
                        if (t.code == 0) {
                            finish()
                        }
                    }

                    override fun onCompleted() {
                    }
                })
    }

    private val onAddPicClickListener = object : GridImageAdapter.onAddPicClickListener {
       override fun onAddPicClick() {
            PictureSelector.create(this@ReportActivity)
                    .openGallery(PictureMimeType.ofAll())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .maxSelectNum(9)// 最大图片选择数量
//                    .minSelectNum(1)// 最小选择数量
                    .imageSpanCount(3)// 每行显示个数
                    .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                    .compress(true)// 是否压缩
                    .synOrAsy(true)//同步true或异步false 压缩 默认同步
                    .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    .withAspectRatio(16, 9)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                    .selectionMedia(selectList)// 是否传入已选图片
                    .minimumCompressSize(100)// 小于100kb的图片不压缩
                    .forResult(PictureConfig.CHOOSE_REQUEST)//结果回调onActivityResult code
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    val datas = PictureSelector.obtainMultipleResult(data)
                    val media = datas.get(0)
                    selectList.add(media)
                    photoAdapter!!.setList(selectList)
                    photoAdapter!!.notifyDataSetChanged()

                    val file = File(media.path)
                    val requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
                    val body = MultipartBody.Part.createFormData("file", file.name, requestBody)
                    NetWork.getService(ImpService::class.java)
                            .UploadFile(body)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(object : Action1<UploadFileBean> {
                                override fun call(t: UploadFileBean?) {
                                    if (t!!.code == 0) {
                                        val imgId1 = t.data.id
                                        Ids.add(imgId1.toString())
                                    }
                                }
                            })
                    ClearCache()
                }
            }
        }
    }

    private fun ClearCache() {
        PictureFileUtils.deleteCacheDirFile(this);
        RxPermissions(this)
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


