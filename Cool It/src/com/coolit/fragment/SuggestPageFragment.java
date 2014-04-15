package com.coolit.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.coolit.R;
import com.coolit.library.ServerRequest;
import com.coolit.utility.GlobalVariablesCollection;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;

public class SuggestPageFragment extends Fragment {

	PullToRefreshWebView suggestWebView;

	static Context mContext;

	boolean loadingFinished = true;
	boolean redirect = false;

	public static Fragment newInstance(Context context) {

		SuggestPageFragment f = new SuggestPageFragment();

		mContext = context;

		return f;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.suggest_page_fragment, null);

		suggestWebView = (PullToRefreshWebView) root
				.findViewById(R.id.suggestWebView);

		// Set a listener to be invoked when the list should be refreshed.

		suggestWebView.setOnRefreshListener(new OnRefreshListener<WebView>() {

			@Override
			public void onRefresh(PullToRefreshBase<WebView> refreshView) {
				// TODO Auto-generated method stub

				String label = DateUtils.formatDateTime(mContext,
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				// Call onRefreshComplete when the list has been
				// refreshed.
				suggestWebView.onRefreshComplete();

			}

		});

		suggestWebView.getRefreshableView().getSettings()
				.setJavaScriptEnabled(true);

		suggestWebView.getRefreshableView().getSettings()
				.setAppCacheMaxSize(5 * 1024 * 1024); // 5MB
		suggestWebView.getRefreshableView().getSettings()
				.setAppCachePath(mContext.getCacheDir().getAbsolutePath());
		suggestWebView.getRefreshableView().getSettings()
				.setAllowFileAccess(true);
		suggestWebView.getRefreshableView().getSettings()
				.setAppCacheEnabled(true);
		suggestWebView.getRefreshableView().getSettings()
				.setJavaScriptEnabled(true);
		suggestWebView.getRefreshableView().getSettings()
				.setCacheMode(WebSettings.LOAD_DEFAULT); // load online by
															// default

		suggestWebView.setBackgroundColor(getResources().getColor(
				android.R.color.transparent));

		try {

			int sdk_version = Build.VERSION.SDK_INT;

			if (sdk_version > 14) {

				suggestWebView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);

			}

		} catch (Exception e) {
		}

		if (!ServerRequest.isNetworkConnected(mContext)) { // loading offline
			suggestWebView.getRefreshableView().getSettings()
					.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		} else {

			suggestWebView.getRefreshableView().loadUrl(
					"http://ndm.com.np/coolit/suggestion.html");

		}

		suggestWebView.getRefreshableView().setWebViewClient(
				new WebViewClient() {

					@Override
					public boolean shouldOverrideUrlLoading(WebView view,
							String urlNewString) {
						if (!loadingFinished) {
							redirect = true;
						}

						loadingFinished = false;
						view.loadUrl(urlNewString);
						return true;
					}

					@Override
					public void onPageStarted(WebView view, String url,
							Bitmap facIcon) {
						loadingFinished = false;
						// SHOW LOADING IF IT ISNT ALREADY VISIBLE

					}

					@Override
					public void onPageFinished(WebView view, String url) {
						if (!redirect) {
							loadingFinished = true;

						}

						if (loadingFinished && !redirect) {
							// HIDE LOADING IT HAS FINISHED

							try {
								GlobalVariablesCollection.globalPD.dismiss();

							} catch (Exception e) {
							}

						} else {
							redirect = false;
						}

					}
				});

		return root;

	}
}
