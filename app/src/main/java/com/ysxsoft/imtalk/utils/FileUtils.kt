package com.ysxsoft.imtalk.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.*

object FileUtils {

    /**
     * 绝对路径
     *
     * @return
     */
    fun getSDCardPath(context: Context): String {
        val SDPATH = Environment.getExternalStorageDirectory().absolutePath + "/" + AppUtil.getCurrentPageName(context) + "/saveImg"
        val file = File(SDPATH)
        return if (file.mkdirs()) {
            SDPATH
        } else SDPATH
    }

    // 生成文件
    @Deprecated("")
    fun makeFilePath(filePath: String, fileName: String): File? {
        var file: File? = null
        makeRootDirectory(filePath)
        try {
            file = File(filePath + fileName)
            if (!file.exists()) {
                file.createNewFile()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return file
    }

    // 生成文件夹
    private fun makeRootDirectory(filePath: String) {
        var file: File? = null
        try {
            file = File(filePath)
            if (!file.exists()) {
                file.mkdir()
            }
        } catch (e: Exception) {
            Log.i("error:", e.toString() + "")
        }

    }

    /**
     * 保存图片
     * @param bm bitmap
     * @param fileName  文件名
     * @throws IOException
     */
    @Deprecated("")
    @Throws(IOException::class)
    fun saveImg(context: Context, bm: Bitmap, fileName: String) {
        val file = File(getSDCardPath(context))
        if (!file.exists()) {
            file.mkdirs()
        }
        val imgFile = File(getSDCardPath(context), fileName)
        if (!imgFile.exists()) {
            imgFile.createNewFile()
        }
        val bos = BufferedOutputStream(FileOutputStream(imgFile))
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos)
        bos.flush()
        bos.close()
    }

    /**
     * 保存文件，文件名为当前日期
     * @param context
     * @param bitmap bitmap
     * @param bitName 图片名称
     */
    fun saveBitmap(context: Context, bitmap: Bitmap, bitName: String) {
        val fileName: String
        val file: File
        if (Build.BRAND == "Xiaomi") { // 小米手机
            fileName = Environment.getExternalStorageDirectory().path + "/DCIM/Camera/" + bitName
        } else {  // Meizu 、Oppo
            fileName = Environment.getExternalStorageDirectory().path + "/DCIM/" + bitName
        }
        file = File(fileName)

        if (file.exists()) {
            file.delete()
        }
        val out: FileOutputStream
        try {
            out = FileOutputStream(file)
            // 格式为 JPEG，照相机拍出的图片为JPEG格式的，PNG格式的不能显示在相册中
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)) {
                out.flush()
                out.close()
                // 插入图库
                MediaStore.Images.Media.insertImage(context.contentResolver, file.absolutePath, bitName, null)
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()

        }

        // 发送广播，通知刷新图库的显示
        context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://$fileName")))

    }
}