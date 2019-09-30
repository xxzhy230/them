package com.yijian.them.ui.home.fragment;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.yijian.them.R;
import com.yijian.them.api.AuthApi;
import com.yijian.them.basic.BasicActivity;
import com.yijian.them.basic.BasicFragment;
import com.yijian.them.common.App;
import com.yijian.them.common.Config;
import com.yijian.them.ui.home.HomeMoudle;
import com.yijian.them.ui.home.adapter.TuijianAdapter;
import com.yijian.them.ui.login.DataMoudle;
import com.yijian.them.utils.JumpUtils;
import com.yijian.them.utils.StringUtils;
import com.yijian.them.utils.dialog.AlertUtils;
import com.yijian.them.utils.dialog.DynamicDialog;
import com.yijian.them.utils.dialog.ImageDialog;
import com.yijian.them.utils.http.CallBack;
import com.yijian.them.utils.http.Http;
import com.yijian.them.utils.http.JsonResult;
import com.yijian.them.utils.location.LocationUtil;
import com.yqjr.utils.spUtils.SPUtils;
import com.yqjr.utils.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NearbyFragment extends BasicFragment implements OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.lvHomeTuijian)
    ListView lvHomeTuijian;
    @BindView(R.id.ivDefault)
    ImageView ivDefault;
    @BindView(R.id.tvDefault)
    TextView tvDefault;
    @BindView(R.id.llDefault)
    LinearLayout llDefault;
    @BindView(R.id.srlLayout)
    SmartRefreshLayout srlLayout;
    private int page = 1;
    private TuijianAdapter adapter;
    private ImageDialog imageDialog;

    @Override
    protected void onClickEvent() {
    }

    @Override
    protected View getResourceView() {
        View rootView = View.inflate(getActivity(), R.layout.fragment_nearby, null);
        ButterKnife.bind(this, rootView);
        return rootView;
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
            getData();
        }
    }

    @Override
    protected void initView(Bundle bundle) {
        context = (BasicActivity) getActivity();
        adapter = new TuijianAdapter();
        adapter.setType(1);
        lvHomeTuijian.setAdapter(adapter);
        location();
    }

    private void getData() {
        App.locationUtil.onceLocation(true);
        App.locationUtil.startLocation();
        App.locationUtil.setOnLocationListener(new LocationUtil.OnLocationListener() {
            @Override
            public void onLocation(double latitude, double longitude, String cityCode) {
                if (TextUtils.isEmpty(cityCode)) {
                    llDefault.setVisibility(View.VISIBLE);
                    tvDefault.setText("请开启定位权限");
                    ivDefault.setImageResource(R.mipmap.default_load);
                } else {
                    llDefault.setVisibility(View.GONE);
                    nearby(cityCode, StringUtils.double6String(latitude), StringUtils.double6String(longitude));
                }
            }
        });
        adapter.setOnLikeListener(new TuijianAdapter.OnLikeListener() {
            @Override
            public void like(HomeMoudle.DataBean dataBean) {
                isLike(dataBean);
            }

            @Override
            public void svaeImage(List<String> urls, int position) {
                imageDialog = new ImageDialog(getActivity(), urls, position);
                imageDialog.show();
                imageDialog.setOnSaveImageListener(new ImageDialog.OnSaveImageListener() {
                    @Override
                    public void onSaveImage(String url) {
                        imageDialog.saveImage(url);
                    }
                });
            }

            @Override
            public void clickMore(final HomeMoudle.DataBean dataBean) {
                HomeMoudle.DataBean.UserBriefVoBean userBriefVo = dataBean.getUserBriefVo();
                int userId = userBriefVo.getUserId();
                if (SPUtils.getInt(Config.USERID) == userId) {
                    DynamicDialog dynamicDialog = new DynamicDialog(getActivity(), 1);
                    dynamicDialog.show();
                    dynamicDialog.setOnClicklistener(new DynamicDialog.OnClicklistener() {
                        @Override
                        public void onClick(int type) {
                            delDynamic(dataBean);
                        }
                    });
                } else {
                    DynamicDialog dynamicDialog = new DynamicDialog(getActivity(), 2);
                    dynamicDialog.show();
                    dynamicDialog.setOnClicklistener(new DynamicDialog.OnClicklistener() {
                        @Override
                        public void onClick(int type) {
                            System.out.println("------2----- : " + type);
                        }
                    });
                }

            }

            @Override
            public void joinTeam(String teamd, String teamName) {
                teamOutOrAdd(teamd, teamName);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        page = 1;
        getData();
    }

    private void nearby(String cityCode, String latitude, String longitude) {
        AlertUtils.showProgress(false, getActivity());

        Http.http.createApi(AuthApi.class).nearby(cityCode, latitude, longitude, page, "10")
                .compose(context.<JsonResult<List<HomeMoudle.DataBean>>>bindToLifecycle())
                .compose(context.<JsonResult<List<HomeMoudle.DataBean>>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<List<HomeMoudle.DataBean>>() {
                    @Override
                    public void success(List<HomeMoudle.DataBean> response, int code) {
                        AlertUtils.dismissProgress();
                        Log.d("获取附近信息: ", response + "");
                        if (code == 10001) {
                            adapter.setData(response);
                            page++;
                        } else if (code == 10000) {
                            srlLayout.finishLoadMoreWithNoMoreData();
                            if (page == 1) {
                                llDefault.setVisibility(View.VISIBLE);
                                ivDefault.setImageResource(R.mipmap.default_dynamic);
                                tvDefault.setText("暂无动态，快发个动态吧");
                            }
                        } else if (code == 50002) {
                            llDefault.setVisibility(View.VISIBLE);
                            ivDefault.setImageResource(R.mipmap.default_load);
                            tvDefault.setText("哎呀！加载失败");
                        }
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(getActivity(), errorMessage + "");
                    }
                }));
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        App.locationUtil.onceLocation(true);
        App.locationUtil.startLocation();
        App.locationUtil.setOnLocationListener(new LocationUtil.OnLocationListener() {
            @Override
            public void onLocation(double latitude, double longitude, String cityCode) {
                nearby(cityCode, StringUtils.double6String(latitude), StringUtils.double6String(longitude));
            }
        });
        refreshLayout.finishRefresh();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        App.locationUtil.onceLocation(true);
        App.locationUtil.startLocation();
        App.locationUtil.setOnLocationListener(new LocationUtil.OnLocationListener() {
            @Override
            public void onLocation(double latitude, double longitude, String cityCode) {
                nearby(cityCode, StringUtils.double6String(latitude), StringUtils.double6String(longitude));
            }
        });
        refreshLayout.finishLoadMore();
    }

    /**
     * 删除动态
     *
     * @param dataBean
     */
    private void delDynamic(final HomeMoudle.DataBean dataBean) {
        int dynamicId = dataBean.getDynamicId();
        Http.http.createApi(AuthApi.class).delDynamic(dynamicId + "")
                .compose(context.<JsonResult<String>>bindToLifecycle())
                .compose(context.<JsonResult<String>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<String>() {
                    @Override
                    public void success(String response, int code) {
                        AlertUtils.dismissProgress();
                        Log.d("删除动态: ", response + "");
                        if (code == 200) {
                            adapter.delDynamic(dataBean);
                            ToastUtils.toastCenter(getActivity(), "删除成功");
                        } else if (code == 50001) {
                            ToastUtils.toastCenter(getActivity(), "删除动态不存在");
                        }
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(getActivity(), errorMessage + "");
                    }
                }));
    }

    private void isLike(final HomeMoudle.DataBean dataBean) {
        int dynamicId = dataBean.getDynamicId();
        HomeMoudle.DataBean.UserBriefVoBean userBriefVo = dataBean.getUserBriefVo();
        int userId = userBriefVo.getUserId();
        Map<String, String> map = new HashMap<>();
        map.put("dynamicId", dynamicId + "");
        map.put("userId", userId + "");
        AlertUtils.showProgress(false, getActivity());
        Http.http.createApi(AuthApi.class).like(map)
                .compose(context.<JsonResult<DataMoudle.DataBean>>bindToLifecycle())
                .compose(context.<JsonResult<DataMoudle.DataBean>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<DataMoudle.DataBean>() {
                    @Override
                    public void success(DataMoudle.DataBean response, int code) {
                        AlertUtils.dismissProgress();
                        Log.d("点赞: ", response + "");
                        if (code == 200) {
                            int isLike = response.getIsLike();
                            dataBean.setIsLike(isLike);
                            int likeCount = response.getLikeCount();
                            dataBean.setLikeCount(likeCount);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(getActivity(), errorMessage + "");
                    }
                }));
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
                getData();
            } else {
                ToastUtils.toastCenter(getActivity(), "请开启定位权限");
            }
        }
    }
    private void teamOutOrAdd(final String teamId, final String teamName) {
        Http.http.createApi(AuthApi.class).teamOutOrAdd(teamId, "1")
                .compose(context.<JsonResult<String>>bindToLifecycle())
                .compose(context.<JsonResult<String>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<String>() {
                    @Override
                    public void success(String str, int code) {
                        AlertUtils.dismissProgress();
                        ChatInfo chatInfo = new ChatInfo();
                        chatInfo.setId(teamId);
                        chatInfo.setChatName(teamName);
                        chatInfo.setType(TIMConversationType.Group);
                        JumpUtils.jumpMessageActivity(getActivity(), 0, chatInfo);
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(getActivity(), errorMessage + "");
                    }
                }));
    }
}
