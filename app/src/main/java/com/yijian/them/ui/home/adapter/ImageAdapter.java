package com.yijian.them.ui.home.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yijian.them.R;
import com.yijian.them.common.App;
import com.yijian.them.utils.dialog.ImageDialog;
import com.yijian.them.view.FilletImageView;
import com.yqjr.utils.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageAdapter extends BaseAdapter {
    private List<String> mlist = new ArrayList<>();
    private OnSaveImageListener onSaveImageListener;
    private int type = 1; // 1 动态列表  2 发送动态
    private OnAddImageListener onAddImageListener;

    public void setOnAddImageListener(OnAddImageListener onAddImageListener) {
        this.onAddImageListener = onAddImageListener;
    }

    public void setOnSaveImageListener(OnSaveImageListener onSaveImageListener) {
        this.onSaveImageListener = onSaveImageListener;
    }

    public ImageAdapter(List<String> mlist, int type) {
        this.mlist = mlist;
        this.type = type;
    }

    @Override
    public int getCount() {
        return mlist.size();
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_image, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ViewGroup.LayoutParams layoutParams = holder.ivImage.getLayoutParams();
        int width = (App.mWidth - StringUtils.dp2px(parent.getContext(), 80)) / 3;
        if (mlist.size() > 1) {
            layoutParams.width = width;
            layoutParams.height = width;
            holder.ivImage.setLayoutParams(layoutParams);
        } else {
            layoutParams.width = StringUtils.dp2px(parent.getContext(), 100);
            layoutParams.height = StringUtils.dp2px(parent.getContext(), 100);
            holder.ivImage.setLayoutParams(layoutParams);
        }
        String image = mlist.get(position);
        if (type == 1) {
            Picasso.with(parent.getContext()).load(image).into(holder.ivImage);
            holder.ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onSaveImageListener != null) {
                        onSaveImageListener.onSaveImage(mlist);
                    }
                }
            });
        } else {

            if ("1".equals(image)) {
                holder.ivDel.setVisibility(View.INVISIBLE);
                Picasso.with(parent.getContext()).load(R.mipmap.self_add).into(holder.ivImage);
//                holder.ivImage.setImageResource(R.mipmap.self_add);
            } else {
                holder.ivDel.setVisibility(View.VISIBLE);
                File file = new File(image);
                Picasso.with(parent.getContext()).load(file).into(holder.ivImage);
            }
            holder.ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String image = mlist.get(position);
                    if ("1".equals(image)) {
                        if (onAddImageListener != null) {
                            onAddImageListener.onAddImage();
                        }
                    }
                }
            });
            holder.ivDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String image = mlist.get(position);
                    if (!"1".equals(image)) {
                        if (onAddImageListener != null) {
                            onAddImageListener.onDelImage(position);
                        }
                    }
                }
            });
        }

        return convertView;
    }

    public void setData(List<String> data) {
        this.mlist = data;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        @BindView(R.id.ivImage)
        ImageView ivImage;
        @BindView(R.id.ivDel)
        ImageView ivDel;


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
