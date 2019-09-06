package com.yijian.them.ui.home.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMValueCallBack;
import com.yijian.them.R;
import com.yijian.them.api.AuthApi;
import com.yijian.them.basic.BasicActivity;
import com.yijian.them.basic.BasicFragment;
import com.yijian.them.common.App;
import com.yijian.them.ui.home.adapter.GroupAdapter;
import com.yijian.them.utils.dialog.AlertUtils;
import com.yijian.them.utils.http.CallBack;
import com.yijian.them.utils.http.Http;
import com.yijian.them.utils.http.JsonResult;
import com.yqjr.utils.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 群
 */
public class CreatGroupFragment extends BasicFragment {


    @BindView(R.id.etGroupName)
    EditText etGroupName;
    @BindView(R.id.etGroupContent)
    EditText etGroupContent;

    @Override
    protected void onClickEvent() {
    }

    @Override
    protected View getResourceView() {
        View view = View.inflate(getActivity(), R.layout.fragment_creat_group, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView(Bundle bundle) {
    }


    @OnClick({R.id.tvBack, R.id.tvCreat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvBack:
                getActivity().finish();
                break;
            case R.id.tvCreat:
                String groupName = etGroupName.getText().toString().trim();
                if (TextUtils.isEmpty(groupName)) {
                    ToastUtils.toastCenter(getActivity(), "请填写群名称");
                    return;
                }
                String groupContent = etGroupContent.getText().toString().trim();
                creatTeam(groupName, groupContent);
                break;
        }
    }

    private void creatTeam(String groupName, String groupContent) {
        AlertUtils.showProgress(false, getActivity());
        TIMGroupManager.CreateGroupParam param = new TIMGroupManager.CreateGroupParam("Public", groupName);
        param.setIntroduction(groupContent);
        TIMGroupManager.getInstance().createGroup(param, new TIMValueCallBack<String>() {
            @Override
            public void onError(int i, String s) {
                AlertUtils.dismissProgress();
                ToastUtils.toastCenter(getActivity(), s);
            }

            @Override
            public void onSuccess(String s) {
                AlertUtils.dismissProgress();
                ToastUtils.toastCenter(getActivity(), "创建成功");
                getActivity().finish();
            }
        });
    }

}
