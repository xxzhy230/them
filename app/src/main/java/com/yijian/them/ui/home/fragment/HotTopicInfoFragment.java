package com.yijian.them.ui.home.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.picasso.Picasso;
import com.yijian.them.R;
import com.yijian.them.api.AuthApi;
import com.yijian.them.basic.BasicActivity;
import com.yijian.them.basic.BasicFragment;
import com.yijian.them.ui.home.GroupMoudle;
import com.yijian.them.ui.home.HomeMoudle;
import com.yijian.them.ui.home.adapter.HotTopicAdapter;
import com.yijian.them.ui.home.adapter.TagAdapter;
import com.yijian.them.ui.home.adapter.TuijianAdapter;
import com.yijian.them.utils.dialog.AlertUtils;
import com.yijian.them.utils.http.CallBack;
import com.yijian.them.utils.http.Http;
import com.yijian.them.utils.http.JsonResult;
import com.yijian.them.utils.picasso.PicassoRoundTransform;
import com.yijian.them.view.MyListView;
import com.yqjr.utils.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 话题
 */
public class HotTopicInfoFragment extends BasicFragment implements OnRefreshListener, OnLoadMoreListener {

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
    private TagAdapter adapter;
    private String type = "hot";


    @Override
    protected void onClickEvent() {
        context = (BasicActivity) getActivity();
        Log.d("话题标签 ", topicId);
        topicInfo();
        dynamicTag();
    }


    @Override
    protected View getResourceView() {
        View view = View.inflate(getActivity(), R.layout.activity_hot_topic_info, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView(Bundle bundle) {
        srlLayout.setOnLoadMoreListener(this);
        srlLayout.setOnRefreshListener(this);
        adapter = new TagAdapter();
        dynamicAdapter = new TuijianAdapter();
        mlvTopicDynamic.setAdapter(adapter);
//        lvTopicContent.setAdapter(adapter);
//        lvTopicTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (hotTopicAdapter != null) {
//                    GroupMoudle.DataBean item = hotTopicAdapter.getItem(position);
//                    topicName = item.getTopicName();
//                    if (topicId.equals(item.getTopicId())) {
//                        return;
//                    }
//                    topicId = item.getTopicId();
//                    hotTopicAdapter.setSelectPosition(position);
//                    tag();
//                }
//            }
//        });
//        lvTopicContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                GroupMoudle.DataBean item = adapter.getItem(position);
//                Intent intent = new Intent();
//                intent.putExtra(Config.TOPICID, topicId);
//                intent.putExtra(Config.TOPICNAME, topicName);
//                intent.putExtra(Config.TAGID, item.getTagId());
//                intent.putExtra(Config.TAGNAME, item.getTagName());
//                getActivity().setResult(0, intent);
//                getActivity().finish();
//            }
//        });
        rgTagInfo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbRecommended:
                        type = "hot";
                        page = 1;
                        if (dynamicAdapter!=null){
                            dynamicAdapter.clear();
                        }
                        dynamicTag();
                        break;
                    case R.id.rbNew:
                        type = "new";
                        page = 1;
                        if (dynamicAdapter!=null){
                            dynamicAdapter.clear();
                        }
                        dynamicTag();
                        break;
                    case R.id.rbGroup:
                        break;
                }
            }
        });
    }

    private TuijianAdapter dynamicAdapter;

    private void dynamicTag() {
        AlertUtils.showProgress(false, getActivity());
        Http.http.createApi(AuthApi.class).dynamicTag(topicId, page + "", type)
                .compose(context.<JsonResult<List<HomeMoudle.DataBean>>>bindToLifecycle())
                .compose(context.<JsonResult<List<HomeMoudle.DataBean>>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<List<HomeMoudle.DataBean>>() {
                    @Override
                    public void success(List<HomeMoudle.DataBean> dataBeans, int code) {
                        if (code == 10001) {
                            dynamicAdapter.setData(dataBeans);
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

    private void topicInfo() {
        AlertUtils.showProgress(false, getActivity());
        Http.http.createApi(AuthApi.class).tagInfo(topicId)
                .compose(context.<JsonResult<GroupMoudle.DataBean>>bindToLifecycle())
                .compose(context.<JsonResult<GroupMoudle.DataBean>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<GroupMoudle.DataBean>() {
                    @Override
                    public void success(GroupMoudle.DataBean dataBeans, int code) {
                        String tagName = dataBeans.getTagName();
                        tvTagName.setText(tagName);
                        String tagDesc = dataBeans.getTagDesc();
                        tvTagContent.setText(tagDesc);
                        boolean follow = dataBeans.isFollow();
                        String tagHeat = dataBeans.getTagHeat();
                        if (follow) {
                            tvFollow.setText(tagHeat + "人已参与");
                        } else {
                            tvFollow.setText("一起参与吧" + tagHeat + "人已参与");
                        }
                        String tagUrl = dataBeans.getTagUrl();
                        if (!TextUtils.isEmpty(tagUrl)) {
                            Picasso.with(getActivity()).load(tagUrl).into(ivImageBg);
                            Picasso.with(getActivity()).load(tagUrl).transform(new PicassoRoundTransform()).into(ivHead);
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
        Http.http.createApi(AuthApi.class).tag(topicId, page + "")
                .compose(context.<JsonResult<List<GroupMoudle.DataBean>>>bindToLifecycle())
                .compose(context.<JsonResult<List<GroupMoudle.DataBean>>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<List<GroupMoudle.DataBean>>() {
                    @Override
                    public void success(List<GroupMoudle.DataBean> dataBeans, int code) {
                        if (code == 10000) {
                            if (page == 1) {
//                                llDefault.setVisibility(View.VISIBLE);
//                                ivDefault.setImageResource(R.mipmap.default_dynamic);
//                                tvDefault.setText("暂无数据");
                            }
                            srlLayout.finishLoadMoreWithNoMoreData();
                        } else if (code == 10001) {
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

                break;
        }


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


    public void setTagId(String tagId) {
        this.topicId = tagId;
    }

}
