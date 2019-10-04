package com.yijian.them.ui.mine.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.yijian.them.R;
import com.yijian.them.api.AuthApi;
import com.yijian.them.ui.mine.moudel.Follwermoudel;
import com.yijian.them.utils.JumpUtils;
import com.yijian.them.utils.dialog.HintDialog;
import com.yijian.them.view.CircleImageView;
import com.yqjr.utils.service.OkHttp;
import com.yqjr.utils.service.StringJsonCallBack;
import com.yqjr.utils.spUtils.SPUtils;
import com.yqjr.utils.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class FollwerAdapter extends BaseAdapter {
    private List<Follwermoudel.DataBean> data = new ArrayList<>();
    private int type = 1; // 1 关注 2 粉丝 3 黑名单
    private OnDelBlackListener onDelBlackListener;

    public void setOnDelBlackListener(OnDelBlackListener onDelBlackListener) {
        this.onDelBlackListener = onDelBlackListener;
    }

    public FollwerAdapter(int type) {
        this.type = type;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void setData(List<Follwermoudel.DataBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_follower, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Follwermoudel.DataBean dataBean = data.get(position);
        String gender = dataBean.getGender();
        String nickName = dataBean.getNickName();
        String realImg = dataBean.getRealImg();
        if (!TextUtils.isEmpty(realImg)) {
            Picasso.with(parent.getContext()).load(realImg).into(holder.civHead);
        }
        if (!TextUtils.isEmpty(nickName)) {
            holder.tvNickName.setText(nickName);
        }
        if ("0".equals(gender)) {
            holder.ivSex.setImageResource(R.mipmap.self_girl);
        } else {
            holder.ivSex.setImageResource(R.mipmap.self_boy);
        }
        if (type == 3) {
            holder.tvNoFollower.setVisibility(View.GONE);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Follwermoudel.DataBean dataBean = data.get(position);
                    showDel(parent.getContext(), dataBean);
                }
            });
        } else {
            boolean followed = dataBean.isFollowed();
            if (followed) {
                holder.tvNoFollower.setTextColor(parent.getContext().getResources().getColor(R.color.white));
                holder.tvNoFollower.setBackgroundResource(R.drawable.shape_follower_yes_bg);
                holder.tvNoFollower.setText("已关注");
                holder.tvNoFollower.setTextColor(parent.getContext().getResources().getColor(R.color.color_FF666666));
                holder.tvNoFollower.setBackgroundResource(R.drawable.shape_follower_no_bg);
            } else {
                holder.tvNoFollower.setTextColor(parent.getContext().getResources().getColor(R.color.white));
                holder.tvNoFollower.setBackgroundResource(R.drawable.shape_follower_yes_bg);
                holder.tvNoFollower.setText("关注");
            }
        }
        holder.tvNoFollower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Follwermoudel.DataBean dataBean = data.get(position);
                isfollow(parent.getContext(), dataBean);
            }
        });
        holder.civHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Follwermoudel.DataBean dataBean = data.get(position);
                int userId = dataBean.getUserId();
                JumpUtils.jumpUserInfoActivity(parent.getContext(), userId);
            }
        });
        return convertView;
    }

    private void showDel(Context context, final Follwermoudel.DataBean dataBean) {
        String nickName = dataBean.getNickName();
        HintDialog hintDialog = new HintDialog(context);
        hintDialog.setMessage("确定将'" + nickName + "'从黑名单中移除?", true);
        hintDialog.setOnCommitListener(new HintDialog.OnCommitListener() {
            @Override
            public void onCommit() {
                if (onDelBlackListener != null) {
                    onDelBlackListener.onDelBlack(dataBean);
                }
            }
        });
        hintDialog.show();
    }

    /**
     * 黑名单列表移除
     *
     * @param dataBean
     */
    public void delItem(Follwermoudel.DataBean dataBean) {
        if (data != null) {
            data.remove(dataBean);
            notifyDataSetChanged();
        }
    }

    static class ViewHolder {
        @BindView(R.id.civHead)
        CircleImageView civHead;
        @BindView(R.id.tvNickName)
        TextView tvNickName;
        @BindView(R.id.ivSex)
        ImageView ivSex;
        @BindView(R.id.tvNoFollower)
        TextView tvNoFollower;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private void isfollow(final Context mContext, final Follwermoudel.DataBean dataBean) {
        int userId = dataBean.getUserId();
        OkHttp.post().addHeader("token", SPUtils.getToken()).url(AuthApi.FOLLOW + userId)
                .build().execute(new StringJsonCallBack() {
            @Override
            public void onError(Call call, Exception e, int i) {
                super.onError(call, e, i);
            }

            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                System.out.println("关注列表 : " + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int code = jsonObject.getInt("code");
                    String msg = jsonObject.getString("msg");
                    if (code == 200) {
                        if (type == 1) {
                            data.remove(dataBean);
                        } else if (type == 2) {
                            boolean followed = dataBean.isFollowed();
                            dataBean.setFollowed(!followed);
                        }

                        notifyDataSetChanged();
                    } else {
                        ToastUtils.toastCenter(mContext, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 移除黑名单监听
     */
    public interface OnDelBlackListener {
        void onDelBlack(Follwermoudel.DataBean dataBean);
    }
}
