package com.yijian.them.ui.message.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yijian.them.R;
import com.yijian.them.ui.home.HomeMoudle;
import com.yijian.them.utils.Times;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeamMessageAdapter extends BaseAdapter {
    private List<HomeMoudle.DataBean> mList = new ArrayList<>();

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public HomeMoudle.DataBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_system_message, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HomeMoudle.DataBean dataBean = mList.get(position);
        String fromUname = dataBean.getFromUname();
        String pushType = dataBean.getPushType();
        if ("81".equals(pushType)) {
            holder.tvSystemTitle.setText(fromUname + "  " + "加入了你的小队>>");
        } else {
            holder.tvSystemTitle.setText(fromUname + "  " + "离开了你的小队>>");
        }
        String createDte = dataBean.getPushAt();
        try {
            if (!TextUtils.isEmpty(createDte)) {
                holder.tvSystemTime.setText(Times.getTime(createDte));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.ivImage.setImageResource(R.mipmap.message_message);
        return convertView;
    }

    public void setData(List<HomeMoudle.DataBean> response) {
        mList = response;
        notifyDataSetChanged();
    }

    static
    class ViewHolder {
        @BindView(R.id.tvSystemTitle)
        TextView tvSystemTitle;
        @BindView(R.id.tvSystemTime)
        TextView tvSystemTime;
        @BindView(R.id.ivImage)
        ImageView ivImage;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
