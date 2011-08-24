package de.c3t;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

class RssHandler {

	public RssContent fetchRSS(String url) throws XmlPullParserException, ClientProtocolException, URISyntaxException, IOException {
		List<String> titles = new ArrayList<String>();
		List<String> links = new ArrayList<String>();
		List<String> texts = new ArrayList<String>();

		XmlPullParserFactory factory = null;
		factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser xpp = null;
		xpp = factory.newPullParser();

		xpp.setInput(new InputStreamReader(getUrlData(url)));
		int eventType = 0;
		eventType = xpp.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG && xpp.getName().compareToIgnoreCase("item") == 0) {
				while (eventType != XmlPullParser.END_DOCUMENT) {
					if (eventType == XmlPullParser.START_TAG) {
						if (xpp.getName().compareToIgnoreCase("guid") == 0) {
							links.add(xpp.nextText());
						} else if (xpp.getName().compareToIgnoreCase("title") == 0) {
							titles.add(xpp.nextText());
						} else if (xpp.getName().compareToIgnoreCase("encoded") == 0) {
							texts.add(xpp.nextText());
						}
					}
					eventType = xpp.next();
				}
			}
			eventType = xpp.next();
		}
		RssContent rss = new RssContent(titles, links, texts);
		return rss;
	}

	private InputStream getUrlData(String url) throws URISyntaxException, ClientProtocolException, IOException {

		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet method = new HttpGet(new URI(url));
		HttpResponse res = client.execute(method);

		return res.getEntity().getContent();
	}

}

class RssContent {
	List<String> titles = new ArrayList<String>();

	List<String> links = new ArrayList<String>();

	List<String> texts = new ArrayList<String>();

	public RssContent(List<String> titles, List<String> links, List<String> texts) {
		this.titles = titles;
		this.links = links;
		this.texts = texts;
	}

	public List<String> getTitles() {
		return titles;
	}

	public List<String> getLinks() {
		return links;
	}

	public List<String> getTexts() {
		return texts;
	}
}