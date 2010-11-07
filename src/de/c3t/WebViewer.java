
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
	    mWebView.loadUrl(getIntent().getStringExtra("URL"));
	}
}
