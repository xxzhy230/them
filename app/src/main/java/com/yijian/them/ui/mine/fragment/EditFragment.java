package com.yijian.them.ui.mine.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.yijian.them.R;
import com.yijian.them.basic.BasicActivity;
import com.yijian.them.basic.BasicFragment;
import com.yqjr.utils.Utils;
import com.yqjr.utils.utils.ForbidEditUtils;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditFragment extends BasicFragment {
    @BindView(R.id.etNickOrRemark)
    EditText etNickOrRemark;
    @BindView(R.id.tvLength)
    TextView tvLength;
    private int type = 1;  //1 昵称  2 签名

    public void setType(int type) {
        this.type = type;
    }

    @Override
    protected View getResourceView() {
        View rootView = View.inflate(getActivity(), R.layout.fragment_edit, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onClickEvent() {
        showInput();
        etNickOrRemark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    if (type == 1) {
                        tvLength.setText("16");
                    } else {
                        tvLength.setText("30");
                    }
                } else {
                    if (type == 1) {
                        tvLength.setText(16 - s.toString().length() + "");
                    } else {
                        tvLength.setText(30 - s.toString().length() + "");
                    }
                }
            }
        });
    }

    @Override
    protected void initView(Bundle bundle) {
        context = (BasicActivity) getActivity();
        if (type == 1) {
            etNickOrRemark.setHint("给自己起个特别的昵称吧");
            etNickOrRemark.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
            tvLength.setText("16");
        } else {
            etNickOrRemark.setHint("介绍一下你自己哟");
            etNickOrRemark.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
            tvLength.setText("30");
        }
    }

    public String getInputText() {
        ForbidEditUtils.hideInput(getActivity(), etNickOrRemark);
        return etNickOrRemark.getText().toString().trim();
    }

    private void showInput() {
        Timer timer = new Timer();//设置定时器
        timer.schedule(new TimerTask() {
            @Override
            public void run() {//弹出软键盘的代码
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etNickOrRemark, InputMethodManager.RESULT_SHOWN);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }, 300);//设置300毫秒的时长

    }
}
