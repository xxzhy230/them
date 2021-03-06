package com.yijian.them.ui.mine.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.yijian.them.R;
import com.yijian.them.api.AuthApi;
import com.yijian.them.basic.BasicActivity;
import com.yijian.them.basic.BasicFragment;
import com.yijian.them.utils.http.CallBack;
import com.yijian.them.utils.http.Http;
import com.yijian.them.utils.http.JsonResult;
import com.yqjr.utils.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetPhoneFragment extends BasicFragment {
    @BindView(R.id.etPasswordOld)
    EditText etPasswordOld;
    @BindView(R.id.etPasswordNew)
    EditText etPasswordNew;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.llOldPwd)
    LinearLayout llOldPwd;

    private int type = 0;//0 原密码修改密码  1 短信修改密码
    private String passwordOld;
    private String passwordNew;
    private String password;
    private String phone = "";
    private String verifyCode = "";


    public void setType(int type) {
        this.type = type;
    }

    @Override
    protected View getResourceView() {
        View rootView = View.inflate(getActivity(), R.layout.fragment_set_pwd, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onClickEvent() {

    }

    @Override
    protected void initView(Bundle bundle) {
        context = (BasicActivity) getActivity();
        if (type == 1) {
            llOldPwd.setVisibility(View.VISIBLE);
        } else {
            llOldPwd.setVisibility(View.GONE);
        }
    }


    @OnClick({R.id.tvSubmit, R.id.tvForgetPwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvSubmit:
                if (type == 1) {
                    passwordOld = etPasswordOld.getText().toString().trim();
                    if (TextUtils.isEmpty(passwordOld)) {
                        ToastUtils.toastCenter(getActivity(), "请输入旧密码");
                        return;
                    }
                }

                passwordNew = etPasswordNew.getText().toString();
                if (TextUtils.isEmpty(passwordNew)) {
                    ToastUtils.toastCenter(getActivity(), "请输入新密码");
                    return;
                }
                password = etPassword.getText().toString().trim();
                if (TextUtils.isEmpty(password)) {
                    ToastUtils.toastCenter(getActivity(), "请输入确认密码");
                    return;
                }
                setPwd();
                break;
            case R.id.tvForgetPwd:
                break;
        }


    }

    private void setPwd() {
        Map<String, String> map = new HashMap();
        map.put("newPassword", passwordNew);
        map.put("verifyPassword", password);
        if (type == 0) {
            map.put("oldPassword", passwordOld);
        } else {
            map.put("phone", phone);
            map.put("verifyCode", verifyCode);
        }
        map.put("phone", "");
        map.put("verifyCode", "");
        Http.http.createApi(AuthApi.class).settingPwd(map, type+"")
                .compose(context.<JsonResult<Object>>bindToLifecycle())
                .compose(context.<JsonResult<Object>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<Object>() {
                    @Override
                    public void success(Object response,int code) {
                        Log.d("修改密码: ", "");
                        ToastUtils.toastCenter(getActivity(), "修改成功");
                        getActivity().finish();
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        ToastUtils.toastCenter(getActivity(), errorMessage + "");
                    }
                }));
    }
}
