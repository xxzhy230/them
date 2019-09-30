package com.yijian.them.ui.team.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.squareup.picasso.Picasso;
import com.yijian.them.R;
import com.yijian.them.ui.team.moudle.TeamInfoMoudle;
import com.yijian.them.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupMembersAdapter extends RecyclerView.Adapter<GroupMembersAdapter.ViewHolder> {
    private List<TeamInfoMoudle.DataBean.MembersBean> mlist = new ArrayList<>();
    private OnSaveImageListener onSaveImageListener;
private Context mContext;
    private OnAddImageListener onAddImageListener;

    public GroupMembersAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setOnAddImageListener(OnAddImageListener onAddImageListener) {
        this.onAddImageListener = onAddImageListener;
    }

    public void setOnSaveImageListener(OnSaveImageListener onSaveImageListener) {
        this.onSaveImageListener = onSaveImageListener;
    }

    public GroupMembersAdapter(Context mContext,List<TeamInfoMoudle.DataBean.MembersBean> mlist) {
        this.mContext = mContext;
        this.mlist = mlist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View convertView = View.inflate(mContext, R.layout.item_team_members, null);
        ViewHolder holder = new ViewHolder(convertView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TeamInfoMoudle.DataBean.MembersBean membersBean = mlist.get(position);
        String realImg = membersBean.getRealImg();
        Picasso.with(mContext).load(realImg).into(holder.civTeamMember);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.civTeamMember)
        CircleImageView civTeamMember;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnSaveImageListener {
        void onSaveImage(List<String> urls);
    }

    public interface OnAddImageListener {
        void onAddImage();

        void onDelImage(int position);
    }
}
