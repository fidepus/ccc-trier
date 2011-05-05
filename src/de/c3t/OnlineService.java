package de.c3t;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;

public class OnlineService extends Service {
	//private static final String TAG = "C3TOnlineService";

	//private Timer timer;

	private boolean clubOnline = true;

	private ClubStatus clubStatus;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		//timer = new Timer();

		clubStatus = new ClubStatus(this);
	}

	@Override
	public void onStart(Intent intent, int startid) {
		//timer.scheduleAtFixedRate(new TimerTask() { 
			//public void run() {
	//you don't know how much RAM a timer needs…
		new Thread(new Runnable() {
			
			public void run() {
				while (true) { 
					boolean newClubStatus = clubStatus.getStatus();
					if (!clubOnline && newClubStatus) // Club is online now but wasn't at the last check
						sendNotification();
					clubOnline = newClubStatus;
					try {Thread.sleep(60*60*1000);} catch (InterruptedException e) {}
			}				
			}
		}).start();

			//}
		//}, 0, 60*60 * 1000);
	}

	void sendNotification() {
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
}
