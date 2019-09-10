package com.yijian.them.ui.team;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yijian.them.R;
import com.yijian.them.basic.BasicActivity;
import com.yijian.them.common.Config;
import com.yijian.them.utils.JumpUtils;
import com.yijian.them.utils.fragments.Fragments;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TeamActivity extends BasicActivity {
    @BindView(R.id.tvTitleBar)
    TextView tvTitleBar;
    @BindView(R.id.ivRightBar)
    ImageView ivRightBar;
    @BindView(R.id.tvRightBar)
    TextView tvRightBar;
    @BindView(R.id.flDynamic)
    FrameLayout flDynamic;
    @BindView(R.id.llTitleBar)
    LinearLayout llTitleBar;

    private String dynamicId;
    private int messageType;

    @Override
    public int initView() {
        return R.layout.activity_team;
    }

    @Override
    public void initData() {
        ButterKnife.bind(this);
        messageType = getIntent().getIntExtra(Config.TEAMTYPE, 0);
        String title = getIntent().getStringExtra(Config.TEAMTITLE);
        dynamicId = getIntent().getStringExtra(Config.TEAMID);
        tvTitleBar.setText(title);
        if (messageType == 2
                || messageType == 3
                || messageType == 6
                || messageType == 4
                || messageType == 7
                || messageType == 8
                || messageType == 9
                || messageType == 10) {
            llTitleBar.setVisibility(View.GONE);
        }
        Fragments.init().commitTeam(messageType, getSupportFragmentManager(), R.id.flDynamic, dynamicId);
    }

    @OnClick({R.id.tvTitleBar, R.id.ivRightBar, R.id.tvRightBar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvTitleBar:
                finish();
                break;
            case R.id.ivRightBar:
                break;
            case R.id.tvRightBar:
                if (messageType == 5) {
                    JumpUtils.jumpDynamicActivity(this, 6, "创建群聊", "");
                }
                break;
        }
    }
}
