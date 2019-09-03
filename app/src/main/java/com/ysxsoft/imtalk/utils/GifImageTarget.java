package com.ysxsoft.imtalk.utils;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;

public class GifImageTarget extends ImageViewTarget<GifDrawable> {
    public OnGifTargetListener listener;

    public GifImageTarget(ImageView view) {
        super(view);
    }

    public void setListener(OnGifTargetListener listener) {
        this.listener = listener;
    }

    @Override
    public void onLoadFailed(@Nullable Drawable errorDrawable) {
        Log.e("tag","onLoadFailed");
    }

    @Override
    public void onResourceReady(@NonNull GifDrawable resource, @Nullable Transition<? super GifDrawable> transition) {
        resource.setLoopCount(1);
    }

    @Override
    public void setRequest(@Nullable Request request) {
        Log.e("tag","setRequest");

    }

    @Nullable
    @Override
    public Request getRequest() {
        Log.e("tag","Request");
        return null;
    }

    @Override
    public void onStart() {
        Log.e("tag","onStart");

    }

    @Override
    public void onStop() {
        Log.e("tag","onStop");
        if(listener!=null){
            listener.onStop();
        }
    }

    @Override
    protected void setResource(@Nullable GifDrawable resource) {
        if(listener!=null){
            listener.onResourceReady(resource);
        }
    }

    public interface OnGifTargetListener{
        void onResourceReady(GifDrawable resource);
        void onStop();
    }
}
