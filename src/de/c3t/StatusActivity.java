package de.c3t;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

public class StatusActivity extends Activity {

	boolean isOn = false;

	ClubStatus clubStatus;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.status);
		findViewById(R.id.StatusLayout).getBackground().setDither(true);
		clubStatus = new ClubStatus(this);
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

	void setStatusOff() {
		findViewById(R.id.StatusLayout).setBackgroundResource(R.drawable.status_off);
		ImageView image = (ImageView) findViewById(R.id.StatusLogo);
		image.setImageResource(R.drawable.status_porta_off);
		isOn = false;
	}

	void fetchStatus() {
		if (clubStatus.getStatus())
			setStatusOn();
		else
			setStatusOff();
	}

	boolean getStatus() {
		return isOn;
	}
}
