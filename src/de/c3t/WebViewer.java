package de.c3t;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebViewer extends Activity {

	WebView mWebView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webviewer);

		mWebView = (WebView) findViewById(R.id.webview);
		mWebView.getSettings().setJavaScriptEnabled(true);
		String URL = getIntent().getStringExtra("URL");
		if (URL != null)
			mWebView.loadUrl(URL);
		else {
			String data = getIntent().getStringExtra("data");
			mWebView.loadData(data, "text/html", "utf-8");
			
		}
	}
}
