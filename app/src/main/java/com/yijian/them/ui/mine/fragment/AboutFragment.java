package com.yijian.them.ui.mine.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yijian.them.R;
import com.yijian.them.basic.BasicFragment;
import com.yijian.them.utils.JumpUtils;
import com.yijian.them.utils.http.Http;
import com.yqjr.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AboutFragment extends BasicFragment {
    @BindView(R.id.tvVersion)
    TextView tvVersion;
    @BindView(R.id.tvYonghu)
    TextView tvYonghu;
    @BindView(R.id.tvYinsi)
    TextView tvYinsi;

    @Override
    protected View getResourceView() {
        View rootView = View.inflate(getActivity(), R.layout.fragment_about, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onClickEvent() {

    }

    @Override
    protected void initView(Bundle bundle) {
        String versionCode = Utils.getVersionName();
        tvVersion.setText("版本号 : " + versionCode);
    }


    @OnClick({R.id.tvYonghu, R.id.tvYinsi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvYonghu:
                JumpUtils.jumpWebActivity(getActivity(), "用户协议", Http.baseUrl + "app/userAgreement");
                break;
            case R.id.tvYinsi:
                JumpUtils.jumpWebActivity(getActivity(), "隐私协议", Http.baseUrl + "app/privacyPolicy");
                break;
        }
    }
}
