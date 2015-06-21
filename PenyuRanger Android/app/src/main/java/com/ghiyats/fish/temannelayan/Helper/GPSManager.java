package com.ghiyats.fish.temannelayan.Helper;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.widget.Toast;


import com.ghiyats.fish.temannelayan.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class GPSManager implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private Context mContext;

    private final String TAG = "GPSManager: ";

    private static final int PLAY_SERVICE_RESOLUTION__REQUEST = 1000;
    //location flags
    private boolean isGpsEnabled = false;

    private boolean isNetworkEnabled= false;
    private boolean isBestProviderAvail=false;


    private static int UPDATE_INTERVAL = 5000; //5 seconds
    private static int FASTEST__INTERVAL = 1000; //1seconds
    private static int DISPLACEMENT = 10;
    //location object to save location
    Location mLastLocation;


    //Google API Client to interact with Google Play Service
    GoogleApiClient mGoogleApiClient;

    LocationManager mLocationManager;

    public boolean checkProviderAvail(){
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        isGpsEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        Log.d(TAG,"GPS"+String.valueOf(isGpsEnabled));
        isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Log.d(TAG,"Network"+String.valueOf(isNetworkEnabled));
        if (isNetworkEnabled && isGpsEnabled){
            isBestProviderAvail =true;
            Log.d(TAG,"isBestProviderAvail"+String.valueOf(isBestProviderAvail));
        }
        else if (!isGpsEnabled || !isNetworkEnabled){
            isBestProviderAvail=false;
            Log.d(TAG,"isBestProviderAvail"+String.valueOf(isBestProviderAvail));
            setSettingAlert();
        }
        return isBestProviderAvail;
    }

    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    public void setSettingAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle)
                .setTitle("Location is Disabled")
                .setMessage("it seems that the location is not enabled on your device, " +
                        "Please go to setting and make sure you have Location Access" +
                        " with High Accuracy Enabled")
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mContext.startActivity(intent);
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

    public GPSManager(Context mContext) {
        this.mContext = mContext;
        if (isPlayserviceAvail()){
            //buildGoogleApiClient();
            checkProviderAvail();
            //mGoogleApiClient.connect();
        }
    }


    public synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        Log.d(TAG, "Google API Client built");
    }

    private boolean isPlayserviceAvail(){
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mContext);
        if (resultCode!=ConnectionResult.SUCCESS){
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
                GooglePlayServicesUtil.getErrorDialog(resultCode,(Activity) mContext,PLAY_SERVICE_RESOLUTION__REQUEST).show();
            }
            else {
                Toast.makeText(mContext, "This Device is Not Supported", Toast.LENGTH_SHORT).show();
                ((Activity)mContext).finish();
            }
            Log.d(TAG,"result false");
            return false;
        }
        Log.d(TAG, "result true");
        return true;
    }


    public Location getCurrentLocation(){
        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();
        LocationRequest newLocationRequest;
        newLocationRequest = new LocationRequest();
        newLocationRequest.setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST__INTERVAL)
                .setSmallestDisplacement(DISPLACEMENT)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, newLocationRequest, this);
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        double longitude;
        double latitude;

        if (mLastLocation!= null){
            longitude = mLastLocation.getLongitude();
            latitude = mLastLocation.getLatitude();
            ClipboardManager clip = (ClipboardManager)mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData data = ClipData.newPlainText("Location",latitude+","+longitude);
            clip.setPrimaryClip(data);
            Log.d(TAG,"Last Location :"+latitude+","+longitude);
        }
        else {
            Log.d(TAG, "unable to get location");
        }
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        return mLastLocation;
    }

    public String getLocationInfo(double latitude,double longitude){
        Geocoder geocoder;
        List<Address> addresses = new ArrayList<Address>();
        geocoder = new Geocoder(mContext, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude,longitude,1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        }catch (Exception e){
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();

        return knownName;
    }



    public boolean isNetworkEnabled() {
        return isNetworkEnabled;
    }

    public boolean isBestProviderAvail() {
        return isBestProviderAvail;
    }

    public boolean isGpsEnabled() {
        return isGpsEnabled;
    }

    public Location getmLastLocation() {
        return mLastLocation;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "connected");
        getCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }
}
