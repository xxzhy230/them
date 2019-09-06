package com.yijian.them.ui.mine.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yijian.them.R;
import com.yijian.them.api.AuthApi;
import com.yijian.them.basic.BasicActivity;
import com.yijian.them.basic.BasicFragment;
import com.yijian.them.common.Config;
import com.yijian.them.ui.login.DataMoudle;
import com.yijian.them.utils.JumpUtils;
import com.yijian.them.utils.dialog.AlertUtils;
import com.yijian.them.utils.http.CallBack;
import com.yijian.them.utils.http.Http;
import com.yijian.them.utils.http.JsonResult;
import com.yqjr.utils.spUtils.SPUtils;
import com.yqjr.utils.utils.CountDownUtil;
import com.yqjr.utils.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CodeFragment extends BasicFragment {

    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.btCode)
    Button btCode;
    @BindView(R.id.btNext)
    Button btNext;
    @BindView(R.id.llCode)
    LinearLayout llCode;

    private View rootView;
    private String phone;
    private int type = 0;// 0 密码验证  1 换绑手机号 2 换绑手机号验证

    public void setType(int type) {
        this.type = type;
    }

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
        phone = SPUtils.getString(Config.LOGINPHONE);
        tvPhone.setText(phone);
        if (type == 0) {
            CountDownUtil countDownUtil = new CountDownUtil(btCode, 60, 1);
            countDownUtil.start();
            btCode.setEnabled(false);
        } else if (type == 1) {
            llCode.setVisibility(View.GONE);
            btNext.setText("获取验证码");
        }

    }


    @OnClick({R.id.btCode, R.id.btNext})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btCode:
                AlertUtils.showProgress(false, getActivity());
                getCode(phone);
                btCode.setEnabled(false);
                break;
            case R.id.btNext:
                if (type == 0) {
                    String code = etCode.getText().toString().trim();
                    if (TextUtils.isEmpty(code)) {
                        ToastUtils.toastCenter(getActivity(), "请输入验证码");
                        return;
                    }
                    SPUtils.putString(Config.CODE, code);
                    JumpUtils.jumpMineActivity(getActivity(), 13, "设置密码");
                    getActivity().finish();
                } else if (type == 1) {
                    AlertUtils.showProgress(false, getActivity());
                    getCode(phone);
                }else if (type ==2){
                    String code = etCode.getText().toString().trim();
                    if (TextUtils.isEmpty(code)) {
                        ToastUtils.toastCenter(getActivity(), "请输入验证码");
                        return;
                    }

                    JumpUtils.jumpMineActivity(getActivity(), 17, "修改手机号码");
                    getActivity().finish();
                }

                break;
        }
    }


    private void getCode(String phone) {
        Http.http.createApi(AuthApi.class).getCode(phone)
                .enqueue(new Callback<DataMoudle>() {
                    @Override
                    public void onResponse(Call<DataMoudle> call, Response<DataMoudle> response) {
                        AlertUtils.dismissProgress();
                        CountDownUtil countDownUtil = new CountDownUtil(btCode, 60, 1);
                        countDownUtil.start();
                        btCode.setEnabled(false);
                        if (type == 0) {
                            etCode.setText(response.body().getData().getCode());
                            ToastUtils.toastCenter(getActivity(), "获取验证码成功");
                        } else if (type == 1) {
                            type = 2;
                            llCode.setVisibility(View.VISIBLE);
                            btNext.setText("验证");
                        }

                    }

                    @Override
                    public void onFailure(Call<DataMoudle> call, Throwable t) {
                        btCode.setEnabled(true);
                        AlertUtils.dismissProgress();
                    }
                });
    }

}
