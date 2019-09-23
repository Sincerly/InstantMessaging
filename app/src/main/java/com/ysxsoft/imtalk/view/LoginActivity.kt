package com.ysxsoft.imtalk.view

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.umeng.socialize.UMAuthListener
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.utils.SocializeUtils
import com.ysxsoft.imtalk.MainActivity
import com.ysxsoft.imtalk.R
import com.ysxsoft.imtalk.R.style.dialog
import com.ysxsoft.imtalk.bean.LoginBean
import com.ysxsoft.imtalk.bean.ThirdLoginBean
import com.ysxsoft.imtalk.chatroom.net.retrofit.RetrofitUtil
import com.ysxsoft.imtalk.chatroom.utils.log.SLog
import com.ysxsoft.imtalk.impservice.ImpService
import com.ysxsoft.imtalk.utils.AppUtil
import com.ysxsoft.imtalk.utils.BaseActivity
import com.ysxsoft.imtalk.utils.NetWork
import com.ysxsoft.imtalk.utils.SpUtils
import io.rong.imkit.RongIM
import io.rong.imlib.RongIMClient
import kotlinx.android.synthetic.main.login_layout.*
import org.json.JSONException
import org.json.JSONObject
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *Create By 胡
 *on 2019/7/3 0003
 */
class LoginActivity : BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.login_layout
    }

    private var dialog: ProgressDialog? = null
    var type = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog = ProgressDialog(this)
        setLightStatusBar(false)
        initView()
    }

    private fun initView() {
//        img_logo.setImageBitmap(AppUtil.getLogoBitmap(mContext))
        img_logo.setImageDrawable(AppUtil.getLogoBitmap(mContext))
        //忘记密码
        tv_forget_password.setOnClickListener {
            startActivity(ForgetPswActivity::class.java)
        }
        //注册账号
        tv_register.setOnClickListener {
            startActivity(RegisterActivity::class.java)
        }
        img_wechat.setOnClickListener {
            UMQQLoglin(SHARE_MEDIA.WEIXIN)
            type = 2
        }
        img_qq.setOnClickListener {
            UMQQLoglin(SHARE_MEDIA.QQ)
            type = 3
        }

        tv_login.setOnClickListener {
            if (TextUtils.isEmpty(ed_phone.text.toString().trim())) {
                showToastMessage("用户名不能为空")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(ed_pwd.text.toString().trim())) {
                showToastMessage("密码不能为空")
                return@setOnClickListener
            }

             if (ed_pwd.text.toString().trim().length<6) {
                showToastMessage("密码不能少于六位")
                return@setOnClickListener
            }

            saveData()
        }
    }

    private fun UMQQLoglin(share_media: SHARE_MEDIA?) {
        UMShareAPI.get(mContext).getPlatformInfo(this@LoginActivity, share_media, authListener)
    }

    var authListener = object : UMAuthListener {
        override fun onStart(platform: SHARE_MEDIA) {
            SocializeUtils.safeShowDialog(dialog)
        }

        override fun onComplete(platform: SHARE_MEDIA, action: Int, data: Map<String, String>) {
            SocializeUtils.safeCloseDialog(dialog)
            getInfo(platform)
        }

        override fun onError(platform: SHARE_MEDIA, action: Int, t: Throwable) {
            SocializeUtils.safeCloseDialog(dialog)
            showToastMessage("失败：" + t.message)
        }

        override fun onCancel(platform: SHARE_MEDIA, action: Int) {
            SocializeUtils.safeCloseDialog(dialog)
            showToastMessage("取消了===" + action)
        }

    }

    private fun getInfo(shareMedia: SHARE_MEDIA) {
        UMShareAPI.get(this@LoginActivity).getPlatformInfo(this@LoginActivity, shareMedia, object : UMAuthListener {
            override fun onStart(share_media: SHARE_MEDIA) {
                SocializeUtils.safeShowDialog(dialog)
            }

            override fun onComplete(share_media: SHARE_MEDIA, i: Int, map: Map<String, String>) {
                SocializeUtils.safeCloseDialog(dialog)
                val sb = StringBuilder()
                for (key in map.keys) {
                    sb.append(key).append(" : ").append(map[key]).append("\n")
                }
                val jsonObject = JSONObject(map)
                Log.d("tag===",jsonObject.toString())
                try {
                    val openid = jsonObject.getString("uid")
                    val nickname = jsonObject.getString("name")
                    val sex = jsonObject.getString("gender")
                    val avatar = jsonObject.getString("iconurl")
                    val map1 = HashMap<String, String>()

                    map1.put("type", type.toString())
                    map1.put("openid", openid)
                    map1.put("nickname", nickname)
                    map1.put("icon", avatar)
                    if ("0".equals(sex)) {
                        map1.put("sex", "1")
                    } else {
                        map1.put("sex", "2")
                    }
                    val body = RetrofitUtil.createJsonRequest(map1)
                    NetWork.getService(ImpService::class.java)
                            .ThirdLogin(body)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(object : Observer<ThirdLoginBean> {
                                override fun onError(e: Throwable?) {
                                    showToastMessage(e!!.message.toString())
                                }

                                override fun onNext(t: ThirdLoginBean?) {
                                    if (t!!.code == 0) {
                                        RongIM.connect(t.data.userInfo.chat_token, object : RongIMClient.ConnectCallback() {
                                            override fun onTokenIncorrect() {
                                                SLog.e(TAG, "RongIMClient onTokenIncorrect")
                                            }

                                            override fun onSuccess(s: String) {
                                                SLog.d(TAG, "RongIMClient connect success")

                                                SpUtils.saveSp(mContext, "token", t.data.userInfo.token)
                                                if (!TextUtils.isEmpty(t.data.userInfo.chat_token) && t.data.userInfo.chat_token != null) {
                                                    SpUtils.saveSp(mContext, "chat_token", t.data.userInfo.chat_token)
                                                }
                                                SpUtils.saveSp(mContext, "uid", t.data.userInfo.uid.toString())
                                                com.ysxsoft.imtalk.chatroom.utils.SpUtils.saveSp(mContext, "uid", t.data.userInfo.uid.toString())
                                                startActivity(MainActivity::class.java)
//                                                startActivity(ImprovingDataActivity::class.java)
                                                finish()
                                            }

                                            override fun onError(errorCode: RongIMClient.ErrorCode) {
                                                SLog.e(TAG, "RongIMClient connect onError:" + errorCode.value + "-" + errorCode.message)
                                            }
                                        })
                                    }
                                }

                                override fun onCompleted() {
                                }
                            })


                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onError(share_media: SHARE_MEDIA, i: Int, throwable: Throwable) {
                //                result.setText("错误" + throwable.getMessage());
                showToastMessage("失败")
                SocializeUtils.safeCloseDialog(dialog)
            }

            override fun onCancel(share_media: SHARE_MEDIA, i: Int) {
                SocializeUtils.safeCloseDialog(dialog)
                showToastMessage("用户已取消")
            }
        })
    }

    private fun saveData() {
        NetWork.getService(ImpService::class.java)
                .Login(ed_phone.text.toString().trim(), ed_pwd.text.toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<LoginBean> {
                    override fun onError(e: Throwable?) {
                        Log.d("tag======",e!!.message.toString())
                    }

                    override fun onNext(t: LoginBean?) {
                        showToastMessage(t!!.msg)
                        if (t!!.code == 0) {
                            RongIM.connect(t.data.chat_token, object : RongIMClient.ConnectCallback() {
                                override fun onTokenIncorrect() {
                                    SLog.e(TAG, "RongIMClient onTokenIncorrect")
                                }

                                override fun onSuccess(s: String) {
                                    SpUtils.saveSp(mContext, "token", t.data.token)
                                    if (!TextUtils.isEmpty(t.data.chat_token) && t.data.chat_token != null) {
                                        SpUtils.saveSp(mContext, "chat_token", t.data.chat_token)
                                    }
                                    SpUtils.saveSp(mContext, "uid", t.data.uid.toString())
                                    com.ysxsoft.imtalk.chatroom.utils.SpUtils.saveSp(mContext, "uid", t.data.uid.toString())
                                    startActivity(MainActivity::class.java)
                                    finish()
                                    SLog.d(TAG, "RongIMClient connect success")
                                }

                                override fun onError(errorCode: RongIMClient.ErrorCode) {
                                    SLog.e(TAG, "RongIMClient connect onError:" + errorCode.value + "-" + errorCode.message)
                                }
                            })
                        }
                    }

                    override fun onCompleted() {
                    }
                })


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        UMShareAPI.get(this).release()

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        UMShareAPI.get(this).onSaveInstanceState(outState)
    }
}