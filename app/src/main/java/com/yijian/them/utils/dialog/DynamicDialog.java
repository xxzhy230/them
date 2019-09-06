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

public class DynamicDialog extends BaseDialog {
    private int type; // 1 删除  2  举报
    private OnClicklistener onClicklistener;

    public void setOnClicklistener(OnClicklistener onClicklistener) {
        this.onClicklistener = onClicklistener;
    }

    public DynamicDialog(Context context, int type) {
        super(context);
        this.type = type;
        initView();
    }

    private void initView() {
        View view = View.inflate(getContext(), R.layout.dialog_dynamic, null);
        LinearLayout llJb = view.findViewById(R.id.llJb);
        LinearLayout llDynamic = view.findViewById(R.id.llDynamic);
        LinearLayout llBlack = view.findViewById(R.id.llBlack);
        LinearLayout llDel = view.findViewById(R.id.llDel);
        TextView tvCancel = view.findViewById(R.id.tvCancel);
        if (type == 1) {
            llJb.setVisibility(View.GONE);
            llDynamic.setVisibility(View.GONE);
            llBlack.setVisibility(View.GONE);
            llDel.setVisibility(View.VISIBLE);
        } else if (type ==2){
            llDel.setVisibility(View.GONE);
        }else {
            llJb.setVisibility(View.VISIBLE);
            llDynamic.setVisibility(View.GONE);
            llBlack.setVisibility(View.GONE);
            llDel.setVisibility(View.GONE);
        }
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        llJb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onClicklistener != null) {
                    onClicklistener.onClick(1);
                }
            }
        });
        llDynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onClicklistener != null) {
                    onClicklistener.onClick(2);
                }
            }
        });
        llBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onClicklistener != null) {
                    onClicklistener.onClick(3);
                }
            }
        });
        llDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onClicklistener != null) {
                    onClicklistener.onClick(4);
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
