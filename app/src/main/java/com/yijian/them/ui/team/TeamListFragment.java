package com.yijian.them.ui.team;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.yijian.them.R;
import com.yijian.them.api.AuthApi;
import com.yijian.them.basic.BasicActivity;
import com.yijian.them.basic.BasicFragment;
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

    @Override
    protected void onClickEvent() {
        teamList();
    }

    @Override
    protected View getResourceView() {
        View rootView = View.inflate(getActivity(), R.layout.fragment_team_list, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected void initView(Bundle bundle) {
        context = (BasicActivity)getActivity();

    }

    private void teamList() {
        Http.http.createApi(AuthApi.class).teamList()
                .compose(context.<JsonResult<List<TeamMoudle.DataBean>>>bindToLifecycle())
                .compose(context.<JsonResult<List<TeamMoudle.DataBean>>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<List<TeamMoudle.DataBean>>() {
                    @Override
                    public void success(List<TeamMoudle.DataBean> response, int code) {
//                        if (code == 10001) {
//                            teamAdapter.setData(response);
//                            page++;
//                        } else if (code == 10000) {
//                            srlLayout.finishLoadMoreWithNoMoreData();
//                            if (page == 1) {
//                                llDefault.setVisibility(View.VISIBLE);
//                                ivDefault.setImageResource(R.mipmap.default_dynamic);
//                                tvDefault.setText("暂无小队信息，快加入小队吧");
//                            }
//                        } else if (code == 50002) {
//                            llDefault.setVisibility(View.VISIBLE);
//                            ivDefault.setImageResource(R.mipmap.default_load);
//                            tvDefault.setText("哎呀！加载失败");
//                        }

                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        ToastUtils.toastCenter(getActivity(), errorMessage + "");
                    }
                }));
    }

}
