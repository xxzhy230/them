package com.yijian.them.ui.team.adapter;

import android.content.Context;
import android.media.Image;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcssloop.widget.RCRelativeLayout;
import com.squareup.picasso.Picasso;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.yijian.them.R;
import com.yijian.them.common.App;
import com.yijian.them.common.Config;
import com.yijian.them.ui.team.TeamMoudle;
import com.yijian.them.utils.JumpUtils;
import com.yijian.them.utils.StringUtils;
import com.yijian.them.view.CircleImageView;
import com.yqjr.utils.spUtils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeamListAdapter extends BaseAdapter {
    private List<TeamMoudle.DataBean> mList = new ArrayList();
    private Context mContext;
    private OnTeamOutListener onTeamOutListener;

    public void setOnTeamOutListener(OnTeamOutListener onTeamOutListener) {
        this.onTeamOutListener = onTeamOutListener;
    }

    public TeamListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<TeamMoudle.DataBean> data) {
        mList.addAll(data);
        notifyDataSetChanged();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final TeamMoudle.DataBean dataBean = mList.get(position);
        String teamName = dataBean.getTeamName();
        String teamDesc = dataBean.getTeamDesc();
        holder.tvTeamDesc.setText("活动内容 : " + teamDesc);
        holder.tvTeamName.setText(teamName);
        String createAt = dataBean.getCreateAt();
        holder.tvTeamTime.setText(createAt.split(" ")[0]);
        String distance = dataBean.getDistance();
        if (TextUtils.isEmpty(distance)) {
            distance = "0";
        }
        holder.tvDis.setText((Double.parseDouble(distance) / 1000) + "Km");
        List<String> teamImgUrls = dataBean.getTeamImgUrls();
        if (teamImgUrls != null && teamImgUrls.size() > 0) {
            String s = teamImgUrls.get(0);
            Picasso.with(mContext).load(s).into(holder.ivImageBg);
        }
        List<TeamMoudle.DataBean.MembersBean> members = dataBean.getMembers();
        if (members != null && members.size() > 0) {
            TeamMoudle.DataBean.MembersBean membersBean = members.get(0);
            String realImg = membersBean.getRealImg();
            String gender = membersBean.getGender();
            String nickName = membersBean.getNickName();
            String birthday = membersBean.getBirthday();
            holder.tvTeamUserName.setText(nickName);
            int userId = membersBean.getUserId();
            if (userId == SPUtils.getInt(Config.USERID)) {
                holder.tvOutTeam.setText("解散小队");
            } else {
                holder.tvOutTeam.setText("退出小队");
            }
            Picasso.with(mContext).load(realImg).into(holder.civTeamUser);
            if ("1".equals(gender)) {
                holder.llSex.setBackgroundResource(R.drawable.shape_sex_man_bg);
                holder.ivSex.setImageResource(R.mipmap.register_icon_man_selected);
            } else {
                holder.llSex.setBackgroundResource(R.drawable.shape_sex_woman_bg);
                holder.ivSex.setImageResource(R.mipmap.register_icon_woman_selected);
            }
            holder.tvTeamUserSex.setText(StringUtils.getAge(birthday));
            holder.tvGroupChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TeamMoudle.DataBean dataBean = mList.get(position);
                    ChatInfo chatInfo = new ChatInfo();
                    chatInfo.setType(TIMConversationType.Group);
                    chatInfo.setId(dataBean.getChatgroupId());
                    chatInfo.setChatName(dataBean.getTeamName());
                    JumpUtils.jumpMessageActivity(mContext, 0, chatInfo);
                }
            });
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TeamMoudle.DataBean dataBean = mList.get(position);
                    JumpUtils.jumpTeamInfoActivity(mContext, dataBean.getTeamId());
                }
            });
            holder.tvOutTeam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TeamMoudle.DataBean dataBean = mList.get(position);
                    if (onTeamOutListener != null) {
                        onTeamOutListener.onTeamOut(dataBean);
                    }
                }
            });
        }
        return convertView;
    }

    public void remove(TeamMoudle.DataBean dataBean) {
        mList.remove(dataBean);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        @BindView(R.id.ivImageBg)
        ImageView ivImageBg;
        @BindView(R.id.civTeamUser)
        CircleImageView civTeamUser;
        @BindView(R.id.tvTeamUserName)
        TextView tvTeamUserName;
        @BindView(R.id.tvDis)
        TextView tvDis;
        @BindView(R.id.tvTeamUserSex)
        TextView tvTeamUserSex;
        @BindView(R.id.tvTeamTime)
        TextView tvTeamTime;
        @BindView(R.id.tvTeamName)
        TextView tvTeamName;
        @BindView(R.id.tvTeamDesc)
        TextView tvTeamDesc;
        @BindView(R.id.tvOutTeam)
        TextView tvOutTeam;
        @BindView(R.id.tvGroupChat)
        TextView tvGroupChat;
        @BindView(R.id.ivSex)
        ImageView ivSex;
        @BindView(R.id.llSex)
        LinearLayout llSex;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnTeamOutListener {
        void onTeamOut(TeamMoudle.DataBean dataBean);
    }
}
