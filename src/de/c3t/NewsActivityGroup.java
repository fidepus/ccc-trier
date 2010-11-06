package de.c3t;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NewsActivityGroup extends ActivityGroup{
	public static NewsActivityGroup group;
	
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		//this.setContentView(NewsActivity.getWindow().getDecorView());
		group = this;
		View view = getLocalActivityManager().startActivity("NewsActivity",
                new Intent(this, NewsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
		
		setContentView(view);
	}
	
}
