package com.yijian.them.ui.mine.activity;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yijian.them.R;
import com.yijian.them.api.AuthApi;
import com.yijian.them.basic.BasicActivity;
import com.yijian.them.common.Config;
import com.yijian.them.ui.login.DataMoudle;
import com.yijian.them.ui.mine.fragment.EditFragment;
import com.yijian.them.ui.mine.fragment.FeedBackFragment;
import com.yijian.them.utils.fragments.Fragments;
import com.yijian.them.utils.http.CallBack;
import com.yijian.them.utils.http.Http;
import com.yijian.them.utils.http.JsonResult;
import com.yqjr.utils.spUtils.SPUtils;
import com.yqjr.utils.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;

public class FollowerActivity extends BasicActivity {

    @BindView(R.id.tvTitleBar)
    TextView tvTitleBar;
    @BindView(R.id.ivRightBar)
    ImageView ivRightBar;
    @BindView(R.id.tvRightBar)
    TextView tvRightBar;
    @BindView(R.id.flFollower)
    FrameLayout flFollower;
    private int mineType;
    public int userId;


    @Override
    public int initView() {
        return R.layout.activity_follower;
    }

    @Override
    public void initData() {
        initFragment();
    }

    private void initFragment() {
        mineType = getIntent().getIntExtra(Config.MINETYPE, 0);
        String title = getIntent().getStringExtra(Config.MINETITLE);
        tvTitleBar.setText(title);
        userId = getIntent().getIntExtra(Config.USERID, 0);
        Fragments.init().commitFollower(mineType, getSupportFragmentManager(), R.id.flFollower,userId);
    }


    @OnClick({R.id.tvTitleBar, R.id.tvRightBar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvTitleBar:
                if (mineType == 10 || mineType == 11) {
                    ((EditFragment) Fragments.init().getFragment(10)).getInputText();
                }
                finish();
                break;
            case R.id.tvRightBar:
                if (mineType == 10) {
                    EditFragment fragment = (EditFragment) Fragments.init().getFragment(10);
                    String inputText = fragment.getInputText();
                    setUser(10, inputText);
                } else if (mineType == 11) {
                    EditFragment fragment = (EditFragment) Fragments.init().getFragment(10);
                    String inputText = fragment.getInputText();
                    setUser(11, inputText);
                }else if (mineType == 1){
                    ((FeedBackFragment) Fragments.init().getFragment(1)).feedback();

                }
                break;
        }

    }

    /**
     * 修改用户信息
     */
    private void setUser(final int type, final String content) {
        AuthApi api = Http.http.createApi(AuthApi.class);
        Observable<JsonResult<DataMoudle.DataBean>> jsonResultObservable = null;
        if (type == 10) {//头像
            jsonResultObservable = api.setUserNickName(content);
        } else if (type == 11) {//签名
            jsonResultObservable = api.setUserSign(content + "");
        }
        jsonResultObservable.compose(this.<JsonResult<DataMoudle.DataBean>>bindToLifecycle())
                .compose(this.<JsonResult<DataMoudle.DataBean>>applySchedulers())
                .subscribe(this.newSubscriber(new CallBack<DataMoudle.DataBean>() {
                    @Override
                    public void success(DataMoudle.DataBean response, int code) {
                        if (type == 10) {//昵称
                            SPUtils.putString(Config.NICKNAME, content);
                        } else if (type == 11) {//签名
                            SPUtils.putString(Config.SIGN, content);
                        }
                        finish();
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        ToastUtils.toastCenter(FollowerActivity.this, errorMessage + "");
                    }
                }));
    }
}
