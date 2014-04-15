package com.coolit.utility;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.coolit.R;

public class CustomProgressBarDialog {

	public static Dialog showProgressDialog(Activity thisActivity) {

		Dialog customProgressDialog;

		customProgressDialog = new Dialog(thisActivity);
		customProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		customProgressDialog
				.setContentView(R.layout.custom_progress_dialog_box);

		customProgressDialog.setCancelable(true);

		// for Rounded Corner background Transparent
		customProgressDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));

		customProgressDialog.show();

		return customProgressDialog;

	}

}
