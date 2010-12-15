package de.c3t;

import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RssList extends ListActivity {

	String clubRss = "http://c3t.de/feed/rss/";
	String planetRss = "http://ccc-trier.de/planet/rss/";
	String galaxyRss = "http://ccc-trier.de/galaxy/rss/";
	String twitterRss = "http://twitter.com/statuses/user_timeline/29011077.rss";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final List<String> links;

		RssHandler rssHandler = new RssHandler();
		RssContent rssContent = null;

		if (isOnline()) {
			try {
				rssContent = rssHandler.fetchRSS(clubRss);
				links = rssContent.getLinks();
			} catch (Exception e) {
				return;
			}

			setListAdapter(new ArrayAdapter<String>(this, R.layout.rsslist,
					rssContent.getTitles()));

			ListView lv = getListView();
			lv.setTextFilterEnabled(true);

			lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					Intent intent = new Intent(RssList.this, WebViewer.class);
					intent.putExtra("URL",links.get(position));
					startActivity(intent);
					// When clicked, show a toast with the TextView text
					// Toast.makeText(getApplicationContext(),
					// links.get(position),
					// Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	private boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}
}