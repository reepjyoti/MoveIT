package eurecom.moveit;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by test on 21/01/2018.
 */

public class Sending_Destination extends Fragment implements View.OnClickListener, LocationListener
{
    TabLayout tabLayout;
    View rootView;
    LocationManager locationManager;
    Location currentLocation;

    Geocoder geocoder;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    public boolean permissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    public Sending_Destination() {
        // Required empty public constructor
    }

    @Override
    public void onStart()
    {
        super.onStart();
        this.locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!gpsEnabled)
            enableLocationSettings();

        geocoder = new Geocoder(getContext(), Locale.getDefault());

    }

    private void enableLocationSettings(){
        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(settingsIntent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_sending_destination, container, false);

        tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs);

        rootView.findViewById(R.id.toPrice).setOnClickListener(this);
        rootView.findViewById(R.id.locationButton).setOnClickListener(this);
        rootView.findViewById(R.id.locationButtonDest).setOnClickListener(this);

        return rootView;
    }

    private String getAddressFromLocation()
    {
        getLocation();

        if (this.currentLocation != null) {
            try
            {
                List<Address> addresses = this.geocoder.getFromLocation(this.currentLocation.getLatitude(), this.currentLocation.getLongitude(), 1);
                if (addresses.size() >= 1)
                {
                    String returnString = "", temp = "";
                    returnString += ((temp = addresses.get(0).getAddressLine(0)) != null) ? " " + temp : "";
                    returnString += ((temp = addresses.get(0).getLocality()) != null) ? " " + temp : "";
                    returnString += ((temp = addresses.get(0).getAdminArea()) != null) ? " " + temp : " ";
                    returnString += ((temp = addresses.get(0).getCountryName()) != null) ? " " + temp : "";

                    return returnString;
                } else
                    return "";

            } catch(IOException e) {}

            return "";
        } else
            return "";
    }

    @Override
    public void onClick(View v)
    {

        switch(v.getId())
        {
            case R.id.locationButton:

                EditText fromField = rootView.findViewById(R.id.from);
                fromField.setText(getAddressFromLocation());
                break;

            case R.id.locationButtonDest:
                EditText destField = rootView.findViewById(R.id.destination);
                destField.setText(getAddressFromLocation());
                break;

            case R.id.toPrice:

                String destination = ((EditText) rootView.findViewById(R.id.destination)).getText().toString();
                String from = ((EditText) rootView.findViewById(R.id.from)).getText().toString();

                ((Send) getActivity()).saveDestination(from, destination);
                tabLayout.getTabAt(2).select();
                break;
        }


    }

    private void getLocation()
    {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            if (locationManager != null)
                this.currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            else
                Log.d("SendingDestination", "Can't get location");

        } catch (SecurityException e) {
            getLocationPermission();
        }
    }


    private void getLocationPermission()
    {
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            this.permissionGranted = true;
        else
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        this.permissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.permissionGranted = true;
                }
            }
        }
        getLocation();
    }


    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(getContext(), "Gps is now on", Toast.LENGTH_SHORT).show();
        getLocation();
    }

    @Override
    public void onProviderDisabled(String provider)
    {
        if (this.locationManager != null)
            this.locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location)
    {
        this.currentLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.locationManager != null) {
            this.locationManager.removeUpdates(this);
        }
    }
}
