package de.c3t;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import de.c3t.NaviActivityHelper.PathOverlay;

public class NaviActivity extends MapActivity {
	private MapView mapView;

	private MyLocationOverlay myLocationOverlay;

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// navi.xml contains a MapView
		setContentView(R.layout.navi);
		// extract MapView from layout
		MapView mapView = (MapView) findViewById(R.id.mapview);
		MapController mapController = mapView.getController();
		mapController.setZoom(15);
		final GeoPoint clubCoordinates = new GeoPoint(49764708, 6652758);
		mapController.animateTo(clubCoordinates);
		mapView.setBuiltInZoomControls(true);

		// create an overlay that shows our current location
		myLocationOverlay = new MyLocationOverlay(this, mapView);

		// add this overlay to the MapView and refresh it
		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.pin);
		NaviItemizedOverlay itemizedoverlay = new NaviItemizedOverlay(drawable, this);
		mapView.getOverlays().add(myLocationOverlay);
		mapView.postInvalidate();

		OverlayItem overlayitem = new OverlayItem(clubCoordinates, "CCC Trier", "Paulinstr. 123");

		itemizedoverlay.addOverlay(overlayitem);
		final PathOverlay pathOverlay = new PathOverlay();
		mapOverlays.add(pathOverlay);
		new Thread(new Runnable() {
			public void run() {
				if (myLocationOverlay.getMyLocation() != null)
					showRoute(pathOverlay, myLocationOverlay.getMyLocation(), clubCoordinates);
				else {
					System.out.println("de.c3t.NaviActivity: no location -> no route");
				}
			}
		}).start();
		mapOverlays.add(itemizedoverlay);
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				showRoute(pathOverlay, location, toLocation(clubCoordinates));
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}
		};

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10 * 1000, 50, locationListener);
	}

	private void showRoute(PathOverlay overlay, GeoPoint start, GeoPoint end) {
		showRoute(overlay, toLocation(start), toLocation(end));
	}

	private void showRoute(PathOverlay overlay, Location lstart, Location lend) {
		String url = getRouteXMLURL(lstart.getLatitude() + "," + lstart.getLongitude(), lend.getLatitude() + "," + lend.getLongitude());
		System.out.println("de.c3t.NaviActivity: using URL " + url);
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = null;
			xpp = factory.newPullParser();

			xpp.setInput(new InputStreamReader(ClubStatus.getUrlData(url)));
			int eventType = 0;
			int state = 0;// 1 = next text is lat //2 = next text is lng //3 = ignore cords
			int lat6 = 0, lon6 = 0;
			eventType = xpp.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				// System.out.println("de.c3t " + xpp.getName());
				if (eventType == XmlPullParser.START_TAG) {
					if (state != 3) {
						if (xpp.getName().equals("lat"))
							state = 1;
						else if (xpp.getName().equals("lng"))
							state = 2;
					} else {
						if (xpp.getName().equals("step"))
							state = 0;
					}
				} else if (eventType == XmlPullParser.TEXT) {
					if (state == 1) {
						lat6 = toMicroDegrees(Float.parseFloat(xpp.getText()));
						state = 0;
					} else if (state == 2) {
						lon6 = toMicroDegrees(Float.parseFloat(xpp.getText()));
						overlay.addPoint(new GeoPoint(lat6, lon6));
						System.out.println("de.c3t.NaviActivity: adding waypoint " + lat6 + "," + lon6);
						state = 0;
					}
				} else if (eventType == XmlPullParser.END_TAG) {
					if (xpp.getName().equals("step"))
						state = 3;
				}

				eventType = xpp.next();
			}
		} catch (XmlPullParserException e) {
			System.out.println("de.c3t.NaviActivity: XmlPullParserException");
		} catch (ClientProtocolException e) {
			System.out.println("de.c3t.NaviActivity: ClientProtocolException");
		} catch (URISyntaxException e) {
			System.out.println("de.c3t.NaviActivity: URISyntaxException");
		} catch (IOException e) {
			System.out.println("de.c3t.NaviActivity: IOException");
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// when our activity resumes, we want to register for location updates
		myLocationOverlay.enableMyLocation();
		myLocationOverlay.enableCompass();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// when our activity pauses, we want to remove listening for location updates
		myLocationOverlay.disableMyLocation();
		myLocationOverlay.disableCompass();
	}

	private void zoomToMyLocation() {
		GeoPoint myLocationGeoPoint = myLocationOverlay.getMyLocation();
		if (myLocationGeoPoint != null) {
			mapView.getController().animateTo(myLocationGeoPoint);
			mapView.getController().setZoom(10);
		} else {
			Toast.makeText(this, "Cannot determine location", Toast.LENGTH_SHORT).show();
		}
	}

	private String getRouteXMLURL(String start, String end) {
		return "http://maps.googleapis.com/maps/api/directions/xml?origin=" + start + "&destination=" + end + "&sensor=true";
	}

	public static Location toLocation(GeoPoint point) {
		Location result = new Location("");
		result.setLatitude(toDegrees(point.getLatitudeE6()));
		result.setLongitude(toDegrees(point.getLongitudeE6()));
		return result;
	}

	/**
	 * Convert microdegrees to degrees.
	 * 
	 * @param degreesE6
	 *          Value in microdegrees.
	 * @return Value in degrees.
	 */
	public static double toDegrees(int degreesE6) {
		return (double) degreesE6 / 1000000;
	}

	public static int toMicroDegrees(double degrees) {
		return (int) (degrees * 1000000);
	}

	/*
	 * public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);
	 * 
	 * TextView textview = new TextView(this); textview.setText("Hier ist später die Karte");
	 * setContentView(textview); }
	 */
}