package com.yijian.them.utils.dialog;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yijian.them.R;

public class HintDialog extends BaseDialog {

    private TextView tvMessage;
    private OnCommitListener onCommitListener;
    private LinearLayout llCancel;

    public void setOnCommitListener(OnCommitListener onCommitListener) {
        this.onCommitListener = onCommitListener;
    }

    public HintDialog(Context context) {
        super(context);
        init(context);
    }

    private void init(Context mContext) {
        setCanceledOnTouchOutside(false);
        View view = View.inflate(mContext, R.layout.dialog_hint, null);
        TextView tvCommit = view.findViewById(R.id.tvCommit);
        tvMessage = view.findViewById(R.id.tvMessage);
        llCancel = view.findViewById(R.id.llCancel);
        TextView tvCancel = view.findViewById(R.id.tvCancel);

        tvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onCommitListener != null) {
                    onCommitListener.onCommit();
                }
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setContentView(view);
    }

    public void setMessage(String message, boolean isShowCancel) {
        tvMessage.setText(message);
        if (isShowCancel) {
            llCancel.setVisibility(View.VISIBLE);
        } else {
            llCancel.setVisibility(View.GONE);
        }

    }

    public interface OnCommitListener {
        void onCommit();
    }
}
