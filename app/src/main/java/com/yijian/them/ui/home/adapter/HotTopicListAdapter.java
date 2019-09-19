package com.yijian.them.ui.home.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yijian.them.R;
import com.yijian.them.ui.home.HomeMoudle;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HotTopicListAdapter extends BaseAdapter {
    private List<HomeMoudle.DataBean> dataBeans;
    private int selectPosition = 0;

    public HotTopicListAdapter(List<HomeMoudle.DataBean> dataBeans) {
        this.dataBeans = dataBeans;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_hot_topic_list, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HomeMoudle.DataBean dataBean = dataBeans.get(position);
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
