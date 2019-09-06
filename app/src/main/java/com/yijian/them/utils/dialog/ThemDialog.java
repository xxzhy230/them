package com.yijian.them.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.yijian.them.R;

public class ThemDialog extends Dialog {
    private Context context;
    private DialogOnitem OnItemClickListener;
    private String title;
    private String content;

    // content 第一个为标题 如果数组第一个值为空 不显示title
    public ThemDialog(Context context, boolean cancelable, OnCancelListener cancelListener,
                      DialogOnitem onItemClickListener, String title, String content) {
        super(context, cancelable, cancelListener);
        this.context = context;
        this.OnItemClickListener = onItemClickListener;
        this.title = title;
        this.content = content;
        init();

    }

    public void init() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_clear_cache, null);
        setContentView(view);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText(title);
        TextView tvContent = view.findViewById(R.id.tvContent);
        tvContent.setText(content);
        TextView tvSubmit = view.findViewById(R.id.tvSubmit);
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (OnItemClickListener != null) {
                    OnItemClickListener.onItemClickListener(0);
                }
            }
        });

        TextView tvCancel = view.findViewById(R.id.tvCancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
