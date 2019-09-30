package com.yijian.them.ui.team;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.yijian.them.R;
import com.yijian.them.api.AuthApi;
import com.yijian.them.basic.BasicActivity;
import com.yijian.them.common.App;
import com.yijian.them.common.Config;
import com.yijian.them.ui.team.adapter.TeamMembersAdapter;
import com.yijian.them.ui.team.moudle.TeamInfoMoudle;
import com.yijian.them.utils.JumpUtils;
import com.yijian.them.utils.dialog.AlertUtils;
import com.yijian.them.utils.dialog.ReportDialog;
import com.yijian.them.utils.http.CallBack;
import com.yijian.them.utils.http.Http;
import com.yijian.them.utils.http.JsonResult;
import com.yqjr.utils.spUtils.SPUtils;
import com.yqjr.utils.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TeamInfoActivity extends BasicActivity {
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.ivMore)
    ImageView ivMore;
    @BindView(R.id.ivImage)
    ImageView ivImage;
    @BindView(R.id.tvTeamTitle)
    TextView tvTeamTitle;
    @BindView(R.id.tvTeamContent)
    TextView tvTeamContent;
    @BindView(R.id.tvTeamInfo)
    TextView tvTeamInfo;
    @BindView(R.id.tvTeamNum)
    TextView tvTeamNum;
    @BindView(R.id.gvTeamHead)
    GridView gvTeamHead;
    @BindView(R.id.tvAddTeam)
    TextView tvAddTeam;
    private String teamId;
    private String teamName;
    private String teamDesc;
    private TeamInfoMoudle.DataBean.MembersBean membersBean;

    @Override
    public int initView() {
        return R.layout.activity_team_info;
    }

    @Override
    public void initData() {
        ButterKnife.bind(this);
        teamId = getIntent().getStringExtra(Config.TEAMID);
    }

    @Override
    protected void onResume() {
        super.onResume();
        teamInfo(teamId);
    }

    private void teamInfo(String teamId) {
        Http.http.createApi(AuthApi.class).teamInfo(teamId)
                .compose(this.<JsonResult<TeamInfoMoudle.DataBean>>bindToLifecycle())
                .compose(this.<JsonResult<TeamInfoMoudle.DataBean>>applySchedulers())
                .subscribe(this.newSubscriber(new CallBack<TeamInfoMoudle.DataBean>() {
                    @Override
                    public void success(TeamInfoMoudle.DataBean dataBean, int code) {
                        AlertUtils.dismissProgress();
                        List<TeamInfoMoudle.DataBean.MembersBean> members = dataBean.getMembers();
                        String teamImgUrl = dataBean.getTeamImgUrl();
                        teamName = dataBean.getTeamName();
                        tvTeamTitle.setText(teamName);
                        if (!TextUtils.isEmpty(teamImgUrl)) {
                            Picasso.with(TeamInfoActivity.this).load(teamImgUrl).into(ivImage);
                        }
                        tvTeamNum.setText(members.size() + "/8");
                        teamDesc = dataBean.getTeamDesc();
                        tvTeamContent.setText(teamDesc);
                        String teamMember = dataBean.getTeamMember();
                        String[] split = teamMember.split(",");
                        for (int i = 0; i < split.length; i++) {
                            if (split[i].equals(SPUtils.getInt(Config.USERID) + "")) {
                                tvAddTeam.setVisibility(View.GONE);
                                break;
                            } else {
                                tvAddTeam.setVisibility(View.VISIBLE);
                            }
                        }
                        if (members != null && members.size() > 0) {
                            membersBean = members.get(0);
                            TeamMembersAdapter adapter = new TeamMembersAdapter(members);
                            gvTeamHead.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(TeamInfoActivity.this, errorMessage + "");
                    }
                }));
    }


    @OnClick({R.id.ivBack, R.id.ivMore, R.id.tvAddTeam})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivMore:
                int userId = membersBean.getUserId();
                if (userId == SPUtils.getInt(Config.USERID)) {
                    ReportDialog dialog = new ReportDialog(this, 2);
                    dialog.show();
                    dialog.setOnClicklistener(new ReportDialog.OnClicklistener() {
                        @Override
                        public void onClick(int type) {
                            if (type == 1) {//举报
                                JumpUtils.jumpReportActivity(TeamInfoActivity.this, teamId, 3, "", "");
                            } else if (type == 2) {//编辑
                                SPUtils.putString(Config.TEAMTITLE, teamName);
                                SPUtils.putString(Config.TEAMDESC, teamDesc);
                                JumpUtils.jumpTeamActivity(TeamInfoActivity.this, 2, "", teamId);
                            }
                        }
                    });
                } else {
                    ReportDialog dialog = new ReportDialog(this, 3);
                    dialog.show();
                    dialog.setOnClicklistener(new ReportDialog.OnClicklistener() {
                        @Override
                        public void onClick(int type) {
                            if (type == 1) {//举报
                                JumpUtils.jumpReportActivity(TeamInfoActivity.this, teamId, 3, "", "");
                            }
                        }
                    });
                }
                break;
            case R.id.tvAddTeam:
                AlertUtils.showProgress(false,this);
                teamOutOrAdd();
                break;
        }
    }

    private void teamOutOrAdd() {
        Http.http.createApi(AuthApi.class).teamOutOrAdd(teamId, "1")
                .compose(this.<JsonResult<String>>bindToLifecycle())
                .compose(this.<JsonResult<String>>applySchedulers())
                .subscribe(this.newSubscriber(new CallBack<String>() {
                    @Override
                    public void success(String str, int code) {
                        AlertUtils.dismissProgress();
//                        teamInfo(teamId);
                        ChatInfo chatInfo = new ChatInfo();
                        chatInfo.setId(teamId);
                        chatInfo.setChatName(teamName);
                        chatInfo.setType(TIMConversationType.Group);
                        JumpUtils.jumpMessageActivity(TeamInfoActivity.this,0,chatInfo);
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(TeamInfoActivity.this, errorMessage + "");
                    }
                }));
    }
}
