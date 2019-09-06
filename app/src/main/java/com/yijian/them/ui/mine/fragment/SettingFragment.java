package com.yijian.them.ui.mine.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yijian.them.R;
import com.yijian.them.basic.BasicFragment;
import com.yijian.them.utils.JumpUtils;
import com.yijian.them.utils.dialog.ThemDialog;
import com.yijian.them.utils.dialog.DialogOnitem;
import com.yijian.them.utils.fragments.Fragments;
import com.yijian.them.utils.manager.DataCleanManager;
import com.yqjr.utils.base.AppManager;
import com.yqjr.utils.spUtils.SPUtils;
import com.yqjr.utils.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingFragment extends BasicFragment {
    @BindView(R.id.llChangePwd)
    LinearLayout llChangePwd;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.llChangePhone)
    LinearLayout llChangePhone;
    @BindView(R.id.llAnQuanSetting)
    LinearLayout llAnQuanSetting;
    @BindView(R.id.tvClearSize)
    TextView tvClearSize;
    @BindView(R.id.llClear)
    LinearLayout llClear;
    @BindView(R.id.llShare)
    LinearLayout llShare;
    @BindView(R.id.llOutLogin)
    LinearLayout llOutLogin;
    private ThemDialog dialog;

    @Override
    protected View getResourceView() {
        View rootView = View.inflate(getActivity(), R.layout.fragment_setting
                , null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onClickEvent() {

    }

    @Override
    protected void initView(Bundle bundle) {
//获取缓存大小
        String cleanNum = null;
        try {
            cleanNum = DataCleanManager.getTotalCacheSize(getActivity());
            tvClearSize.setText(cleanNum);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OnClick({R.id.llChangePwd, R.id.llChangePhone, R.id.llAnQuanSetting, R.id.llClear, R.id.llShare, R.id.llOutLogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llChangePwd:
                JumpUtils.jumpMineActivity(getActivity(), 12, "修改密码");
                break;
            case R.id.llChangePhone:
                JumpUtils.jumpMineActivity(getActivity(), 16, "安全验证");
                break;
            case R.id.llAnQuanSetting:
                break;
            case R.id.llClear:
                //清理缓存
                dialog = new ThemDialog(getActivity(), false, null, new DialogOnitem() {
                    @Override
                    public void onItemClickListener(int position) {

                        //清理缓存
                        try {
                            DataCleanManager.clearAllCache(getActivity());
                            try {
                                String cleanNum = DataCleanManager.getTotalCacheSize(getActivity());
                                tvClearSize.setText(cleanNum);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ToastUtils.toastCenter(getActivity(), "缓存已清除");
                    }
                }, "清理缓存", "请确认是否删除缓存?");
                dialog.show();

                break;
            case R.id.llShare:
                break;
            case R.id.llOutLogin:
                //清理缓存
                dialog = new ThemDialog(getActivity(), false, null, new DialogOnitem() {
                    @Override
                    public void onItemClickListener(int position) {
                        AppManager.getAppManager().finishAllActivity();
                        Fragments.init().finish();
                        SPUtils.getInstance().edit().clear().commit();
                        JumpUtils.jumpLoginActivity(getActivity(), 0, "", "");
                    }
                }, "退出登录", "点击确认退出登录");
                dialog.show();
                break;
        }
    }
}
