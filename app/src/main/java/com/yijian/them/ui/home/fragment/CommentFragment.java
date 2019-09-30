package com.yijian.them.ui.home.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcssloop.widget.RCRelativeLayout;
import com.squareup.picasso.Picasso;
import com.yijian.them.R;
import com.yijian.them.api.AuthApi;
import com.yijian.them.basic.BasicActivity;
import com.yijian.them.basic.BasicFragment;
import com.yijian.them.common.Config;
import com.yijian.them.ui.home.HomeMoudle;
import com.yijian.them.ui.home.activity.PlayPickActivity;
import com.yijian.them.ui.home.adapter.CommentListAdapter;
import com.yijian.them.ui.home.adapter.ImageAdapter;
import com.yijian.them.ui.login.DataMoudle;
import com.yijian.them.utils.JumpUtils;
import com.yijian.them.utils.SoftKeyBoardListener;
import com.yijian.them.utils.StringUtils;
import com.yijian.them.utils.Times;
import com.yijian.them.utils.dialog.AlertUtils;
import com.yijian.them.utils.dialog.DynamicDialog;
import com.yijian.them.utils.dialog.ImageDialog;
import com.yijian.them.utils.http.CallBack;
import com.yijian.them.utils.http.Http;
import com.yijian.them.utils.http.JsonResult;
import com.yijian.them.view.CircleImageView;
import com.yijian.them.view.MyListView;
import com.yijian.them.view.MyScrollView;
import com.yijian.them.view.NoScrollGridView;
import com.yqjr.utils.spUtils.SPUtils;
import com.yqjr.utils.utils.ForbidEditUtils;
import com.yqjr.utils.utils.ToastUtils;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommentFragment extends BasicFragment {
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.civHead)
    CircleImageView civHead;
    @BindView(R.id.tvNickName)
    TextView tvNickName;
    @BindView(R.id.ivMore)
    ImageView ivMore;
    @BindView(R.id.civHead1)
    CircleImageView civHead1;
    @BindView(R.id.tvNickName1)
    TextView tvNickName1;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvZan)
    TextView tvZan;
    @BindView(R.id.tvContent)
    TextView tvContent;
    @BindView(R.id.tvTag)
    TextView tvTag;
    @BindView(R.id.nsgvImage)
    NoScrollGridView nsgvImage;
    @BindView(R.id.rcrlImage)
    RCRelativeLayout rcrlImage;
    @BindView(R.id.tvGroup)
    TextView tvGroup;
    @BindView(R.id.mlvComment)
    MyListView mlvComment;
    @BindView(R.id.etComment)
    EditText etComment;
    @BindView(R.id.tvSend)
    TextView tvSend;
    @BindView(R.id.llComment)
    LinearLayout llComment;
    @BindView(R.id.msvComment)
    MyScrollView msvComment;
    @BindView(R.id.ivDefault)
    ImageView ivDefault;
    @BindView(R.id.tvDefault)
    TextView tvDefault;
    @BindView(R.id.llDefault)
    LinearLayout llDefault;

    private String dynamicId;
    private int userId;
    private CommentListAdapter adapter;
    private HomeMoudle.DataBean commentData;
    private ImageDialog imageDialog;

    @Override
    protected void onClickEvent() {
        context = (BasicActivity) getActivity();
        getDynamic();
    }

    @Override
    protected View getResourceView() {
        View view = View.inflate(getActivity(), R.layout.fragment_comment, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView(Bundle bundle) {
        SoftKeyBoardListener.setListener(getActivity(), new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                StringUtils.setMargins(llComment, 0, 0, 0, height);
            }

            @Override
            public void keyBoardHide(int height) {
                StringUtils.setMargins(llComment, 0, 0, 0, 0);
            }
        });
        etComment.setInputType(InputType.TYPE_NULL);
        etComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etComment.setInputType(InputType.TYPE_CLASS_TEXT);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etComment, InputMethodManager.RESULT_SHOWN);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

            }
        });
        msvComment.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                if (scrollY > 100) {
                    civHead.setVisibility(View.VISIBLE);
                    tvNickName.setVisibility(View.VISIBLE);
                } else {
                    civHead.setVisibility(View.INVISIBLE);
                    tvNickName.setVisibility(View.INVISIBLE);
                }
            }
        });
        adapter = new CommentListAdapter();
        mlvComment.setAdapter(adapter);
    }


    @OnClick({R.id.ivBack, R.id.ivMore, R.id.tvZan, R.id.tvSend, R.id.civHead1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                getActivity().finish();
                break;
            case R.id.civHead1:
                JumpUtils.jumpUserInfoActivity(getContext(), userId);
                break;
            case R.id.ivMore:
                if (SPUtils.getInt(Config.USERID) == userId) {
                    DynamicDialog dynamicDialog = new DynamicDialog(getActivity(), 1);
                    dynamicDialog.show();
                    dynamicDialog.setOnClicklistener(new DynamicDialog.OnClicklistener() {
                        @Override
                        public void onClick(int type) {
                            delDynamic();
                        }
                    });
                } else {
                    DynamicDialog dynamicDialog = new DynamicDialog(getActivity(), 2);
                    dynamicDialog.show();
                    dynamicDialog.setOnClicklistener(new DynamicDialog.OnClicklistener() {
                        @Override
                        public void onClick(int type) {
                            if (type == 1) {
                                JumpUtils.jumpReportActivity(getActivity(), commentData.getDynamicId() + "", 0, "", "");
                            } else if (type == 2) {
                                blackDynamic(commentData);
                            } else {
                                blackUser(commentData);
                            }
                        }
                    });
                }
                break;
            case R.id.tvZan:
                AlertUtils.showProgress(false, getActivity());
                isLike();
                break;
            case R.id.tvSend:
                String comment = etComment.getText().toString().trim();
                if (TextUtils.isEmpty(comment)) {
                    ToastUtils.toastCenter(getActivity(), "请输入你的评论");
                    return;
                }
                AlertUtils.showProgress(false, getActivity());
                ForbidEditUtils.hideInput(getActivity(), etComment);
                etComment.setText("");
                sendComment(comment);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        comment();
    }

    public void setDynamicId(String dynamicId) {
        this.dynamicId = dynamicId;
    }

    private void getDynamic() {
        Http.http.createApi(AuthApi.class).dynamic(dynamicId + "")
                .compose(context.<JsonResult<HomeMoudle.DataBean>>bindToLifecycle())
                .compose(context.<JsonResult<HomeMoudle.DataBean>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<HomeMoudle.DataBean>() {
                    @Override
                    public void success(HomeMoudle.DataBean dataBean, int code) {
                        commentData = dataBean;
                        AlertUtils.dismissProgress();
                        String content = dataBean.getContent();
                        if (TextUtils.isEmpty(content)) {
                            tvContent.setVisibility(View.GONE);
                        } else {
                            tvContent.setVisibility(View.VISIBLE);
                            tvContent.setText(content);
                        }
                        List<String> imgUrls = dataBean.getImgUrls();
                        ViewGroup.LayoutParams layoutParams = nsgvImage.getLayoutParams();
                        if (imgUrls != null && imgUrls.size() > 0) {
                            rcrlImage.setVisibility(View.VISIBLE);
                            if (imgUrls.size() == 1) {
                                layoutParams.width = com.yqjr.utils.utils.StringUtils.dp2px(getActivity(), 100);
                                nsgvImage.setLayoutParams(layoutParams);
                                nsgvImage.setNumColumns(1);
                            } else if (imgUrls.size() >= 5) {//5 6 7 8 9张
                                nsgvImage.setNumColumns(3);
                                layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                                nsgvImage.setLayoutParams(layoutParams);
                            } else if (imgUrls.size() == 4 || imgUrls.size() == 2) {//两张或者4张
                                nsgvImage.setNumColumns(2);
                                layoutParams.width = com.yqjr.utils.utils.StringUtils.dp2px(getActivity(), 200);
                                nsgvImage.setLayoutParams(layoutParams);
                            } else {//三张
                                nsgvImage.setNumColumns(3);
                                layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                                nsgvImage.setLayoutParams(layoutParams);
                            }
                            final String videoUrl = dataBean.getVideoUrl();
                            ImageAdapter imageAdapter = new ImageAdapter(imgUrls, 1);
                            if (!TextUtils.isEmpty(videoUrl)) {
                                imageAdapter.setVideo(true);
                                imageAdapter.setOnPlayListener(new ImageAdapter.OnPlayListener() {
                                    @Override
                                    public void onPlay() {
                                        Intent intent = new Intent(getActivity(), PlayPickActivity.class);
                                        intent.putExtra(Config.VIDEOURL, videoUrl);
                                        startActivity(intent);
                                    }
                                });
                            } else {
                                imageAdapter.setVideo(false);
                                imageAdapter.setOnSaveImageListener(new ImageAdapter.OnSaveImageListener() {
                                    @Override
                                    public void onSaveImage(List<String> urls, int position) {
                                        imageDialog = new ImageDialog(getActivity(), urls, position);
                                        imageDialog.show();
                                        imageDialog.setOnSaveImageListener(new ImageDialog.OnSaveImageListener() {
                                            @Override
                                            public void onSaveImage(String url) {
                                                imageDialog.saveImage(url);
                                            }
                                        });
                                    }
                                });
                            }
                            nsgvImage.setAdapter(imageAdapter);


                        } else {
                            rcrlImage.setVisibility(View.GONE);
                        }
                        String groupName = dataBean.getGroupName();
                        final String groupId = dataBean.getGroupId();
                        if (!TextUtils.isEmpty(groupName) && !TextUtils.isEmpty(groupId)) {
                            tvGroup.setVisibility(View.VISIBLE);
                            tvGroup.setText("群聊:" + groupName);
                        } else {
                            tvGroup.setVisibility(View.GONE);
                        }
                        HomeMoudle.DataBean.UserBriefVoBean userBriefVo = dataBean.getUserBriefVo();
                        if (userBriefVo != null) {
                            userId = userBriefVo.getUserId();
                            String nickName = userBriefVo.getNickName();
                            tvNickName1.setText(nickName);
                            tvNickName.setText(nickName);
                            String realImg = userBriefVo.getRealImg();
                            if (!TextUtils.isEmpty(realImg)) {
                                Picasso.with(getActivity()).load(realImg).into(civHead1);
                                Picasso.with(getActivity()).load(realImg).into(civHead);
                            }
                        }
                        String tagName = dataBean.getTagName();
                        String tagId = dataBean.getTagId();
                        if (!TextUtils.isEmpty(tagName) && !TextUtils.isEmpty(tagId)) {
                            tvTag.setVisibility(View.VISIBLE);
                            tvTag.setText("#" + tagName);
                        } else {
                            tvTag.setVisibility(View.GONE);
                        }
                        int isLike = dataBean.getIsLike();
                        if (isLike == 1) {
                            tvZan.setTextColor(getResources().getColor(R.color.color_F06063));
                            Drawable nav_up = getResources().getDrawable(R.mipmap.home_zan_select);
                            nav_up.setBounds(0, 0, 45, 39);
                            tvZan.setCompoundDrawables(nav_up, null, null, null);
                        } else {
                            tvZan.setTextColor(getResources().getColor(R.color.color_FF333333));
                            Drawable nav_up = getResources().getDrawable(R.mipmap.home_zan);
                            nav_up.setBounds(0, 0, 45, 39);
                            tvZan.setCompoundDrawables(nav_up, null, null, null);
                        }
                        int likeCount = dataBean.getLikeCount();
                        if (likeCount > 0) {
                            tvZan.setText(likeCount + "");
                        } else {
                            tvZan.setText("赞");
                        }
                        String createDte = dataBean.getCreateDte();
                        try {
                            tvTime.setText(Times.getTime(createDte));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(getActivity(), errorMessage + "");
                    }
                }));
    }


    private void isLike() {
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
                            int likeCount = response.getLikeCount();
                            if (isLike == 1) {
                                tvZan.setTextColor(getResources().getColor(R.color.color_F06063));
                                Drawable nav_up = getResources().getDrawable(R.mipmap.home_zan_select);
                                nav_up.setBounds(0, 0, 45, 39);
                                tvZan.setCompoundDrawables(nav_up, null, null, null);
                            } else {
                                tvZan.setTextColor(getResources().getColor(R.color.color_FF999999));
                                Drawable nav_up = getResources().getDrawable(R.mipmap.home_zan);
                                nav_up.setBounds(0, 0, 45, 39);
                                tvZan.setCompoundDrawables(nav_up, null, null, null);
                            }
                            if (likeCount < 1) {
                                tvZan.setText("赞");
                            } else {
                                tvZan.setText(likeCount + "");
                            }
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
     */
    private void delDynamic() {
        Http.http.createApi(AuthApi.class).delDynamic(dynamicId + "")
                .compose(context.<JsonResult<String>>bindToLifecycle())
                .compose(context.<JsonResult<String>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<String>() {
                    @Override
                    public void success(String response, int code) {
                        AlertUtils.dismissProgress();
                        Log.d("删除动态: ", response + "");
                        if (code == 200) {
                            getActivity().finish();
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

    /**
     * 评论列表
     */
    private void comment() {
        Http.http.createApi(AuthApi.class).comment(dynamicId + "")
                .compose(context.<JsonResult<List<HomeMoudle.DataBean>>>bindToLifecycle())
                .compose(context.<JsonResult<List<HomeMoudle.DataBean>>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<List<HomeMoudle.DataBean>>() {
                    @Override
                    public void success(List<HomeMoudle.DataBean> dataBeans, int code) {
                        AlertUtils.dismissProgress();
                        if (dataBeans != null && dataBeans.size() > 0) {
                            mlvComment.setVisibility(View.VISIBLE);
                            llDefault.setVisibility(View.GONE);
                            adapter.setData(dataBeans);
                        } else {
                            mlvComment.setVisibility(View.GONE);
                            llDefault.setVisibility(View.VISIBLE);
                            ivDefault.setImageResource(R.mipmap.default_commentary);
                            tvDefault.setText("暂无评论,要不抢个沙发?");
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
     * 发送评论
     *
     * @param comment
     */
    private void sendComment(String comment) {
        Map map = new HashMap();
        map.put("content", comment);
        map.put("dynamicId", dynamicId);
        map.put("fromAvatar", SPUtils.getString(Config.REALIMG));
        map.put("fromUname", SPUtils.getString(Config.NICKNAME));
        map.put("toUid", userId);

        Http.http.createApi(AuthApi.class).sendComment(map)
                .compose(context.<JsonResult<String>>bindToLifecycle())
                .compose(context.<JsonResult<String>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<String>() {
                    @Override
                    public void success(String dataBeans, int code) {
                        AlertUtils.dismissProgress();
                        comment();
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
        int dynamicId = dataBean.getDynamicId();
        Http.http.createApi(AuthApi.class).blackUser(dynamicId + "")
                .compose(context.<JsonResult<String>>bindToLifecycle())
                .compose(context.<JsonResult<String>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<String>() {
                    @Override
                    public void success(String response, int code) {
                        AlertUtils.dismissProgress();
                        Log.d("加入黑名单: ", response + "");
//                        List<HomeMoudle.DataBean> list = adapter.getList();
//                        list.remove(dataBean);
//                        adapter.notifyDataSetChanged();
//                        page=1;
//                        recommended();
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
//                        List<HomeMoudle.DataBean> list = adapter.getList();
////                        list.remove(dataBean);
////                        adapter.notifyDataSetChanged();
                        getActivity().finish();
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(getActivity(), errorMessage + "");
                    }
                }));
    }

}
