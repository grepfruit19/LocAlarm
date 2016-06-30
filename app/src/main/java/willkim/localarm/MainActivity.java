package willkim.localarm;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.provider.SyncStateContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.location.Location;
import android.location.LocationManager;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import java.util.List;
import java.util.Locale;



public class MainActivity extends AppCompatActivity{
public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=1;
    //Gets a reference to the system LocationManager
    LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    //Handles address to coords
    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
    //Will throw alarm
    Intent intent = new Intent(SyncStateContract.Constants.ACTION_PROXIMITY_ALERT);
    //Handles locations
    LocationListener locationListener = new LocationListener(){
        public void onLocationChanged(Location location){
            //Called when a new location is found by the network location provider.
            //makeUseOfNewLocation(location);
        }

        public void onStatusChanged(String provider, int status, Bundle extras){}

        public void onProviderEnabled(String provider){}

        public void onProviderDisabled(String provider) {}
    };


    public void sendAddress(View view){

        //Some permission checkery
        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }

        //Location provider
        String locationProvider = LocationManager.GPS_PROVIDER;

        //Handles updates, and last known location for faster init.
        locationManager.requestLocationUpdates(locationProvider,(60*1000),10,locationListener);
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);

        /* TODO: Sort out findViewByID*/
        final EditText addressField = (EditText) findViewById(R.id.EditTextName);
        String destination = addressField.getText().toString();

        List<Address> addresses=null;
        double destinationLat, destinationLong;
        try {
            addresses = geocoder.getFromLocationName(destination, 1);
        }
        catch (IOException IO) {
            new AlertDialog.Builder(this)
                    .setTitle("Network Unavailable")
                    .setMessage("Network is unavailable (or some unexpected error has occurred")
                    .setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
        }
        catch (IllegalArgumentException Nada) {
            new AlertDialog.Builder(this)
                    .setTitle("Null Argument")
                    .setMessage("No address entered")
                    .setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
        }
        if (addresses.size() > 0){
            destinationLat = addresses.get(0).getLatitude();
            destinationLong = addresses.get(0).getLongitude();
        }


        //Checks when you enter the area.
        locationManager.addProximityAlert(destinationLat, destinationLong,500,-1,intent);

    }
    //TODO: FINISH THIS
    //Wrapper for locManagers addProximityAlert
    public void addProximityAlert(double latitude, double longitude, float radius, long expiration){
        Intent intent = new Intent(PROX_ALERT_INTENT);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




    }

    @Override
    public void showTextView(){
        findViewById
    }


}
