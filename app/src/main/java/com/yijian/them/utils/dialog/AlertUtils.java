package com.yijian.them.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import com.yijian.them.R;


/**
 *
 * @description 自定义公共弹窗
 */
public class AlertUtils {
	private static Dialog pg = null;
	private static Context conext;

	/*
	 * 显示进度条
	 */
	public static void showProgress(boolean isCancel, Context cxt) {
		if (pg == null || (conext != null && conext.hashCode() != cxt.hashCode())) {
			conext = cxt;
			pg = new Dialog(cxt, R.style.myDialog);
			pg.setContentView(R.layout.progress_dialog_small);
			pg.setCanceledOnTouchOutside(false);// 按返回键不能退出
			pg.setOnDismissListener(new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					dismissProgress();
				}
			});
		}
		if (null != pg &&!pg.isShowing()) {
			pg.setCancelable(isCancel);
			pg.show();
		}
	}

	/*
	 * 进度条消失
	 */
	public static void dismissProgress() {
		if (pg != null)
			if (pg.isShowing())
				pg.dismiss();
	}
	/*
	 * 显示进度条
	 */
	public static void showProgress(boolean isCancel, Context cxt, final long time) {
		if (pg == null || (conext != null && conext.hashCode() != cxt.hashCode())) {
			conext = cxt;
			pg = new Dialog(cxt, R.style.myDialog);
			pg.setContentView(R.layout.progress_dialog_small);
			pg.setCanceledOnTouchOutside(false);// 按返回键不能退出
			pg.setOnDismissListener(new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					// stopThread();//对线程进行销毁操作等
					dismissProgress();
				}
			});
		}
		if (null != pg &&!pg.isShowing()) {
			pg.setCancelable(isCancel);
			pg.show();
		}
		new Thread(){
			@Override
			public void run() {
				super.run();
				try {
					Thread.sleep(time);//休眠3秒
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				dismissProgress();
			}
		}.start();
	}
}
