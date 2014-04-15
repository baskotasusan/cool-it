package com.coolit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.coolit.utility.ShowToastMessage;

public class SplashScreenActivity extends Activity {

	private static final int SPLASH_TIME = 1000;// in milliseconds

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		// Assign Context to ShowToastMessageClass Context ctx object;
		new ShowToastMessage(getApplicationContext());

		try {
			new Handler().postDelayed(new Runnable() {

				public void run() {

					// Make Intent
					Intent mCardIntent = new Intent(SplashScreenActivity.this,
							MainActivity.class);
					mCardIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

					startActivity(mCardIntent);

					overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
					finish();

				}

			}, SPLASH_TIME);

		} catch (Exception e) {
		}

	}

}
