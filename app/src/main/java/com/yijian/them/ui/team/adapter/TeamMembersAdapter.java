package com.yijian.them.ui.team.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yijian.them.R;
import com.yijian.them.common.App;
import com.yijian.them.ui.team.moudle.TeamInfoMoudle;
import com.yijian.them.view.CircleImageView;
import com.yqjr.utils.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeamMembersAdapter extends BaseAdapter {
    private List<TeamInfoMoudle.DataBean.MembersBean> mlist = new ArrayList<>();
    private OnSaveImageListener onSaveImageListener;

    private OnAddImageListener onAddImageListener;

    public void setOnAddImageListener(OnAddImageListener onAddImageListener) {
        this.onAddImageListener = onAddImageListener;
    }

    public void setOnSaveImageListener(OnSaveImageListener onSaveImageListener) {
        this.onSaveImageListener = onSaveImageListener;
    }

    public TeamMembersAdapter(List<TeamInfoMoudle.DataBean.MembersBean> mlist) {
        this.mlist = mlist;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public TeamInfoMoudle.DataBean.MembersBean getItem(int position) {
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
            convertView = View.inflate(parent.getContext(), R.layout.item_team_members, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TeamInfoMoudle.DataBean.MembersBean membersBean = mlist.get(position);
        String realImg = membersBean.getRealImg();
        Picasso.with(parent.getContext()).load(realImg).placeholder(R.mipmap.register_icon_avatar).
                error(R.mipmap.register_icon_avatar).into(holder.civTeamMember);
//        holder.civTeamMember.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (onSaveImageListener != null) {
//                    onSaveImageListener.onSaveImage(mlist);
//                }
//            }
//        });


        return convertView;
    }



    static class ViewHolder {
        @BindView(R.id.civTeamMember)
        CircleImageView civTeamMember;


        ViewHolder(View view) {
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
