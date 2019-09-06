package com.yijian.them.ui.mine.activity;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.yijian.them.R;
import com.yijian.them.api.AuthApi;
import com.yijian.them.basic.BasicActivity;
import com.yijian.them.common.Config;
import com.yijian.them.ui.login.DataMoudle;
import com.yijian.them.utils.JumpUtils;
import com.yijian.them.utils.dialog.AlertUtils;
import com.yijian.them.utils.http.CallBack;
import com.yijian.them.utils.http.Http;
import com.yijian.them.utils.http.JsonResult;
import com.yqjr.utils.base.AppManager;
import com.yqjr.utils.spUtils.SPUtils;
import com.yqjr.utils.utils.CountDownUtil;
import com.yqjr.utils.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutActivity extends BasicActivity {
    @BindView(R.id.tvTitleBar)
    TextView tvTitleBar;
    @BindView(R.id.tvContent)
    TextView tvContent;

    @Override
    public int initView() {
        return R.layout.activity_about;
    }

    @Override
    public void initData() {
        ButterKnife.bind(this);
//        int type = getIntent().getIntExtra(Config.ABOUTTYPE, 0);
//        if (type ==1){
//            JumpUtils.jumpWebActivity(this,"用户协议","");
//            tvTitleBar.setText("用户协议");
//            userAgreement();
//        }else{
//            tvTitleBar.setText("隐私协议");
//        }

    }

    /**
     * 用户协议
     */
    private void userAgreement() {
        Http.http.createApi(AuthApi.class).userAgreement()
                .compose(this.<JsonResult<String>>bindToLifecycle())
                .compose(this.<JsonResult<String>>applySchedulers())
                .subscribe(this.newSubscriber(new CallBack<String>() {
                    @Override
                    public void success(String response,int code) {
                        tvContent.setText(Html.fromHtml(response));
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(AboutActivity.this, errorMessage + "");
                    }
                }));
    }


    @OnClick(R.id.tvTitleBar)
    public void onViewClicked() {
        finish();
    }
}
