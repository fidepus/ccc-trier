package de.c3t;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.RemoteViews;

public class StatusWidgetProvider extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		ClubStatus cs = new ClubStatus(context);
		boolean clubStatus = cs.getStatus();
		final int N = appWidgetIds.length;

		// Perform this loop procedure for each App Widget that belongs to this provider
		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];

			// Create an Intent to launch ExampleActivity
			Intent intent = new Intent(context, CCCTrier.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

			// Get the layout for the App Widget and attach an on-click listener
			// to the button
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.status_widget);
			views.setOnClickPendingIntent(R.id.StatusWidgetFrameLayout, pendingIntent);
			// set clubstatus
			if (clubStatus) {
				views.setInt(R.id.StatusLogo, "setBackgroundResource", R.drawable.status_on_widget);
				views.setInt(R.id.StatusLogo, "setImageResource", R.drawable.status_porta_on_widget);
			} else {
				views.setInt(R.id.StatusLogo, "setBackgroundResource", R.drawable.status_off_widget);
				views.setInt(R.id.StatusLogo, "setImageResource", R.drawable.status_porta_off_widget);
			}

			// Tell the AppWidgetManager to perform an update on the current app widget
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
}
