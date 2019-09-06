package com.yijian.them.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.yijian.them.MainActivity;
import com.yijian.them.common.Config;
import com.yijian.them.ui.home.activity.DynamicActivity;
import com.yijian.them.ui.home.activity.HotTopicInfoActivity;
import com.yijian.them.ui.home.activity.ReportActivity;
import com.yijian.them.ui.login.LoginActivity;
import com.yijian.them.ui.login.SplashActivity;
import com.yijian.them.ui.login.WelcomeActivity;
import com.yijian.them.ui.message.MessageActivity;
import com.yijian.them.ui.mine.activity.AboutActivity;
import com.yijian.them.ui.mine.activity.FollowerActivity;
import com.yijian.them.ui.mine.activity.MineActivity;
import com.yijian.them.ui.mine.activity.UserInfoActivity;
import com.yijian.them.ui.mine.activity.WebActivity;
import com.yijian.them.ui.team.TeamInfoActivity;

public class JumpUtils {
    /**
     * @param context
     * @param type    0 登录  1 注册  2 编辑信息
     */
    public static void jumpLoginActivity(Context context, int type, String phone, String password) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(Config.LOGINTYPE, type);
        intent.putExtra(Config.PASSWORD, password);
        intent.putExtra(Config.PHONE, phone);
        context.startActivity(intent);
    }

    /**
     * 跳转到主界面
     *
     * @param context
     */
    public static void jumpMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }


    /**
     * @param context
     * @param type    0 编辑信息  1 用户反馈  2 版本信息 3 关于我们 4 设置
     */
    public static void jumpMineActivity(Context context, int type, String title) {
        Intent intent = new Intent(context, MineActivity.class);
        intent.putExtra(Config.MINETYPE, type);
        intent.putExtra(Config.MINETITLE, title);
        context.startActivity(intent);
    }

    /**
     * @param context
     * @param type    0 编辑信息  1 用户反馈  2 版本信息 3 关于我们 4 设置
     */
    public static void jumpFollowerActivity(Context context, int type, String title, int userId) {
        Intent intent = new Intent(context, FollowerActivity.class);
        intent.putExtra(Config.MINETYPE, type);
        intent.putExtra(Config.MINETITLE, title);
        intent.putExtra(Config.USERID, userId);
        context.startActivity(intent);
    }

    /**
     * 跳转到主界面
     *
     * @param context
     * @param chatInfo
     */
    public static void jumpMessageActivity(Context context, int type, ChatInfo chatInfo) {
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(Config.MESSAGETYPE, type);
        intent.putExtra(Config.CHATINFO, chatInfo);
        context.startActivity(intent);
    }


    public static void jumpWebActivity(Context context, String title, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(Config.ABOUTTITLE, title);
        intent.putExtra(Config.ABOUTURL, url);
        context.startActivity(intent);
    }

    public static void jumpUserInfoActivity(Context context, int userId) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        intent.putExtra(Config.USERID, userId);
        context.startActivity(intent);
    }

    public static void jumpDynamicActivity(Context context, int type, String title, String dynamicId) {
        Intent intent = new Intent(context, DynamicActivity.class);
        intent.putExtra(Config.DYNAMICID, dynamicId);
        intent.putExtra(Config.DYNAMICTYPE, type);
        intent.putExtra(Config.DYNAMICTITLE, title);
        context.startActivity(intent);
    }

    public static void jumpDynamicActivity(Fragment fragment, int type, String title, String dynamicId) {
        Intent intent = new Intent(fragment.getContext(), DynamicActivity.class);
        intent.putExtra(Config.DYNAMICID, dynamicId);
        intent.putExtra(Config.DYNAMICTYPE, type);
        intent.putExtra(Config.DYNAMICTITLE, title);
        fragment.startActivityForResult(intent, type);
    }

    public static void jumpReportActivity(Context context, String dynamicId, int type, String reportedId, String commentId) {
        Intent intent = new Intent(context, ReportActivity.class);
        intent.putExtra(Config.DYNAMICID, dynamicId);
        intent.putExtra(Config.REPORTTYPE, type);
        intent.putExtra(Config.REPORTEDID, reportedId);
        intent.putExtra(Config.COMMENTID, commentId);
        context.startActivity(intent);
    }

    public static void jumpSplashActivity(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        context.startActivity(intent);
    }

    public static void jumpTeamInfoActivity(Context mContext, String teamId) {
        Intent intent = new Intent(mContext, TeamInfoActivity.class);
        intent.putExtra(Config.TEAMID, teamId);
        mContext.startActivity(intent);
    }

    public static void jumpHotTopicInfoActivity(Context mContext, String tagId) {
        Intent intent = new Intent(mContext, HotTopicInfoActivity.class);
        intent.putExtra(Config.TAGID, tagId);
        mContext.startActivity(intent);
    }
}
