package com.coolit.adapter;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.coolit.R;

public class NearMeListAdapter extends BaseAdapter {

	List<String> currentIDList;

	List<String> locationList;

	List<String> tempList;

	List<String> humidityList;

	List<String> ferhList;

	private LayoutInflater mInflater;

	public NearMeListAdapter(Context activity, List<String> currentIDList,

	List<String> currentLocationList,

	List<String> tempList, List<String> ferhList,

	List<String> humidityList) {

		mInflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.currentIDList = currentIDList;
		this.locationList = currentLocationList;
		this.tempList = tempList;
		this.humidityList = humidityList;
		this.ferhList = ferhList;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return currentIDList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int id) {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		convertView = mInflater.inflate(R.layout.nearme_layout_item, parent,
				false);

		TextView locationTV = (TextView) convertView
				.findViewById(R.id.locationTV);

		try {

			TextView tempTV = (TextView) convertView.findViewById(R.id.tempTV);

			TextView humidityTV = (TextView) convertView
					.findViewById(R.id.humidityTV);

			locationTV.setText(locationList.get(position));

			Spanned tempStr = Html.fromHtml(tempList.get(position) + "<sup>"
					+ " o</sup>C" + "/" + ferhList.get(position) + " F");

			tempTV.setText(tempStr);
			humidityTV.setText(humidityList.get(position) + "%");

		} catch (Exception e) {
		}

		return convertView;

	}
}
