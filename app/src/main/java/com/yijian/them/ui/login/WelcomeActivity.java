package com.yijian.them.ui.login;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMManager;
import com.yijian.them.R;
import com.yijian.them.common.App;
import com.yijian.them.common.Config;
import com.yijian.them.utils.JumpUtils;
import com.yijian.them.utils.SignCheck;
import com.yqjr.utils.Utils;
import com.yqjr.utils.base.AppManager;
import com.yqjr.utils.base.BaseActivity;
import com.yqjr.utils.spUtils.SPUtils;
import com.yqjr.utils.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeActivity extends BaseActivity {

    @Override
    public void onClickEvent() {
    }

    @Override
    public View getView() {
        return View.inflate(this, R.layout.activity_welcome, null);
    }

    @Override
    protected void initView() {
        if (!isTaskRoot()) {
            finish();
            return;
        }
        if (isSign()) {
            App.mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    initActivity();
                }
            }, 2000);
        } else {
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
            String loginUser = TIMManager.getInstance().getLoginUser();

            if (TextUtils.isEmpty(loginUser)) {
                messageLogin();
            } else {
                JumpUtils.jumpMainActivity(this);
                finish();
            }

        } else {
            JumpUtils.jumpSplashActivity(this);
            finish();
        }
    }

    private void messageLogin() {
        TIMManager.getInstance().login(SPUtils.getInt(Config.USERID) + "", SPUtils.getString(Config.USERSIGN), new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                Log.d("IM登录 Error: ", s);
            }

            @Override
            public void onSuccess() {
                Log.d("IM登录 : ", "Success");
                JumpUtils.jumpMainActivity(WelcomeActivity.this);
                finish();
            }
        });
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
