/*
 * Copyright (c) 2014, 青岛司通科技有限公司 All rights reserved.
 * File Name：SectionsPagerAdapter.java
 * Version：V1.0
 * Author：zhaokaiqiang
 * Date：2014-10-29
 */

package com.peter.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

	private String[] titles = new String[] { "TAB1", "TAB2", "TAB3", "TAB4","1234567890" };

	public SectionsPagerAdapter(FragmentManager fragmentManager) {
		super(fragmentManager);
	}

	@Override
	public Fragment getItem(int position) {
		return PlaceholderFragment.newInstance();
	}

	@Override
	public int getCount() {
		return titles.length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return titles[position];
	}

}
