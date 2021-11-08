package com.example.hikerstool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView textViewLat, textViewLng, textViewAlt, textViewAcc, textViewAdd;

    LocationManager locationManager;
    LocationListener locationListener;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewLat = (TextView) findViewById(R.id.textViewLat);
        textViewLng = (TextView) findViewById(R.id.textViewLng);
        textViewAlt = (TextView) findViewById(R.id.textViewAlt);
        textViewAcc = (TextView) findViewById(R.id.textViewAcc);
        textViewAdd = (TextView) findViewById(R.id.textViewAdd);

        //Getting the location from the user

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {


                String latitude = "Latitude: " + location.getLatitude();
                String longitude = "Longitude: " + location.getLongitude();
                String altitude = "Altitude: " + location.getAltitude();
                String accuracy = "Accuracy: " + location.getAccuracy();
                String address = "Address:\n";

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    if(listAddresses != null && listAddresses.size() > 0) {

                        if(listAddresses.get(0).getThoroughfare() != null) {
                            address += listAddresses.get(0).getThoroughfare() + " ";
                        }

                        if(listAddresses.get(0).getLocality() != null) {
                            address += listAddresses.get(0).getLocality() + " ";
                        }

                        if(listAddresses.get(0).getPostalCode() != null) {
                            address += listAddresses.get(0).getPostalCode() + " ";
                        }

                        if(listAddresses.get(0).getAdminArea() != null) {
                            address += listAddresses.get(0).getAdminArea() + " ";
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                textViewLat.setText(latitude);
                textViewLng.setText(longitude);
                textViewAlt.setText(altitude);
                textViewAcc.setText(accuracy);
                textViewAdd.setText(address);

            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

    }
}