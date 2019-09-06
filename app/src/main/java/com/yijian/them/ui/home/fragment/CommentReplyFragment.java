package com.yijian.them.ui.home.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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
import com.yijian.them.ui.home.GroupMoudle;
import com.yijian.them.ui.home.HomeMoudle;
import com.yijian.them.ui.home.adapter.CommentListAdapter;
import com.yijian.them.ui.home.adapter.CommentReplyListAdapter;
import com.yijian.them.ui.home.adapter.ImageAdapter;
import com.yijian.them.ui.login.DataMoudle;
import com.yijian.them.utils.JumpUtils;
import com.yijian.them.utils.SoftKeyBoardListener;
import com.yijian.them.utils.StringUtils;
import com.yijian.them.utils.Times;
import com.yijian.them.utils.dialog.AlertUtils;
import com.yijian.them.utils.dialog.DynamicDialog;
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

public class CommentReplyFragment extends BasicFragment {
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.civHead1)
    CircleImageView civHead1;
    @BindView(R.id.tvNickName1)
    TextView tvNickName1;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvContent)
    TextView tvContent;
    @BindView(R.id.mlvComment)
    MyListView mlvComment;
    @BindView(R.id.etComment)
    EditText etComment;
    @BindView(R.id.tvSend)
    TextView tvSend;
    @BindView(R.id.llComment)
    LinearLayout llComment;
    @BindView(R.id.llCommentTitle)
    LinearLayout llCommentTitle;

    private String dynamicId;
    private CommentReplyListAdapter adapter;
    private GroupMoudle.DataBean dataBean;
    private String toUid;
    private String toUname;
    private String commentId;
    private int replyType;

    @Override
    protected void onClickEvent() {
        context = (BasicActivity) getActivity();
        initComment();
        reportList();
    }

    private void initComment() {
        dataBean = Config.dataBean;
        String fromAvatar = dataBean.getFromAvatar();
        String fromUname = dataBean.getFromUname();
        String content = dataBean.getContent();
        String createDte = dataBean.getCreateDte();
        tvContent.setText(content);
        try {
            tvTime.setText(Times.getTime(createDte));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tvNickName1.setText(fromUname);
        if (!TextUtils.isEmpty(fromAvatar)) {
            Picasso.with(getActivity()).load(fromAvatar).into(civHead1);
        }
        toUid = dataBean.getFromUid();
        toUname = dataBean.getFromUname();
        commentId = dataBean.getCommentId();
        replyType = 0;
    }

    @Override
    protected View getResourceView() {
        View view = View.inflate(getActivity(), R.layout.fragment_comment_reply, null);
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
        adapter = new CommentReplyListAdapter();
        mlvComment.setAdapter(adapter);
        mlvComment.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final GroupMoudle.DataBean item = adapter.getItem(position);
                String fromUid = item.getFromUid();
                if ((SPUtils.getInt(Config.USERID) + "").equals(fromUid)) {
                    DynamicDialog dynamicDialog = new DynamicDialog(getActivity(), 1);
                    dynamicDialog.show();
                    dynamicDialog.setOnClicklistener(new DynamicDialog.OnClicklistener() {
                        @Override
                        public void onClick(int type) {
                            delComment(item);
                        }
                    });
                } else {
                    DynamicDialog dynamicDialog = new DynamicDialog(getActivity(), 3);
                    dynamicDialog.show();
                    dynamicDialog.setOnClicklistener(new DynamicDialog.OnClicklistener() {
                        @Override
                        public void onClick(int type) {
                            if (type == 1) {
                                JumpUtils.jumpReportActivity(getActivity(), dynamicId, 1, item.getFromUid(), item.getCommentId());
                            }
                        }
                    });
                }
                return true;
            }
        });
        mlvComment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final GroupMoudle.DataBean item = adapter.getItem(position);
                etComment.setHint("回复 " + item.getFromUname());
                toUid = item.getFromUid();
                toUname = item.getFromUname();
                commentId = item.getCommentId();
                replyType = 1;
            }
        });
    }


    @OnClick({R.id.ivBack, R.id.tvSend, R.id.llCommentTitle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                getActivity().finish();
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
            case R.id.llCommentTitle:
                toUid = dataBean.getFromUid();
                toUname = dataBean.getFromUname();
                commentId = dataBean.getCommentId();
                etComment.setHint("随便说点什么...");
                replyType = 0;
                break;
        }
    }


    public void setDynamicId(String dynamicId) {
        this.dynamicId = dynamicId;
    }
    /**
     * 删除评论
     */
    private void delComment(final GroupMoudle.DataBean dataBean) {
        Http.http.createApi(AuthApi.class).delComment(dataBean.getCommentId())
                .compose(context.<JsonResult<String>>bindToLifecycle())
                .compose(context.<JsonResult<String>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<String>() {
                    @Override
                    public void success(String response, int code) {
                        AlertUtils.dismissProgress();
                        Log.d("删除评论: ", response + "");
                        if (code == 200) {
                            getActivity().finish();
                            adapter.delComment(dataBean);
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
    private void reportList() {
        Http.http.createApi(AuthApi.class).reportList(dataBean.getCommentId() + "")
                .compose(context.<JsonResult<List<GroupMoudle.DataBean>>>bindToLifecycle())
                .compose(context.<JsonResult<List<GroupMoudle.DataBean>>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<List<GroupMoudle.DataBean>>() {
                    @Override
                    public void success(List<GroupMoudle.DataBean> dataBeans, int code) {
                        AlertUtils.dismissProgress();
                        adapter.setData(dataBeans);
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
        map.put("commentId", commentId);
        map.put("dynamicId", dynamicId);
        map.put("fromAvatar", SPUtils.getString(Config.REALIMG));
        map.put("fromUname", SPUtils.getString(Config.NICKNAME));
        map.put("replyType", replyType);
        map.put("toUid", toUid);
        map.put("toUname", toUname);
        Http.http.createApi(AuthApi.class).sendReply(map)
                .compose(context.<JsonResult<String>>bindToLifecycle())
                .compose(context.<JsonResult<String>>applySchedulers())
                .subscribe(context.newSubscriber(new CallBack<String>() {
                    @Override
                    public void success(String dataBeans, int code) {
                        AlertUtils.dismissProgress();
                        reportList();
                    }

                    @Override
                    public void fail(String errorMessage, int status) {
                        AlertUtils.dismissProgress();
                        ToastUtils.toastCenter(getActivity(), errorMessage + "");
                    }
                }));
    }
}
