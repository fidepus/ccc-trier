package de.c3t;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class StatusActivity extends Activity {

	boolean isOn = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.status);
		findViewById(R.id.StatusLayout).getBackground().setDither(true);
		fetchStatus();
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
	}

	void setStatusOff() {
		findViewById(R.id.StatusLayout).setBackgroundResource(R.drawable.status_off);
		ImageView image = (ImageView) findViewById(R.id.StatusLogo);
		image.setImageResource(R.drawable.status_porta_off);
		isOn = false;
	}

	void fetchStatus() {
		if (ClubStatus.getStatus(this))
			setStatusOn();
		else
			setStatusOff();
	}

	boolean getStatus() {
		return isOn;
	}
}
