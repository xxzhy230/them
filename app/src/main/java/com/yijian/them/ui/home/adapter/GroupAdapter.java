package com.yijian.them.ui.home.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.ext.group.TIMGroupBaseInfo;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfo;
import com.yijian.them.R;
import com.yijian.them.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupAdapter extends BaseAdapter {
    private List<TIMGroupBaseInfo> timGroupBaseInfos = new ArrayList<>();
    private final TIMGroupManager instance;

    public GroupAdapter() {
        instance = TIMGroupManager.getInstance();
    }

    @Override
    public int getCount() {
        return timGroupBaseInfos.size();
    }

    @Override
    public TIMGroupBaseInfo getItem(int position) {
        return timGroupBaseInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_group, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TIMGroupBaseInfo timGroupBaseInfo = timGroupBaseInfos.get(position);
        String faceUrl = timGroupBaseInfo.getFaceUrl();
        if (!TextUtils.isEmpty(faceUrl)) {
            Picasso.with(parent.getContext()).load(faceUrl).into(holder.civHead);
        } else {
            Picasso.with(parent.getContext()).load(R.mipmap.group_common_list).into(holder.civHead);
        }
        holder.tvGroupTitle.setText(timGroupBaseInfo.getGroupName());
        TIMGroupDetailInfo timGroupDetailInfo = instance.queryGroupInfo(timGroupBaseInfo.getGroupId());
        if (timGroupDetailInfo != null) {
            String groupIntroduction = timGroupDetailInfo.getGroupIntroduction();
            if (!TextUtils.isEmpty(groupIntroduction)) {
                holder.tvGroupInfo.setText(groupIntroduction);
            }
        }else {
            holder.tvGroupInfo.setText("");
        }

        return convertView;
    }

    public void setData(List<TIMGroupBaseInfo> timGroupBaseInfos) {
        this.timGroupBaseInfos = timGroupBaseInfos;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        @BindView(R.id.civHead)
        CircleImageView civHead;
        @BindView(R.id.tvGroupTitle)
        TextView tvGroupTitle;
        @BindView(R.id.tvGroupInfo)
        TextView tvGroupInfo;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
