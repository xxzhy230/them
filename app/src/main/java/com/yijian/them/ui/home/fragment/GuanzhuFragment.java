package com.yijian.them.ui.home.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.yijian.them.common.Config;
import com.yijian.them.ui.home.HomeMoudle;
import com.yijian.them.ui.home.adapter.RandomTagAdapter;
import com.yijian.them.ui.home.adapter.TuijianAdapter;
import com.yijian.them.ui.login.DataMoudle;
import com.yijian.them.utils.JumpUtils;
import com.yijian.them.utils.dialog.AlertUtils;
import com.yijian.them.utils.dialog.DynamicDialog;
import com.yijian.them.utils.dialog.ImageDialog;
import com.yijian.them.utils.http.CallBack;
import com.yijian.them.utils.http.Http;
import com.yijian.them.utils.http.JsonResult;
import com.yijian.them.view.MyListView;
import com.yqjr.utils.spUtils.SPUtils;
import com.yqjr.utils.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuanzhuFragment extends BasicFragment implements OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.tvTopic)
    TextView tvTopic;
    @BindView(R.id.gvTag)
    GridView gvTag;
    @BindView(R.id.tvTagRandom)
    TextView tvTagRandom;
    @BindView(R.id.srlLayout)
    SmartRefreshLayout srlLayout;
    @BindView(R.id.lvHomeTuijian)
    MyListView lvHomeTuijian;
    @BindView(R.id.ivDefault)
    ImageView ivDefault;
    @BindView(R.id.tvDefault)
    TextView tvDefault;
    @BindView(R.id.llDefault)
    LinearLayout llDefault;
    private int page = 1;
    private TuijianAdapter adapter;
    private ImageDialog imageDialog;
    private String loadUrl;
    private RandomTagAdapter randomTagAdapter;


    @Override
    protected View getResourceView() {
        View rootView = View.inflate(getActivity(), R.layout.fragment_guanzhu, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onClickEvent() {
        random();
        adapter = new TuijianAdapter();
        lvHomeTuijian.setAdapter(adapter);
        following();
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
                        loadUrl = url;
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
                            if (type == 1) {
                                JumpUtils.jumpReportActivity(getActivity(), dataBean.getDynamicId() + "",0,"","");
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
                teamOutOrAdd(teamd, teamName);
            }
        });
        gvTag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HomeMoudle.DataBean item = randomTagAdapter.getItem(position);
                String tagDesc = item.getTagDesc();
                String tagName = item.getTagName();
                String tagId = item.getTagId();
                JumpUtils.jumpHotTopicInfoActivity(getActivity(),tagId);
            }
        });
    }


    @Override
    protected void initView(Bundle bundle) {
        context = (BasicActivity) getActivity();
        srlLayout.setOnRefreshListener(this);
        srlLayout.setOnLoadMoreListener(this);
    }


    @OnClick({R.id.tvTagRandom,R.id.tvTopic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvTagRandom:
                random();
                break;
            case R.id.tvTopic:
                SPUtils.putInt(Config.TOPICTYPE,1);
                JumpUtils.jumpDynamicActivity(this,4,"话题广场","");
                break;
        }
    }

    private void random() {
        Http.http.createApi(AuthApi.class).random()
                .compose(context.<JsonResult<List<HomeMoudle.DataBean>>>bindToLifecycle())
                .compose(context.<JsonResult<List<HomeMoudle.DataBean>>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<List<HomeMoudle.DataBean>>() {
                    @Override
                    public void success(List<HomeMoudle.DataBean> dataBeans, int code) {
                        AlertUtils.dismissProgress();
                        randomTagAdapter = new RandomTagAdapter(dataBeans);
                        gvTag.setAdapter(randomTagAdapter);
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(getActivity(), errorMessage + "");
                    }
                }));
    }
    private void following() {
        AlertUtils.showProgress(false, getActivity());
        Http.http.createApi(AuthApi.class).following(page+"")
                .compose(context.<JsonResult<List<HomeMoudle.DataBean>>>bindToLifecycle())
                .compose(context.<JsonResult<List<HomeMoudle.DataBean>>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<List<HomeMoudle.DataBean>>() {
                    @Override
                    public void success(List<HomeMoudle.DataBean> response, int code) {
                        AlertUtils.dismissProgress();
                        Log.d("获取推荐信息: ", response + "");
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
     * 加入黑名单
     *
     * @param dataBean
     */
    private void blackUser(final HomeMoudle.DataBean dataBean) {
        AlertUtils.showProgress(false, getActivity());
        int userId = dataBean.getUserBriefVo().getUserId();
        Http.http.createApi(AuthApi.class).blackUser(userId + "")
                .compose(context.<JsonResult<String>>bindToLifecycle())
                .compose(context.<JsonResult<String>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<String>() {
                    @Override
                    public void success(String response, int code) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(getActivity(),"加入黑名单成功");
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(getActivity(), errorMessage + "");
                    }
                }));
    }

    /**
     * 屏蔽动态
     *
     * @param dataBean
     */
    private void blackDynamic(final HomeMoudle.DataBean dataBean) {
        AlertUtils.showProgress(false, getActivity());
        int dynamicId = dataBean.getDynamicId();
        Http.http.createApi(AuthApi.class).blackDynamic(dynamicId + "")
                .compose(context.<JsonResult<String>>bindToLifecycle())
                .compose(context.<JsonResult<String>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<String>() {
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
                        ToastUtils.toastCenter(getActivity(), errorMessage + "");
                    }
                }));
    }
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        adapter.clear();
        following();
        refreshLayout.finishRefresh();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        following();
        refreshLayout.finishLoadMore();
    }

    private void teamOutOrAdd(final String teamId, final String teamName) {
        Http.http.createApi(AuthApi.class).teamOutOrAdd(teamId.replace("team:teamId:", ""), "1")
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
