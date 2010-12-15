package de.c3t;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;
import android.view.View;
import android.widget.Button;

public class NewsActivity extends Activity {

	static String feedUrlString = "http://ccc-trier.de/feed/rss/";

	// names of the XML tags
	static final String RSS = "rss";
	static final String CHANNEL = "channel";
	static final String ITEM = "item";

	static final String PUB_DATE = "pubDate";
	static final String DESCRIPTION = "description";
	static final String LINK = "link";
	static final String TITLE = "title";

	private URL feedUrl;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newstab);
		
        final Button button = (Button) findViewById(R.id.club);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            //	Toast.makeText(NewsActivity.this, "hello", Toast.LENGTH_LONG).show();
            	Intent showClubListIntent=new Intent(NewsActivity.this,RssList.class);
            	startActivity(showClubListIntent);
            	View view = NewsActivityGroup.group.getLocalActivityManager().startActivity("RssList", showClubListIntent).getDecorView();
            	
            	NewsActivityGroup.group.setContentView(view);
            }
        }); 
		
        /*	TabHost tabHost = getTabHost();
		tabHost.addTab(tabHost.newTabSpec("1").setIndicator("Club").setContent(new Intent(this, DummyText.class)));
		tabHost.addTab(tabHost.newTabSpec("2").setIndicator("Planet").setContent(new Intent(this, StatusActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("3").setIndicator("Twitter").setContent(new Intent(this, NaviActivity.class)));
		**/
		try {
			feedUrl = new URL(feedUrlString);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	protected InputStream getInputStream() {
		try {
			return feedUrl.openConnection().getInputStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Message> parse() {
		final Message currentMessage = new Message();
		RootElement root = new RootElement(RSS);
		final List<Message> messages = new ArrayList<Message>();
		Element itemlist = root.getChild(CHANNEL);
		Element item = itemlist.getChild(ITEM);
		item.setEndElementListener(new EndElementListener() {
			public void end() {
				messages.add(currentMessage.copy());
			}
		});
		item.getChild(TITLE).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentMessage.setTitle(body);
			}
		});
		item.getChild(LINK).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentMessage.setLink(body);
			}
		});
		item.getChild(DESCRIPTION).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentMessage.setDescription(body);
			}
		});
		item.getChild(PUB_DATE).setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentMessage.setDate(body);
			}
		});
		try {
			Xml.parse(this.getInputStream(), Xml.Encoding.UTF_8, root.getContentHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return messages;
	}

}
