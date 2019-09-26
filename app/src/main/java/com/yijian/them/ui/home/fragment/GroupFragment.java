package com.yijian.them.ui.home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupBaseInfo;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfo;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfoResult;
import com.tencent.imsdk.ext.group.TIMGroupSelfInfo;
import com.yijian.them.R;
import com.yijian.them.api.AuthApi;
import com.yijian.them.basic.BasicActivity;
import com.yijian.them.basic.BasicFragment;
import com.yijian.them.common.Config;
import com.yijian.them.ui.home.adapter.GroupAdapter;
import com.yijian.them.utils.dialog.AlertUtils;
import com.yijian.them.utils.http.CallBack;
import com.yijian.them.utils.http.Http;
import com.yijian.them.utils.http.JsonResult;
import com.yqjr.utils.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 群
 */
public class GroupFragment extends BasicFragment {
    @BindView(R.id.lvGroup)
    ListView lvGroup;
    private GroupAdapter groupAdapter;
    private TIMGroupManager instance;

    @Override
    protected void onClickEvent() {
        context = (BasicActivity) getActivity();
    }

    @Override
    protected View getResourceView() {
        View view = View.inflate(getActivity(), R.layout.fragment_group, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView(Bundle bundle) {
        context = (BasicActivity) getActivity();
        groupAdapter = new GroupAdapter();
        lvGroup.setAdapter(groupAdapter);
        lvGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TIMGroupBaseInfo item = groupAdapter.getItem(position);
                Intent intent = new Intent();
                intent.putExtra(Config.GROUPNAME,item.getGroupName());
                intent.putExtra(Config.GROUPID,item.getGroupId());
                getActivity().setResult(1,intent);
                getActivity().finish();
            }
        });
        instance = TIMGroupManager.getInstance();

    }

    @Override
    public void onResume() {
        super.onResume();
        instance.getGroupList(new TIMValueCallBack<List<TIMGroupBaseInfo>>() {
            @Override
            public void onError(int i, String s) {
            }

            @Override
            public void onSuccess(List<TIMGroupBaseInfo> timGroupBaseInfos) {
                if (timGroupBaseInfos != null && timGroupBaseInfos.size() > 0) {
                    groupAdapter.setData(timGroupBaseInfos);
                }
            }
        });
    }
}
