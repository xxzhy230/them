package com.yijian.them.ui.home.activity;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.picasso.Picasso;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfoResult;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.yijian.them.R;
import com.yijian.them.api.AuthApi;
import com.yijian.them.basic.BasicActivity;
import com.yijian.them.common.Config;
import com.yijian.them.ui.home.HomeMoudle;
import com.yijian.them.ui.home.adapter.HotTopicAdapter;
import com.yijian.them.ui.home.adapter.TopicGroupAdapter;
import com.yijian.them.ui.home.adapter.TuijianAdapter;
import com.yijian.them.ui.login.DataMoudle;
import com.yijian.them.utils.JumpUtils;
import com.yijian.them.utils.dialog.AlertUtils;
import com.yijian.them.utils.dialog.DynamicDialog;
import com.yijian.them.utils.dialog.ImageDialog;
import com.yijian.them.utils.dialog.ReportDialog;
import com.yijian.them.utils.http.CallBack;
import com.yijian.them.utils.http.Http;
import com.yijian.them.utils.http.JsonResult;
import com.yijian.them.utils.http.JsonUtil;
import com.yijian.them.utils.picasso.PicassoRoundTransform;
import com.yijian.them.view.MyListView;
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

/**
 * 话题
 */
public class HotTopicInfoActivity extends BasicActivity implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.ivImageBg)
    ImageView ivImageBg;
    @BindView(R.id.ivHead)
    ImageView ivHead;
    @BindView(R.id.tvTagName)
    TextView tvTagName;
    @BindView(R.id.tvTagContent)
    TextView tvTagContent;
    @BindView(R.id.rbRecommended)
    RadioButton rbRecommended;
    @BindView(R.id.rbNew)
    RadioButton rbNew;
    @BindView(R.id.rbGroup)
    RadioButton rbGroup;
    @BindView(R.id.mlvTopicDynamic)
    MyListView mlvTopicDynamic;
    @BindView(R.id.mlvTopicGroup)
    MyListView mlvTopicGroup;

    @BindView(R.id.srlLayout)
    SmartRefreshLayout srlLayout;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvCreat)
    TextView tvCreat;
    @BindView(R.id.tvFollow)
    TextView tvFollow;
    @BindView(R.id.ivDefault)
    ImageView ivDefault;
    @BindView(R.id.tvDefault)
    TextView tvDefault;
    @BindView(R.id.llDefault)
    LinearLayout llDefault;
    @BindView(R.id.rgTagInfo)
    RadioGroup rgTagInfo;

    private HotTopicAdapter hotTopicAdapter;
    private String topicId;
    private int page = 1;
    private String type = "hot";
    private ImageDialog imageDialog;
    private String loadUrl;
    private boolean follow;
    private String tagHeat;
    private TopicGroupAdapter topicGroupAdapter;
    private String tagName;
    @Override
    public int initView() {
        StatusBarUtil.setStatusBar(this, false, false);
        return R.layout.activity_hot_topic_info;
    }
    @Override
    public void initData() {
        ButterKnife.bind(this);
        topicId = getIntent().getStringExtra(Config.TAGID);
        srlLayout.setOnLoadMoreListener(this);
        srlLayout.setOnRefreshListener(this);
        dynamicAdapter = new TuijianAdapter();
        mlvTopicDynamic.setAdapter(dynamicAdapter);
        rgTagInfo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbRecommended:
                        type = "hot";
                        page = 1;
                        if (dynamicAdapter != null) {
                            dynamicAdapter.clear();
                        }
                        mlvTopicGroup.setVisibility(View.GONE);
                        mlvTopicDynamic.setVisibility(View.VISIBLE);
                        dynamicTag();
                        break;
                    case R.id.rbNew:
                        type = "new";
                        page = 1;
                        if (dynamicAdapter != null) {
                            dynamicAdapter.clear();
                        }
                        mlvTopicGroup.setVisibility(View.GONE);
                        mlvTopicDynamic.setVisibility(View.VISIBLE);
                        dynamicTag();
                        break;
                    case R.id.rbGroup:
                        mlvTopicGroup.setVisibility(View.VISIBLE);
                        mlvTopicDynamic.setVisibility(View.GONE);
                        topicGroupAdapter = new TopicGroupAdapter();
                        mlvTopicGroup.setAdapter(topicGroupAdapter);
                        tagGroups();
                        break;
                }
            }
        });
        topicInfo();
        dynamicTag();
        dynamicAdapter.setOnLikeListener(new TuijianAdapter.OnLikeListener() {
            @Override
            public void like(HomeMoudle.DataBean dataBean) {
                isLike(dataBean);
            }

            @Override
            public void svaeImage(List<String> urls, int position) {
                imageDialog = new ImageDialog(HotTopicInfoActivity.this, urls, position);
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
                    DynamicDialog dynamicDialog = new DynamicDialog(HotTopicInfoActivity.this, 1);
                    dynamicDialog.show();
                    dynamicDialog.setOnClicklistener(new DynamicDialog.OnClicklistener() {
                        @Override
                        public void onClick(int type) {
                            delDynamic(dataBean);
                        }
                    });
                } else {
                    DynamicDialog dynamicDialog = new DynamicDialog(HotTopicInfoActivity.this, 2);
                    dynamicDialog.show();
                    dynamicDialog.setOnClicklistener(new DynamicDialog.OnClicklistener() {
                        @Override
                        public void onClick(int type) {
                            if (type == 1) {
                                JumpUtils.jumpReportActivity(HotTopicInfoActivity.this, dataBean.getDynamicId() + "", 0, "", "");
                            } else if (type == 2) {
                                blackDynamic(dataBean);
                            } else {
                                blackUser(dataBean);
                            }
                        }
                    });
                }
            }
        });
        mlvTopicGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TIMGroupDetailInfoResult item = topicGroupAdapter.getItem(position);
                String groupId = item.getGroupId();
                ChatInfo chatInfo = new ChatInfo();
                chatInfo.setChatName(item.getGroupName());
                chatInfo.setId(groupId);
                chatInfo.setType(TIMConversationType.Group);
                JumpUtils.jumpMessageActivity(HotTopicInfoActivity.this, 0, chatInfo);
            }
        });
    }




    private TuijianAdapter dynamicAdapter;

    private void dynamicTag() {
        AlertUtils.showProgress(false, this);
        OkHttp.get().addHeader("token", SPUtils.getToken()).url(AuthApi.DYNAMICTAG + topicId)
                .addParams("page", page + "")
                .addParams("type", type)
                .build().execute(new StringJsonCallBack() {
            @Override
            public void onError(Call call, Exception e, int i) {
                super.onError(call, e, i);
            }

            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                System.out.println("动态 : " + s);
                AlertUtils.dismissProgress();
                Gson gson = new Gson();
                HomeMoudle homeMoudle = gson.fromJson(s, HomeMoudle.class);
                int code = homeMoudle.getCode();
                if (code == 10001) {
                    llDefault.setVisibility(View.GONE);
                    List<HomeMoudle.DataBean> data = homeMoudle.getData();
                    if (data != null && data.size() > 0) {
                        dynamicAdapter.setData(data);
                        page++;
                    }

                } else if (code == 200) {
                    llDefault.setVisibility(View.GONE);
                    List<HomeMoudle.DataBean> data = homeMoudle.getData();
                    if (data != null && data.size() > 0) {
                        dynamicAdapter.setData(data);
                        page++;
                    }
                } else if (code == 10000) {
                    srlLayout.finishLoadMoreWithNoMoreData();
                    if (page == 1) {
                        llDefault.setVisibility(View.VISIBLE);
                        ivDefault.setImageResource(R.mipmap.default_dynamic);
                        tvDefault.setText("暂无动态，快发个动态吧");
                    }
                } else {
                    llDefault.setVisibility(View.VISIBLE);
                    ivDefault.setImageResource(R.mipmap.default_load);
                    tvDefault.setText("哎呀！加载失败");
                }
            }
        });

    }

    private void topicInfo() {
        AlertUtils.showProgress(false, this);
        OkHttp.get().addHeader("token", SPUtils.getToken()).url(AuthApi.TAG + topicId)
                .build().execute(new StringJsonCallBack() {
            @Override
            public void onError(Call call, Exception e, int i) {
                super.onError(call, e, i);
            }

            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                System.out.println("获取话题详情 : " + s);
                AlertUtils.dismissProgress();
                DataMoudle dataMoudle = JsonUtil.fromJson(s, DataMoudle.class);
                int code = dataMoudle.getCode();
                if (code == 200) {
                    DataMoudle.DataBean data = dataMoudle.getData();
                    tagName = data.getTagName();
                    tvTagName.setText(tagName);
                    String tagDesc = data.getTagDesc();
                    tvTagContent.setText(tagDesc);
                    follow = data.isFollow();
                    tagHeat = data.getTagHeat();
                    if (follow) {
                        tvFollow.setText(tagHeat + "人已参与");
                    } else {
                        tvFollow.setText("一起参与吧 " + tagHeat + "人已参与");
                    }
                    String tagUrl = data.getTagUrl();
                    if (!TextUtils.isEmpty(tagUrl)) {
                        Picasso.with(HotTopicInfoActivity.this).load(tagUrl).into(ivImageBg);
                        Picasso.with(HotTopicInfoActivity.this).load(tagUrl).transform(new PicassoRoundTransform()).into(ivHead);
                    }
                }
            }
        });

    }

    private void tagGroups() {
        Http.http.createApi(AuthApi.class).tagGroups(topicId)
                .compose(this.<JsonResult<List<String>>>bindToLifecycle())
                .compose(this.<JsonResult<List<String>>>applySchedulers())
                .subscribe(this.newSubscriber(new CallBack<List<String>>() {
                    @Override
                    public void success(final List<String> dataBeans, int code) {
                        if (dataBeans != null && dataBeans.size() > 0) {
                            llDefault.setVisibility(View.GONE);
                            TIMGroupManager.getInstance().getGroupInfo(dataBeans, new TIMValueCallBack<List<TIMGroupDetailInfoResult>>() {
                                @Override
                                public void onError(int i, String s) {
                                    llDefault.setVisibility(View.VISIBLE);
                                    tvDefault.setText("暂无群聊数据");
                                    System.out.println("------------:" + s);
                                }

                                @Override
                                public void onSuccess(List<TIMGroupDetailInfoResult> timGroupDetailInfoResults) {
                                    System.out.println("------------:" + timGroupDetailInfoResults.size());
                                    if (timGroupDetailInfoResults != null && timGroupDetailInfoResults.size() > 0) {
                                        llDefault.setVisibility(View.GONE);
                                        topicGroupAdapter.setData(timGroupDetailInfoResults);
                                    } else {
                                        llDefault.setVisibility(View.VISIBLE);
                                        tvDefault.setText("暂无群聊数据");
                                    }
                                }
                            });
                        } else {
                            llDefault.setVisibility(View.VISIBLE);
                            tvDefault.setText("暂无群聊数据");
                            ToastUtils.toastCenter(HotTopicInfoActivity.this, "暂无群聊数据");
                        }
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(HotTopicInfoActivity.this, errorMessage + "");
                    }
                }));
    }


    @OnClick({R.id.ivBack, R.id.tvCreat, R.id.tvFollow, R.id.ivSendDynamic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.tvCreat:
                ReportDialog dialog = new ReportDialog(this, 1);
                dialog.show();
                dialog.setOnClicklistener(new ReportDialog.OnClicklistener() {
                    @Override
                    public void onClick(int type) {
                        JumpUtils.jumpReportActivity(HotTopicInfoActivity.this, topicId, 2, "", "");
                    }
                });
                break;
            case R.id.tvFollow:
                AlertUtils.showProgress(false,this);
                followedTag();
                break;
            case R.id.ivSendDynamic:
                SPUtils.putInt(Config.TOPICSENDDYNAMIC, 1);
                SPUtils.putString(Config.TAGID, topicId);
                SPUtils.putString(Config.TAGNAME, tagName);
                JumpUtils.jumpDynamicActivity(this, 2, "", "");
                break;
        }
    }

    /**
     * 关注tag
     */
    private void followedTag() {
        Http.http.createApi(AuthApi.class).followedTag(topicId)
                .compose(this.<JsonResult<HomeMoudle.DataBean>>bindToLifecycle())
                .compose(this.<JsonResult<HomeMoudle.DataBean>>applySchedulers())
                .subscribe(this.newSubscriber(new CallBack<HomeMoudle.DataBean>() {
                    @Override
                    public void success(HomeMoudle.DataBean response, int code) {
                        AlertUtils.dismissProgress();
                        Log.d("关注: ", response + "");
                        follow = response.isFollow();
                        tagHeat = response.getTagHeat();
                        if (follow) {
                            tvFollow.setText(tagHeat + "人已参与");
                        } else {
                            tvFollow.setText("一起参与吧 " + tagHeat + "人已参与");
                        }
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(HotTopicInfoActivity.this, errorMessage + "");
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
                            dynamicAdapter.delDynamic(dataBean);
                            ToastUtils.toastCenter(HotTopicInfoActivity.this, "删除成功");
                        } else if (code == 50001) {
                            ToastUtils.toastCenter(HotTopicInfoActivity.this, "删除动态不存在");
                        }
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(HotTopicInfoActivity.this, errorMessage + "");
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
        AlertUtils.showProgress(false, HotTopicInfoActivity.this);
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
                            dynamicAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(HotTopicInfoActivity.this, errorMessage + "");
                    }
                }));
    }

    /**
     * 加入黑名单
     *
     * @param dataBean
     */
    private void blackUser(final HomeMoudle.DataBean dataBean) {
        AlertUtils.showProgress(false, HotTopicInfoActivity.this);
        int dynamicId = dataBean.getDynamicId();
        Http.http.createApi(AuthApi.class).blackUser(dynamicId + "")
                .compose(this.<JsonResult<String>>bindToLifecycle())
                .compose(this.<JsonResult<String>>applySchedulers())
                .subscribe(this.newSubscriber(new CallBack<String>() {
                    @Override
                    public void success(String response, int code) {
                        AlertUtils.dismissProgress();
                        Log.d("加入黑名单: ", response + "");
//                        List<HomeMoudle.DataBean> list = adapter.getList();
//                        list.remove(dataBean);
//                        adapter.notifyDataSetChanged();
                        page = 1;
//                        following();
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(HotTopicInfoActivity.this, errorMessage + "");
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
                        List<HomeMoudle.DataBean> list = dynamicAdapter.getList();
                        list.remove(dataBean);
                        dynamicAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(HotTopicInfoActivity.this, errorMessage + "");
                    }
                }));
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        topicInfo();
        refreshLayout.finishRefresh();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        topicInfo();
        refreshLayout.finishLoadMore();
    }


}
