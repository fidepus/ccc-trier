package de.c3t;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class StartupReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent indent) {
		SharedPreferences settings = context.getSharedPreferences("c3tStatus", 0);
		boolean clubOnline = settings.getBoolean("status", true);
		ClubStatus cs = new ClubStatus(context);
		boolean newStatus = cs.getStatus();
		if (clubOnline != newStatus){
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean("status", newStatus);
			editor.commit();
		}	
	}

}
