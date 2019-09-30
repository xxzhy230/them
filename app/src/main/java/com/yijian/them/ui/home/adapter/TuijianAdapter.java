package com.yijian.them.ui.home.adapter;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcssloop.widget.RCRelativeLayout;
import com.squareup.picasso.Picasso;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupSelfInfo;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.yijian.them.R;
import com.yijian.them.common.Config;
import com.yijian.them.ui.home.HomeMoudle;
import com.yijian.them.ui.home.activity.PlayPickActivity;
import com.yijian.them.utils.JumpUtils;
import com.yijian.them.utils.Times;
import com.yijian.them.utils.dialog.GroupDialog;
import com.yijian.them.view.CircleImageView;
import com.yijian.them.view.NoScrollGridView;
import com.yqjr.utils.utils.StringUtils;
import com.yqjr.utils.utils.ToastUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TuijianAdapter extends BaseAdapter {
    private List<HomeMoudle.DataBean> mList = new ArrayList<>();
    private OnLikeListener onLikeListener;
    private int type = 0;

    public void setType(int type) {
        this.type = type;
    }

    public TuijianAdapter(List<HomeMoudle.DataBean> mList) {
        this.mList = mList;
    }

    public TuijianAdapter() {
    }

    public void setOnLikeListener(OnLikeListener onLikeListener) {
        this.onLikeListener = onLikeListener;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_tuijian, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final HomeMoudle.DataBean dataBean = mList.get(position);
        List<String> imgUrls = dataBean.getImgUrls();
        ViewGroup.LayoutParams layoutParams = holder.nsgvImage.getLayoutParams();
        if (imgUrls != null && imgUrls.size() > 0) {
            holder.rcrlImage.setVisibility(View.VISIBLE);
            if (imgUrls.size() == 1) {
                layoutParams.width = StringUtils.dp2px(parent.getContext(), 100);
                holder.nsgvImage.setLayoutParams(layoutParams);
                holder.nsgvImage.setNumColumns(1);
            } else if (imgUrls.size() >= 5) {//5 6 7 8 9张
                holder.nsgvImage.setNumColumns(3);
                layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                holder.nsgvImage.setLayoutParams(layoutParams);
            } else if (imgUrls.size() == 4 || imgUrls.size() == 2) {//两张或者4张
                holder.nsgvImage.setNumColumns(2);
                layoutParams.width = StringUtils.dp2px(parent.getContext(), 200);
                holder.nsgvImage.setLayoutParams(layoutParams);
            } else {//三张
                holder.nsgvImage.setNumColumns(3);
                layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                holder.nsgvImage.setLayoutParams(layoutParams);
            }
            ImageAdapter imageAdapter = new ImageAdapter(imgUrls, 1);
            final String videoUrl = dataBean.getVideoUrl();
            if (TextUtils.isEmpty(videoUrl)) {
                imageAdapter.setVideo(false);
            } else {
                imageAdapter.setVideo(true);
            }

            holder.nsgvImage.setAdapter(imageAdapter);
            imageAdapter.setOnSaveImageListener(new ImageAdapter.OnSaveImageListener() {
                @Override
                public void onSaveImage(List<String> urls, int position) {
                    if (onLikeListener != null) {
                        onLikeListener.svaeImage(urls, position);
                    }
                }
            });
            imageAdapter.setOnPlayListener(new ImageAdapter.OnPlayListener() {
                @Override
                public void onPlay() {
                    Intent intent = new Intent(parent.getContext(), PlayPickActivity.class);
                    intent.putExtra(Config.VIDEOURL, videoUrl);
                    parent.getContext().startActivity(intent);
                }
            });
        } else {
            holder.rcrlImage.setVisibility(View.GONE);
        }
        String content = dataBean.getContent();
        if (TextUtils.isEmpty(content)) {
            holder.tvContent.setVisibility(View.GONE);
        } else {
            holder.tvContent.setVisibility(View.VISIBLE);
            holder.tvContent.setText(content);
        }
        int commentCount = dataBean.getCommentCount();
        if (commentCount > 0) {
            holder.tvPLNum.setText(commentCount + "");
        } else {
            holder.tvPLNum.setText("评论");
        }
        int isLike = dataBean.getIsLike();
        if (isLike == 1) {
            Drawable nav_up = parent.getContext().getResources().getDrawable(R.mipmap.home_zan_select);
            nav_up.setBounds(0, 0, 45, 39);
            holder.tvZanNum.setCompoundDrawables(nav_up, null, null, null);
            holder.tvZanNum.setTextColor(parent.getContext().getResources().getColor(R.color.color_F06063));
        } else {
            holder.tvZanNum.setTextColor(parent.getContext().getResources().getColor(R.color.color_FF333333));
            Drawable nav_up = parent.getContext().getResources().getDrawable(R.mipmap.home_zan);
            nav_up.setBounds(0, 0, 45, 39);
            holder.tvZanNum.setCompoundDrawables(nav_up, null, null, null);
        }
        int likeCount = dataBean.getLikeCount();
        if (likeCount > 0) {
            holder.tvZanNum.setText(likeCount + "");
        } else {
            holder.tvZanNum.setText("点赞");
        }

        String createDte = dataBean.getCreateDte();
        try {
            holder.tvTime.setText(Times.getTime(createDte));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        HomeMoudle.DataBean.UserBriefVoBean userBriefVo = dataBean.getUserBriefVo();
        if (userBriefVo != null) {
            String nickName = userBriefVo.getNickName();
            holder.tvNickName.setText(nickName);
            String realImg = userBriefVo.getRealImg();
            if (!TextUtils.isEmpty(realImg)) {
                Picasso.with(parent.getContext()).load(realImg).into(holder.civHead);
            }
        }
        String localName = dataBean.getLocalName();
        String distance = dataBean.getDistance();

        if (!TextUtils.isEmpty(localName)) {
            holder.llAddress.setVisibility(View.VISIBLE);
            if (type == 1) {
                holder.tvAddress.setText(localName + " " + com.yijian.them.utils.StringUtils.getDis(distance));
            } else {
                holder.tvAddress.setText(localName);
            }
        } else {
            holder.llAddress.setVisibility(View.GONE);
        }
        String groupName = dataBean.getGroupName();
        final String groupId = dataBean.getGroupId();
        if (!TextUtils.isEmpty(groupName) && !TextUtils.isEmpty(groupId)) {
            holder.tvGroup.setVisibility(View.VISIBLE);
            holder.tvGroup.setText("群聊:" + groupName);
        } else {
            holder.tvGroup.setVisibility(View.GONE);
        }
        String tagName = dataBean.getTagName();
        String tagId = dataBean.getTagId();
        if (!TextUtils.isEmpty(tagName) && !TextUtils.isEmpty(tagId)) {
            holder.tvTag.setVisibility(View.VISIBLE);
            holder.tvTag.setText("#" + tagName);
        } else {
            holder.tvTag.setVisibility(View.GONE);
        }
        holder.tvTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeMoudle.DataBean dataBean = mList.get(position);
                JumpUtils.jumpHotTopicInfoActivity(parent.getContext(), dataBean.getTagId());
            }
        });
        /**
         * 点赞
         */
        holder.tvZanNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLikeListener != null) {
                    HomeMoudle.DataBean dataBean = mList.get(position);
                    onLikeListener.like(dataBean);
                }
            }
        });
        /**
         * 跳转到详情
         */
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeMoudle.DataBean dataBean = mList.get(position);
                int dynamicId = dataBean.getDynamicId();
                JumpUtils.jumpDynamicActivity(parent.getContext(), 7, "", dynamicId + "");
            }
        });
        holder.ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLikeListener != null) {
                    HomeMoudle.DataBean dataBean = mList.get(position);
                    onLikeListener.clickMore(dataBean);
                }
            }
        });
        holder.tvGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final HomeMoudle.DataBean dataBean = mList.get(position);
                TIMGroupManager.getInstance().getSelfInfo(groupId, new TIMValueCallBack<TIMGroupSelfInfo>() {
                    @Override
                    public void onError(int i, String s) {
                        if (s.equals("no permission")) {
                            GroupDialog dialog = new GroupDialog(parent.getContext(), dataBean.getGroupId(), dataBean.getGroupName());
                            dialog.show();
                            dialog.setOnJoinListener(new GroupDialog.OnJoinListener() {
                                @Override
                                public void onJoinTeam(String teamId, String teamName) {
                                    if (onLikeListener != null) {
                                        onLikeListener.joinTeam(teamId, teamName);
                                    }
                                }
                            });
                        } else if (s.equals("this group does not exist")) {
                            ToastUtils.toastCenter(parent.getContext(), "群组已解散");
                        }
                    }

                    @Override
                    public void onSuccess(TIMGroupSelfInfo timGroupSelfInfo) {
                        if (timGroupSelfInfo != null) {
                            ChatInfo chatInfo = new ChatInfo();
                            chatInfo.setChatName(dataBean.getGroupName());
                            chatInfo.setId(dataBean.getGroupId());
                            chatInfo.setType(TIMConversationType.Group);
                            JumpUtils.jumpMessageActivity(parent.getContext(), 0, chatInfo);
                        }
                    }
                });
            }
        });
        return convertView;
    }

    public void setData(List<HomeMoudle.DataBean> mList) {
        this.mList.addAll(mList);
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    public void delDynamic(HomeMoudle.DataBean dataBean) {
        if (dataBean != null) {
            mList.remove(dataBean);
            notifyDataSetChanged();
        }
    }

    public List<HomeMoudle.DataBean> getList() {
        return mList;
    }

    static class ViewHolder {
        @BindView(R.id.civHead)
        CircleImageView civHead;
        @BindView(R.id.tvContent)
        TextView tvContent;
        @BindView(R.id.nsgvImage)
        NoScrollGridView nsgvImage;
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.tvPLNum)
        TextView tvPLNum;
        @BindView(R.id.tvZanNum)
        TextView tvZanNum;
        @BindView(R.id.tvNickName)
        TextView tvNickName;
        @BindView(R.id.tvAddress)
        TextView tvAddress;
        @BindView(R.id.ivMore)
        ImageView ivMore;
        @BindView(R.id.llAddress)
        LinearLayout llAddress;
        @BindView(R.id.tvTag)
        TextView tvTag;
        @BindView(R.id.tvGroup)
        TextView tvGroup;
        @BindView(R.id.rcrlImage)
        RCRelativeLayout rcrlImage;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnLikeListener {
        void like(HomeMoudle.DataBean dataBean);

        void svaeImage(List<String> urls, int position);

        void clickMore(HomeMoudle.DataBean dataBean);

        void joinTeam(String teamd, String teamName);
    }
}
