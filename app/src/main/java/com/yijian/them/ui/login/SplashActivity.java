package com.yijian.them.ui.login;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.yijian.them.R;
import com.yijian.them.utils.JumpUtils;
import com.yijian.them.utils.SignCheck;
import com.yqjr.utils.base.AppManager;
import com.yqjr.utils.base.BaseActivity;
import com.yqjr.utils.spUtils.SPUtils;
import com.yqjr.utils.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends BaseActivity {
    @BindView(R.id.btLogin)
    Button btLogin;
    @BindView(R.id.btRegist)
    Button btRegist;

    @Override
    public void onClickEvent() {
        ButterKnife.bind(this);
    }

    @Override
    public View getView() {
        return View.inflate(this, R.layout.activity_splash, null);
    }

    @Override
    protected void initView() {
        if (!isTaskRoot()) {
            finish();
            return;
        }
        if (isSign()) {
            initActivity();
        }else{
            finish();
        }
    }

    private boolean isSign() {
        SignCheck signCheck = new SignCheck(this, "67:FE:E5:D1:78:06:27:5C:10:51:2C:55:FF:60:1A:74:8E:7A:61:92");
        boolean check = signCheck.check();
        if (!check) {
            ToastUtils.toastCenter(this, "apk包来源不正确,请重新下载!");
            return check;
        } else {
            return check;
        }

    }

    @Override
    protected void changeNet() {

    }

    private void initActivity() {
        // 判断是否是第一次开启应用
        String token = SPUtils.getToken();
        // 如果是第一次启动，则先进入登录
        if (!TextUtils.isEmpty(token)) {
            JumpUtils.jumpMainActivity(this);
            finish();
        }
    }

    @OnClick({R.id.btLogin, R.id.btRegist})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btLogin:
                JumpUtils.jumpLoginActivity(this, 0, "", "");
                break;
            case R.id.btRegist:
                JumpUtils.jumpLoginActivity(this, 1, "", "");
                break;
        }
    }

    private long waitTime = 2000;
    private long touchTime = 0;
    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();// 毫秒数
        if ((currentTime - touchTime) >= waitTime) {
            ToastUtils.toastCenter(this, "再按一次退出程序");
            touchTime = currentTime;
        } else {
            AppManager.getAppManager().finishAllActivity();
            finish();
            System.exit(0);
        }
    }
}
