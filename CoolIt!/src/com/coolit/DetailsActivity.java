package com.coolit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.widget.TextView;

import com.coolit.library.OfflineDbHandler;

public class DetailsActivity extends Activity {

	TextView topBarHeading;

	TextView currentLocationTV, otherLocationTV, currentTempTV, otherTempTV,
			currentHumidityTV, otherHumidityTV, currentDifferenceTV,
			otherDifferenceTV;

	String currentLocationName, otherLocationName, currentTemp, otherTemp,
			currentHumidity, otherHumidity, currentDifference, otherDifference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);

		Intent intent = getIntent();

		otherLocationName = intent.getStringExtra("location");

		otherTemp = intent.getStringExtra("temp");

		otherHumidity = intent.getStringExtra("humidity");

		Spanned tempOtherStr = Html.fromHtml(String.valueOf(otherTemp)
				+ "<sup>" + " o</sup>C");

		double tempOther = Double.valueOf(otherTemp);

		double humidityOther = Double.valueOf(otherHumidity);

		double fer = (9 * tempOther) / 5 + 32;

		String ferhOther = String.valueOf(fer);

		Spanned ferhOtherStr = Html.fromHtml(String.valueOf(ferhOther) + " F");

		otherTemp = tempOtherStr.toString() + "/" + ferhOtherStr.toString();

		topBarHeading = (TextView) findViewById(R.id.topBarHeading);

		topBarHeading.setText("Details");

		currentLocationTV = (TextView) findViewById(R.id.currentLocationTV);
		otherLocationTV = (TextView) findViewById(R.id.otherLocationTV);
		currentTempTV = (TextView) findViewById(R.id.currentTempTV);
		otherTempTV = (TextView) findViewById(R.id.otherTempTV);
		currentHumidityTV = (TextView) findViewById(R.id.currentHumidityTV);
		otherHumidityTV = (TextView) findViewById(R.id.otherHumidityTV);
		currentDifferenceTV = (TextView) findViewById(R.id.currentDifferenceTV);
		otherDifferenceTV = (TextView) findViewById(R.id.otherDifferenceTV);

		OfflineDbHandler odb = new OfflineDbHandler(getApplicationContext());

		currentLocationName = odb.getData("current_location", "displayName");

		String t = odb.getData("current_location", "temp");
		String h = odb.getData("current_location", "humidity");

		double temp = Double.valueOf(t);

		double humdity = Double.valueOf(h);

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

		currentTemp = tempStr + "/" + ferhStr;

		currentHumidity = humdity + " %";

		currentLocationTV.setText(currentLocationName);
		otherLocationTV.setText(otherLocationName);
		currentTempTV.setText(currentTemp);
		otherTempTV.setText(otherTemp);
		currentHumidityTV.setText(currentHumidity);
		otherHumidityTV.setText(otherHumidity + "%");

		double diffHum = humdity - humidityOther;

		double diffTemp = temp - tempOther;

		double diffFerh = (9 * diffTemp) / 5 + 32;

		Spanned tempStrDiff = Html.fromHtml(String.valueOf(diffTemp) + "<sup>"
				+ " o</sup>C");

		Spanned ferhStrDiff = Html.fromHtml(String.valueOf(diffFerh) + " F");

		currentDifference = tempStrDiff + "/" + ferhStrDiff;

		otherDifference = diffHum + "%";

		currentDifferenceTV.setText(currentDifference);
		otherDifferenceTV.setText(otherDifference);

	}

}
