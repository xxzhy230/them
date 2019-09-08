package com.yijian.them.ui.home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yijian.them.R;
import com.yijian.them.api.AuthApi;
import com.yijian.them.basic.BasicActivity;
import com.yijian.them.basic.BasicFragment;
import com.yijian.them.common.Config;
import com.yijian.them.ui.home.GroupMoudle;
import com.yijian.them.ui.home.adapter.HotTopicAdapter;
import com.yijian.them.ui.home.adapter.TagAdapter;
import com.yijian.them.utils.JumpUtils;
import com.yijian.them.utils.dialog.AlertUtils;
import com.yijian.them.utils.http.CallBack;
import com.yijian.them.utils.http.Http;
import com.yijian.them.utils.http.JsonResult;
import com.yqjr.utils.spUtils.SPUtils;
import com.yqjr.utils.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 话题
 */
public class HotTopicFragment extends BasicFragment implements OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.lvTopicTitle)
    ListView lvTopicTitle;
    @BindView(R.id.lvTopicContent)
    ListView lvTopicContent;
    @BindView(R.id.srlLayout)
    SmartRefreshLayout srlLayout;
    @BindView(R.id.ivDefault)
    ImageView ivDefault;
    @BindView(R.id.tvDefault)
    TextView tvDefault;
    @BindView(R.id.llDefault)
    LinearLayout llDefault;
    @BindView(R.id.tvCreat)
    TextView tvCreat;

    private HotTopicAdapter hotTopicAdapter;
    private String topicId;
    private int page = 1;
    private TagAdapter adapter;
    private String topicName;
    private int topicType;// 0 动态选择   1 话题广场
    private int selectPosition = 0;

    @Override
    protected void onClickEvent() {
        context = (BasicActivity) getActivity();
        topicType = SPUtils.getInt(Config.TOPICTYPE);
    }

    @Override
    public void onResume() {
        super.onResume();
        page = 1;
        if (adapter != null) {
            adapter.clear();
        }
        topic();
    }

    @Override
    protected View getResourceView() {
        View view = View.inflate(getActivity(), R.layout.fragment_hot_topic, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView(Bundle bundle) {
        srlLayout.setOnLoadMoreListener(this);
        srlLayout.setOnRefreshListener(this);
        adapter = new TagAdapter();
        lvTopicContent.setAdapter(adapter);
        lvTopicTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectPosition = position;
                if (hotTopicAdapter != null) {
                    GroupMoudle.DataBean item = hotTopicAdapter.getItem(position);
                    topicName = item.getTopicName();
                    if (topicId.equals(item.getTopicId())) {
                        return;
                    }
                    topicId = item.getTopicId();
                    hotTopicAdapter.setSelectPosition(position);
                    page = 1;
                    if (adapter != null) {
                        adapter.clear();
                    }
                    if (position == 0) {
                        followList();
                    } else {
                        tag();
                    }

                }
            }
        });
        lvTopicContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GroupMoudle.DataBean item = adapter.getItem(position);
                if (topicType == 0) {
                    Intent intent = new Intent();
                    intent.putExtra(Config.TOPICID, topicId);
                    intent.putExtra(Config.TOPICNAME, topicName);
                    intent.putExtra(Config.TAGID, item.getTagId());
                    intent.putExtra(Config.TAGNAME, item.getTagName());
                    getActivity().setResult(0, intent);
                    getActivity().finish();
                } else {
                    JumpUtils.jumpHotTopicInfoActivity(getActivity(), item.getTagId());
                }

            }
        });

    }

    private void followList() {
        Http.http.createApi(AuthApi.class).followList(page + "")
                .compose(context.<JsonResult<List<GroupMoudle.DataBean>>>bindToLifecycle())
                .compose(context.<JsonResult<List<GroupMoudle.DataBean>>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<List<GroupMoudle.DataBean>>() {
                    @Override
                    public void success(List<GroupMoudle.DataBean> dataBeans, int code) {
                        if (code == 10000) {
                            if (page == 1) {
                                llDefault.setVisibility(View.VISIBLE);
                                ivDefault.setImageResource(R.mipmap.default_dynamic);
                                tvDefault.setText("暂无数据");
                            }
                            srlLayout.finishLoadMoreWithNoMoreData();
                        } else if (code == 10001) {
                            llDefault.setVisibility(View.GONE);
                            page++;
                            adapter.setDataBeans(dataBeans);
                        }
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(getActivity(), errorMessage + "");
                    }
                }));
    }

    private void topic() {
        AlertUtils.showProgress(false, getActivity());
        Http.http.createApi(AuthApi.class).topic()
                .compose(context.<JsonResult<List<GroupMoudle.DataBean>>>bindToLifecycle())
                .compose(context.<JsonResult<List<GroupMoudle.DataBean>>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<List<GroupMoudle.DataBean>>() {
                    @Override
                    public void success(List<GroupMoudle.DataBean> dataBeans, int code) {
                        if (dataBeans != null && dataBeans.size() > 0) {
                            hotTopicAdapter = new HotTopicAdapter(dataBeans);
                            lvTopicTitle.setAdapter(hotTopicAdapter);
                            followList();
                        } else {
                            AlertUtils.dismissProgress();
                        }
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(getActivity(), errorMessage + "");
                    }
                }));
    }

    private void tag() {
        Http.http.createApi(AuthApi.class).tag(page + "", topicId)
                .compose(context.<JsonResult<List<GroupMoudle.DataBean>>>bindToLifecycle())
                .compose(context.<JsonResult<List<GroupMoudle.DataBean>>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<List<GroupMoudle.DataBean>>() {
                    @Override
                    public void success(List<GroupMoudle.DataBean> dataBeans, int code) {
                        if (code == 10000) {
                            if (page == 1) {
                                llDefault.setVisibility(View.VISIBLE);
                                ivDefault.setImageResource(R.mipmap.default_dynamic);
                                tvDefault.setText("暂无数据");
                            }
                            srlLayout.finishLoadMoreWithNoMoreData();
                        } else if (code == 10001) {
                            llDefault.setVisibility(View.GONE);
                            page++;
                            adapter.setDataBeans(dataBeans);
                        }
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(getActivity(), errorMessage + "");
                    }
                }));
    }


    @OnClick({R.id.ivBack, R.id.tvCreat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                getActivity().finish();
                break;
            case R.id.tvCreat:
                JumpUtils.jumpDynamicActivity(getActivity(), 10, "", "");
                break;
        }


    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        if (selectPosition == 0) {
            followList();
        } else {
            tag();
        }
        refreshLayout.finishRefresh();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (selectPosition == 0) {
            followList();
        } else {
            tag();
        }
        refreshLayout.finishLoadMore();
    }


}
