package com.coolit.adapter;

import com.coolit.fragment.HomePageFragment;
import com.coolit.fragment.OtherPageFragment;
import com.coolit.fragment.SuggestPageFragment;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class HomePageViewPagerAdapter extends FragmentPagerAdapter {
	private Context _context;

	public HomePageViewPagerAdapter(Context context, FragmentManager fm) {
		super(fm);

		_context = context;

	}

	@Override
	public Fragment getItem(int position) {
		Fragment f = new Fragment();
		switch (position) {

		case 0:
			f = HomePageFragment.newInstance(_context);

			break;
		case 1:
			f = OtherPageFragment.newInstance(_context);
			break;

		case 2:
			f = SuggestPageFragment.newInstance(_context);
			break;

		}
		return f;
		
	}

	@Override
	public int getCount() {
		return 3;
	}
	
	 // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
		return "Page " + position;
    }

}
