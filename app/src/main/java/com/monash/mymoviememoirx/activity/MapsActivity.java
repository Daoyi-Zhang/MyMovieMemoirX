package com.monash.mymoviememoirx.activity;

import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.monash.mymoviememoirx.R;
import com.monash.mymoviememoirx.network.task.FindAllCinemas;
import com.monash.mymoviememoirx.pojo.Cinema;
import com.monash.mymoviememoirx.pojo.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final String TAG = "MapsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        //init camera location
        initCameraLocation();
        //init cinemas marker
        initMarks();


    }

    @SuppressLint("StaticFieldLeak")
    private void initMarks(){
        new FindAllCinemas(){
            @Override
            protected void onPostExecute(List<Cinema> cinemas) {
                if (cinemas != null){
                    Log.d(TAG, "onPostExecute: cinemas: " + cinemas);
                        for(int i = 0; i < cinemas.size(); i++){
                            String cinemaName = cinemas.get(i).getCinemaName();
                            String postcode = cinemas.get(i).getLocationPostcode();
                            String state = "";
                            int postcodeI = Integer.parseInt(postcode);
                            if((postcodeI >= 1000 && postcodeI <= 2599)
                                    || (postcodeI >= 2619 && postcodeI <= 2898)
                                    || (postcodeI >= 2921 && postcodeI <= 2999)){
                                state = "NSW";
                            }
                            else if((postcodeI >= 200 && postcodeI <= 299)
                                    || (postcodeI >= 2600 && postcodeI <= 2618)
                                    || (postcodeI >= 2900 && postcodeI <= 2920)){
                                state = "ACT";
                            }else if((postcodeI >= 3000 && postcodeI <= 3999)
                                    || (postcodeI >= 8000 && postcodeI <= 8999)){
                                state = "VIC";
                            }else if((postcodeI >= 4000 && postcodeI <= 4999)
                                    || (postcodeI >= 9000 && postcodeI <= 9999)){
                                state = "QLD";
                            }else if((postcodeI >= 5000 && postcodeI <= 5999)
                            ){
                                state = "SA";
                            }else if((postcodeI >= 6000 && postcodeI <= 6797)
                                    || (postcodeI >= 6800 && postcodeI <= 6999)){
                                state = "WA";
                            }else if((postcodeI >= 7000 && postcodeI <= 7999)
                            ){
                                state = "TAS";
                            }else if((postcodeI >= 800 && postcodeI <= 900)
                            ){
                                state = "NT";
                            }
                            LatLng cinema = getLocationFromAddress(getApplicationContext(), cinemaName +  " " + state + " " + postcode);
                            if(cinema == null){
                                Log.d(TAG, "onPostExecute: not found: " + cinemaName +  " " + state + " " + postcode);
                            }else {
                                mMap.addMarker(new MarkerOptions().position(cinema).title(cinemaName));
                            }
                        }
                }
            }
        }.execute();
    }

    private void initCameraLocation(){
        User user = (User) getIntent().getExtras().get("currentUser");
        String address = user.getAddress();
        String state = user.getState();
        String postcode = user.getPostcode();
        if (address != null && postcode != null){
            LatLng userHome = getLocationFromAddress(getApplicationContext(), address +  " " + state + " " + postcode);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(userHome));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(userHome).title("home");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            mMap.addMarker(markerOptions);
        }else {
            Toast.makeText(this, "ERROR: Network issue, please try again!", Toast.LENGTH_LONG).show();
        }
    }


    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }
        return p1;
    }
}
