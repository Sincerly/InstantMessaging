package com.ysxsoft.imtalk.utils

import com.google.gson.GsonBuilder
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

object NetWork {

    var BaseUrl = "http://chitchat.rhhhyy.com/admin.php/api/"

    /**
     * 获取Service对象
     */
    fun <T : Any> getService(tClass: Class<T>): T {
        val client = OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).build()

        val retrofit = Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
//                    .addConverterFactory(LenientGsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build()
        return retrofit.create(tClass)
    }

    fun <T : Any> getTokenService(tClass: Class<T>): T {
        val retrofit = Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(getOkHttpClient())
                .build()
        return retrofit.create(tClass)
    }

    fun getOkHttpClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    return@addInterceptor chain.proceed(chain.request().newBuilder().header("token", SpUtils.getSp(BaseApplication.mContext!!, "token")).build())
                }
                .build()
        return httpClient
    }

    /**
     * 获取Service对象
     */
    open fun <T : Any> getCheckService(tClass: Class<T>): T {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://jiuchen.vip/")
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
//                    .addConverterFactory(LenientGsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
        return retrofit.create(tClass)
    }


    /**
     * 图文 上传
     * @param map
     * @param imageFile
     * @return
     */
    fun builder(map: Map<String, String>?, imageFile: ArrayList<String>?): MultipartBody.Builder {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        if (map != null) {
            val keys = map.keys
            val iterator = keys.iterator()
            while (iterator.hasNext()) {
                val key = iterator.next()
                builder.addFormDataPart(key, map[key])
            }
        }
        if (imageFile != null && imageFile.size > 0) {
            for (i in imageFile.indices) {
                builder.addFormDataPart("exit_pics[i]", ".jpg", RequestBody.create(MediaType.parse("image/*"), File(imageFile[i])))
            }
        } else {
            throw IllegalArgumentException("The param is null") as Throwable
        }
        return builder
    }


    fun IdCardbuilder(map: Map<String, String>?, imageFile: ArrayList<String>?): MultipartBody.Builder {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        if (map != null) {
            val keys = map.keys
            val iterator = keys.iterator()
            while (iterator.hasNext()) {
                val key = iterator.next()
                builder.addFormDataPart(key, map[key])
            }
        }
        if (imageFile != null && imageFile.size > 0) {
            for (i in 0..imageFile.size) {
                when (i) {
                    0 -> builder.addFormDataPart("z_card", ".jpg", RequestBody.create(MediaType.parse("image/*"), File(imageFile[i])))
                    1 -> builder.addFormDataPart("f_card", ".jpg", RequestBody.create(MediaType.parse("image/*"), File(imageFile[i])))
                }
            }
        } else {
            throw IllegalArgumentException("The param is null")
        }
        return builder
    }
}

