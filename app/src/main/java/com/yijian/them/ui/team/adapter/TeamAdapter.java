package com.yijian.them.ui.team.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yijian.them.R;
import com.yijian.them.common.App;
import com.yijian.them.ui.team.TeamMoudle;
import com.yijian.them.utils.JumpUtils;
import com.yijian.them.utils.picasso.PicassoRoundTransform;
import com.yijian.them.view.CircleImageView;
import com.yqjr.utils.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> {
    private List<TeamMoudle.DataBean> mList = new ArrayList();
    private Context mContext;

    public TeamAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public TeamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //加载item 布局文件
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_item, parent, false);
        return new TeamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TeamViewHolder holder, int position) {
        final TeamMoudle.DataBean dataBean = mList.get(position);
        String teamName = dataBean.getTeamName();
        holder.tvTeamTitle.setText(teamName);
        String distance = dataBean.getDistance();
        holder.tvTeamAddress.setText("距离你:" + (Double.parseDouble(distance) * 1000) +"Km");

        double teamImgHeight = dataBean.getTeamImgHeight();
        double teamImgWidth = dataBean.getTeamImgWidth();
        //图片宽高比
        double rate = teamImgWidth / teamImgHeight;
        //屏幕宽度一半
        int width = App.mWidth / 2 - StringUtils.dp2px(mContext, 10);
        int height;
        if (rate > 1.5) {
            teamImgHeight = 1335;
            teamImgWidth = 750;
            height = (int) (width * teamImgHeight / teamImgWidth);
        } else if (rate <= 1.5 && rate >= 0.7) {
            height = (int) (width * teamImgHeight * 1.7 / teamImgWidth);
        } else {
            height = (int) (width * teamImgHeight / teamImgWidth);
        }

        ViewGroup.LayoutParams layoutParams = holder.rlTeam.getLayoutParams();
        layoutParams.height = height;
        holder.rlTeam.setLayoutParams(layoutParams);
        List<String> teamImgUrls = dataBean.getTeamImgUrls();
        if (teamImgUrls != null && teamImgUrls.size() > 0) {
            String s = teamImgUrls.get(0);
            Picasso.with(mContext).load(s).into(holder.ivTeamBg);
        }
        List<TeamMoudle.DataBean.MembersBean> members = dataBean.getMembers();
        holder.tvTeamNum.setText("参与人数:" + members.size());
        if (members != null && members.size() > 0) {
            TeamMoudle.DataBean.MembersBean membersBean = members.get(0);
            String realImg = membersBean.getRealImg();
            String gender = membersBean.getGender();
            String nickName = membersBean.getNickName();
            holder.tvNickName.setText(nickName);
            Picasso.with(mContext).load(realImg).into(holder.civHead);
            if ("1".equals(gender)) {
                holder.llSex.setBackgroundResource(R.drawable.shape_sex_man_bg);
                holder.ivSex.setImageResource(R.mipmap.register_icon_man_selected);
            } else {
                holder.llSex.setBackgroundResource(R.drawable.shape_sex_woman_bg);
                holder.ivSex.setImageResource(R.mipmap.register_icon_woman_selected);
            }
            holder.tvTeamAge.setText(com.yijian.them.utils.StringUtils.getAge(membersBean.getBirthday()));
            holder.rlTeam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JumpUtils.jumpTeamInfoActivity(mContext, dataBean.getTeamId());
                }
            });
        }
    }

    public void setData(List<TeamMoudle.DataBean> data) {
        mList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void clear() {
        if (mList != null) {
            mList.clear();
            notifyDataSetChanged();
        }
    }

    static class TeamViewHolder extends RecyclerView.ViewHolder {
        ImageView ivTeamBg;
        ImageView ivTeamTopBg;
        CircleImageView civHead;
        ImageView ivSex;
        TextView tvTeamTitle;
        TextView tvTeamAddress;
        TextView tvTeamNum;
        TextView tvTeamAge;
        TextView tvNickName;
        LinearLayout llSex;
        RelativeLayout rlTeam;

        public TeamViewHolder(View itemView) {
            super(itemView);
            ivTeamBg = itemView.findViewById(R.id.ivTeamBg);
            ivSex = itemView.findViewById(R.id.ivSex);
            tvTeamTitle = itemView.findViewById(R.id.tvTeamTitle);
            tvTeamAddress = itemView.findViewById(R.id.tvTeamAddress);
            tvTeamNum = itemView.findViewById(R.id.tvTeamNum);
            tvTeamAge = itemView.findViewById(R.id.tvTeamAge);
            tvNickName = itemView.findViewById(R.id.tvNickName);
            rlTeam = itemView.findViewById(R.id.rlTeam);
            llSex = itemView.findViewById(R.id.llSex);

            civHead = itemView.findViewById(R.id.civHead);
            ivTeamTopBg = itemView.findViewById(R.id.ivTeamTopBg);

        }
    }
}
