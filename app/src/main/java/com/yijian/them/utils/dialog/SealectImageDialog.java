package com.yijian.them.utils.dialog;


import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yijian.them.R;

/**
 * 拍照Dialog
 */
public class SealectImageDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private DialogOnitem OnItemClickListener;
    private Dialog customDialog;

    // content 第一个为标题 如果数组第一个值为空 不显示title
    public SealectImageDialog(Context context, boolean cancelable, OnCancelListener cancelListener, String[] content,
                              DialogOnitem onItemClickListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
        this.OnItemClickListener = onItemClickListener;
        init();

    }

    /**
     * Create the custom dialog
     */

    public void init() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.bottom_dialog, null);
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        customDialog = new Dialog(context, R.style.Dialog);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        customDialog.setContentView(layout, layoutParams);
        customDialog.setCanceledOnTouchOutside(true);
        Window window = customDialog.getWindow();
//        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.alpha = 0.85f;
//        window.setAttributes(lp);

        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.myDialog); // 添加动画

        TextView tvAblum = (TextView) window.findViewById(R.id.tvAblum);
        TextView tvCamera = (TextView) window.findViewById(R.id.tvCamera);
        tvAblum.setOnClickListener(this);
        tvCamera.setOnClickListener(this);
        customDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvCamera:
                if (OnItemClickListener != null)
                    OnItemClickListener.onItemClickListener(0);
                customDialog.dismiss();
                break;
            case R.id.tvAblum:
                if (OnItemClickListener != null)
                    OnItemClickListener.onItemClickListener(1);
                customDialog.dismiss();
                break;
        }
    }
}
