package com.yijian.them.ui.login;

import android.support.v4.app.FragmentManager;
import android.view.View;

import com.yijian.them.R;
import com.yijian.them.basic.BasicActivity;
import com.yijian.them.common.Config;
import com.yijian.them.utils.fragments.Fragments;

import butterknife.OnClick;

public class LoginActivity extends BasicActivity {

    private int loginType;//0 登录  1 注册 2 修改信息
    private FragmentManager fragmentManager;


    @Override
    public int initView() {
        return R.layout.activity_login;
    }

    @Override
    public void initData() {
        loginType = getIntent().getIntExtra(Config.LOGINTYPE, 0);
        fragmentManager = getSupportFragmentManager();
        Fragments.init().commitLogin(loginType, fragmentManager, R.id.flLogin);
    }


    @OnClick({R.id.tvTitleBar})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.tvTitleBar:
                finish();
                break;
        }
    }
}
