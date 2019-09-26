package com.yijian.them.ui.home.adapter;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

public class CommentReplyListAdapter extends BaseAdapter {
    private List<HomeMoudle.DataBean> dataBeans = new ArrayList<>();

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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_comment_list, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HomeMoudle.DataBean dataBean = dataBeans.get(position);
        String content = dataBean.getContent();
        String fromAvatar = dataBean.getFromAvatar();
        String fromUname = dataBean.getFromUname();
        String createDte = dataBean.getCreateDte();
        int replyType = dataBean.getReplyType();
        if (replyType == 0) {
            holder.tvContent.setText(content);
        } else {
            String str = "回复<font color='#999999'>@" + dataBean.getToUname() + "</font>:<font color='#333333'>" + content + "</font>";
            holder.tvContent.setText(Html.fromHtml(str));
        }
        try {
            holder.tvTime.setText(Times.getTime(createDte));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tvNickName1.setText(fromUname);
//        holder.tvContent.setText(content);
        if (!TextUtils.isEmpty(fromAvatar)) {
            Picasso.with(parent.getContext()).load(fromAvatar).into(holder.civHead1);
        }
//        List<ReplyListMoudle> replyList = dataBean.getReplyList();
//        if (replyList != null && replyList.size() > 0) {
//            holder.mlvComment.setVisibility(View.VISIBLE);
//            CommentAdapter commentAdapter = new CommentAdapter(replyList);
//            holder.mlvComment.setAdapter(commentAdapter);
//        } else {
        holder.mlvComment.setVisibility(View.GONE);
//        }
        holder.civHead1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeMoudle.DataBean dataBean = dataBeans.get(position);
                String fromUid = dataBean.getFromUid();
                JumpUtils.jumpUserInfoActivity(parent.getContext(), Integer.parseInt(fromUid));
            }
        });
        return convertView;
    }

    public void setData(List<HomeMoudle.DataBean> dataBeans) {
        this.dataBeans = dataBeans;
        notifyDataSetChanged();
    }

    public void delComment(HomeMoudle.DataBean dataBean) {
        dataBeans.remove(dataBean);
        notifyDataSetChanged();
    }


    static class ViewHolder {
        @BindView(R.id.civHead1)
        CircleImageView civHead1;
        @BindView(R.id.tvNickName1)
        TextView tvNickName1;
        @BindView(R.id.tvContent)
        TextView tvContent;
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.mlvComment)
        MyListView mlvComment;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
