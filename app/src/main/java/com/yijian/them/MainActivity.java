package com.yijian.them;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.squareup.picasso.Picasso;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMManager;
import com.yijian.them.api.AuthApi;
import com.yijian.them.basic.BasicActivity;
import com.yijian.them.common.Config;
import com.yijian.them.ui.login.DataMoudle;
import com.yijian.them.utils.fragments.Fragments;
import com.yijian.them.utils.http.CallBack;
import com.yijian.them.utils.http.Http;
import com.yijian.them.utils.http.JsonResult;
import com.yqjr.utils.base.AppManager;
import com.yqjr.utils.spUtils.SPUtils;
import com.yqjr.utils.utils.StatusBarUtil;
import com.yqjr.utils.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BasicActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.flMain)
    FrameLayout flMain;
    @BindView(R.id.rbHome)
    RadioButton rbHome;
    @BindView(R.id.rbTeam)
    RadioButton rbTeam;
    @BindView(R.id.rbMessage)
    RadioButton rbMessage;
    @BindView(R.id.rbMine)
    RadioButton rbMine;
    @BindView(R.id.rgMain)
    RadioGroup rgMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initView() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        rgMain.setOnCheckedChangeListener(this);
        initFragment();
        getUserInfo();
    }

    private void initFragment() {
        Fragments.init().commitMain(0, getSupportFragmentManager(), R.id.flMain);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rbHome:
                Fragments.init().commitMain(0, getSupportFragmentManager(), R.id.flMain);
                break;
            case R.id.rbTeam:
                Fragments.init().commitMain(1, getSupportFragmentManager(), R.id.flMain);
                break;
            case R.id.rbMessage:
                Fragments.init().commitMain(2, getSupportFragmentManager(), R.id.flMain);
                break;
            case R.id.rbMine:
                Fragments.init().commitMain(3, getSupportFragmentManager(), R.id.flMain);
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

    private void getUserInfo() {
        Http.http.createApi(AuthApi.class).getUser()
                .compose(this.<JsonResult<DataMoudle.DataBean>>bindToLifecycle())
                .compose(this.<JsonResult<DataMoudle.DataBean>>applySchedulers())
                .subscribe(this.newSubscriber(new CallBack<DataMoudle.DataBean>() {
                    @Override
                    public void success(DataMoudle.DataBean response,int code) {
                        Log.d("获取用户信息: ", response + "");
                        String birthday = response.getBirthday();
                        String gender = response.getGender();
                        String nickName = response.getNickName();
                        int userId = response.getUserId();
                        String regn = response.getRegn();
                        String realImg = response.getRealImg();
                        String sign = response.getSign();

                        SPUtils.putString(Config.BIRTHDAY, birthday);
                        SPUtils.putString(Config.GENDER, gender);
                        SPUtils.putString(Config.NICKNAME, nickName);
                        SPUtils.putInt(Config.USERID, userId);
                        SPUtils.putString(Config.REGN, regn);
                        SPUtils.putString(Config.REALIMG, realImg);
                        SPUtils.putString(Config.SIGN, sign);
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        ToastUtils.toastCenter(MainActivity.this, errorMessage + "");
                    }
                }));
    }



}
