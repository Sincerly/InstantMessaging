package com.ysxsoft.imtalk.view;

import android.animation.ValueAnimator;

public class Test {
    public void test(){
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1.0f);
        animator.setDuration(3000);
        animator.start();
    }
}
