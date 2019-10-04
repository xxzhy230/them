package com.yijian.them.ui.team.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yijian.them.R;
import com.yijian.them.common.Config;
import com.yijian.them.ui.team.moudle.TeamInfoMoudle;
import com.yijian.them.view.CircleImageView;
import com.yqjr.utils.spUtils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MembersAdapter extends BaseAdapter {
    private List<TeamInfoMoudle.DataBean.MembersBean> mlist = new ArrayList<>();
    private List<TeamInfoMoudle.DataBean.MembersBean> isSelectList = new ArrayList<>();
    private int type = 0;// 0  编辑   1  取消
    private OnSelectChangeListener onSelectChangeListener;

    public void setOnSelectChangeListener(OnSelectChangeListener onSelectChangeListener) {
        this.onSelectChangeListener = onSelectChangeListener;
    }

    @Override
    public int getCount() {
        return mlist.size();
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
            convertView = View.inflate(parent.getContext(), R.layout.item_members, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TeamInfoMoudle.DataBean.MembersBean membersBean = mlist.get(position);
        String realImg = membersBean.getRealImg();
        Picasso.with(parent.getContext()).load(realImg).into(holder.civTeamMember);
        String nickName = membersBean.getNickName();
        holder.tvNickName.setText(nickName);
        int userId = membersBean.getUserId();
        if (type == 0) {
            holder.ivSelect.setVisibility(View.GONE);
        } else if (type == 1) {
            if (SPUtils.getInt(Config.USERID) == userId) {
                holder.ivSelect.setVisibility(View.INVISIBLE);
            } else {
                holder.ivSelect.setVisibility(View.VISIBLE);
            }
        }
        boolean followed = membersBean.getFollowed();
        if (followed) {
            holder.ivSelect.setImageResource(R.mipmap.icon_shenqing_select);
        } else {
            holder.ivSelect.setImageResource(R.mipmap.icon_shenqing_unselect);
        }
        holder.ivSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TeamInfoMoudle.DataBean.MembersBean membersBean = mlist.get(position);
                boolean followed = membersBean.getFollowed();
                if (followed) {
                    membersBean.setFollowed(false);
                    isSelectList.remove(membersBean);
                } else {
                    membersBean.setFollowed(true);
                    isSelectList.add(membersBean);
                }
                notifyDataSetChanged();
                if (onSelectChangeListener != null) {
                    onSelectChangeListener.onSelectChange(isSelectList);
                }
            }
        });
        return convertView;
    }

    public void setData(List<TeamInfoMoudle.DataBean.MembersBean> members) {
        this.mlist = members;
        notifyDataSetChanged();
    }

    public void setType(int type) {
        this.type = type;
        notifyDataSetChanged();
    }

    public List<TeamInfoMoudle.DataBean.MembersBean> getDelList() {
        return isSelectList;
    }

    public void delMembers(List<TeamInfoMoudle.DataBean.MembersBean> delList) {
        for (int i = 0; i < delList.size(); i++) {
            mlist.remove(delList.get(i));
        }
        notifyDataSetChanged();
    }

    public interface OnSaveImageListener {
        void onSaveImage(List<String> urls);
    }

    public interface OnSelectChangeListener {
        void onSelectChange(List<TeamInfoMoudle.DataBean.MembersBean> members);

    }

    static
    class ViewHolder {
        @BindView(R.id.ivSelect)
        ImageView ivSelect;
        @BindView(R.id.civTeamMember)
        CircleImageView civTeamMember;
        @BindView(R.id.tvNickName)
        TextView tvNickName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
