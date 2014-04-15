package com.coolit.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.coolit.DetailsActivity;
import com.coolit.MainActivity;
import com.coolit.R;
import com.coolit.adapter.NearMeListAdapter;
import com.coolit.library.OfflineDbHandler;
import com.coolit.library.ServerRequest;
import com.coolit.utility.CustomProgressBarDialog;
import com.coolit.utility.GlobalVariablesCollection;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class OtherPageFragment extends Fragment {

	PullToRefreshListView nearByListView;

	static Context mContext;

	List<String> currentIDList;

	List<String> locationList;

	List<String> tempList;

	List<String> ferhList;

	List<String> humidityList;

	boolean pulltoRefreshStatus = false;

	public static Fragment newInstance(Context context) {

		OtherPageFragment f = new OtherPageFragment();

		mContext = context;

		return f;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.other_page_fragment, null);

		nearByListView = (PullToRefreshListView) root
				.findViewById(R.id.nearByListView);

		// Get Near Me Data
		calNearMeAsyncTask();

		// Set a listener to be invoked when the list should be refreshed.

		nearByListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub

				String label = DateUtils.formatDateTime(mContext,
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				pulltoRefreshStatus = true;

				calNearMeAsyncTask();

			}

		});

		nearByListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(mContext, DetailsActivity.class);

				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("location", locationList.get(position - 1));
				intent.putExtra("temp", tempList.get(position - 1));
				intent.putExtra("humidity", humidityList.get(position - 1));

				startActivity(intent);

			}

		});

		return root;

	}

	// For Get Json Details Data

	public void calNearMeAsyncTask() {

		if (pulltoRefreshStatus) {
			if (ServerRequest.isNetworkConnected(mContext)) {

				GlobalVariablesCollection.globalPD = CustomProgressBarDialog
						.showProgressDialog(MainActivity.thisActivity);
				GlobalVariablesCollection.globalPD.show();

			}

		}

		List<NameValuePair> postParamsValues = new ArrayList<NameValuePair>();

		OfflineDbHandler odb = new OfflineDbHandler(mContext);
		String result = "";

		String postParams = "";

		result = odb.getData("near_by_details", postParams);

		if (result != null) {
			doPostActivity(result);
			doUITask(postParams, result);
		}

		if (!ServerRequest.isNetworkConnected(mContext)) {
			ServerRequest.showNoConnectionDialog(MainActivity.thisActivity);

		}

		if (ServerRequest.isNetworkConnected(mContext))
			new NearMeAsyncTask(postParamsValues, postParams).execute();

	}

	public class NearMeAsyncTask extends AsyncTask<Void, Void, String> {
		List<NameValuePair> postParamsValues;
		String id;

		public NearMeAsyncTask(List<NameValuePair> articlesPostValues2,
				String id2) {
			this.postParamsValues = articlesPostValues2;
			this.id = id2;

		}

		@Override
		protected String doInBackground(Void... arg0) {

			ServerRequest client = new ServerRequest();

			return client.requestGetHttp("http://ndm.com.np/coolit/select.php",
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

		OfflineDbHandler odb = new OfflineDbHandler(mContext);

		currentIDList = new ArrayList<String>();
		locationList = new ArrayList<String>();

		tempList = new ArrayList<String>();

		humidityList = new ArrayList<String>();

		ferhList = new ArrayList<String>();

		odb.addData("near_by_details", "", result);

		try {
			String jsontext = new String(result);

			// take object according to json format result

			JSONArray innerArray = new JSONArray(jsontext);

			for (int i = 0; i < innerArray.length(); i++) {

				JSONObject obj = innerArray.getJSONObject(i);

				String temp = "";

				String humdity = "";

				String location = "";

				try {

					temp = obj.getString("temperature");

				} catch (Exception e) {
				}

				try {

					humdity = obj.getString("humidity");

				} catch (Exception e) {
				}

				try {

					location = obj.getString("location");

				} catch (Exception e) {
				}

				String id = "";
				try {

					id = obj.getString("id");

				} catch (Exception e) {
				}

				currentIDList.add(id);
				locationList.add(location);
				tempList.add(temp);

				double f = 0;

				if (temp.length() > 0) {

					double t = Double.valueOf(temp);

					f = (t * 9) / 5 + 32;

				}

				String ferh = String.valueOf(f);

				ferhList.add(ferh);
				humidityList.add(humdity);

			}

		} catch (Exception e) {

		}

	}

	// End of Async Task

	public void doUITask(String id, String result) {

		if (pulltoRefreshStatus) {
			try {
				GlobalVariablesCollection.globalPD.dismiss();

			} catch (Exception e) {

			}

			pulltoRefreshStatus = false;

		}

		// Call onRefreshComplete when the list has been
		// refreshed.
		nearByListView.onRefreshComplete();

		OfflineDbHandler odb = new OfflineDbHandler(mContext);

		String t = odb.getData("sys", "temp");

		String hum = odb.getData("sys", "hum");

		if (t != null) {

			String location = odb.getData("sys", "location");

			currentIDList.add("23");
			locationList.add(location);
			tempList.add(t);

			double f1 = 0;

			if (t.length() > 0) {

				double t1 = Double.valueOf(t);

				f1 = (t1 * 9) / 5 + 32;

			}

			String ferh = String.valueOf(f1);

			ferhList.add(ferh);
			humidityList.add(hum);

		}

		NearMeListAdapter nearMeAdapter = new NearMeListAdapter(mContext,
				currentIDList, locationList, tempList, ferhList, humidityList);

		nearByListView.getRefreshableView().setAdapter(nearMeAdapter);

	}
}
