package com.yijian.them.utils.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yijian.them.R;

public class SelectImageOrVideoDialog extends BaseDialog {
    private OnClicklistener onClicklistener;

    public void setOnClicklistener(OnClicklistener onClicklistener) {
        this.onClicklistener = onClicklistener;
    }

    public SelectImageOrVideoDialog(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        View view = View.inflate(getContext(), R.layout.dialog_select_image_or_video, null);
        LinearLayout llImage = view.findViewById(R.id.llImage);
        LinearLayout llVideo = view.findViewById(R.id.llVideo);
        TextView tvCancel = view.findViewById(R.id.tvCancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        llImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onClicklistener != null) {
                    onClicklistener.onClick(1);
                }
            }
        });
        llVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onClicklistener != null) {
                    onClicklistener.onClick(2);
                }
            }
        });
        setContentView(view);
    }

    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        window.setBackgroundDrawableResource(R.color.transparent);
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.BOTTOM);
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度
        lp.height = WindowManager.LayoutParams.MATCH_PARENT; // 高度
        lp.y = 0;
        window.setAttributes(lp);
        try {
            Context context = getContext();
            if (context != null && context instanceof Activity) {
                if (((Activity) context).isFinishing()) {
                    return;
                }
            }
            if (isShowing()) {
                return;
            }
            super.show();
        } catch (Throwable t) {

        }
    }

    public interface OnClicklistener {
        // 1 举报 2 屏蔽 3 拉黑 4 删除
        void onClick(int type);
    }
}
