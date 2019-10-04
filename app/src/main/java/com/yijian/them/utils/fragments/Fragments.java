package com.yijian.them.utils.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.yijian.them.ui.home.HomeFragment;
import com.yijian.them.ui.home.fragment.AddressFragment;
import com.yijian.them.ui.home.fragment.CommentFragment;
import com.yijian.them.ui.home.fragment.CommentReplyFragment;
import com.yijian.them.ui.home.fragment.CreatGroupFragment;
import com.yijian.them.ui.home.fragment.CreatTagFragment;
import com.yijian.them.ui.home.fragment.GroupFragment;
import com.yijian.them.ui.home.fragment.HotTopicFragment;
import com.yijian.them.ui.home.fragment.SearchFragment;
import com.yijian.them.ui.home.fragment.SendDynamicFragment;
import com.yijian.them.ui.login.fragment.EditUserInfoFragment;
import com.yijian.them.ui.login.fragment.LoginFragment;
import com.yijian.them.ui.login.fragment.RegistFragment;
import com.yijian.them.ui.message.ChatFragment;
import com.yijian.them.ui.message.DynamicMessageFragment;
import com.yijian.them.ui.message.fragment.EditTeamFragment;
import com.yijian.them.ui.message.fragment.GroupInfoFragment;
import com.yijian.them.ui.message.MessageFragment;
import com.yijian.them.ui.message.fragment.MembersFragment;
import com.yijian.them.ui.message.fragment.MessageInfoFragment;
import com.yijian.them.ui.message.SystemMessageFragment;
import com.yijian.them.ui.message.TeamMessageFragment;
import com.yijian.them.ui.mine.MineFragment;
import com.yijian.them.ui.mine.fragment.AboutFragment;
import com.yijian.them.ui.mine.fragment.BlackListFragment;
import com.yijian.them.ui.mine.fragment.ChangePhoneFragment;
import com.yijian.them.ui.mine.fragment.CodeFragment;
import com.yijian.them.ui.mine.fragment.DongTaiFragment;
import com.yijian.them.ui.mine.fragment.EditFragment;
import com.yijian.them.ui.mine.fragment.EditInfoFragment;
import com.yijian.them.ui.mine.fragment.FeedBackFragment;
import com.yijian.them.ui.mine.fragment.FensiFragment;
import com.yijian.them.ui.mine.fragment.GuanzhuFragment;
import com.yijian.them.ui.mine.fragment.SetPwdFragment;
import com.yijian.them.ui.mine.fragment.SettingFragment;
import com.yijian.them.ui.mine.fragment.UserInfoFragment;
import com.yijian.them.ui.mine.fragment.VersionFragment;
import com.yijian.them.ui.mine.fragment.ZanFragment;
import com.yijian.them.ui.team.CreatTeamFragment;
import com.yijian.them.ui.team.TeamFragment;
import com.yijian.them.ui.team.TeamListFragment;

public class Fragments {
    private static Fragments fragments;

    private FragmentTransaction fragmentTransaction;
    private Fragment loginFragment;
    private Fragment registFragment;
    private Fragment editUserInfoFragment;


    private Fragment homeFragment;
    private Fragment teamFragment;
    private Fragment messageFragment;
    private Fragment mineFragment;

    private Fragment editInfoFragment;
    private Fragment aboutFragment;
    private Fragment feedbackFragment;
    private Fragment settingFragment;
    private Fragment versionFragment;
    private Fragment userIndoFragment;
    private Fragment dongTaiFragment;
    private ZanFragment zanFragment;
    private GuanzhuFragment guanZhuFragment;
    private FensiFragment fenSiFragment;
    private EditFragment editFragment;
    private SetPwdFragment setPwdFragment;
    private CodeFragment codeFragment;
    private ChangePhoneFragment changePhoneFragment;
    private BlackListFragment blackListFragment;

    private ChatFragment chartFragment;
    private GroupInfoFragment groupInfoFragment;
    private SendDynamicFragment sendDynamicFragment;
    private AddressFragment addressFragment;

    private GroupFragment groupFragment;
    private CreatGroupFragment creatGroupFragment;
    private HotTopicFragment hotTopicFragment;
    private CommentFragment commentFragment;
    private CommentReplyFragment commentReplyFragment;
    private SearchFragment searchFragment;
    private CreatTagFragment creatTagFragment;
    private TeamListFragment teamListFragment;
    private CreatTeamFragment creatTeamFragment;

    private SystemMessageFragment systemMessageFragment;
    private DynamicMessageFragment dynamicMessageFragment;
    private TeamMessageFragment teamMessageFragment;
    private MessageInfoFragment messageInfoFragment;
    private MembersFragment membersFragment;
    private EditTeamFragment editTeamFragment;

    public static Fragments init() {
        if (fragments == null) {
            fragments = new Fragments();
        }
        return fragments;
    }

    public void commitLogin(int type, FragmentManager fragmentManager, int frameLayout) {
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (type) {
            case 0://登录
                if (loginFragment == null) {
                    loginFragment = new LoginFragment();
                }
                fragmentTransaction.replace(frameLayout, loginFragment);
                break;
            case 1://注册
                if (registFragment == null) {
                    registFragment = new RegistFragment();
                }
                fragmentTransaction.replace(frameLayout, registFragment);
                break;
            case 3://设置个人信息
                if (editUserInfoFragment == null) {
                    editUserInfoFragment = new EditUserInfoFragment();

                }
                fragmentTransaction.replace(frameLayout, editUserInfoFragment);
                break;
        }
        fragmentTransaction.commit();
    }

    public void commitMain(int type, FragmentManager fragmentManager, int frameLayout) {
        fragmentTransaction = fragmentManager.beginTransaction();
        hintMain();
        switch (type) {
            case 0://首页
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    fragmentTransaction.add(frameLayout, homeFragment);
                }
                fragmentTransaction.show(homeFragment);
                break;
            case 1://小队
                if (teamFragment == null) {
                    teamFragment = new TeamFragment();
                    fragmentTransaction.add(frameLayout, teamFragment);
                }
                fragmentTransaction.show(teamFragment);
                break;
            case 2://消息
                if (messageFragment == null) {
                    messageFragment = new MessageFragment();
                    fragmentTransaction.add(frameLayout, messageFragment);
                }
                fragmentTransaction.show(messageFragment);
                break;
            case 3://我的
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                    fragmentTransaction.add(frameLayout, mineFragment);
                }
                fragmentTransaction.show(mineFragment);
                break;
        }
        fragmentTransaction.commit();
    }

    public void commitMine(int type, FragmentManager fragmentManager, int frameLayout) {

        fragmentTransaction = fragmentManager.beginTransaction();
        switch (type) {
            case 0://编辑资料
                if (editInfoFragment == null) {
                    editInfoFragment = new EditInfoFragment();
                }
                fragmentTransaction.replace(frameLayout, editInfoFragment);
                break;
            case 1://用户反馈
                if (feedbackFragment == null) {
                    feedbackFragment = new FeedBackFragment();
                }
                fragmentTransaction.replace(frameLayout, feedbackFragment);
                break;
            case 2://版本信息
                if (versionFragment == null) {
                    versionFragment = new VersionFragment();
                }
                fragmentTransaction.replace(frameLayout, versionFragment);
                break;
            case 3://关于Them
                if (aboutFragment == null) {
                    aboutFragment = new AboutFragment();
                }
                fragmentTransaction.replace(frameLayout, aboutFragment);
                break;
            case 4://设置
                if (settingFragment == null) {
                    settingFragment = new SettingFragment();
                }
                fragmentTransaction.replace(frameLayout, settingFragment);
                break;
            case 5://用户信息
                if (userIndoFragment == null) {
                    userIndoFragment = new UserInfoFragment();
                }
                fragmentTransaction.replace(frameLayout, userIndoFragment);
                break;
            case 6://动态
                if (dongTaiFragment == null) {
                    dongTaiFragment = new DongTaiFragment();
                }
                fragmentTransaction.replace(frameLayout, dongTaiFragment);
                break;
            case 7://赞
                if (zanFragment == null) {
                    zanFragment = new ZanFragment();
                }
                fragmentTransaction.replace(frameLayout, zanFragment);
                break;
            case 8://关注
                if (guanZhuFragment == null) {
                    guanZhuFragment = new GuanzhuFragment();
                }
                fragmentTransaction.replace(frameLayout, guanZhuFragment);
                break;
            case 9://粉丝
                if (fenSiFragment == null) {
                    fenSiFragment = new FensiFragment();
                }
                fragmentTransaction.replace(frameLayout, fenSiFragment);
                break;
            case 10://修改昵称
                if (editFragment == null) {
                    editFragment = new EditFragment();

                }
                editFragment.setType(1);
                fragmentTransaction.replace(frameLayout, editFragment);
                break;
            case 11://修改签名
                if (editFragment == null) {
                    editFragment = new EditFragment();
                }
                editFragment.setType(2);
                fragmentTransaction.replace(frameLayout, editFragment);
                break;
            case 12://设置密码
                if (editFragment == null) {
                    setPwdFragment = new SetPwdFragment();
                }
                if (setPwdFragment == null) {
                    return;
                }
                setPwdFragment.setType(0);
                fragmentTransaction.replace(frameLayout, setPwdFragment);
                break;
            case 13://忘记密码
                if (setPwdFragment == null) {
                    setPwdFragment = new SetPwdFragment();
                }
                if (setPwdFragment == null) {
                    return;
                }
                setPwdFragment.setType(1);
                fragmentTransaction.replace(frameLayout, setPwdFragment);
                break;
            case 14://聊天
                if (chartFragment == null) {
                    chartFragment = new ChatFragment();
                }
                fragmentTransaction.replace(frameLayout, chartFragment);
                break;
            case 15://获取验证码
                if (codeFragment == null) {
                    codeFragment = new CodeFragment();
                }
                codeFragment.setType(0);
                fragmentTransaction.replace(frameLayout, codeFragment);
                break;
            case 16://安全验证
                if (codeFragment == null) {
                    codeFragment = new CodeFragment();
                }
                codeFragment.setType(1);
                fragmentTransaction.replace(frameLayout, codeFragment);
                break;
            case 17://修改手机号码
                if (changePhoneFragment == null) {
                    changePhoneFragment = new ChangePhoneFragment();
                }
                fragmentTransaction.replace(frameLayout, changePhoneFragment);
                break;
            case 18://黑名单管理
                if (blackListFragment == null) {
                    blackListFragment = new BlackListFragment();
                }
                fragmentTransaction.replace(frameLayout, blackListFragment);
                break;
        }
        fragmentTransaction.commit();
    }

    public void commitFollower(int type, FragmentManager fragmentManager, int frameLayout, int userId) {

        fragmentTransaction = fragmentManager.beginTransaction();
        switch (type) {
            case 7://赞
                if (zanFragment == null) {
                    zanFragment = new ZanFragment();
                }
                zanFragment.setUserId(userId);
                fragmentTransaction.replace(frameLayout, zanFragment);
                break;
            case 8://关注
                if (guanZhuFragment == null) {
                    guanZhuFragment = new GuanzhuFragment();
                }
                guanZhuFragment.setUserId(userId);
                fragmentTransaction.replace(frameLayout, guanZhuFragment);
                break;
            case 9://粉丝
                if (fenSiFragment == null) {
                    fenSiFragment = new FensiFragment();
                }
                fenSiFragment.setUserId(userId);
                fragmentTransaction.replace(frameLayout, fenSiFragment);
                break;

        }
        fragmentTransaction.commit();
    }

    public void commitMessage(int type, FragmentManager fragmentManager, int frameLayout, ChatInfo chatInfo) {
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (type) {
            case 0://聊天
                if (chartFragment == null) {
                    chartFragment = new ChatFragment();
                }
                chartFragment.setChatInfo(chatInfo);
                fragmentTransaction.replace(frameLayout, chartFragment);
                break;
            case 1://群设置
                if (groupInfoFragment == null) {
                    groupInfoFragment = new GroupInfoFragment();
                }
                groupInfoFragment.setChatInfo(chatInfo);
                fragmentTransaction.replace(frameLayout, groupInfoFragment);
                break;
            case 2://系统消息
                if (systemMessageFragment == null) {
                    systemMessageFragment = new SystemMessageFragment();
                }
                fragmentTransaction.replace(frameLayout, systemMessageFragment);
                break;
            case 3://评论和赞消息
                if (dynamicMessageFragment == null) {
                    dynamicMessageFragment = new DynamicMessageFragment();
                }
                fragmentTransaction.replace(frameLayout, dynamicMessageFragment);
                break;
            case 4://小队消息
                if (teamMessageFragment == null) {
                    teamMessageFragment = new TeamMessageFragment();
                }
                fragmentTransaction.replace(frameLayout, teamMessageFragment);
                break;
            case 5://消息设置
                if (messageInfoFragment == null) {
                    messageInfoFragment = new MessageInfoFragment();
                }
                messageInfoFragment.setChatInfo(chatInfo);
                fragmentTransaction.replace(frameLayout, messageInfoFragment);
                break;
            case 6://群成员列表
                if (membersFragment == null) {
                    membersFragment = new MembersFragment();
                }
                membersFragment.setChatInfo(chatInfo);
                fragmentTransaction.replace(frameLayout, membersFragment);
                break;
            case 7://修改小队名称
                if (editTeamFragment == null) {
                    editTeamFragment = new EditTeamFragment();
                }
                editTeamFragment.setChatInfo(chatInfo, 7);
                fragmentTransaction.replace(frameLayout, editTeamFragment);
                break;
            case 8://修改小队描述
                if (editTeamFragment == null) {
                    editTeamFragment = new EditTeamFragment();
                }
                editTeamFragment.setChatInfo(chatInfo, 8);
                fragmentTransaction.replace(frameLayout, editTeamFragment);
                break;
        }
        fragmentTransaction.commit();
    }

    public void commitDynamic(int type, FragmentManager fragmentManager, int frameLayout, String dynamicId) {
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (type) {
            case 1://聊天
                if (chartFragment == null) {
                    chartFragment = new ChatFragment();
                }
                fragmentTransaction.replace(frameLayout, chartFragment);
                break;
            case 2://发送动态
                if (sendDynamicFragment == null) {
                    sendDynamicFragment = new SendDynamicFragment();
                }
                fragmentTransaction.replace(frameLayout, sendDynamicFragment);
                break;
            case 3://选择位置
                if (addressFragment == null) {
                    addressFragment = new AddressFragment();
                }
                fragmentTransaction.replace(frameLayout, addressFragment);
                break;
            case 4://话题
                if (hotTopicFragment == null) {
                    hotTopicFragment = new HotTopicFragment();
                }
                fragmentTransaction.replace(frameLayout, hotTopicFragment);
                break;
            case 5://小队
                if (groupFragment == null) {
                    groupFragment = new GroupFragment();
                }
                fragmentTransaction.replace(frameLayout, groupFragment);
                break;
            case 6://创建群聊
                if (creatGroupFragment == null) {
                    creatGroupFragment = new CreatGroupFragment();
                }
                fragmentTransaction.replace(frameLayout, creatGroupFragment);
                break;
            case 7://评论
                if (commentFragment == null) {
                    commentFragment = new CommentFragment();
                }
                commentFragment.setDynamicId(dynamicId);
                fragmentTransaction.replace(frameLayout, commentFragment);
                break;
            case 8://回复
                if (commentReplyFragment == null) {
                    commentReplyFragment = new CommentReplyFragment();
                }
                commentReplyFragment.setDynamicId(dynamicId);
                fragmentTransaction.replace(frameLayout, commentReplyFragment);
                break;
            case 9://搜索
                if (searchFragment == null) {
                    searchFragment = new SearchFragment();
                }
                fragmentTransaction.replace(frameLayout, searchFragment);
                break;
            case 10://创建话题
                if (creatTagFragment == null) {
                    creatTagFragment = new CreatTagFragment();
                }
                fragmentTransaction.replace(frameLayout, creatTagFragment);
                break;


        }
        fragmentTransaction.commit();
    }


    public void commitTeam(int type, FragmentManager fragmentManager, int frameLayout, String teamId) {
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (type) {
            case 1://参与小队列表
                if (teamListFragment == null) {
                    teamListFragment = new TeamListFragment();
                }
                fragmentTransaction.replace(frameLayout, teamListFragment);
                break;
            case 2://创建小队
                if (creatTeamFragment == null) {
                    creatTeamFragment = new CreatTeamFragment();
                }
                creatTeamFragment.setTeamId(teamId);
                fragmentTransaction.replace(frameLayout, creatTeamFragment);

        }
        fragmentTransaction.commit();
    }


    private void hintMain() {
        if (homeFragment != null) {
            fragmentTransaction.hide(homeFragment);
        }
        if (teamFragment != null) {
            fragmentTransaction.hide(teamFragment);
        }
        if (messageFragment != null) {
            fragmentTransaction.hide(messageFragment);
        }
        if (mineFragment != null) {
            fragmentTransaction.hide(mineFragment);
        }
    }

    public Fragment getFragment(int type) {
        switch (type) {
            case 0:
                return editInfoFragment;
            case 10:
                return editFragment;
            case 11:
                return editFragment;
            case 1:
                return feedbackFragment;

        }
        return null;
    }

    public void finish() {
        mineFragment = null;
        teamFragment = null;
        messageFragment = null;
        homeFragment = null;
    }
}
