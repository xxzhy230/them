package com.yijian.them.ui.message.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yijian.them.R;
import com.yijian.them.ui.home.HomeMoudle;
import com.yijian.them.utils.Times;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SystemMessageAdapter extends BaseAdapter {
    private List<HomeMoudle.DataBean> mList = new ArrayList<>();

    @Override
    public int getCount() {
        return mList.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_system_message, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        HomeMoudle.DataBean dataBean = mList.get(position);
        String title = dataBean.getTitle();
        holder.tvSystemTitle.setText(title);
        String createDte = dataBean.getCreateDte();
        try {
            if (!TextUtils.isEmpty(createDte)){
                holder.tvSystemTime.setText(Times.getTime(createDte));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
