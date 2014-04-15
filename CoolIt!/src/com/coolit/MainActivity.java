package com.coolit;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coolit.adapter.HomePageViewPagerAdapter;
import com.coolit.library.OfflineDbHandler;
import com.coolit.library.ServerRequest;
import com.coolit.utility.JSONWeatherParser;
import com.coolit.utility.ShowToastMessage;
import com.coolit.utility.WeatherHttpClient;
import com.handmark.pulltorefresh.extras.viewpager.PullToRefreshViewPager;
import com.survivingwithandroid.weatherapp.model.Weather;

public class MainActivity extends FragmentActivity implements OnClickListener {

	PullToRefreshViewPager pulltoRefreshViewPager;

	HomePageViewPagerAdapter homePageViewPagerAdapter;

	LinearLayout homeLL, nearByLL, suggestLL;

	TextView homeTV, nearByTV, suggestTV;

	View homeView, nearByView, suggestView;

	TextView topBarHeading;

	public static Activity thisActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		thisActivity = MainActivity.this;

		// take references for Menbu Bar
		takeReferencesMenuBar();

		// Take References for Pull for view pager
		pulltoRefreshViewPager = (PullToRefreshViewPager) findViewById(R.id.pulltoRefreshViewPager);

		homePageViewPagerAdapter = new HomePageViewPagerAdapter(
				getApplicationContext(), getSupportFragmentManager());

		pulltoRefreshViewPager.getRefreshableView().setAdapter(
				homePageViewPagerAdapter);

		pulltoRefreshViewPager.getRefreshableView().setOffscreenPageLimit(2);

		pulltoRefreshViewPager.getRefreshableView().setOnPageChangeListener(
				new OnPageChangeListener() {

					@Override
					public void onPageSelected(int position) {
						// TODO Auto-generated method stub

						if (position == 0) {

							disableAllTab();

							homeTV.setTextAppearance(getApplicationContext(),
									R.style.mediumTextSizeWidthBold);

							homeView.setVisibility(View.VISIBLE);

						} else if (position == 1) {

							disableAllTab();

							nearByTV.setTextAppearance(getApplicationContext(),
									R.style.mediumTextSizeWidthBold);

							nearByView.setVisibility(View.VISIBLE);

						} else if (position == 2) {

							disableAllTab();

							suggestTV.setTextAppearance(
									getApplicationContext(),
									R.style.mediumTextSizeWidthBold);

							suggestView.setVisibility(View.VISIBLE);

						}

					}

					@Override
					public void onPageScrolled(int position,
							float positionOffset, int positionOffsetPixels) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onPageScrollStateChanged(int state) {
						// TODO Auto-generated method stub

					}
				});

		topBarHeading = (TextView) findViewById(R.id.topBarHeading);

		topBarHeading.setText("Cool it!");

		// JSONWeatherTask task = new JSONWeatherTask();
		// task.execute(new String[] { "Bhaktapur" });

		// JSONWeatherTask task1 = new JSONWeatherTask();
		// task1.execute(new String[] { "Lalitpur" });

	}

	// take references for Menbu Bar
	public void takeReferencesMenuBar() {

		homeLL = (LinearLayout) findViewById(R.id.homeLL);
		nearByLL = (LinearLayout) findViewById(R.id.nearByLL);
		suggestLL = (LinearLayout) findViewById(R.id.suggestLL);

		homeLL.setOnClickListener(this);
		nearByLL.setOnClickListener(this);
		suggestLL.setOnClickListener(this);

		homeTV = (TextView) findViewById(R.id.homeTV);
		nearByTV = (TextView) findViewById(R.id.nearByTV);
		suggestTV = (TextView) findViewById(R.id.suggestTV);

		homeView = (View) findViewById(R.id.homeView);
		nearByView = (View) findViewById(R.id.nearByView);
		suggestView = (View) findViewById(R.id.suggestView);

		homeTV.setTextAppearance(getApplicationContext(),
				R.style.mediumTextSizeWidthBold);
		nearByTV.setTextAppearance(getApplicationContext(),
				R.style.mediumTextSize);
		suggestTV.setTextAppearance(getApplicationContext(),
				R.style.mediumTextSize);

		homeView.setVisibility(View.VISIBLE);

		nearByView.setVisibility(View.GONE);

		suggestView.setVisibility(View.GONE);

	}

	public void disableAllTab() {

		homeTV.setTextAppearance(getApplicationContext(),
				R.style.mediumTextSize);
		nearByTV.setTextAppearance(getApplicationContext(),
				R.style.mediumTextSize);
		suggestTV.setTextAppearance(getApplicationContext(),
				R.style.mediumTextSize);

		homeView.setVisibility(View.GONE);

		nearByView.setVisibility(View.GONE);

		suggestView.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {

		case R.id.homeLL:

			pulltoRefreshViewPager.getRefreshableView().setCurrentItem(0, true);

			break;

		case R.id.nearByLL:

			pulltoRefreshViewPager.getRefreshableView().setCurrentItem(1, true);

			break;

		case R.id.suggestLL:

			pulltoRefreshViewPager.getRefreshableView().setCurrentItem(2, true);

			break;

		}

	}

	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	//
	// // TODO Auto-generated method stub
	//
	// super.onActivityResult(requestCode, resultCode, data);
	//
	// if (resultCode == RESULT_OK) {
	//
	// if (requestCode == 101) {
	//
	// // set the width variable to the returned data
	//
	// ShowToastMessage.showToast("Ok");
	//
	// }
	// } else {
	//
	// finish();
	// }
	//
	// }

	// For Get Json Details Data
	public void calSaveSystemData(String temp, String humidity, String location) {

		OfflineDbHandler odb = new OfflineDbHandler(getApplicationContext());

		if (location.length() == 0) {
			location = odb.getData("current_location", "displayName");
		}

		List<NameValuePair> postParamsValues = new ArrayList<NameValuePair>();
		postParamsValues.add(new BasicNameValuePair("temp", temp));
		postParamsValues.add(new BasicNameValuePair("humidity", humidity));
		postParamsValues.add(new BasicNameValuePair("location", location));

		if (ServerRequest.isNetworkConnected(getApplicationContext()))
			new SaveSysDataAsyncTask(postParamsValues, "").execute();

	}

	public class SaveSysDataAsyncTask extends AsyncTask<Void, Void, String> {
		List<NameValuePair> postParamsValues;
		String id;

		public SaveSysDataAsyncTask(List<NameValuePair> articlesPostValues2,
				String id2) {
			this.postParamsValues = articlesPostValues2;
			this.id = id2;

		}

		@Override
		protected String doInBackground(Void... arg0) {

			ServerRequest client = new ServerRequest();

			return client.requestGetHttp("http://ndm.com.np/coolit/insert.php",
					postParamsValues);

		}

		@Override
		protected void onPostExecute(String result) {
			// pd.cancel();
			doPostActivity(result);
			doUITask(id, result);

		}
	}

	// For mCard user Login
	public void doPostActivity(String result) {

		OfflineDbHandler odb = new OfflineDbHandler(MainActivity.this);

		try {
			String jsontext = new String(result);

			// take object according to json format result
			JSONObject userObj = new JSONObject(jsontext);

		} catch (Exception e) {

		}

	}

	// End of Async Task

	public void doUITask(String id, String result) {

		ShowToastMessage.showToast("Successfully Saved into Server");

	}

	// For Send Different Data
	// For Get Current Location Weather
	private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

		String location = "";

		@Override
		protected Weather doInBackground(String... params) {

			location = params[0];

			Weather weather = new Weather();
			String data = ((new WeatherHttpClient()).getWeatherData(params[0]));

			try {
				weather = JSONWeatherParser.getWeather(data);

				// Let's retrieve the icon
				weather.iconData = ((new WeatherHttpClient())
						.getImage(weather.currentCondition.getIcon()));

			} catch (Exception e) {
				e.printStackTrace();
			}
			return weather;

		}

		@Override
		protected void onPostExecute(Weather weather) {
			super.onPostExecute(weather);

			double temp = Math.round((weather.temperature.getTemp() - 275.15));

			double humdity = weather.currentCondition.getHumidity();

			calSaveSystemData("" + temp, "" + humdity, location);

		}

	}

}
