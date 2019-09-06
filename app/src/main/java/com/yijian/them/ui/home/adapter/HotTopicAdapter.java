package com.yijian.them.ui.home.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yijian.them.R;
import com.yijian.them.ui.home.GroupMoudle;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HotTopicAdapter extends BaseAdapter {
    private List<GroupMoudle.DataBean> dataBeans;
    private int selectPosition = 0;

    public HotTopicAdapter(List<GroupMoudle.DataBean> dataBeans) {
        this.dataBeans = dataBeans;
    }

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
            convertView = View.inflate(parent.getContext(), R.layout.item_hot_topic, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == selectPosition){
            holder.tvHotTopicTitle.setTextColor(parent.getContext().getResources().getColor(R.color.color_3B7AFF));
        }else {
            holder.tvHotTopicTitle.setTextColor(parent.getContext().getResources().getColor(R.color.color_FF333333));
        }
        GroupMoudle.DataBean dataBean = dataBeans.get(position);
        holder.tvHotTopicTitle.setText(dataBean.getTopicName());
        return convertView;
    }

    public void setSelectPosition(int position) {
        this.selectPosition = position;
        notifyDataSetChanged();
    }

    static
    class ViewHolder {
        @BindView(R.id.tvHotTopicTitle)
        TextView tvHotTopicTitle;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
