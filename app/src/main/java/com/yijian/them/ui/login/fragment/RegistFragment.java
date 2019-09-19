package com.yijian.them.ui.login.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yijian.them.R;
import com.yijian.them.api.AuthApi;
import com.yijian.them.basic.BasicActivity;
import com.yijian.them.basic.BasicFragment;
import com.yijian.them.ui.login.DataMoudle;
import com.yijian.them.utils.JumpUtils;
import com.yijian.them.utils.http.CallBack;
import com.yijian.them.utils.http.Http;
import com.yijian.them.utils.http.JsonResult;
import com.yqjr.utils.utils.CountDownUtil;
import com.yqjr.utils.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistFragment extends BasicFragment {
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.btCode)
    Button btCode;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.btNext)
    Button btNext;
    private View rootView;


    @Override
    protected View getResourceView() {
        rootView = View.inflate(getActivity(), R.layout.fragment_regist, null);
        return rootView;
    }

    @Override
    public void onClickEvent() {

    }

    @Override
    protected void initView(Bundle bundle) {
        context = (BasicActivity) getActivity();
        ButterKnife.bind(this, rootView);
    }


    @OnClick({R.id.btCode, R.id.btNext})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btCode:
                String phone = etPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.toastCenter(getActivity(), "请输入手机号码");
                    return;
                }
                getCode(phone);
                btCode.setEnabled(false);
                break;
            case R.id.btNext:
                //获取手机号码
                String phone1 = etPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone1)) {
                    ToastUtils.toastCenter(getActivity(), "请输入手机号码");
                    return;
                }
                //校验验证码
                String code = etCode.getText().toString().trim();
                if (TextUtils.isEmpty(code)) {
                    ToastUtils.toastCenter(getActivity(), "请输入验证码");
                    return;
                }
                //密码
                String password = etPassword.getText().toString().trim();
                if (TextUtils.isEmpty(password)) {
                    ToastUtils.toastCenter(getActivity(), "请输入密码");
                    return;
                }
                verifyCode(phone1, code,password);
                break;
        }
    }

    private void verifyCode(final String phone, String code, final String password) {
        Http.http.createApi(AuthApi.class).verifyCode(phone, code)
                .compose(context.<JsonResult<Boolean>>bindToLifecycle())
                .compose(context.<JsonResult<Boolean>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<Boolean>() {
                    @Override
                    public void success(Boolean response,int code) {
                        Log.d("校验验证码: ", response + "");
                        JumpUtils.jumpLoginActivity(getActivity(),3,phone,password);
                        getActivity().finish();
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        ToastUtils.toastCenter(getActivity(), errorMessage + "");
                    }
                }));
    }

    private void getCode(String phone) {
        Http.http.createApi(AuthApi.class).getCode(phone)
                .compose(context.<JsonResult<String>>bindToLifecycle())
                .compose(context.<JsonResult<String>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<String>() {
                    @Override
                    public void success(String response,int code) {
                        if (code == 200){
                            ToastUtils.toastCenter(getActivity(), "获取验证码成功");
                            CountDownUtil countDownUtil = new CountDownUtil(btCode, 60, 1);
                            countDownUtil.start();
                        }
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        ToastUtils.toastCenter(getActivity(), errorMessage + "");
                        btCode.setEnabled(true);
                    }
                }));

    }
}
