package com.yijian.them.ui.message.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelectionModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.squareup.picasso.Picasso;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit;
import com.yijian.them.R;
import com.yijian.them.api.AuthApi;
import com.yijian.them.basic.BasicActivity;
import com.yijian.them.basic.BasicFragment;
import com.yijian.them.common.Config;
import com.yijian.them.ui.message.MessageActivity;
import com.yijian.them.ui.team.adapter.TeamMembersAdapter;
import com.yijian.them.ui.team.moudle.TeamInfoMoudle;
import com.yijian.them.utils.JumpUtils;
import com.yijian.them.utils.dialog.DialogOnitem;
import com.yijian.them.utils.dialog.SealectImageDialog;
import com.yijian.them.utils.http.CallBack;
import com.yijian.them.utils.http.Http;
import com.yijian.them.utils.http.JsonResult;
import com.yijian.them.view.CircleImageView;
import com.yqjr.utils.base.AppManager;
import com.yqjr.utils.spUtils.SPUtils;
import com.yqjr.utils.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageInfoFragment extends BasicFragment {
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvGroupName)
    TextView tvGroupName;
    @BindView(R.id.llGroupName)
    LinearLayout llGroupName;
    @BindView(R.id.llGroupRemark)
    LinearLayout llGroupRemark;
    @BindView(R.id.sGroupTop)
    Switch sGroupTop;
    @BindView(R.id.llReport)
    LinearLayout llReport;
    @BindView(R.id.tvOutGroup)
    TextView tvOutGroup;
    @BindView(R.id.tvTeamNum)
    TextView tvTeamNum;
    private ChatInfo chatInfo;
    private String groupId;
    private int type;// 1 队长  0 不是队长
    private boolean topConversation;
    //拍照
    private static final int PHOTO_REQUEST_CAREMA = 1;
    //相册
    public static final int SELECT_PHOTO = 2;
    private String[] reportArr = new String[]{"从手机相册选择", "拍照"};


    @Override
    protected void onClickEvent() {

    }

    @Override
    protected View getResourceView() {
        View view = View.inflate(getActivity(), R.layout.fragment_message_info, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView(Bundle bundle) {
        context = (BasicActivity) getActivity();
        if (chatInfo == null) {
            return;
        }
        String chatName = chatInfo.getChatName();
        tvGroupName.setText(chatName);
        groupId = chatInfo.getId();
        tvOutGroup.setText("解散群组");
        getGroupMembers();
        topConversation = ConversationManagerKit.getInstance().isTopConversation(groupId);
        if (topConversation) {
            sGroupTop.setChecked(true);
        } else {
            sGroupTop.setChecked(false);
        }
        sGroupTop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ConversationManagerKit.getInstance().setConversationTop(groupId, true);
                } else {
                    ConversationManagerKit.getInstance().setConversationTop(groupId, false);
                }
            }
        });
    }


    /**
     * 获取群组成员
     */
    private void getGroupMembers() {
        TIMGroupManager.getInstance().getGroupMembers(groupId, new TIMValueCallBack<List<TIMGroupMemberInfo>>() {
            @Override
            public void onError(int i, String s) {
                Log.d("群成员列表失败  ", s + "  状态码  " + i);
            }

            @Override
            public void onSuccess(List<TIMGroupMemberInfo> timGroupMemberInfos) {
                tvTeamNum.setText(timGroupMemberInfos.size() + "人");

                List<String> userIdList = new ArrayList<>();
                for (int i = 0; i < timGroupMemberInfos.size(); i++) {
                    TIMGroupMemberInfo timGroupMemberInfo = timGroupMemberInfos.get(i);
                    String userId = timGroupMemberInfo.getUser();
                    userIdList.add(userId);
                    int role = timGroupMemberInfo.getRole();
                    if (400 == role) {
                        type =1;
                        tvOutGroup.setText("解散群组");
                    } else {
                        type =0;
                        tvOutGroup.setText("退出群组");
                    }
                }
            }
        });
    }


    @OnClick({R.id.ivBack, R.id.llGroupName, R.id.llGroupRemark, R.id.llReport, R.id.llGroupHead,
            R.id.tvOutGroup, R.id.llMembers})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                getActivity().finish();
                break;
            case R.id.llGroupName:
                if (type == 1) {
                    JumpUtils.jumpMessageActivity(this, 7, chatInfo);
                } else {
                    ToastUtils.toastCenter(getActivity(), "只有队长可以修改群名称");
                }
                break;
            case R.id.llGroupRemark:
                if (type == 1) {
                    JumpUtils.jumpMessageActivity(getActivity(), 8, chatInfo);
                } else {
                    ToastUtils.toastCenter(getActivity(), "只有队长可以修改群公告");
                }
                break;
            case R.id.llGroupHead:
                if (type == 1) {
                    new SealectImageDialog(getActivity(), false, null, reportArr,
                            new DialogOnitem() {
                                public void onItemClickListener(int position) {
                                    switch (position) {
                                        case 1:
                                            select_photo();
                                            break;
                                        case 0:
                                            takePhoto();
                                        default:
                                            break;
                                    }
                                }
                            });
                } else {
                    ToastUtils.toastCenter(getActivity(), "只有队长可以修改群头像");
                }
                break;
            case R.id.llReport:
                JumpUtils.jumpReportActivity(getActivity(), chatInfo.getId(), 3, "", "");
                break;
            case R.id.tvOutGroup:
                if (type == 1) {//解散小队
                    delGroup();
                } else {//退出小队
                    outGroup();
                }
                break;
            case R.id.llMembers:
                JumpUtils.jumpMessageActivity(getActivity(), 6, chatInfo);
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == 7) {
            String teamName = data.getStringExtra("teamName");
            tvGroupName.setText(teamName);
        } else if (requestCode == PictureConfig.CHOOSE_REQUEST) {
            final List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            if (selectList != null && selectList.size() > 0) {
                LocalMedia localMedia = selectList.get(0);
                String compressPath = localMedia.getCompressPath();
                if (TextUtils.isEmpty(compressPath)) {
                    compressPath = localMedia.getPath();
                }
//                headFile = new File(compressPath);
//                editTeamImg();
            }
        }
    }
    /**
     * 退出群组
     */
    private void outGroup() {
        TIMGroupManager.getInstance().quitGroup(groupId, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                Log.d("退出群组失败: ", s + "   " + i);
            }

            @Override
            public void onSuccess() {
                SPUtils.putString(Config.DELTEAMCHAT, groupId);
                ToastUtils.toastCenter(getActivity(), "退出成功");
                AppManager.getAppManager().finishActivity(MessageActivity.class);
                getActivity().finish();
            }
        });
    }

    /**
     * 删除群组
     */
    private void delGroup() {
        TIMGroupManager.getInstance().deleteGroup(groupId, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                Log.d("解散群组失败: ", s + "   " + i);
            }

            @Override
            public void onSuccess() {
                SPUtils.putString(Config.DELTEAMCHAT, groupId);
                ToastUtils.toastCenter(getActivity(), "解散成功");
                AppManager.getAppManager().finishActivity(MessageActivity.class);
                getActivity().finish();
            }
        });
    }


    public void setChatInfo(ChatInfo chatInfo) {
        this.chatInfo = chatInfo;
    }




    /**
     * 从相册选择
     */
    private void select_photo() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                "android.permission.WRITE_EXTERNAL_STORAGE")
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(),
                "android.permission.READ_EXTERNAL_STORAGE")
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(),
                "android.permission.CAMERA")
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.CAMERA"},
                    SELECT_PHOTO);
        } else {
            openAlbum();
        }
    }

    private void openAlbum() {
        PictureSelectionModel pictureSelectionModel = PictureSelector.create(this).openGallery(PictureMimeType.ofImage());
        pictureSelectionModel.compress(true);
        pictureSelectionModel.selectionMode(PictureConfig.SINGLE);
        pictureSelectionModel.isCamera(false).
                imageFormat(PictureMimeType.PNG).
                minimumCompressSize(200).isGif(false).
                forResult(PictureConfig.CHOOSE_REQUEST);
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        if (ContextCompat.checkSelfPermission(getActivity(), "android.permission.WRITE_EXTERNAL_STORAGE")
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(),
                "android.permission.READ_EXTERNAL_STORAGE")
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(),
                "android.permission.CAMERA")
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.CAMERA"},
                    PHOTO_REQUEST_CAREMA);
        } else {
            openCamera();
        }
    }

    /**
     * 打开相机
     */
    public void openCamera() {
        //检查是否有拍照权限，以免崩溃
        if (ContextCompat.checkSelfPermission(getActivity(), "android.permission.CAMERA")
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{"android.permission.CAMERA"},
                    PHOTO_REQUEST_CAREMA);
            return;
        } else {
            PictureSelectionModel pictureSelectionModel = PictureSelector.create(this)
                    .openCamera(PictureMimeType.ofImage());
            pictureSelectionModel.compress(true);
            pictureSelectionModel.isCamera(false).
                    imageFormat(PictureMimeType.PNG).
                    minimumCompressSize(200).isGif(false).
                    forResult(PictureConfig.CHOOSE_REQUEST);
        }
    }

    /**
     * @param requestCode  申请码
     * @param permissions  申请的权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PHOTO_REQUEST_CAREMA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                ToastUtils.toastCenter(getActivity(), "请开启拍摄权限！");
            }
        } else if (requestCode == SELECT_PHOTO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openAlbum();
            } else {
                ToastUtils.toastCenter(getActivity(), "请开启权限！");
            }
        }
    }

}
