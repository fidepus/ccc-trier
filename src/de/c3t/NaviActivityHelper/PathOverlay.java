package de.c3t.NaviActivityHelper;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

public class PathOverlay extends com.google.android.maps.Overlay {
	private ArrayList<GeoPoint> points = new ArrayList<GeoPoint>();
	
  @Override
  public boolean draw(Canvas canvas, MapView mv, boolean shadow, long when) {
          super.draw(canvas, mv, shadow);
          drawPath(mv, canvas);
          return true;
  }

  public void drawPath(MapView mv, Canvas canvas) {
          int x1 = -1, y1 = -1, x2 = -1, y2 = -1;
          Paint paint = new Paint();
          paint.setColor(Color.GREEN);
          paint.setStyle(Paint.Style.STROKE);
          paint.setStrokeWidth(3);
          for (int i = 0; i < points.size(); i++) {
                  Point point = new Point();
                  mv.getProjection().toPixels(points.get(i), point);
                  x2 = point.x;
                  y2 = point.y;
                  if (i > 0) {
                          canvas.drawLine(x1, y1, x2, y2, paint);
                  }
                  x1 = x2;
                  y1 = y2;
          }
  }
  
  public void addPoint(GeoPoint p){
  	points.add(p);
  }
  
  public void clear(){
  	points.clear();
  }
}
