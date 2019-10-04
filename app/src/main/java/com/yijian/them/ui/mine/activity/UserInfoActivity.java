package com.yijian.them.ui.mine.activity;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.picasso.Picasso;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.yijian.them.R;
import com.yijian.them.api.AuthApi;
import com.yijian.them.basic.BasicActivity;
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
import com.yijian.them.view.CircleImageView;
import com.yqjr.utils.service.OkHttp;
import com.yqjr.utils.service.StringJsonCallBack;
import com.yqjr.utils.spUtils.SPUtils;
import com.yqjr.utils.utils.StatusBarUtil;
import com.yqjr.utils.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class UserInfoActivity extends BasicActivity implements OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.ivImageBg)
    ImageView ivImageBg;
    @BindView(R.id.civHead)
    CircleImageView civHead;
    @BindView(R.id.tvNickName)
    TextView tvNickName;
    @BindView(R.id.tvRemark)
    TextView tvRemark;
    @BindView(R.id.tvZan)
    TextView tvZan;
    @BindView(R.id.llZan)
    LinearLayout llZan;
    @BindView(R.id.tvGuanzhu)
    TextView tvGuanzhu;
    @BindView(R.id.llGuanzhu)
    LinearLayout llGuanzhu;
    @BindView(R.id.tvFensi)
    TextView tvFensi;
    @BindView(R.id.llFensi)
    LinearLayout llFensi;
    @BindView(R.id.tvDtNum)
    TextView tvDtNum;
    @BindView(R.id.lvDongtai)
    ListView lvDongtai;
    @BindView(R.id.ivDefault)
    ImageView ivDefault;
    @BindView(R.id.tvDefault)
    TextView tvDefault;
    @BindView(R.id.llDefault)
    LinearLayout llDefault;
    @BindView(R.id.srlLayout)
    SmartRefreshLayout srlLayout;
    @BindView(R.id.ivSex)
    ImageView ivSex;
    @BindView(R.id.tvAge)
    TextView tvAge;
    private int page = 1;
    private int userId;
    private TuijianAdapter adapter;
    private ImageDialog imageDialog;

    @Override
    public int initView() {
        StatusBarUtil.setStatusBar(this, false, false);
        return R.layout.activity_user_info;

    }

    @Override
    public void initData() {
        ButterKnife.bind(this);
        srlLayout.setOnRefreshListener(this);
        srlLayout.setOnLoadMoreListener(this);
        userId = getIntent().getIntExtra(Config.USERID, 0);
        adapter = new TuijianAdapter();
        lvDongtai.setAdapter(adapter);
        user();
        dynamicInfo();
        getStatistics();
        adapter.setOnLikeListener(new TuijianAdapter.OnLikeListener() {
            @Override
            public void like(HomeMoudle.DataBean dataBean) {
                isLike(dataBean);
            }

            @Override
            public void svaeImage(List<String> urls, int position) {
                imageDialog = new ImageDialog(UserInfoActivity.this, urls, position);
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
                    DynamicDialog dynamicDialog = new DynamicDialog(UserInfoActivity.this, 1);
                    dynamicDialog.show();
                    dynamicDialog.setOnClicklistener(new DynamicDialog.OnClicklistener() {
                        @Override
                        public void onClick(int type) {
                            delDynamic(dataBean);
                        }
                    });
                } else {
                    DynamicDialog dynamicDialog = new DynamicDialog(UserInfoActivity.this, 2);
                    dynamicDialog.show();
                    dynamicDialog.setOnClicklistener(new DynamicDialog.OnClicklistener() {
                        @Override
                        public void onClick(int type) {
                            if (type == 1) {
                                JumpUtils.jumpReportActivity(UserInfoActivity.this, dataBean.getDynamicId() + "", 0, "", "");
                            } else if (type == 2) {
                                blackDynamic(dataBean);
                            } else {
                                blackUser(dataBean);
                            }
                        }
                    });
                }
            }

            @Override
            public void joinTeam(String teamd, String teamName) {
                teamOutOrAdd(teamd,teamName);
            }
        });
    }

    @OnClick({R.id.tvTitleBar, R.id.llGuanzhu, R.id.llFensi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvTitleBar:
                finish();
                break;
            case R.id.llGuanzhu:
                JumpUtils.jumpFollowerActivity(this, 8, "我的关注", userId);
                break;
            case R.id.llFensi:
                JumpUtils.jumpFollowerActivity(this, 9, "我的粉丝", userId);
                break;
        }

    }

    /**
     * 个人动态
     */
    private void dynamicInfo() {
        OkHttp.get().addHeader("token", SPUtils.getToken()).url(AuthApi.USERINFO + userId)
                .addParams("page", page + "")
                .build().execute(new StringJsonCallBack() {
            @Override
            public void onError(Call call, Exception e, int i) {
                super.onError(call, e, i);
            }

            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                System.out.println("个人信息 : " + s);
                Gson gson = new Gson();
                HomeMoudle homeMoudle = gson.fromJson(s, HomeMoudle.class);
                int code = homeMoudle.getCode();
                if (code == 10001) {
                    page++;
                    List<HomeMoudle.DataBean> data = homeMoudle.getData();
                    if (data != null && data.size() > 0) {
                        llDefault.setVisibility(View.GONE);
                        tvDtNum.setText(data.size() + "");
                        adapter.setData(data);
                    } else {
                        llDefault.setVisibility(View.VISIBLE);
                        tvDefault.setText("暂无动态");
                    }
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
        });
    }

    /**
     * 获取用户信息
     */
    private void user() {
        OkHttp.get().addHeader("token", SPUtils.getToken()).url(AuthApi.USER + userId)
                .build().execute(new StringJsonCallBack() {
            @Override
            public void onError(Call call, Exception e, int i) {
                super.onError(call, e, i);
            }

            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                System.out.println("个人信息 : " + s);
                Gson gson = new Gson();
                DataMoudle dataMoudle = gson.fromJson(s, DataMoudle.class);
                int code = dataMoudle.getCode();
                if (code == 200) {
                    DataMoudle.DataBean data = dataMoudle.getData();
                    String nickName = data.getNickName();
                    String realImg = data.getRealImg();
                    String sign = data.getSign();
                    Picasso.with(UserInfoActivity.this).load(realImg).into(ivImageBg);
                    Picasso.with(UserInfoActivity.this).load(realImg).into(civHead);
                    tvNickName.setText(nickName);
                    tvRemark.setText(sign);
                    String birthday = data.getBirthday();
                    String age = StringUtils.getAge(birthday);
                    tvAge.setText(age);
                    String gender = data.getGender();
                    if ("1".equals(gender)) {
                        ivSex.setImageResource(R.mipmap.register_icon_man_selected);
                    } else {
                        ivSex.setImageResource(R.mipmap.register_icon_woman_selected);
                    }
                }
            }
        });

    }

    private void getStatistics() {
        OkHttp.get().addHeader("token", SPUtils.getToken()).url(AuthApi.STATISTICS)
                .addParams("othersId", userId + "")
                .build().execute(new StringJsonCallBack() {
            @Override
            public void onError(Call call, Exception e, int i) {
                super.onError(call, e, i);
            }

            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                System.out.println("个人信息 : " + s);
                Gson gson = new Gson();
                DataMoudle dataMoudle = gson.fromJson(s, DataMoudle.class);
                int code = dataMoudle.getCode();
                if (code == 200) {
                    DataMoudle.DataBean data = dataMoudle.getData();
                    int fansCount = data.getFansCount();
                    int followingCount = data.getFollowingCount();
                    int likeCount = data.getLikeCount();

                    tvFensi.setText(fansCount + "");
                    tvGuanzhu.setText(followingCount + "");
                    tvZan.setText(likeCount + "");
                }
            }
        });

    }

    /**
     * 加入黑名单
     *
     * @param dataBean
     */
    private void blackUser(final HomeMoudle.DataBean dataBean) {
        AlertUtils.showProgress(false, UserInfoActivity.this);
        int userId = dataBean.getUserBriefVo().getUserId();
        Http.http.createApi(AuthApi.class).blackUser(userId + "")
                .compose(this.<JsonResult<String>>bindToLifecycle())
                .compose(this.<JsonResult<String>>applySchedulers())
                .subscribe(this.newSubscriber(new CallBack<String>() {
                    @Override
                    public void success(String response, int code) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(UserInfoActivity.this,"加入黑名单成功");
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(UserInfoActivity.this, errorMessage + "");
                    }
                }));
    }

    /**
     * 屏蔽动态
     *
     * @param dataBean
     */
    private void blackDynamic(final HomeMoudle.DataBean dataBean) {
        AlertUtils.showProgress(false, this);
        int dynamicId = dataBean.getDynamicId();
        Http.http.createApi(AuthApi.class).blackDynamic(dynamicId + "")
                .compose(this.<JsonResult<String>>bindToLifecycle())
                .compose(this.<JsonResult<String>>applySchedulers())
                .subscribe(this.newSubscriber(new CallBack<String>() {
                    @Override
                    public void success(String response, int code) {
                        AlertUtils.dismissProgress();
                        Log.d("屏蔽动态: ", response + "");
                        List<HomeMoudle.DataBean> list = adapter.getList();
                        list.remove(dataBean);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(UserInfoActivity.this, errorMessage + "");
                    }
                }));
    }

    /**
     * 删除动态
     *
     * @param dataBean
     */
    private void delDynamic(final HomeMoudle.DataBean dataBean) {
        int dynamicId = dataBean.getDynamicId();
        Http.http.createApi(AuthApi.class).delDynamic(dynamicId + "")
                .compose(this.<JsonResult<String>>bindToLifecycle())
                .compose(this.<JsonResult<String>>applySchedulers())
                .subscribe(this.newSubscriber(new CallBack<String>() {
                    @Override
                    public void success(String response, int code) {
                        AlertUtils.dismissProgress();
                        Log.d("删除动态: ", response + "");
                        if (code == 200) {
                            adapter.delDynamic(dataBean);
                            ToastUtils.toastCenter(UserInfoActivity.this, "删除成功");
                        } else if (code == 50001) {
                            ToastUtils.toastCenter(UserInfoActivity.this, "删除动态不存在");
                        }
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(UserInfoActivity.this, errorMessage + "");
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
        AlertUtils.showProgress(false, this);
        Http.http.createApi(AuthApi.class).like(map)
                .compose(this.<JsonResult<DataMoudle.DataBean>>bindToLifecycle())
                .compose(this.<JsonResult<DataMoudle.DataBean>>applySchedulers())
                .subscribe(this.newSubscriber(new CallBack<DataMoudle.DataBean>() {
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
                        ToastUtils.toastCenter(UserInfoActivity.this, errorMessage + "");
                    }
                }));
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        dynamicInfo();
        refreshLayout.finishLoadMore();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        adapter.clear();
        dynamicInfo();
        refreshLayout.finishRefresh();
    }

    private void teamOutOrAdd(final String teamId, final String teamName) {
        Http.http.createApi(AuthApi.class).teamOutOrAdd(teamId.replace("team:teamId:", ""), "1")
                .compose(this.<JsonResult<String>>bindToLifecycle())
                .compose(this.<JsonResult<String>>applySchedulers())
                .subscribe(this.newSubscriber(new CallBack<String>() {
                    @Override
                    public void success(String str, int code) {
                        AlertUtils.dismissProgress();
                        ChatInfo chatInfo = new ChatInfo();
                        chatInfo.setId(teamId);
                        chatInfo.setChatName(teamName);
                        chatInfo.setType(TIMConversationType.Group);
                        JumpUtils.jumpMessageActivity(UserInfoActivity.this, 0, chatInfo);
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(UserInfoActivity.this, errorMessage + "");
                    }
                }));
    }
}
