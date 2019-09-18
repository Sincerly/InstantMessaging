package com.ysxsoft.imtalk.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.ysxsoft.imtalk.R;
import com.ysxsoft.imtalk.chatroom.utils.ToastUtils;
import com.ysxsoft.imtalk.utils.KeyBoardUtils;

public class InputDialog extends Dialog {
    private Context mContext;
    private OnDialogClickListener listener;
    private TextView inputNum;

    public OnDialogClickListener getListener() {
        return listener;
    }

    public void setListener(OnDialogClickListener listener) {
        this.listener = listener;
    }

    public InputDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        init();
    }

    private View init() {
        View view = View.inflate(mContext, R.layout.view_input_num, null);
        inputNum = view.findViewById(R.id.inputNum);
        TextView submitNum = view.findViewById(R.id.submitNum);
        submitNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(inputNum.getText().toString().trim())) {
                    ToastUtils.showToast("请输入数量！");
                    return;
                }
                if (listener != null) {
                    listener.input(inputNum.getText().toString().trim());
                }
                closeKeyBord();
                dismiss();
            }
        });
        return view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(true);
        setContentView(init());
    }

    public void closeKeyBord(){
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(inputNum.getWindowToken(), 0);
        }
    }

    public void showDialog() {
        if (!isShowing()) {
            InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(inputNum, 0);

            show();
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            getWindow().setAttributes(lp);
            getWindow().setGravity(Gravity.BOTTOM);
        }
    }

    public interface OnDialogClickListener {
        void input(String giftNum);
    }
}