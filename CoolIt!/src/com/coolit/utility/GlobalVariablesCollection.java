package com.coolit.utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;

public class GlobalVariablesCollection {

	// ProgressDialog instance for loading display
	public static Dialog globalPD;

	public static Context ctx;

	public static int appWidth;

	public static int appHeight;

	public static float scale;

	static Activity thisActivity;

	// For Other Profile View
	public static boolean otherProfileViewClicked = false;

	public GlobalVariablesCollection(Context ctx, Activity thisActivity) {

		GlobalVariablesCollection.ctx = ctx;

		GlobalVariablesCollection.thisActivity = thisActivity;

		DisplayMetrics dm = new DisplayMetrics();

		scale = ctx.getResources().getDisplayMetrics().density;

		thisActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);

		appWidth = dm.widthPixels;

		appHeight = dm.heightPixels;

	}

}
