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
import com.yijian.them.utils.JumpUtils;
import com.yijian.them.utils.Times;
import com.yijian.them.view.CircleImageView;
import com.yijian.them.view.MyListView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DynamicMessageAdapter extends BaseAdapter {
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_dynamic_message, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HomeMoudle.DataBean dataBean = mList.get(position);
        String fromUname = dataBean.getFromUname();
        String pushType = dataBean.getPushType();
        if ("2".equals(pushType)){
            holder.tvSystemTitle.setText(fromUname + " 赞了你的动态");
        }else if ("31".equals(pushType)){
            holder.tvSystemTitle.setText(fromUname + " 评论你的动态");
        }else if ("32".equals(pushType)){
            holder.tvSystemTitle.setText(fromUname + " 回复你的动态");
        }

        String commentContent = dataBean.getCommentContent();
        if (TextUtils.isEmpty(commentContent)) {
            holder.tvContent.setVisibility(View.GONE);
            holder.tvTime.setVisibility(View.GONE);
            holder.tvTimeNew.setVisibility(View.VISIBLE);
        } else {
            holder.tvContent.setText(commentContent);
            holder.tvContent.setVisibility(View.VISIBLE);
            holder.tvTime.setVisibility(View.VISIBLE);
            holder.tvTimeNew.setVisibility(View.GONE);
        }
        String pushAt = dataBean.getPushAt();
        try {
            holder.tvTime.setText(Times.getTime(pushAt) + "  回复");
            holder.tvTimeNew.setText(Times.getTime(pushAt) + "  回复");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dynamicImg = dataBean.getDynamicImg();
        if (TextUtils.isEmpty(dynamicImg)) {
            holder.ivDynamicImage.setVisibility(View.GONE);
        } else {
            Picasso.with(parent.getContext()).load(dynamicImg).into(holder.ivDynamicImage);
            holder.ivDynamicImage.setVisibility(View.VISIBLE);
        }
        String fromAvatar = dataBean.getFromAvatar();
        if (TextUtils.isEmpty(fromAvatar)) {
            holder.civHeadImage.setVisibility(View.GONE);
        } else {
            Picasso.with(parent.getContext()).load(fromAvatar).into(holder.civHeadImage);
            holder.civHeadImage.setVisibility(View.VISIBLE);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeMoudle.DataBean dataBean = mList.get(position);
                int dynamicId = dataBean.getDynamicId();
                JumpUtils.jumpDynamicActivity(parent.getContext(), 7, "", dynamicId + "");
            }
        });
        return convertView;
    }

    public void setData(List<HomeMoudle.DataBean> response) {
        mList = response;
        notifyDataSetChanged();
    }

    static
    class ViewHolder {
        @BindView(R.id.civHeadImage)
        CircleImageView civHeadImage;
        @BindView(R.id.tvSystemTitle)
        TextView tvSystemTitle;
        @BindView(R.id.tvContent)
        TextView tvContent;
        @BindView(R.id.ivDynamicImage)
        ImageView ivDynamicImage;
        @BindView(R.id.mlvComment)
        MyListView mlvComment;
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.tvTimeNew)
        TextView tvTimeNew;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
