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

public class ConversDialog extends BaseDialog {
    private OnClicklistener onClicklistener;
    private TextView tvUnRead;
    private TextView tvTop;

    public void setOnClicklistener(OnClicklistener onClicklistener) {
        this.onClicklistener = onClicklistener;
    }

    public ConversDialog(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        View view = View.inflate(getContext(), R.layout.dialog_convers, null);
        LinearLayout llTop = view.findViewById(R.id.llTop);
        tvTop = view.findViewById(R.id.tvTop);
        LinearLayout llUnRead = view.findViewById(R.id.llUnRead);
        tvUnRead = view.findViewById(R.id.tvUnRead);
        LinearLayout llDel = view.findViewById(R.id.llDel);
        LinearLayout llCancel = view.findViewById(R.id.llCancel);
        llCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        llTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onClicklistener != null) {
                    String top = tvTop.getText().toString();
                    if ("置顶".equals(top)) {
                        onClicklistener.onClick(1);
                    } else {
                        onClicklistener.onClick(2);
                    }
                }
            }
        });
        llUnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onClicklistener != null) {
                    String unRead = tvUnRead.getText().toString();
                    if ("标记已读".equals(unRead)) {
                        onClicklistener.onClick(3);
                    } else {
                        onClicklistener.onClick(4);
                    }
                }
            }
        });
        llDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onClicklistener != null) {
                    onClicklistener.onClick(5);
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

    public void isRead(boolean isRead) {
        if (isRead) {
            tvUnRead.setText("标记未读");
        } else {
            tvUnRead.setText("标记已读");
        }
    }

    public void isTop(boolean isTop) {
        if (isTop) {
            tvTop.setText("取消置顶");
        } else {
            tvTop.setText("置顶");
        }
    }

    public interface OnClicklistener {
        // 1 举报 2 屏蔽 3 拉黑 4 删除
        void onClick(int type);
    }
}
