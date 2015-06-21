package com.ghiyats.fish.temannelayan.Activity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.widget.Toast;


import com.ghiyats.fish.temannelayan.Helper.LocationDbHelper;
import com.ghiyats.fish.temannelayan.Model.TurtleModel;
import com.ghiyats.fish.temannelayan.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsView extends ActionBarActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LocationDbHelper dbHelper;

    private static final int PLAY_SERVICE_RESOLUTION__REQUEST = 1000;
    //location flags
    private boolean isGpsEnabled = false;

    private boolean isNetworkEnabled = false;
    private boolean isBestProviderAvail=false;

    private static int DISPLACEMENT = 10;

    private static String TAG="MapsView";

    //location object to save location
    Location mLastLocation;


    //Google API Client to interact with Google Play Service
    GoogleApiClient mGoogleApiClient;

    AppCompatDialog dialog;

    LocationManager mLocationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_view);
        dbHelper = new LocationDbHelper(this);

        if (isPlayserviceAvail()){
            buildGoogleApiClient();
            mGoogleApiClient.connect();
            checkProviderAvail();
        }


        setUpMapIfNeeded();

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        ArrayList<TurtleModel> locations = new ArrayList<TurtleModel>();
        locations = dbHelper.load();
        for (TurtleModel turtle : locations) {
            mMap.addMarker(new MarkerOptions().position(
                    new LatLng(Double.parseDouble(turtle.getLatitude()),
                            Double.parseDouble(turtle.getLongitude()))).title(turtle.getName()+", "
                    +turtle.getTurtleCategory()+" "
                    +turtle.getJmlTelur()+" butir"));
        }
        mMap.setMyLocationEnabled(true);

        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }


    public boolean checkProviderAvail(){
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        isGpsEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        Log.d(TAG,"GPS"+String.valueOf(isGpsEnabled));
        isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Log.d(TAG, "Network" + String.valueOf(isNetworkEnabled));
        if (isNetworkEnabled && isGpsEnabled){
            isBestProviderAvail =true;
            Log.d(TAG,"isBestProviderAvail"+String.valueOf(isBestProviderAvail));
        }
        else if (!isGpsEnabled && !isNetworkEnabled){
            isBestProviderAvail=false;
            Log.d(TAG,"isBestProviderAvail"+String.valueOf(isBestProviderAvail));
            setSettingAlert();
        }
        return isBestProviderAvail;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        setUpMap();
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.d("MapsView", "connected");
        try {
            getCurrentLocation();

        }catch (Exception e){
            e.printStackTrace();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getCurrentLocation();
                }
            }, 2000);
            Log.d(TAG,"Exception executed");
        }
    }


    public void setSettingAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle)
                .setTitle("Location is Disabled")
                .setMessage("it seems that the location is not enabled on your device, " +
                        "Please go to setting and make sure you have Location Access" +
                        " with High Accuracy Enabled")
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    public synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        Log.d(TAG, "Google API Client built");
    }

    private boolean isPlayserviceAvail(){
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode!=ConnectionResult.SUCCESS){
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
                GooglePlayServicesUtil.getErrorDialog(resultCode,this,PLAY_SERVICE_RESOLUTION__REQUEST).show();
            }
            else {
                Toast.makeText(this, "This Device is Not Supported", Toast.LENGTH_SHORT).show();
                finish();
            }
            Log.d(TAG,"Play Service not Available");
            return false;
        }
        Log.d(TAG, "Play Service Available");
        return true;
    }


    public Location getCurrentLocation(){
        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();
        LocationRequest newLocationRequest = new LocationRequest();
        newLocationRequest.setInterval(5000)
                .setFastestInterval(1000)
                .setSmallestDisplacement(DISPLACEMENT)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, newLocationRequest, this);
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        double longitude;
        double latitude;

        if (mLastLocation!= null){
            longitude = mLastLocation.getLongitude();
            latitude = mLastLocation.getLatitude();
            Log.d(TAG,"Last Location :"+latitude+","+longitude);
        }
        else {
            Log.d(TAG, "unable to get location");
        }

        //change map camera to current location
        CameraPosition myPosition = new CameraPosition.Builder()
                .target(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude()))
                .zoom(15)
                .bearing(0)
                .tilt(40)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));

        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
        return mLastLocation;
    }


    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("MapsView", "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }

    @Override
    public void onLocationChanged(Location location) {

    }
}
