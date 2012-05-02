package de.c3t;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

public class C2DM extends BroadcastReceiver {
	public void onReceive(Context context, Intent intent) {
		System.out.println("OnRecive " + intent.getAction());
		if (intent.getAction().equals("com.google.android.c2dm.intent.REGISTRATION")) {
			handleRegistration(context, intent);
		} else if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {
			handleMessage(context, intent);
		}
	}

	private void handleRegistration(Context context, Intent intent) {
		final String registration = intent.getStringExtra("registration_id");
		if (intent.getStringExtra("error") != null) {
			// Registration failed, should try again later.
			System.out.println("de.c3t.C2DM: Registration failed, should try again later.");
		} else if (intent.getStringExtra("unregistered") != null) {
			// unregistration done, new messages from the authorized sender will be rejected
			System.out.println("de.c3t.C2DM: unregistration done, new messages from the authorized sender will be rejected.");
		} else if (registration != null) {
			// Send the registration ID to the 3rd party site that is sending the messages.
			// This should be done in a separate thread.
			// When done, remember that all registration is done.
			System.out.println("de.c3t.C2DM: registration sucess, sending id " + registration + " to server");
			new Thread(new Runnable() {

				public void run() {
					sendGetRequest("http://84.200.208.230/android-clubstatus/handleRegistration.php?registration_id=" + registration);
				}

			}).start();
		}
	}

	private void handleMessage(Context context, Intent intent) {
		SharedPreferences settings = context.getSharedPreferences("c3tStatus", 0);
		boolean clubOnline = settings.getBoolean("status", true);
		boolean newStatus = ClubStatus.getStatus(context);
		if (!clubOnline && newStatus)
			sendNotification(context);
		System.out.println("C2DM: recived C2DM-Message, old status: " + clubOnline + ", new status: " + newStatus);
		if (clubOnline != newStatus) {
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean("status", newStatus);
			editor.commit();
			
			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
			int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, StatusWidgetProvider.class));
			for (int i = 0; i < appWidgetIds.length; i++) {
				int appWidgetId = appWidgetIds[i];
				StatusWidgetProvider.updateWidget(context, appWidgetManager, appWidgetId, newStatus);
			}
		}
	}

	public static void sendGetRequest(String file) {
		try {
			URL fileURL = new URL(file);
			InputStream fis = null;
			try {
				URLConnection con = fileURL.openConnection();
				fis = con.getInputStream();
				byte[] buffer = new byte[0xFFFF];
				while (fis.read(buffer) != -1) {
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fis != null)
					try {
						fis.close();
					} catch (IOException e) {
					}
			}
		} catch (MalformedURLException e1) {
		}
	}

	public static void registerC2DM(Context context) {
		Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
		registrationIntent.putExtra("app", PendingIntent.getBroadcast(context, 0, new Intent(), 0)); // boilerplate
		registrationIntent.putExtra("sender", "c3tapp@gmail.com");
		context.startService(registrationIntent);
	}

	public static void unRegisterC2DM(Context context) {
		Intent unregIntent = new Intent("com.google.android.c2dm.intent.UNREGISTER");
		unregIntent.putExtra("app", PendingIntent.getBroadcast(context, 0, new Intent(), 0));
		context.startService(unregIntent);
	}

	void sendNotification(Context context) {
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);

		int icon = R.drawable.notification_status_porta_off;
		CharSequence tickerText = "Es ist Club!";
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText, when);
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notification.sound = Uri.parse("android.resource://de.c3t/" + R.raw.notification);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		CharSequence contentTitle = "Es ist Club!";
		CharSequence contentText = "Get up and hack!";
		Intent notificationIntent = new Intent(context, CCCTrier.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		final int HELLO_ID = 1;

		mNotificationManager.notify(HELLO_ID, notification);
	}
}
