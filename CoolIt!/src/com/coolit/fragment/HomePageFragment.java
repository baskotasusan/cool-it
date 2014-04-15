package com.coolit.fragment;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.coolit.MainActivity;
import com.coolit.R;
import com.coolit.library.GPSTracker;
import com.coolit.library.OfflineDbHandler;
import com.coolit.utility.CustomProgressBarDialog;
import com.coolit.utility.GlobalVariablesCollection;
import com.coolit.utility.JSONWeatherParser;
import com.coolit.utility.WeatherHttpClient;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.survivingwithandroid.weatherapp.model.Weather;

public class HomePageFragment extends Fragment {

	GPSTracker gpsTracker;

	PullToRefreshScrollView pulltoScrollView;

	static Context mContext;

	double lat, lng;

	ImageView tempImgView, humidityImgView;

	TextView tempValueTV, humidityValueTV, ferhTV;

	String finalAddress = "";

	String cityName = "";

	ViewGroup root;

	boolean pullToRefreshStatus = false;

	public static Fragment newInstance(Context context) {

		HomePageFragment f = new HomePageFragment();

		mContext = context;

		return f;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.home_page_fragment, null);

		this.root = root;

		gpsTracker = new GPSTracker(mContext);

		LocationManager manager = (LocationManager) mContext
				.getSystemService(Context.LOCATION_SERVICE);
		if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			// Ask the user to enable GPS
			AlertDialog.Builder builder = new AlertDialog.Builder(
					MainActivity.thisActivity);
			builder.setTitle("Location Manager");
			builder.setMessage("Would you like to enable GPS?");
			builder.setPositiveButton("No",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});

			builder.setNegativeButton("Yes",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// No location service, no Activity

							// Launch settings, allowing user to make a change
							Intent i = new Intent(
									Settings.ACTION_LOCATION_SOURCE_SETTINGS);

							startActivityForResult(i, 101);

						}
					});
			builder.create().show();

		}

		tempImgView = (ImageView) root.findViewById(R.id.tempImgView);
		humidityImgView = (ImageView) root.findViewById(R.id.humidityImgView);

		tempValueTV = (TextView) root.findViewById(R.id.tempValueTV);
		humidityValueTV = (TextView) root.findViewById(R.id.humidityValueTV);

		ferhTV = (TextView) root.findViewById(R.id.ferhTV);

		pulltoScrollView = (PullToRefreshScrollView) root
				.findViewById(R.id.pull_refresh_scrollview);

		getCurrentData();

		// Set a listener to be invoked when the list should be refreshed.

		pulltoScrollView
				.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						// TODO Auto-generated method stub

						String label = DateUtils.formatDateTime(mContext,
								System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);

						pullToRefreshStatus = true;

						// Update the LastUpdatedLabel
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);

						getCurrentData();

					}

				});

		return root;

	}

	/**
	 * 02 * Determines the the closest Address for a given Location. 03 * 04 * @param
	 * context the context 05 * @param location the location for which an
	 * Address needs to be found 06 * @return and Address or null if not found
	 * 07 * @throws IOException if the Geocoder fails 08
	 */
	public Address getAddressForLocation(Context context, Location location)
			throws IOException {

		if (location == null) {
			return null;
		}

		double latitude = location.getLatitude();
		double longitude = location.getLongitude();
		int maxResults = 1;

		Geocoder gc = new Geocoder(context, Locale.getDefault());
		List<Address> addresses = gc.getFromLocation(latitude, longitude,
				maxResults);

		if (addresses.size() == 1) {
			return addresses.get(0);
		} else {
			return null;
		}
	}

	// For Get Current Location Weather
	private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

		@Override
		protected Weather doInBackground(String... params) {
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

			// cityText.setText(weather.location.getCity() + ","
			// + weather.location.getCountry());
			// condDescr.setText(weather.currentCondition.getCondition() + "("
			// + weather.currentCondition.getDescr() + ")");
			// temp.setText(""
			// + Math.round((weather.temperature.getTemp() - 275.15))
			// + "°C");
			// hum.setText("" + weather.currentCondition.getHumidity() + "%");
			// press.setText("" + weather.currentCondition.getPressure() +
			// " hPa");
			// windSpeed.setText("" + weather.wind.getSpeed() + " mps");
			// windDeg.setText("" + weather.wind.getDeg() + "°");

			// Call onRefreshComplete when the list has been
			// refreshed.
			pulltoScrollView.onRefreshComplete();

			if (pullToRefreshStatus) {

				pullToRefreshStatus = false;

				try {

					GlobalVariablesCollection.globalPD.dismiss();

				} catch (Exception e) {
				}

			}

			double temp = Math.round((weather.temperature.getTemp() - 275.15));

			double humdity = weather.currentCondition.getHumidity();

			OfflineDbHandler odb = new OfflineDbHandler(mContext);

			if (temp >= 0) {

				odb.addData("current_location", "temp", "" + temp);
				odb.addData("current_location", "humidity", "" + humdity);

			}

			String t = odb.getData("current_location", "temp");
			String h = odb.getData("current_location", "humidity");

			if (t == null)
				t = "0";

			if (h == null) {
				h = "0";
			}

			temp = Double.valueOf(t);

			humdity = Double.valueOf(h);

			double ferh = (9 * temp / 5) + 32;

			Spanned tempStr = Html.fromHtml(String.valueOf(temp) + "<sup>"
					+ " o</sup>C");

			Spanned ferhStr = Html.fromHtml(String.valueOf(ferh) + " F");

			if (temp <= 12) {

				tempImgView.setImageResource(R.drawable.low);
				tempValueTV.setText(tempStr);

				ferhTV.setText(ferhStr);

			} else if (temp > 12 && temp <= 30) {

				tempImgView.setImageResource(R.drawable.normal);
				tempValueTV.setText(tempStr);
				ferhTV.setText(ferhStr);

			} else {
				tempImgView.setImageResource(R.drawable.high);
				tempValueTV.setText(tempStr);
				ferhTV.setText(ferhStr);

			}

			// For Humidity

			String humidityStr = humdity + " %";

			if (humdity <= 12) {

				humidityImgView.setImageResource(R.drawable.low);
				humidityValueTV.setText(humidityStr);

			} else if (humdity > 12 && humdity <= 30) {

				humidityImgView.setImageResource(R.drawable.normal);
				humidityValueTV.setText(humidityStr);

			} else {
				humidityImgView.setImageResource(R.drawable.high);
				humidityValueTV.setText(humidityStr);

			}

			ProgressBar progressBar = (ProgressBar) root
					.findViewById(R.id.progressBar);
			progressBar.setVisibility(View.GONE);

		}

	}

	// For Get Current Location Details
	public void getCurrentData() {

		GlobalVariablesCollection.globalPD = CustomProgressBarDialog
				.showProgressDialog(MainActivity.thisActivity);

		GlobalVariablesCollection.globalPD.show();

		if (gpsTracker.canGetLocation()) {

			lat = gpsTracker.getLatitude();

			lng = gpsTracker.getLongitude();

			Location location = gpsTracker.getLocation();

			Address address = null;

			try {

				address = getAddressForLocation(mContext, location);

			} catch (Exception e) {

			}

			if (address != null) {

				StringBuilder builder = new StringBuilder();

				try {
					int maxLines = address.getMaxAddressLineIndex();
					for (int i = 0; i < maxLines; i++) {
						String addressStr = address.getAddressLine(i);
						builder.append(addressStr);
						builder.append(" ");
					}

				} catch (Exception e) {
				}

				finalAddress = builder.toString() + "," + address.getLocality()
						+ "," + address.getSubAdminArea() + ","
						+ address.getAdminArea() + ","
						+ address.getCountryName(); // This is the
				// complete address.

				cityName = address.getLocality();

				OfflineDbHandler odb = new OfflineDbHandler(mContext);

				String displayName = address.getAddressLine(0) + ","
						+ address.getLocality();

				odb.addData("current_location", "locationName", finalAddress);
				odb.addData("current_location", "cityName", cityName);

				odb.addData("current_location", "displayName", displayName);

			}

		}

		TextView locationNameTV = (TextView) root
				.findViewById(R.id.locationNameTV);

		OfflineDbHandler odb = new OfflineDbHandler(mContext);

		finalAddress = odb.getData("current_location", "locationName");

		cityName = odb.getData("current_location", "cityName");

		locationNameTV.setText(finalAddress);

		JSONWeatherTask task = new JSONWeatherTask();
		task.execute(new String[] { cityName });

	}

}
