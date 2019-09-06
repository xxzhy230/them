package com.yijian.them.ui.home.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yijian.them.R;
import com.yijian.them.common.Config;
import com.yijian.them.ui.home.GroupMoudle;
import com.yijian.them.ui.home.ReplyListMoudle;
import com.yijian.them.utils.JumpUtils;
import com.yijian.them.utils.Times;
import com.yijian.them.view.CircleImageView;
import com.yijian.them.view.MyListView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentListAdapter extends BaseAdapter {
    private List<GroupMoudle.DataBean> dataBeans = new ArrayList<>();

    @Override
    public int getCount() {
        return dataBeans.size();
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
            convertView = View.inflate(parent.getContext(), R.layout.item_comment_list, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GroupMoudle.DataBean dataBean = dataBeans.get(position);
        String content = dataBean.getContent();
        String fromAvatar = dataBean.getFromAvatar();
        String fromUname = dataBean.getFromUname();
        String createDte = dataBean.getCreateDte();
        try {
            holder.tvTime.setText(Times.getTime(createDte));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tvNickName1.setText(fromUname);
        holder.tvContent.setText(content);
        if (!TextUtils.isEmpty(fromAvatar)) {
            Picasso.with(parent.getContext()).load(fromAvatar).into(holder.civHead1);
        }
        List<ReplyListMoudle> replyList = dataBean.getReplyList();
        if (replyList!= null && replyList.size() > 0){
            holder.mlvComment.setVisibility(View.VISIBLE);
            CommentAdapter commentAdapter = new CommentAdapter(replyList);
            holder.mlvComment.setAdapter(commentAdapter);
        }else{
            holder.mlvComment.setVisibility(View.GONE);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupMoudle.DataBean dataBean = dataBeans.get(position);
                String dynamicId = dataBean.getDynamicId();
                Config.dataBean = dataBean;
                JumpUtils.jumpDynamicActivity(parent.getContext(),8,"",dynamicId);

            }
        });
        return convertView;
    }

    public void setData(List<GroupMoudle.DataBean> dataBeans) {
        this.dataBeans = dataBeans;
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
