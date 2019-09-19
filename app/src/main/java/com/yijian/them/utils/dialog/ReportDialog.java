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
import com.yqjr.utils.utils.StringUtils;

public class ReportDialog extends BaseDialog {
    private int type; // 1 删除  2  举报
    private OnClicklistener onClicklistener;

    public void setOnClicklistener(OnClicklistener onClicklistener) {
        this.onClicklistener = onClicklistener;
    }

    public ReportDialog(Context context, int type) {
        super(context);
        this.type = type;
        initView();
    }

    private void initView() {
        setCanceledOnTouchOutside(true);
        View view = View.inflate(getContext(), R.layout.dialog_report, null);
        LinearLayout llReport = view.findViewById(R.id.llReport);
        LinearLayout llEditInfo = view.findViewById(R.id.llEditInfo);
        if (type == 2) {
            llEditInfo.setVisibility(View.VISIBLE);
            llReport.setVisibility(View.GONE);
        }
        llReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        llReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onClicklistener != null) {
                    onClicklistener.onClick(1);
                }
            }
        });
        llEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onClicklistener != null) {
                    onClicklistener.onClick(2);
                }
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
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
        // 1 举报
        void onClick(int type);
    }
}
