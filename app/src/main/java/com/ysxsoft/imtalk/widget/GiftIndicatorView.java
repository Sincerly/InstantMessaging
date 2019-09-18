package com.ysxsoft.imtalk.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ysxsoft.imtalk.R;
import com.ysxsoft.imtalk.chatroom.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

public class GiftIndicatorView extends LinearLayout {
    private List<View> views = null;
    private Context context;

    public GiftIndicatorView(Context context) {
        this(context, null);
    }

    public GiftIndicatorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GiftIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
    }

    public void setMax(int pageTotal) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (views == null) {
                    views = new ArrayList<>();
                }
                views.clear();
                removeAllViews();
                for (int i = 0; i < pageTotal; i++) {
                    View v = View.inflate(context, R.layout.view_gift_indicator, null);
                    TextView t = v.findViewById(R.id.t);
                    t.setSelected(i==0);
                    v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    addView(v);
                    views.add(v);
                }
            }
        });

    }

    public void setCurrent(int pageIndex) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (views == null) {
                    views = new ArrayList<>();
                }
                if (views.size() > pageIndex) {
                    for (int i = 0; i < views.size(); i++) {
                        View v = views.get(i);
                        TextView t = v.findViewById(R.id.t);
                        t.setSelected(i == pageIndex);
                    }
                }
            }
        });
    }
}
