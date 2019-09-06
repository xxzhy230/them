package com.yijian.them.ui.home.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yijian.them.R;
import com.yijian.them.ui.home.GroupMoudle;
import com.yijian.them.utils.picasso.PicassoRoundTransform;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TagAdapter extends BaseAdapter {
    private List<GroupMoudle.DataBean> dataBeans = new ArrayList<>();

    @Override
    public int getCount() {
        return dataBeans.size();
    }

    @Override
    public GroupMoudle.DataBean getItem(int position) {
        return dataBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_tag, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GroupMoudle.DataBean dataBean = dataBeans.get(position);
        String tagName = dataBean.getTagName();
        holder.tvTagName.setText(tagName);
        String tagHeat = dataBean.getTagHeat();
        holder.tvTagContent.setText(tagHeat + "人已参加");
        String tagUrl = dataBean.getTagUrl();
        if (!TextUtils.isEmpty(tagUrl)) {
            Picasso.with(parent.getContext()).load(tagUrl).transform(new PicassoRoundTransform())
                    .into(holder.ivImageHead);
        }
        return convertView;
    }

    public void setDataBeans(List<GroupMoudle.DataBean> dataBeans) {
        this.dataBeans.addAll(dataBeans);
    }

    static class ViewHolder {
        @BindView(R.id.ivImageHead)
        ImageView ivImageHead;
        @BindView(R.id.tvTagName)
        TextView tvTagName;
        @BindView(R.id.tvTagContent)
        TextView tvTagContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
