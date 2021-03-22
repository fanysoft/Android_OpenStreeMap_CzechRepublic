package cz.vancura.openstreetmaps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;


import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.kml.Style;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.PointReducer;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Polygon;


import cz.vancura.openstreetmaps.model.KrajPOJO;

import static org.osmdroid.views.CustomZoomButtonsController.Visibility.NEVER;

public class MainActivity extends AppCompatActivity implements MapEventsReceiver {

    private static final String TAG = "myTAG-MainActivity";

    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;

    Context ctx;
    private static int[] colors = new int[15];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "started - onCreate");


        ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        // Color Array init
        InitColorArray();

        // map init
        Log.d(TAG, "map - init");
        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        // map hide default buttons
        map.getZoomController().setVisibility(NEVER);
        // map 2 fingers control on
        map.setMultiTouchControls(true);

        // map move to location and zoom
        Log.d(TAG, "map - zoom to CzechRep");
        IMapController mapController = map.getController();
        mapController.setZoom(8);
        GeoPoint startPoint = new GeoPoint(49.8112336, 15.3535708);
        mapController.setCenter(startPoint);


        /*

        Note : geoJSON data are stores in /app/assets

        1.Original folder - downloaded from http://polygons.openstreetmap.fr/index.py - isert Openstreet map Id for object you are looking for
                files has  x10-thousands points for each vector
        2.OriginalRaw folder - ad1 removed header
        3.Reduced folder - ad2 passed into ReduceJSON method to reduce size
                 once reduced - files has x1-thousands points for each vector
        4.ReducedRaw folder - ad3 removed header

         */



        // Array Kraje - 14 regions
        List<KrajPOJO> krajPOJOList = new ArrayList<>();

        krajPOJOList.add(new KrajPOJO("kraj02stedocesky.geoJSON", "Středočeský kraj", 2)); // POZOR - ve stredoceskem kraji je dira - Praha - nejdriv vykreslit Strdocesky potom Prahu
        krajPOJOList.add(new KrajPOJO("kraj01praha.geoJSON", "Praha", 1));
        krajPOJOList.add(new KrajPOJO("kraj03jihocesky.geoJSON", "Jihočeský kraj", 3));
        krajPOJOList.add(new KrajPOJO("kraj04vysocina.geoJSON", "Kraj Vysočina", 4));
        krajPOJOList.add(new KrajPOJO("kraj05plzensky.geoJSON", "Plzeňský kraj", 14));
        krajPOJOList.add(new KrajPOJO("kraj06karlovarsky.geoJSON", "Karlovarský kraj", 14));
        krajPOJOList.add(new KrajPOJO("kraj07ustecky.geoJSON", "Ústecký kraj", 7));
        krajPOJOList.add(new KrajPOJO("kraj08liberecky.geoJSON", "Liberecký kraj", 7));
        krajPOJOList.add(new KrajPOJO("kraj09hradecky.geoJSON", "Hradecký kraj", 14));
        krajPOJOList.add(new KrajPOJO("kraj10pardubicky.geoJSON", "Pardubický kraj", 12));
        krajPOJOList.add(new KrajPOJO("kraj11olomoucky.geoJSON", "Olomoucký kraj", 8));
        krajPOJOList.add(new KrajPOJO("kraj12moravskoslezky.geoJSON", "Moravskoslezský kraj", 3));
        krajPOJOList.add(new KrajPOJO("kraj13jihomoravsky.geoJSON", "Jihomoravský kraj", 5));
        krajPOJOList.add(new KrajPOJO("kraj14zlinsky.geoJSON", "Zlínský kraj", 5));


        // reduce method
        // - input geoJSON
        // - output reduced geojson file
        // - tolerance in hardcoded in method
        // JSON format like - 14.2406791,50.1112843  14.2404916,50.1112975 - spaces between GeoPair
        // ReduceJSON("2.originalRaw/kraj14zlinskyRaw.geoJSON", colors[1]);

        // loop kraje Array
        for (KrajPOJO krajPOJO : krajPOJOList){

            // show reduced data as FolderOverlay
            ShowKrajFolderOverlay("3.reduced/reduc-" + krajPOJO.getKrajFileName(), colors[krajPOJO.getKrajColor()]);

            // OR show reduced data as Polygon
            // data must be in format like 17.1198342,49.086051 17.1197671,49.0859109
            //ShowKrajPolygon("4.reducedRaw/reduc-" + krajPOJO.getKrajFileName(), krajPOJO.getKrajNazev(), colors[krajPOJO.getKrajColor()]);

        }


    }

    // load geoJSON file into FolderOverlay and show on map
    // Onclick Praha / Stredocesky kraj OK
    private void ShowKrajFolderOverlay(String filename, int color) {

        // OSMBonusPack - Load GeoJSON
        // https://github.com/MKergall/osmbonuspack/wiki/Tutorial_4

        Log.d(TAG, "ShowKrajFolderOverlay " + filename);

        String jsonString = null;
        try {
            InputStream jsonStream;
            jsonStream = getAssets().open(filename);
            int size = jsonStream.available();
            byte[] buffer = new byte[size];
            jsonStream.read(buffer);
            jsonStream.close();
            jsonString = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            Log.e(TAG, "ShowKrajFolderOverlay ERROR " + ex.getLocalizedMessage());
            ex.printStackTrace();
            return;
        }

        KmlDocument kmlDocument = new KmlDocument();
        boolean isOk = kmlDocument.parseGeoJSON(jsonString);


        if (!isOk) {
            Log.e(TAG, "ShowKrajFolderOverlay - parsing Failed, is this a valid GeoJSON?");
        } else {
            // vybarveni - fillColor
            Style defaultStyle = new Style(null, Color.BLACK, 2f, color);
            FolderOverlay geoJsonOverlay = (FolderOverlay) kmlDocument.mKmlRoot.buildOverlay(map, defaultStyle, null, kmlDocument);

            // onClick
            // NOTE : pokud je v JSON pole "properties":{"name":"A sample polygon"}}] - potom se po na polygon klinu zobrazi okno s textem, neni nutno nic pridavat
            MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this);
            geoJsonOverlay.add(mapEventsOverlay);
            // add Overlay
            map.getOverlays().add(geoJsonOverlay);
            // refresh map
            map.invalidate();
        }
    }


    // load geoJSON file into ArrayList of GeoPoint, use DouglasPeucker to reduce nr of points, save to file
    // JSON format like - 14.2406791,50.1112843  14.2404916,50.1112975 - spaces between GeoPair
    private void ReduceJSON(String filename, int color){

        Log.d(TAG, "ReduceJSON " + filename);

        // read file
        String jsonString = null;
        try {
            InputStream jsonStream;
            jsonStream = getAssets().open(filename);
            int size = jsonStream.available();
            byte[] buffer = new byte[size];
            jsonStream.read(buffer);
            jsonStream.close();
            jsonString = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            Log.e(TAG, "ReduceJSON file ERROR " + ex.getLocalizedMessage() + " " + ex.toString());
            ex.printStackTrace();
            return;
        }


        // input Array
        ArrayList<GeoPoint> arrayListToReduce = new ArrayList<>();
        // metoda je PointReducer.java protected tak jsem ji nakopiroval sem aby sla pouzit
        arrayListToReduce = parseKmlCoordinates(jsonString);
        // loop via input
        /*
        int i=0;
        for (GeoPoint geoPoint : arrayListToReduce) {
            Log.d(TAG, "input loop "+i+" " + geoPoint.getLatitude() + " " + geoPoint.getLongitude());
            i++;
        }
        */
        Log.d(TAG, "ReduceJSON - reducer - original size=" + arrayListToReduce.size()); // praha 3601

        // output Array
        ArrayList<GeoPoint> arrayListResult = new ArrayList<>();
        // reducer = The tolerance to decide whether or not to keep a point, in the coordinate system of the points (micro-degrees here) - double
        arrayListResult = PointReducer.reduceWithTolerance(arrayListToReduce, 0.001);  // s tolerance 0.001 vrazi napr Praha z 3601 vysledek 277 - ok !!

        Log.d(TAG, "ReduceJSON - reducer - result size=" + arrayListResult.size());
        // loop via result
        /*
        int y=0;
        for (GeoPoint geoPoint : arrayListResult) {
            Log.d(TAG, "result loop "+y+" " + geoPoint.getLatitude() + " " + geoPoint.getLongitude());
            y++;
        }
        */

        // Polygon
        Polygon polygon = new Polygon();
        polygon.setPoints(arrayListResult);

        // save result to file as KML or GeoJSON
        //https://github.com/MKergall/osmbonuspack/wiki/Tutorial_4
        KmlDocument kmlDocument = new KmlDocument();
        kmlDocument.mKmlRoot.addOverlay(polygon, kmlDocument);

        String newFileName = "reduc.geoJson";
        File localFile = kmlDocument.getDefaultPathForAndroid(newFileName);
        Log.d(TAG, "ReduceJSON - localFile path=" + localFile.getPath()); ///storage/emulated/0/kml/....json, same as /sdcard/kml/.. json

        boolean result = kmlDocument.saveAsGeoJSON(localFile);
        Log.d(TAG, "ReduceJSON - writing file result="+result);


    }


    // load geoJSON file into ArrayList of GeoPoint, show on map as polygon
    // data must be in format like 17.1198342,49.086051 17.1197671,49.0859109
    // Onclick Praha / Stredocesky kraj OK
    private void ShowKrajPolygon(String filename, String krajName, int color){

         Log.d(TAG, "ShowKrajPolygon " + filename);

        // read file
        String jsonString = null;
        try {
            InputStream jsonStream;
            jsonStream = getAssets().open(filename);
            int size = jsonStream.available();
            byte[] buffer = new byte[size];
            jsonStream.read(buffer);
            jsonStream.close();
            jsonString = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            Log.e(TAG, "ShowKrajPolygon ERROR " + ex.getLocalizedMessage() + " " + ex.toString());
            ex.printStackTrace();
            return;
        }

        //Log.d(TAG, "ShowKrajPolygon jsonString=" + jsonString);

        // input Array
        ArrayList<GeoPoint> arrayList = new ArrayList<>();
        // metoda je v PointReducer.java protected tak jsem ji nakopiroval sem aby sla pouzit
        arrayList = parseKmlCoordinates(jsonString);
        Log.d(TAG, "ShowKrajPolygon arrayList size=" + arrayList.size());


        // show result on map as Polygon
        Polygon polygon = new Polygon();
        polygon.getFillPaint().setColor(color); //set fill color
        polygon.setPoints(arrayList);
        polygon.setStrokeWidth(2f); // line width
        polygon.setTitle(krajName);
        polygon.setId(krajName);

        // onClick listener
        polygon.setOnClickListener(new Polygon.OnClickListener() {
            @Override
            public boolean onClick(Polygon polygon, MapView mapView, GeoPoint eventPos) {
                String what = "ShowKrajPolygon onClick on polygon " + polygon.getId();
                Log.d(TAG, what);
                Toast.makeText(ctx, what, Toast.LENGTH_SHORT).show();
                return false;
            }
        });


        // add to map
        map.getOverlayManager().add(polygon);
        // refresh map
        map.invalidate();

    }


    // Array of colors - scale from green to red
    private void InitColorArray(){

        // Array Color
        int jump = 6 ; // 100 / 15 - skok

        for (int i = 0; i < 15; i++) {

            int loopJump = jump * i;

            //  https://developer.android.com/reference/android/graphics/Color
            int A = 120; // Alpha 0-255 - ztmavuje barvu, snizuje pruhlednost

            //int R = i*jump2; // Red 0-255
            int R = (255 * loopJump) / 100; // colors from green to red

            //int G = 100; // Green 0-255
            int G = (255 * (100 - loopJump)) / 100; // colors from green to red

            int B = 0; // blue 0-255

            int newColor = (A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);

            colors[i] = newColor;
        }

    }



    @Override
    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }


    // OnClick short
    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        Log.d(TAG, "singleTapConfirmedHelper - position "  + p.getLatitude() + "," + p.getLongitude() );
        Toast.makeText(this, "Tap on (" + p.getLatitude() + "," + p.getLongitude() + ")", Toast.LENGTH_SHORT).show();
        return false;
    }

    // OnLongClick
    @Override
    public boolean longPressHelper(GeoPoint p) {
        Log.d(TAG, "longPressHelper - position "  + p.getLatitude() + "," + p.getLongitude());
        Toast.makeText(this, "Long press on (" + p.getLatitude() + "," + p.getLongitude() + ")", Toast.LENGTH_SHORT).show();
        return false;
    }


    // From PointReducer.java here it is protected to use
    /** KML coordinates are: lon,lat{,alt} tuples separated by separators (space, tab, cr). */
    private static ArrayList<GeoPoint> parseKmlCoordinates(String input){
        Log.d(TAG, "parseKmlCoordinates");
        LinkedList<GeoPoint> tmpCoords = new LinkedList<>();
        int i = 0;
        int tupleStart = 0;
        int length = input.length();
        boolean startReadingTuple = false;
        while (i<length){
            char c = input.charAt(i);
            if (c==' '|| c=='\n' || c=='\t'){
                if (startReadingTuple){ //just ending coords portion:
                    String tuple = input.substring(tupleStart, i);
                    GeoPoint p = parseKmlCoord(tuple);
                    if (p != null)
                        tmpCoords.add(p);
                    startReadingTuple = false;
                }
            } else { //data
                if (!startReadingTuple){ //just ending space portion
                    startReadingTuple = true;
                    tupleStart = i;
                }
                if (i == length-1){ //at the end => handle last tuple:
                    String tuple = input.substring(tupleStart, i+1);
                    GeoPoint p = parseKmlCoord(tuple);
                    if (p != null)
                        tmpCoords.add(p);
                }
            }
            i++;
        }
        ArrayList<GeoPoint> coordinates = new ArrayList<>(tmpCoords.size());
        coordinates.addAll(tmpCoords);
        return coordinates;
    }

    // From PointReducer.java here it is protected to use
    private static GeoPoint parseKmlCoord(String input){
        int end1 = input.indexOf(',');
        int end2 = input.indexOf(',', end1+1);
        try {
            if (end2 == -1){
                double lon = Double.parseDouble(input.substring(0, end1));
                double lat = Double.parseDouble(input.substring(end1+1, input.length()));
                return new GeoPoint(lat, lon);
            } else {
                double lon = Double.parseDouble(input.substring(0, end1));
                double lat = Double.parseDouble(input.substring(end1+1, end2));
                double alt = Double.parseDouble(input.substring(end2+1, input.length()));
                return new GeoPoint(lat, lon, alt);
            }
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return null;
        }
    }
}

