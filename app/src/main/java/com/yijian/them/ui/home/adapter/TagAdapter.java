package com.yijian.them.ui.home.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yijian.them.R;
import com.yijian.them.ui.home.HomeMoudle;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TagAdapter extends BaseAdapter {
    private List<HomeMoudle.DataBean> dataBeans = new ArrayList<>();
    private int type = 0;
    private OnOffFollowedListener onOffFollowedListener;

    public void setOnOffFollowedListener(OnOffFollowedListener onOffFollowedListener) {
        this.onOffFollowedListener = onOffFollowedListener;
    }

    @Override
    public int getCount() {
        return dataBeans.size();
    }

    @Override
    public HomeMoudle.DataBean getItem(int position) {
        return dataBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_tag, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final HomeMoudle.DataBean dataBean = dataBeans.get(position);
        String tagName = dataBean.getTagName();
        holder.tvTagName.setText(tagName);
        String tagHeat = dataBean.getTagHeat();
        holder.tvTagContent.setText(tagHeat + "人已参加");
        String tagUrl = dataBean.getTagUrl();
        if (!TextUtils.isEmpty(tagUrl)) {
            Picasso.with(parent.getContext()).load(tagUrl).into(holder.ivImageHead);
        }
        if (type == 1) {
            holder.tvAddOrDel.setVisibility(View.VISIBLE);
        } else {
            holder.tvAddOrDel.setVisibility(View.GONE);
        }
        holder.tvAddOrDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeMoudle.DataBean dataBean = dataBeans.get(position);
                if (onOffFollowedListener != null) {
                    onOffFollowedListener.offFollowed(dataBean);
                }
            }
        });
        return convertView;
    }

    public void setDataBeans(List<HomeMoudle.DataBean> dataBeans, int type) {
        this.dataBeans.addAll(dataBeans);
        this.type = type;
        notifyDataSetChanged();
    }

    public void clear() {
        if (dataBeans != null) {
            dataBeans.clear();
            notifyDataSetChanged();
        }
    }

    public void remove(HomeMoudle.DataBean dataBean) {
        dataBeans.remove(dataBean);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        @BindView(R.id.ivImageHead)
        ImageView ivImageHead;
        @BindView(R.id.tvTagName)
        TextView tvTagName;
        @BindView(R.id.tvTagContent)
        TextView tvTagContent;
        @BindView(R.id.tvAddOrDel)
        TextView tvAddOrDel;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnOffFollowedListener {
        void offFollowed(HomeMoudle.DataBean dataBean);
    }
}
