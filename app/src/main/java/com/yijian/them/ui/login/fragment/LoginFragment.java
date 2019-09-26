package com.yijian.them.ui.login.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMManager;
import com.yijian.them.R;
import com.yijian.them.api.AuthApi;
import com.yijian.them.basic.BasicActivity;
import com.yijian.them.basic.BasicFragment;
import com.yijian.them.common.Config;
import com.yijian.them.jpush.ExampleUtil;
import com.yijian.them.ui.login.DataMoudle;
import com.yijian.them.utils.JumpUtils;
import com.yijian.them.utils.http.CallBack;
import com.yijian.them.utils.http.Http;
import com.yijian.them.utils.http.JsonResult;
import com.yqjr.utils.spUtils.SPUtils;
import com.yqjr.utils.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class LoginFragment extends BasicFragment {
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.btLogin)
    Button btLogin;

    @Override
    protected View getResourceView() {
        View rootView = View.inflate(getActivity(), R.layout.fragment_login, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onClickEvent() {

    }

    @Override
    protected void initView(Bundle bundle) {
        context = (BasicActivity) getActivity();
    }


    @OnClick(R.id.btLogin)
    public void onViewClicked() {
        String phone = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.toastCenter(getActivity(), "请输入手机号码");
            return;
        }
        String password = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ToastUtils.toastCenter(getActivity(), "请输入密码");
            return;
        }
        login(phone, password);
    }

    private void login(final String phone, String password) {
        Map<String, String> map = new HashMap();
        map.put("password", password);
        map.put("phone", phone);
        Http.http.createApi(AuthApi.class).login(map)
                .compose(context.<JsonResult<DataMoudle.DataBean>>bindToLifecycle())
                .compose(context.<JsonResult<DataMoudle.DataBean>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<DataMoudle.DataBean>() {
                    @Override
                    public void success(DataMoudle.DataBean response, int code) {
                        Log.d("登录: ", response.getToken() + "");
                        if (response != null) {
                            String token = response.getToken();
                            String userSign = response.getUserSign();
                            int userId = response.getUserId();
                            SPUtils.putInt(Config.USERID, userId);
                            SPUtils.putToken(token);
                            SPUtils.putString(Config.LOGINPHONE, phone);
                            SPUtils.putString(Config.USERSIGN, userSign);
                            getUserInfo();
                        }
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        ToastUtils.toastCenter(getActivity(), errorMessage + "");
                    }
                }));
    }

    private void messageLogin() {
        setAlias(SPUtils.getInt(Config.USERID) + "");
        TIMManager.getInstance().login(SPUtils.getInt(Config.USERID) + "", SPUtils.getString(Config.USERSIGN), new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                Log.d("IM登录 Error: ", s);
            }

            @Override
            public void onSuccess() {
                Log.d("IM登录 : ", "Success");
                JumpUtils.jumpMainActivity(getActivity());
                getActivity().finish();
            }
        });
    }


    private void getUserInfo() {
        Http.http.createApi(AuthApi.class).getUser()
                .compose(context.<JsonResult<DataMoudle.DataBean>>bindToLifecycle())
                .compose(context.<JsonResult<DataMoudle.DataBean>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<DataMoudle.DataBean>() {
                    @Override
                    public void success(DataMoudle.DataBean response, int code) {
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
                        messageLogin();
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        ToastUtils.toastCenter(getActivity(), errorMessage + "");
                    }
                }));
    }


    // 这是来自 JPush Example 的设置别名的 Activity 里的代码。一般 App 的设置的调用入口，在任何方便的地方调用都可以。
    private void setAlias(String alias) {
        // 调用 Handler 来异步设置别名
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i("them", logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i("them", logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e("them", logs);
            }
//            ExampleUtil.showToast(logs, getActivity());
        }
    };
    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d("them", "Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getActivity(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
                default:
                    Log.i("them", "Unhandled msg - " + msg.what);
            }
        }
    };
}
