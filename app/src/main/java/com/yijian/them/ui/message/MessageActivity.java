package com.yijian.them.ui.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.yijian.them.R;
import com.yijian.them.basic.BasicActivity;
import com.yijian.them.common.Config;
import com.yijian.them.utils.StatusBarUtils;
import com.yijian.them.utils.fragments.Fragments;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageActivity extends BasicActivity {
    @BindView(R.id.v_include_bar)
    View vIncludeBar;
    @BindView(R.id.rlTitleBar)
    RelativeLayout rlTitleBar;
    @BindView(R.id.tvTitleBar)
    TextView tvTitleBar;
    @BindView(R.id.ivRightBar)
    ImageView ivRightBar;
    @BindView(R.id.tvRightBar)
    TextView tvRightBar;
    @BindView(R.id.flMessage)
    FrameLayout flMessage;

    private int messageType;


    @Override
    public int initView() {
        StatusBarUtils.setStatusBarColor(MessageActivity.this, R.color.color_FFFEFEFE);
        return R.layout.activity_message;
    }

    @Override
    public void initData() {
        ButterKnife.bind(this);
        messageType = getIntent().getIntExtra(Config.MESSAGETYPE, 0);
        String title = getIntent().getStringExtra(Config.MINETITLE);
        ChatInfo chatInfo = (ChatInfo) getIntent().getSerializableExtra(Config.CHATINFO);
        tvTitleBar.setText(title);
        vIncludeBar.setVisibility(View.GONE);
        rlTitleBar.setVisibility(View.GONE);
        Fragments.init().commitMessage(messageType, getSupportFragmentManager(), R.id.flMessage, chatInfo);

    }


    @OnClick(R.id.tvTitleBar)
    public void onViewClicked() {
        finish();
    }
}
