package de.c3t;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.IBinder;
import android.widget.Toast;

public class OnlineService extends Service {
	private static final String TAG = "C3TOnlineService";

	private Timer timer;

	private boolean clubOnline = true;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Context context = getApplicationContext();
		CharSequence text = "Service created.";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		timer = new Timer();
	}

	@Override
	public void onDestroy() {
		Context context = getApplicationContext();
		CharSequence text = "Service destroyed.";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		timer.cancel();
	}

	@Override
	public void onStart(Intent intent, int startid) {
		Context context = getApplicationContext();
		CharSequence text = "Service started.";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				try {
					checkClubStatus();
				} catch (ClientProtocolException e) {
				} catch (XmlPullParserException e) {
				} catch (URISyntaxException e) {
				} catch (IOException e) {
				}
			}
		}, 0, 60 * 1000);
	}

	boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	void checkClubStatus() throws XmlPullParserException, ClientProtocolException, URISyntaxException, IOException {
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
						if (!clubOnline) {

							String ns = Context.NOTIFICATION_SERVICE;
							NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);

							int icon = R.drawable.status_porta_off;
							CharSequence tickerText = "Es ist Club!";
							long when = System.currentTimeMillis();

							Notification notification = new Notification(icon, tickerText, when);
							notification.defaults |= Notification.DEFAULT_VIBRATE;
							notification.sound = Uri.parse("android.resource://de.c3t/" + R.raw.notification);
							notification.flags |= Notification.FLAG_AUTO_CANCEL;
							Context context = getApplicationContext();
							CharSequence contentTitle = "Es ist Club!";
							CharSequence contentText = "Shut up and hack!";
							Intent notificationIntent = new Intent(this, CCCTrier.class);
							PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

							notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
							final int HELLO_ID = 1;

							mNotificationManager.notify(HELLO_ID, notification);
						}
						clubOnline = true;
					} else if (xpp.getText().compareToIgnoreCase("0") == 0) {
						clubOnline = false;
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
