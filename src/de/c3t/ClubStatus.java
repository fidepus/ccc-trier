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
	static boolean networkIsOnline(Context cw) {
		ConnectivityManager cm = (ConnectivityManager) cw.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	static boolean getStatus(Context cw) {
		boolean clubOnline = false;
		if (networkIsOnline(cw)) {
			try {
			XmlPullParserFactory factory =  XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = null;
			xpp = factory.newPullParser();

			xpp.setInput(new InputStreamReader(getUrlData("http://c3t.de/club/flag.xml")));
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

	public static InputStream getUrlData(String url) throws URISyntaxException, ClientProtocolException, IOException {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet method = new HttpGet(new URI(url));
		HttpResponse res = client.execute(method);

		return res.getEntity().getContent();
	}
}
