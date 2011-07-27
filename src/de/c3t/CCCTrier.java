package de.c3t;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;

public class CCCTrier extends TabActivity

{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//C2DM.registerC2DM(this);

		//startService(new Intent(this, OnlineService.class));
		TabHost tabHost = getTabHost();
		tabHost.addTab(tabHost.newTabSpec("1").setIndicator("News", getResources().getDrawable(R.drawable.ic_tab_news)).setContent(new Intent(this, RssList.class)));
		tabHost.addTab(tabHost.newTabSpec("2").setIndicator("Status", getResources().getDrawable(R.drawable.ic_tab_status)).setContent(new Intent(this, StatusActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("3").setIndicator("Map", getResources().getDrawable(R.drawable.ic_tab_info)).setContent(new Intent(this, NaviActivity.class)));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
		    case R.id.menuItemSettings:
	        Intent settingsActivity = new Intent(getBaseContext(), Settings.class);
	        startActivity(settingsActivity);
		      return true;
		      
		    default:
		        return super.onOptionsItemSelected(item);
	    }
	}
}
