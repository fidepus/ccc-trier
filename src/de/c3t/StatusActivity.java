package de.c3t;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ImageView;

public class StatusActivity extends Activity {

	boolean isOn = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.status);
		findViewById(R.id.StatusLayout).getBackground().setDither(true);
	}

	public void onResume() {
		super.onResume();
		try {
			fetchStatus();
		} catch (Exception e) {
		}
	}

	void setStatusOn() {
		findViewById(R.id.StatusLayout).setBackgroundResource(R.drawable.status_on);
		ImageView image = (ImageView) findViewById(R.id.StatusLogo);
		image.setImageResource(R.drawable.status_porta_on);
		isOn = true;
	}

	void setStatusOff() {
		findViewById(R.id.StatusLayout).setBackgroundResource(R.drawable.status_off);
		ImageView image = (ImageView) findViewById(R.id.StatusLogo);
		image.setImageResource(R.drawable.status_porta_off);
		isOn = false;
	}

	boolean getStatus() {
		return isOn;
	}

	boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	void fetchStatus() throws XmlPullParserException, ClientProtocolException, URISyntaxException, IOException {
		if (isOnline()) {
			XmlPullParserFactory factory = null;
			factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = null;
			xpp = factory.newPullParser();

			xpp.setInput(new InputStreamReader(getUrlData("http://c3t.de/club/flag.xml")));
			int eventType = 0;
			eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {

				if (eventType == XmlPullParser.TEXT) {
					if (xpp.getText().compareToIgnoreCase("1") == 0) {
						setStatusOn();
					} else if (xpp.getText().compareToIgnoreCase("0") == 0) {
						setStatusOff();
					}
				}
				eventType = xpp.next();
			}
		}
	}

	public InputStream getUrlData(String url) throws URISyntaxException, ClientProtocolException, IOException {

		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet method = new HttpGet(new URI(url));
		HttpResponse res = client.execute(method);

		return res.getEntity().getContent();
	}
}
