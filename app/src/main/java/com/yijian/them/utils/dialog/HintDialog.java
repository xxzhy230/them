package com.yijian.them.utils.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.yijian.them.R;

public class HintDialog extends BaseDialog {
    public HintDialog(Context context) {
        super(context);
        init(context);
    }

    private void init(Context mContext) {
        setCanceledOnTouchOutside(false);
        View view = View.inflate(mContext, R.layout.dialog_hint,null);
        TextView tvCommit = view.findViewById(R.id.tvCommit);
        TextView tvMessage = view.findViewById(R.id.tvMessage);
        tvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setContentView(view);
    }
}
