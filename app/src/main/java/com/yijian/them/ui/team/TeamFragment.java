package com.yijian.them.ui.team;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.picasso.Picasso;
import com.yijian.them.R;
import com.yijian.them.api.AuthApi;
import com.yijian.them.basic.BasicActivity;
import com.yijian.them.basic.BasicFragment;
import com.yijian.them.common.App;
import com.yijian.them.common.Config;
import com.yijian.them.ui.login.DataMoudle;
import com.yijian.them.ui.team.adapter.TeamAdapter;
import com.yijian.them.utils.JumpUtils;
import com.yijian.them.utils.StringUtils;
import com.yijian.them.utils.http.CallBack;
import com.yijian.them.utils.http.Http;
import com.yijian.them.utils.http.JsonResult;
import com.yijian.them.utils.location.LocationUtil;
import com.yqjr.utils.spUtils.SPUtils;
import com.yqjr.utils.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class TeamFragment extends BasicFragment implements OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.ivMenu)
    ImageView ivMenu;
    @BindView(R.id.ivAdd)
    ImageView ivAdd;
    @BindView(R.id.rvTeam)
    RecyclerView rvTeam;
    @BindView(R.id.ivDefault)
    ImageView ivDefault;
    @BindView(R.id.tvDefault)
    TextView tvDefault;
    @BindView(R.id.llDefault)
    LinearLayout llDefault;
    @BindView(R.id.srlLayout)
    SmartRefreshLayout srlLayout;
    @BindView(R.id.sbRadius)
    SeekBar sbRadius;
    @BindView(R.id.tvRadius)
    TextView tvRadius;
    private String radius = "20";
    int progress = 0;
    private int page = 1;
    private TeamAdapter teamAdapter;
    private LocationUtil locationUtil;

    @Override
    protected View getResourceView() {
        View rootView = View.inflate(getActivity(), R.layout.fragment_team, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onClickEvent() {
        srlLayout.setOnLoadMoreListener(this);
        srlLayout.setOnRefreshListener(this);
        sbRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //停止触摸
            public void onStopTrackingTouch(SeekBar seekBar) {
                progress = seekBar.getProgress();
                if (progress <= 10 && progress > 0) {
                    sbRadius.setProgress(0);
                } else if (progress < 20 && progress > 10) {
                    sbRadius.setProgress(20);
                } else if (progress < 30 && progress >= 20) {
                    sbRadius.setProgress(20);
                } else if (progress < 40 && progress >= 30) {
                    sbRadius.setProgress(40);
                } else if (progress <= 50 && progress >= 40) {
                    sbRadius.setProgress(40);
                } else if (progress <= 60 && progress >= 50) {
                    sbRadius.setProgress(60);
                } else if (progress <= 70 && progress >= 60) {
                    sbRadius.setProgress(60);
                } else if (progress <= 80 && progress >= 70) {
                    sbRadius.setProgress(80);
                }
                radius = progress + "";
                srlLayout.autoRefresh();
                App.mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onRefresh(srlLayout);
                    }
                }, 800);

            }

            //开始触摸
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            //拖动过程中
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if (progress <= 10 && progress > 0) {
                    tvRadius.setText("距离20Km");
                } else if (progress < 20 && progress > 10) {
                    tvRadius.setText("距离40Km");
                } else if (progress < 30 && progress >= 20) {
                    tvRadius.setText("距离40Km");
                } else if (progress < 40 && progress >= 30) {
                    tvRadius.setText("距离60Km");
                } else if (progress <= 50 && progress >= 40) {
                    tvRadius.setText("距离60Km");
                } else if (progress <= 60 && progress >= 50) {
                    tvRadius.setText("距离80Km");
                } else if (progress <= 70 && progress >= 60) {
                    tvRadius.setText("距离80Km");
                } else if (progress <= 80 && progress >= 70) {
                    tvRadius.setText("距离100Km");
                }
            }
        });
    }

    @Override
    protected void initView(Bundle bundle) {
        context = (BasicActivity) getActivity();
// 创建StaggeredGridLayoutManager实例
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
// 绑定布局管理器
        rvTeam.setLayoutManager(layoutManager);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        rvTeam.setItemAnimator(null);
        teamAdapter = new TeamAdapter(getActivity());
        rvTeam.setAdapter(teamAdapter);
        location();
    }

    private final int FIND_LOCATION = 10001;
    private void location() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                "android.permission.ACCESS_FINE_LOCATION")
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{"android.permission.ACCESS_FINE_LOCATION"},
                    FIND_LOCATION);
        } else {
            page = 1;
            getData();
        }
    }

    private void getData() {
        locationUtil = new LocationUtil(getActivity());
        locationUtil.onceLocation(true);
        locationUtil.setOnLocationListener(new LocationUtil.OnLocationListener() {
            @Override
            public void onLocation(double latitude, double longitude, String cityCode) {
                getTeam(cityCode, StringUtils.double6String(latitude), StringUtils.double6String(longitude) + "", radius);
            }
        });
        locationUtil.startLocation();
    }

    @Override
    public void onResume() {
        super.onResume();
        int creatTeam = SPUtils.getInt(Config.CREATTEAM);
        if (creatTeam == 1) {
            getData();
            SPUtils.putInt(Config.CREATTEAM, 0);
        }
    }

    private void getTeam(String cityCode, String latitude, String longitude, String radius) {
        Http.http.createApi(AuthApi.class).getTeamList(cityCode, latitude, longitude, page + "", radius)
                .compose(context.<JsonResult<List<TeamMoudle.DataBean>>>bindToLifecycle())
                .compose(context.<JsonResult<List<TeamMoudle.DataBean>>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<List<TeamMoudle.DataBean>>() {
                    @Override
                    public void success(List<TeamMoudle.DataBean> response, int code) {
                        Log.d("获取小队: ", response + "");
                        if (page==1){
                            teamAdapter.clear();
                        }
                        if (code == 10001) {
                            teamAdapter.setData(response);
                            page++;
                        } else if (code == 10000) {
                            srlLayout.finishLoadMoreWithNoMoreData();
                            if (page == 1) {
                                llDefault.setVisibility(View.VISIBLE);
                                ivDefault.setImageResource(R.mipmap.default_dynamic);
                                tvDefault.setText("暂无小队信息，快加入小队吧");
                            }
                        } else if (code == 50002) {
                            llDefault.setVisibility(View.VISIBLE);
                            ivDefault.setImageResource(R.mipmap.default_load);
                            tvDefault.setText("哎呀！加载失败");
                        }

                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        ToastUtils.toastCenter(getActivity(), errorMessage + "");
                    }
                }));
    }


    @OnClick({R.id.ivMenu, R.id.ivAdd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivMenu:
                JumpUtils.jumpTeamActivity(getActivity(), 1, "我参与的小队", "");
                break;
            case R.id.ivAdd:
                JumpUtils.jumpTeamActivity(getActivity(), 2, "我参与的小队", "");
                break;
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        getData();
        refreshLayout.finishRefresh();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        getData();
        refreshLayout.finishLoadMore();
    }

    /**
     * @param requestCode  申请码
     * @param permissions  申请的权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FIND_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                page = 1;
                getData();
            } else {
                ToastUtils.toastCenter(getActivity(), "请开启定位权限");
            }
        }
    }
}
