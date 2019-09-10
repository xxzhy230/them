package com.yijian.them.ui.team;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.yijian.them.R;
import com.yijian.them.api.AuthApi;
import com.yijian.them.basic.BasicActivity;
import com.yijian.them.basic.BasicFragment;
import com.yijian.them.ui.team.adapter.TeamListAdapter;
import com.yijian.them.utils.http.CallBack;
import com.yijian.them.utils.http.Http;
import com.yijian.them.utils.http.JsonResult;
import com.yqjr.utils.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeamListFragment extends BasicFragment {
    @BindView(R.id.lvTeamList)
    ListView lvTeamList;
    private TeamListAdapter teamListAdapter;

    @Override
    protected void onClickEvent() {
        teamListAdapter = new TeamListAdapter(getActivity());
        lvTeamList.setAdapter(teamListAdapter);
        teamList();
        teamListAdapter.setOnTeamOutListener(new TeamListAdapter.OnTeamOutListener() {
            @Override
            public void onTeamOut(TeamMoudle.DataBean dataBean) {
                teamOut(dataBean);
            }
        });
    }

    private void teamOut(final TeamMoudle.DataBean dataBean) {
        String teamId = dataBean.getTeamId();
        Http.http.createApi(AuthApi.class).teamOutOrAdd(teamId, "0")
                .compose(context.<JsonResult<String>>bindToLifecycle())
                .compose(context.<JsonResult<String>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<String>() {
                    @Override
                    public void success(String response, int code) {
                        Log.d("获取小队: ", response + "");
                        if (code == 200) {
                            ToastUtils.toastCenter(getActivity(), "退出成功");
                            teamListAdapter.remove(dataBean);
                        }
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        ToastUtils.toastCenter(getActivity(), errorMessage + "");
                    }
                }));
    }

    @Override
    protected View getResourceView() {
        View rootView = View.inflate(getActivity(), R.layout.fragment_team_list, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected void initView(Bundle bundle) {
        context = (BasicActivity) getActivity();
    }

    private void teamList() {
        Http.http.createApi(AuthApi.class).teamList()
                .compose(context.<JsonResult<List<TeamMoudle.DataBean>>>bindToLifecycle())
                .compose(context.<JsonResult<List<TeamMoudle.DataBean>>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<List<TeamMoudle.DataBean>>() {
                    @Override
                    public void success(List<TeamMoudle.DataBean> response, int code) {
                        if (response != null && response.size() > 0) {
                            teamListAdapter.setData(response);
                        }
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        ToastUtils.toastCenter(getActivity(), errorMessage + "");
                    }
                }));
    }

}
