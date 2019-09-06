package com.yijian.them.ui.home.adapter;

import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yijian.them.R;
import com.yijian.them.ui.home.ReplyListMoudle;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentAdapter extends BaseAdapter {
    private List<ReplyListMoudle> replyList;

    public CommentAdapter(List<ReplyListMoudle> replyList) {
        this.replyList = replyList;
    }

    @Override
    public int getCount() {
        if (replyList.size() > 2) {
            return 2;
        } else {
            return replyList.size();
        }
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
            convertView = View.inflate(parent.getContext(), R.layout.item_comment, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ReplyListMoudle replyListMoudle = replyList.get(position);
        String content = replyListMoudle.getContent();
        String fromUname = replyListMoudle.getFromUname();
        int replyType = replyListMoudle.getReplyType();
        if (replyType == 0) {
            holder.tvNickName1.setText(fromUname + ": ");
            holder.tvContent.setText(content);
        } else {
            String str = "<font color='#999999'>" + fromUname + "</font>回复<font color='#999999'>@+" + replyListMoudle.getToUname() + "</font>:<font color='#333333'>" + content + "</font>";
            holder.tvContent.setText(Html.fromHtml(str));
        }

        if (replyList.size() > 2) {
            if (position == 1) {
                holder.tvCommentNum.setVisibility(View.VISIBLE);
                holder.tvCommentNum.setText(replyList.size() - 2 + "条回复");
            } else {
                holder.tvCommentNum.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tvNickName1)
        TextView tvNickName1;
        @BindView(R.id.tvContent)
        TextView tvContent;
        @BindView(R.id.tvCommentNum)
        TextView tvCommentNum;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
