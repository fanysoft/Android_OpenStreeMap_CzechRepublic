package cz.vancura.openstreetmaps;

import android.view.GestureDetector;
import android.view.MotionEvent;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polygon;

public class CustomTapPolygon extends Polygon {

    private String tag;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e, MapView mapView) {
        if (e.getAction() == MotionEvent.ACTION_UP && contains(e)) {
            // YOUR CODE HERE
            return true;
        }
        return super.onSingleTapUp(e, mapView);
    }


    public void onTouchEvent(GestureDetector gestureDetector) {

    }
}


