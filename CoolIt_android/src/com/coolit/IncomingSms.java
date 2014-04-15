package com.coolit;

import com.coolit.fragment.OtherPageFragment;
import com.coolit.library.OfflineDbHandler;
import com.coolit.utility.ShowToastMessage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class IncomingSms extends BroadcastReceiver {

	// Get the object of SmsManager
	final SmsManager sms = SmsManager.getDefault();

	public void onReceive(Context context, Intent intent) {

		// Retrieves a map of extended data from the intent.
		final Bundle bundle = intent.getExtras();

		try {

			if (bundle != null) {

				final Object[] pdusObj = (Object[]) bundle.get("pdus");

				for (int i = 0; i < pdusObj.length; i++) {

					SmsMessage currentMessage = SmsMessage
							.createFromPdu((byte[]) pdusObj[i]);
					String phoneNumber = currentMessage
							.getDisplayOriginatingAddress();

					String senderNum = phoneNumber;
					String message = currentMessage.getDisplayMessageBody();

					Log.i("SmsReceiver", "senderNum: " + senderNum
							+ "; message: " + message);

					// Show Alert
					int duration = Toast.LENGTH_LONG;
					Toast toast = Toast.makeText(context, "senderNum: "
							+ senderNum + ", message: " + message, duration);

					if (senderNum.equalsIgnoreCase("+9779804235570")) {

						try {

							int tempPos = message.indexOf("Temp:");

							int humPos = message.indexOf("Hum:");

							String temp = message
									.substring(tempPos + 5, humPos);

							String hum = message.substring(humPos + 4);

							ShowToastMessage
									.showToast("Successfull Get Information"
											+ "Temperature=" + temp
											+ "\tHumidity=" + hum);

							OfflineDbHandler odb = new OfflineDbHandler(context);

							odb.addData("sys", "temp", temp);

							odb.addData("sys", "hum", hum);

							odb.addData("sys", "location", "Thapathali");

							new MainActivity().calSaveSystemData(temp, hum, "");

						} catch (Exception e) {

						}

					}

				} // end for loop
			} // bundle is null

		} catch (Exception e) {

			Log.e("SmsReceiver", "Exception smsReceiver" + e);

		}
	}
}
