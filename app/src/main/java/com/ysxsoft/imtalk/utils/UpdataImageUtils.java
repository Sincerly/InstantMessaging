package com.ysxsoft.imtalk.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Create By 胡
 * on 2019/6/25 0025
 */
public class UpdataImageUtils {

    public static MultipartBody.Builder builder(Map<String, String> map, ArrayList<String> imageFile) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (map != null) {
            Set<String> keys = map.keySet();
            Iterator<String> iterator = keys.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                builder.addFormDataPart(key, map.get(key));
            }
        }
        if (imageFile != null && imageFile.size() > 0) {
            for (int i = 0; i < imageFile.size(); i++) {
                builder.addFormDataPart("pics[" + i + "]", i + ".jpg", RequestBody.create(MediaType.parse("image/*"), new File(imageFile.get(i))));
            }
        } else {
            throw new IllegalArgumentException("The param is null");
        }
        return builder;
    }

    public static MultipartBody.Builder IdCardbuilder(Map<String, String> map, ArrayList<String> imageFile) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (map != null) {
            Set<String> keys = map.keySet();
            Iterator<String> iterator = keys.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                builder.addFormDataPart(key, map.get(key));
            }
        }
        if (imageFile != null && imageFile.size() > 0) {
            for (int i = 0; i < imageFile.size(); i++) {
                switch (i) {
                    case 0:
                        builder.addFormDataPart("z_card", i + ".jpg", RequestBody.create(MediaType.parse("image/*"), new File(imageFile.get(i))));
                        break;
                    case 1:
                        builder.addFormDataPart("f_card", i + ".jpg", RequestBody.create(MediaType.parse("image/*"), new File(imageFile.get(i))));
                        break;
                }

            }
        } else {
            throw new IllegalArgumentException("The param is null");
        }
        return builder;
    }


}
