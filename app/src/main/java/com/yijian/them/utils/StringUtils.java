package com.yijian.them.utils;

import android.view.View;
import android.view.ViewGroup;

import java.text.DecimalFormat;

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


}
