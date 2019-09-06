package com.yijian.them.utils.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import com.yijian.them.R;


/**
 * Created by gry on 2018/4/9.
 */
public abstract class BaseDialog extends Dialog {
    protected Context mContext;

    public BaseDialog(Context context) {
        super(context, R.style.NewDialog);
        this.mContext = context;

    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;

    }


//    @Override
//    public void show() {
//        Window window = getWindow();
//        window.setBackgroundDrawableResource(R.color.color_f2);
//        WindowManager.LayoutParams lp = window.getAttributes();
//        window.setGravity(Gravity.CENTER);
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度
//        lp.height = WindowManager.LayoutParams.MATCH_PARENT; // 高度
//        lp.y=0;
//        window.setAttributes(lp);
//        try {
//            Context context = getContext();
//            if (context != null && context instanceof Activity) {
//                if (((Activity) context).isFinishing()) {
//                    return;
//                }
//            }
//            if (isShowing()) {
//                return;
//            }
//            super.show();
//        } catch (Throwable t) {
//
//        }
//    }

    @Override
    public void dismiss() {
        try {
            Context context = getContext();
            if (context != null && context instanceof Activity) {
                if (((Activity) context).isFinishing()) {
                    return;
                }
            }
            if (isShowing()) {
                super.dismiss();
            }
        } catch (Throwable t) {

        }
    }


}
