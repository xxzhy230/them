package com.yijian.them.utils;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import java.text.DecimalFormat;
import java.util.Calendar;

public class StringUtils {
    public static String double6String(double dou) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.000000");
        return decimalFormat.format(dou);
    }

    /**
     * 动态设置View间距
     *
     * @param v 控件
     * @param l 左
     * @param t 上
     * @param r 右
     * @param b 下
     */
    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    public static String getAge(String date) {
        String[] split = date.split("-");
        String year = split[0];
        int age = getYear() - Integer.parseInt(year);
        return age + "";
    }

    /**
     * 获取年
     *
     * @return
     */
    public static int getYear() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.YEAR);
    }


    public static String getDis(String distance) {
        if (TextUtils.isEmpty(distance)) {
            return "0m";
        }
        double dis = Double.parseDouble(distance);
        if (dis > 1000) {
            return com.yqjr.utils.utils.StringUtils.double1String(dis / 1000) + "Km";
        } else {
            return com.yqjr.utils.utils.StringUtils.double1String(dis) + "m";
        }

    }
}
