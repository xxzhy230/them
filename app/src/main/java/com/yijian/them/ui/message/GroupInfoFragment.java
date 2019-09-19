package com.yijian.them.ui.message;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.conversation.Conversation;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit;
import com.tencent.qcloud.tim.uikit.modules.conversation.base.ConversationInfo;
import com.yijian.them.R;
import com.yijian.them.api.AuthApi;
import com.yijian.them.basic.BasicActivity;
import com.yijian.them.basic.BasicFragment;
import com.yijian.them.common.Config;
import com.yijian.them.ui.home.HomeMoudle;
import com.yijian.them.ui.team.adapter.TeamMembersAdapter;
import com.yijian.them.ui.team.moudle.TeamInfoMoudle;
import com.yijian.them.utils.JumpUtils;
import com.yijian.them.utils.http.CallBack;
import com.yijian.them.utils.http.Http;
import com.yijian.them.utils.http.JsonResult;
import com.yijian.them.view.CircleImageView;
import com.yqjr.utils.base.AppManager;
import com.yqjr.utils.base.BaseActivity;
import com.yqjr.utils.spUtils.SPUtils;
import com.yqjr.utils.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tencent.qcloud.tim.uikit.modules.conversation.base.ConversationInfo.TYPE_COMMON;

public class GroupInfoFragment extends BasicFragment {
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.gvGroupHead)
    GridView gvGroupHead;
    @BindView(R.id.tvGroupName)
    TextView tvGroupName;
    @BindView(R.id.llGroupName)
    LinearLayout llGroupName;
    @BindView(R.id.civGroupHead)
    CircleImageView civGroupHead;
    @BindView(R.id.llGroupHead)
    LinearLayout llGroupHead;
    @BindView(R.id.llGroupRemark)
    LinearLayout llGroupRemark;
    @BindView(R.id.llMyGroup)
    LinearLayout llMyGroup;
    @BindView(R.id.sGroupTop)
    Switch sGroupTop;
    @BindView(R.id.llReport)
    LinearLayout llReport;
    @BindView(R.id.tvOutGroup)
    TextView tvOutGroup;
    @BindView(R.id.tvGroupNum)
    TextView tvGroupNum;

    private ChatInfo chatInfo;
    private String groupId;
    private int type;
    private boolean topConversation;

    @Override
    protected void onClickEvent() {

    }

    @Override
    protected View getResourceView() {
        View view = View.inflate(getActivity(), R.layout.fragment_group_info, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView(Bundle bundle) {
        context = (BasicActivity) getActivity();
        String chatName = chatInfo.getChatName();
        tvGroupName.setText(chatName);
        groupId = chatInfo.getId();
        if (groupId.contains("teamId")) {
            tvOutGroup.setText("解散小队");
            getTeamMembers();
        } else {
            tvOutGroup.setText("解散群组");
            getGroupMembers();
        }
        topConversation = ConversationManagerKit.getInstance().isTopConversation(groupId);
        if (topConversation) {
            sGroupTop.setChecked(true);
        } else {
            sGroupTop.setChecked(false);
        }
        sGroupTop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    ConversationManagerKit.getInstance().setConversationTop(groupId,true);
                }else{
                    ConversationManagerKit.getInstance().setConversationTop(groupId,false);
                }
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
                        tvGroupNum.setText("查看" + members.size() + "名群成员");
                        TeamMembersAdapter adapter = new TeamMembersAdapter(members);
                        gvGroupHead.setAdapter(adapter);
                        int userId = members.get(0).getUserId();
                        if (userId == SPUtils.getInt(Config.USERID)) {
                            type = 1;
                            tvOutGroup.setText("解散小队");
                        } else {
                            type = 0;
                            tvOutGroup.setText("退出小队");
                        }
                        String teamImgUrl = response.getTeamImgUrl();
                        Picasso.with(getActivity()).load(teamImgUrl).into(civGroupHead);
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        ToastUtils.toastCenter(getActivity(), errorMessage + "");
                    }
                }));
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
                for (int i = 0; i < timGroupMemberInfos.size(); i++) {
                    TIMGroupMemberInfo timGroupMemberInfo = timGroupMemberInfos.get(i);
                    String userId = timGroupMemberInfo.getUser();
                    if (userId.equals(SPUtils.getInt(Config.USERID) + "")) {
                        type = 1;
                        if (groupId.contains("teamId")) {
                            tvOutGroup.setText("解散小队");
                        } else {
                            tvOutGroup.setText("解散群组");
                        }
                        break;
                    } else {
                        type = 0;
                        if (groupId.contains("teamId")) {
                            tvOutGroup.setText("退出小队");
                        } else {
                            tvOutGroup.setText("退出群组");
                        }
                    }
                }
            }
        });
    }


    @OnClick({R.id.ivBack, R.id.llGroupName, R.id.llGroupHead, R.id.llGroupRemark, R.id.llMyGroup, R.id.llReport, R.id.tvOutGroup})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                getActivity().finish();
                break;
            case R.id.llGroupName:
                break;
            case R.id.llGroupHead:
                break;
            case R.id.llGroupRemark:
                break;
            case R.id.llMyGroup:
                JumpUtils.jumpTeamActivity(getActivity(), 1, "我参与的小队", "");
                break;
            case R.id.llReport:


                break;
            case R.id.tvOutGroup:
                if (type == 1) {//解散小队
                    if (groupId.contains("teamId")) {
                        delTeam();
                    } else {
                        delGroup();
                    }
                } else {//退出小队
                    if (groupId.contains("teamId")) {
                        outTeam();
                    } else {
                        outGroup();
                    }

                }
                break;
        }
    }

    /**
     * 退出群组
     */
    private void outGroup() {
        TIMGroupManager.getInstance().quitGroup(groupId, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                Log.d("退出群组失败: ", s + "   " + i);
            }

            @Override
            public void onSuccess() {
                SPUtils.putString(Config.DELTEAMCHAT, groupId);
                ToastUtils.toastCenter(getActivity(), "退出成功");
                AppManager.getAppManager().finishActivity(MessageActivity.class);
                getActivity().finish();
            }
        });
    }

    /**
     * 删除群组
     */
    private void delGroup() {
        TIMGroupManager.getInstance().deleteGroup(groupId, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                Log.d("解散群组失败: ", s + "   " + i);
            }

            @Override
            public void onSuccess() {
                SPUtils.putString(Config.DELTEAMCHAT, groupId);
                ToastUtils.toastCenter(getActivity(), "解散成功");
                AppManager.getAppManager().finishActivity(MessageActivity.class);
                getActivity().finish();
            }
        });
    }

    /**
     * 退出小队
     */
    private void outTeam() {
        Http.http.createApi(AuthApi.class).teamOutOrAdd(groupId.replace("team:teamId:", ""), "0")
                .compose(context.<JsonResult<String>>bindToLifecycle())
                .compose(context.<JsonResult<String>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<String>() {
                    @Override
                    public void success(String response, int code) {
                        Log.d("退出小队: ", response + "");
                        SPUtils.putString(Config.DELTEAMCHAT, groupId);
                        if (code == 200) {
                            ToastUtils.toastCenter(getActivity(), "退出成功");
                            AppManager.getAppManager().finishActivity(MessageActivity.class);
                            getActivity().finish();
                        }
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        ToastUtils.toastCenter(getActivity(), errorMessage + "");
                    }
                }));
    }

    /**
     * 解散小队
     */
    private void delTeam() {
        Http.http.createApi(AuthApi.class).delTeam(groupId.replace("team:teamId:", ""))
                .compose(context.<JsonResult<String>>bindToLifecycle())
                .compose(context.<JsonResult<String>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<String>() {
                    @Override
                    public void success(String response, int code) {
                        Log.d("解散小队: ", response + "");
                        SPUtils.putString(Config.DELTEAMCHAT, groupId);
                        if (code == 200) {
                            ToastUtils.toastCenter(getActivity(), "退出成功");
                            AppManager.getAppManager().finishActivity(MessageActivity.class);
                            getActivity().finish();
                        }
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
}
