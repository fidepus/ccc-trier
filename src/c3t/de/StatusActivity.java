package c3t.de;

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

import android.app.Activity;
import android.os.Bundle;

public class StatusActivity extends Activity {

	boolean isOn = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.status);
	}

	public void onStart() {
		super.onStart();
		try {
			fetchStatus();
		} catch (Exception e) {
		}
	}

	public void onResume() {
		super.onResume();
		try {
			fetchStatus();
		} catch (Exception e) {
		}
	}

	public void onRestart() {
		super.onRestart();
		try {
			fetchStatus();
		} catch (Exception e) {
		}
	}

	void setStatusOn() {
		findViewById(R.id.LinearLayout01).setBackgroundResource(R.drawable.status_led_on);
		isOn = true;
	}

	void setStatusOff() {
		findViewById(R.id.LinearLayout01).setBackgroundResource(R.drawable.status_led_off);
		isOn = false;
	}

	boolean getStatus() {
		return isOn;
	}

	void fetchStatus() throws XmlPullParserException, ClientProtocolException, URISyntaxException, IOException {
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
				System.out.println("TEXT Found: " + xpp.getText());
				if (xpp.getText().compareToIgnoreCase("1") == 0) {
					setStatusOn();
				} else if (xpp.getText().compareToIgnoreCase("0") == 0) {
					setStatusOff();
				}
			}
			eventType = xpp.next();
		}
	}

	public InputStream getUrlData(String url) throws URISyntaxException, ClientProtocolException, IOException {

		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet method = new HttpGet(new URI(url));
		HttpResponse res = client.execute(method);

		return res.getEntity().getContent();
	}
}
