package de.c3t;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class OnlineService extends Service {
	private static final String TAG = "C3TOnlineService";
		
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
		
	}

	@Override
	public void onDestroy() {
		Context context = getApplicationContext();
		CharSequence text = "Service destroyed.";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	
	@Override
	public void onStart(Intent intent, int startid) {
		Context context = getApplicationContext();
		CharSequence text = "Service started.";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
}
