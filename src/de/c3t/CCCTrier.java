package de.c3t;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class CCCTrier extends TabActivity

{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		C2DM.registerC2DM(this); //i'll change this back when i finished debugging c2dm with an emulator without all libs
		/*

		startService(new Intent(this, OnlineService.class));
		TabHost tabHost = getTabHost();
		tabHost.addTab(tabHost.newTabSpec("1").setIndicator("News", getResources().getDrawable(R.drawable.ic_tab_news)).setContent(new Intent(this, RssList.class)));
		tabHost.addTab(tabHost.newTabSpec("2").setIndicator("Status", getResources().getDrawable(R.drawable.ic_tab_status)).setContent(new Intent(this, StatusActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("3").setIndicator("Map", getResources().getDrawable(R.drawable.ic_tab_info)).setContent(new Intent(this, NaviActivity.class)));
*/
	}
}
