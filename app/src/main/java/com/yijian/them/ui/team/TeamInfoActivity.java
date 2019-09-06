package com.yijian.them.ui.team;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yijian.them.R;
import com.yijian.them.api.AuthApi;
import com.yijian.them.basic.BasicActivity;
import com.yijian.them.common.App;
import com.yijian.them.common.Config;
import com.yijian.them.ui.home.activity.ReportActivity;
import com.yijian.them.ui.team.adapter.TeamMembersAdapter;
import com.yijian.them.ui.team.moudle.TeamInfoMoudle;
import com.yijian.them.utils.dialog.AlertUtils;
import com.yijian.them.utils.http.CallBack;
import com.yijian.them.utils.http.Http;
import com.yijian.them.utils.http.JsonResult;
import com.yqjr.utils.spUtils.SPUtils;
import com.yqjr.utils.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public int initView() {
        return R.layout.activity_team_info;
    }

    @Override
    public void initData() {
        ButterKnife.bind(this);
        String teamId = getIntent().getStringExtra(Config.TEAMID);
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
                        String teamName = dataBean.getTeamName();
                        int teamImgWidth = dataBean.getTeamImgWidth();
                        int teamImgHeight = dataBean.getTeamImgHeight();

                        tvTeamTitle.setText(teamName);
                        double width = App.mWidth;
                        double height = width * teamImgHeight / teamImgWidth;
                        ViewGroup.LayoutParams layoutParams = ivImage.getLayoutParams();
                        layoutParams.height = (int) height;
                        ivImage.setLayoutParams(layoutParams);
                        if (!TextUtils.isEmpty(teamImgUrl)) {
                            Picasso.with(TeamInfoActivity.this).load(teamImgUrl).into(ivImage);
                        }

                        tvTeamNum.setText(members.size() + "/" + dataBean.getTeamMember().replace(",", ""));
                        String teamDesc = dataBean.getTeamDesc();
                        tvTeamContent.setText(teamDesc);
                        String teamStatus = dataBean.getTeamStatus();
                        if ("0".equals(teamStatus)) {
                            tvAddTeam.setVisibility(View.VISIBLE);
                        } else {
                            tvAddTeam.setVisibility(View.GONE);
                        }
                        if (members != null && members.size() > 0) {
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
                break;
            case R.id.ivMore:
                break;
            case R.id.tvAddTeam:
                break;
        }
    }
}
