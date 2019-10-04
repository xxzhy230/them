package com.yijian.them.ui.message.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupMemberResult;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.yijian.them.R;
import com.yijian.them.api.AuthApi;
import com.yijian.them.basic.BasicActivity;
import com.yijian.them.basic.BasicFragment;
import com.yijian.them.common.Config;
import com.yijian.them.ui.team.adapter.GroupMembersAdapter;
import com.yijian.them.ui.team.adapter.MembersAdapter;
import com.yijian.them.ui.team.moudle.TeamInfoMoudle;
import com.yijian.them.utils.http.CallBack;
import com.yijian.them.utils.http.Http;
import com.yijian.them.utils.http.JsonResult;
import com.yqjr.utils.spUtils.SPUtils;
import com.yqjr.utils.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MembersFragment extends BasicFragment {

    @BindView(R.id.tvState)
    TextView tvState;
    @BindView(R.id.lbBalckList)
    ListView lbBalckList;
    private MembersAdapter membersAdapter;
    private ChatInfo chatInfo;
    private String groupId;
    private int type = 0;// 0  编辑   1  取消

    @Override
    protected void onClickEvent() {
    }

    @Override
    protected View getResourceView() {
        View view = View.inflate(getActivity(), R.layout.fragment_members, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView(Bundle bundle) {
        context = (BasicActivity) getActivity();
        membersAdapter = new MembersAdapter();
        lbBalckList.setAdapter(membersAdapter);
        groupId = chatInfo.getId();
        membersAdapter.setOnSelectChangeListener(new MembersAdapter.OnSelectChangeListener() {
            @Override
            public void onSelectChange(List<TeamInfoMoudle.DataBean.MembersBean> members) {
                if (type == 1) {
                    if (members.size() > 0) {
                        tvState.setText("删除");
                        tvState.setTextColor(getResources().getColor(R.color.color_F06063));
                    } else {
                        tvState.setText("取消");
                        tvState.setTextColor(getResources().getColor(R.color.color_FF333333));
                    }
                }
            }
        });
        if (groupId.contains("teamId")) {
            getTeamMembers();
        } else {
            getGroupMembers();
        }

    }

    /**
     * 获取群组成员
     */
    private void getGroupMembers() {
        TIMGroupManager.getInstance().getGroupMembers(groupId, new TIMValueCallBack<List<TIMGroupMemberInfo>>() {
            @Override
            public void onError(int i, String s) {
                Log.d("群成员列表失败  ", s + "  状态码  " + i);
            }

            @Override
            public void onSuccess(List<TIMGroupMemberInfo> timGroupMemberInfos) {
                List<String> userIdList = new ArrayList<>();
                for (int i = 0; i < timGroupMemberInfos.size(); i++) {
                    TIMGroupMemberInfo timGroupMemberInfo = timGroupMemberInfos.get(i);
                    String userId = timGroupMemberInfo.getUser();
                    userIdList.add(userId);
                }
                TIMFriendshipManager.getInstance().getUsersProfile(userIdList, true, new TIMValueCallBack<List<TIMUserProfile>>() {
                    @Override
                    public void onError(int i, String s) {

                    }

                    @Override
                    public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                        TeamInfoMoudle.DataBean.MembersBean membersBean = new TeamInfoMoudle.DataBean.MembersBean();
                        List<TeamInfoMoudle.DataBean.MembersBean> membersBeans = membersBean.changeData(timUserProfiles);
                        membersAdapter.setData(membersBeans);
                    }
                });
            }
        });
    }

    /**
     * 获取小队成员
     */
    private void getTeamMembers() {
        Http.http.createApi(AuthApi.class).teamInfo(groupId.replace("team:teamId:", ""))
                .compose(context.<JsonResult<TeamInfoMoudle.DataBean>>bindToLifecycle())
                .compose(context.<JsonResult<TeamInfoMoudle.DataBean>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<TeamInfoMoudle.DataBean>() {
                    @Override
                    public void success(TeamInfoMoudle.DataBean response, int code) {
                        List<TeamInfoMoudle.DataBean.MembersBean> members = response.getMembers();
                        membersAdapter.setData(members);
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        ToastUtils.toastCenter(getActivity(), errorMessage + "");
                    }
                }));
    }


    public void setChatInfo(ChatInfo chatInfo) {
        this.chatInfo = chatInfo;
    }


    @OnClick({R.id.tvCannel, R.id.tvState})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvCannel:
                getActivity().finish();
                break;
            case R.id.tvState:
                String s = tvState.getText().toString();
                if ("删除".equals(s)) {
                    List<TeamInfoMoudle.DataBean.MembersBean> delList = membersAdapter.getDelList();
                    if (delList.size() == 0) {
                        return;
                    }
                    if (groupId.contains("teamId")) {
                        delTeamMember(delList);
                    } else {
                        delGroupUser(delList);
                    }

                } else {
                    if (type == 0) {
                        type = 1;
                        tvState.setText("取消");
                        tvState.setTextColor(getResources().getColor(R.color.color_FF333333));
                        membersAdapter.setType(type);
                    } else if (type == 1) {
                        tvState.setText("编辑");
                        tvState.setTextColor(getResources().getColor(R.color.color_FF333333));
                        type = 0;
                        membersAdapter.setType(type);
                    }
                }
                break;
        }
    }

    private void delGroupUser(final List<TeamInfoMoudle.DataBean.MembersBean> delList) {
        List<String> mList = new ArrayList<>();
        for (int i = 0; i < delList.size(); i++) {
            mList.add(delList.get(i).getUserId() + "");
        }
        TIMGroupManager.DeleteMemberParam param = new TIMGroupManager.DeleteMemberParam(groupId, mList);
        TIMGroupManager.getInstance().deleteGroupMember(param, new TIMValueCallBack<List<TIMGroupMemberResult>>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(List<TIMGroupMemberResult> timGroupMemberResults) {
                List<TeamInfoMoudle.DataBean.MembersBean> delLists = new ArrayList<>();
                for (int i = 0; i < timGroupMemberResults.size(); i++) {
                    long result = timGroupMemberResults.get(i).getResult();
                    if (result == 1) {
                        String userId = timGroupMemberResults.get(i).getUser();
                        for (int j = 0; j < delList.size(); j++) {
                            if (userId.equals(delList.get(j).getUserId() + "")) {
                                delLists.add(delList.get(j));
                            }
                        }
                    }
                }
                membersAdapter.delMembers(delLists);
            }
        });
    }

    private void delTeamMember(final List<TeamInfoMoudle.DataBean.MembersBean> delList) {
        List<String> mDelList = new ArrayList<>();
        for (int i = 0; i < delList.size(); i++) {
            mDelList.add(delList.get(i).getUserId() + "");
        }
        Map map = new HashMap();
        map.put("userIds", mDelList);
        Http.http.createApi(AuthApi.class).delTeamMember(groupId.replace("team:teamId:", ""), map)
                .compose(context.<JsonResult<String>>bindToLifecycle())
                .compose(context.<JsonResult<String>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<String>() {
                    @Override
                    public void success(String response, int code) {
                        membersAdapter.delMembers(delList);
                        ToastUtils.toastCenter(getActivity(), "删除成功");
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        ToastUtils.toastCenter(getActivity(), errorMessage + "");
                    }
                }));
    }
}
