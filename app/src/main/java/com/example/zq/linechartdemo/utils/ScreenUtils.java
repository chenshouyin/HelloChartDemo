package com.example.zq.linechartdemo.utils;

import android.content.Context;
import android.util.DisplayMetrics;

public class ScreenUtils {
	public static int getScreenWidth(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		int w_screen = dm.widthPixels;
		return w_screen;
	}

	public static int getScreenHeight(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		int h_screen = dm.heightPixels;
		return h_screen;
	}
	


	public static int getScreenHeightWithoutStatiusBar(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		int h_screen = dm.heightPixels;
		return h_screen-getStatusBarHeight(context);
	}

	public static int getStatusBarHeight(Context context){

		int statusBarHeight1 = -1;  
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {  
		    statusBarHeight1 = context.getResources().getDimensionPixelSize(resourceId);
		}
		return statusBarHeight1;  
	}
}
