package de.c3t;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

public class MessageList extends ListActivity {

	private List<Message> messages;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);
		loadFeed();
	}

	private void loadFeed() {
		try {
			NewsActivity parser = new NewsActivity();
			messages = parser.parse();
			List<String> titles = new ArrayList<String>(messages.size());
			for (Message msg : messages) {
				titles.add(msg.getTitle());
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row, titles);
			this.setListAdapter(adapter);
		} catch (Throwable t) {
			Log.e("AndroidNews", t.getMessage(), t);
		}
	}

}