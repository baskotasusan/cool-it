package com.coolit.library;

import java.net.URI;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

public class ServerRequest {

	private int METHOD_GET = 1;
	private int METHOD_POST = 2;

	/**
	 * post parameter contains List<NameValuePair> nameValuePairs = new
	 * ArrayList<NameValuePair>(); nameValuePairs.add(new
	 * BasicNameValuePair("image",image_str));
	 **/

	public String requestGetHttp(String url) {
		return requestHttp(METHOD_GET, url, null);
	}

	public String requestGetHttp(String url, List<NameValuePair> parameters) {
		return requestHttp(METHOD_GET,
				url + "?" + URLEncodedUtils.format(parameters, "utf-8"), null);
	}

	public String requestPostHttp(String url, List<NameValuePair> parameters) {
		return requestHttp(METHOD_POST, url, parameters);
	}

	private String requestHttp(int method, String url,
			List<NameValuePair> postParameters) {
		// Log.i("ServerRequest","url = " + url);
		String result = "";
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			if (method == METHOD_POST) {
				HttpPost postrequest = new HttpPost(url);
				UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
						postParameters);
				postrequest.setEntity(formEntity);
				HttpResponse postresponse = client.execute(postrequest);
				result = EntityUtils.toString(postresponse.getEntity());

			} else {
				HttpGet request = new HttpGet(new URI(url));
				HttpResponse response = client.execute(request);

				result = EntityUtils.toString(response.getEntity());

			}

			result = StringEscapeUtils.unescapeHtml4(result);

			// Log.i("ServerRequest", "result = " + result);
			return result;

		} catch (Exception e) {
			e.printStackTrace();
			return "{\"type\":\"connection error\"}";
		} finally {

		}
	}

	public static boolean isNetworkConnected(Context ctx) {
		ConnectivityManager cm = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			return false; // There are no active networks.
		} else
			return true;
	}

	/**
	 * Display a dialog that user has no internet connection
	 * 
	 * @param ctx1
	 * 
	 *            Code from:
	 *            http://osdir.com/ml/Android-Developers/2009-11/msg05044.html
	 */
	public static void showNoConnectionDialog(Context ctx1) {
		final Context ctx = ctx1;
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setCancelable(true);
		builder.setMessage("Internet Connection Not Available");
		builder.setTitle("Please Connect Internet Connection");
		builder.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						ctx.startActivity(new Intent(
								Settings.ACTION_WIRELESS_SETTINGS));
					}
				});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				});
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				return;
			}
		});

		builder.show();
	}

}
