package com.yijian.them.ui.home.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.yijian.them.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddressAdapter extends BaseAdapter {
    private ArrayList<PoiItem> pois;

    public AddressAdapter(ArrayList<PoiItem> pois) {
        this.pois = pois;
    }

    @Override
    public int getCount() {
        return pois.size();
    }

    @Override
    public PoiItem getItem(int position) {
        return pois.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_address, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PoiItem poiItem = pois.get(position);
        String title = poiItem.getTitle();
        holder.tvPoiName.setText(title);
        String direction = poiItem.getSnippet();
        holder.tvAddress.setText(direction);
        return convertView;
    }

    static
    class ViewHolder {
        @BindView(R.id.tvPoiName)
        TextView tvPoiName;
        @BindView(R.id.tvAddress)
        TextView tvAddress;
        @BindView(R.id.ivState)
        ImageView ivState;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
