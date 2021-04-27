package com.vbea.java21.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter
{
	private ArrayList<String> titleList = new ArrayList<String>();
    private ArrayList<Fragment> pageList = new ArrayList<Fragment>();
	
	public FragmentAdapter(FragmentManager fm)
	{
		super(fm);
	}
	
	@Override
    public Fragment getItem(int position) {
        return pageList.get(position);
    }

    @Override
    public int getCount() {
        return titleList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

    public void addItem(Fragment fragment, String title){
        pageList.add(fragment);
        titleList.add(title);
    }
}
