package com.assignment.gistec.test12;

import android.app.ProgressDialog;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private HashMap<Integer,LatLng> locHashMap;
    private HashMap<Integer,String> namHashMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        locHashMap = new HashMap<Integer,LatLng>();
        namHashMap = new HashMap<Integer,String>();



        TestVolly();


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void PutMarker() {
        Toast.makeText(MapsActivity.this, "namHashMap.size()=" + namHashMap.size(), Toast.LENGTH_SHORT).show();


        LatLng latLng= null;
        String name;
        for(int i=0;i<locHashMap.size();i++) {
            latLng = locHashMap.get(i);
            name = namHashMap.get(i);
            mMap.addMarker(new MarkerOptions().position(latLng).title(name));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    private void TestVolly() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://devmaps.mopw.gov.ae/arcgis/rest/services/mopw/mopw_projects/MapServer/find?searchText=e&contains=true&layers=1&returnGeometry=true&returnZ=false&returnM=false&f=pjson";


        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                    //    String s =  response.toString();
                       // Toast.makeText(MapsActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                        try {
                            JSONArray obj = response.getJSONArray("results");
                            for (int i = 0; i < obj.length(); i++) {

                                JSONObject jsonObject = obj.getJSONObject(i);

                                //String a = jsonObject.getString("geometry");
                                JSONObject locJSON =jsonObject.getJSONObject("geometry");
                                String Latitude = locJSON.getString("y");
                                String Longitude= locJSON.getString("x");

                                LatLng latLng = new LatLng(Double.parseDouble(Latitude), Double.parseDouble(Longitude));

                                JSONObject namJSON =jsonObject.getJSONObject("attributes");
                                String nameEng = namJSON.getString("English_Name");
                                String nameAra = namJSON.getString("Arabic_Name");



                                locHashMap.put(i,latLng);
                                namHashMap.put(i,nameEng);


                            }
                            PutMarker();
                            //   String type = jsonObject.getString("type");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });

        queue.add(jsObjRequest);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Maps Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
