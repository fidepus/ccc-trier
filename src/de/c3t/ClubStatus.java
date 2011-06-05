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

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ClubStatus {
	private Context cw;

	private boolean clubOnline = false;

	public ClubStatus(Context cw) {
		this.cw = cw; // This is a reference to the activity or service that created this Object, this
		// is necessary to find out if we are connected to the internet
	}

	boolean networkIsOnline() {
		ConnectivityManager cm = (ConnectivityManager) cw.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	boolean getStatus() {
		if (networkIsOnline()) {
			try {
			XmlPullParserFactory factory =  XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = null;
			xpp = factory.newPullParser();

			xpp.setInput(new InputStreamReader(getUrlData("http://192.168.178.86/android-clubstatus/flag.xml")));
			int eventType = 0;
			eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {

				if (eventType == XmlPullParser.TEXT) {
					if (xpp.getText().compareToIgnoreCase("1") == 0) {
						clubOnline = true;
					} else if (xpp.getText().compareToIgnoreCase("0") == 0) {
						clubOnline = false;
					}
				}
				eventType = xpp.next();
			}
			} catch (XmlPullParserException e) {
			} catch (ClientProtocolException e) {
			} catch (URISyntaxException e) {
			} catch (IOException e) {
			}
		}
		return clubOnline;
	}

	public InputStream getUrlData(String url) throws URISyntaxException, ClientProtocolException, IOException {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet method = new HttpGet(new URI(url));
		HttpResponse res = client.execute(method);

		return res.getEntity().getContent();
	}
}
