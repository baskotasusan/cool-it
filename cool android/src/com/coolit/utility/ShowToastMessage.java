package com.coolit.utility;

import android.content.Context;
import android.widget.Toast;

public class ShowToastMessage {
	
	static Context ctx;
	
	public  ShowToastMessage(Context ctx)
	{
		ShowToastMessage.ctx=ctx;
	}
	
	public static void showToast(String msg)
	{
		Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
		
	}

}
