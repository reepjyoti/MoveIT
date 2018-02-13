package eurecom.moveit;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.R.attr.country;

public class Sender extends AppCompatActivity implements LocationListener {
    EditText txtname, txtphone, txtheight, txtwidth, txtweight, txtdescrip, txtdest,txtprice;
    TextView txtdep ;
    Button btncurrent123, btnorder123,btndest;
    String name, phones,hei,wi,we,desc,price ;
   // protected LocationManager locationManager = null;
    private String provider;
    Location location=null;
    String pos ;
    public static final int MY_PERMISSIONS_LOCATION = 0;
    Geocoder geocoder;
    List<android.location.Address> addresses;
    double lati,longi,a,b ;
    final String TAG = "GPS";
    private final static int ALL_PERMISSIONS_RESULT = 101;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

   // TextView tvLatitude, tvLongitude, tvTime;
    LocationManager locationManager;
    Location loc;
    ArrayList<String> permissions = new ArrayList<>();
    ArrayList<String> permissionsToRequest;
    ArrayList<String> permissionsRejected = new ArrayList<>();
    boolean isGPS = false;
    boolean isNetwork = false;
    boolean canGetLocation = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sender);

        Log.i("Permission: ", "Sender ***********************************************************");
        init();

        geocoder = new Geocoder(Sender.this, Locale.getDefault());
    }
    public void post2() {

        locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);
        isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);

        if (!isGPS && !isNetwork) {
            Log.d(TAG, "Connection off");
            showSettingsAlert();
            getLastLocation();
        } else {
            Log.d(TAG, "Connection on");
            // check permissions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (permissionsToRequest.size() > 0) {
                    requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
                            ALL_PERMISSIONS_RESULT);
                    Log.d(TAG, "Permission requests");
                    canGetLocation = false;
                }
            }

            // get location
            getLocation();


        }
    }

    public void worker (View view){
        Log.i("showLocation", "Entered");
        switch (view.getId()){
            case R.id.btncurrent123:
                Log.i("Permission: ", "current***********************************************************");

         post2();

             
                break;
            case R.id.btnorder123:
                Log.i("Permission: ", "order ***********************************************************");
                post2();
                lati = loc.getLatitude();
                longi = loc.getLongitude();
                String s=Double.toString(lati);
                String d=Double.toString(longi);
                Log.i("Permission: ", "we get location------+++++++----------------++++++++++"+s+" "+d);

                name=txtname.getText().toString();
                phones=txtphone.getText().toString();
                hei=txtheight.getText().toString();
                wi=txtwidth.getText().toString();
                we=txtweight.getText().toString();
                desc=txtdescrip.getText().toString();
                price=txtprice.getText().toString() ;
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("SenderData2");
                Log.i("Permission: ", "my ref is ready +++++++++++++++++++++++++++++++++++++++++++");

                DatabaseReference myPos = database.getReference("Positions");
                Log.i("Permission: ", "my pos is ready +++++++++++++++++++++++++++++++++++++++++++");

                Positions po=new Positions() ;
                po.setLati(lati);
                po.setLongi(longi);
                Profil prof = new Profil();
                prof.setPrice(price);
                prof.setLati(lati);
                prof.setLongi(longi);
                prof.setNames(name);
                prof.setPhone(phones);
                prof.setHeight(hei);
                prof.setWeight(we);
                prof.setDescri(desc);
                Log.i("Permission: ", "info is done");

                myRef.push().setValue(prof);
                myPos.push().setValue(po) ;
                break;

        }
    }

    public void init () {
        btncurrent123 = (Button) findViewById(R.id.btncurrent123);
        btnorder123 = (Button) findViewById(R.id.btnorder123);
        //btndest = (Button) findViewById(R.id.btndesti);
        txtname = (EditText)findViewById(R.id.txtname2);
        txtphone = (EditText) findViewById(R.id.txtphone2);
        txtheight = (EditText) findViewById(R.id.txtheight);
        txtweight = (EditText) findViewById(R.id.txtweight);
        txtwidth = (EditText) findViewById(R.id.txtwidth);
        txtdescrip = (EditText) findViewById(R.id.txtdescr);
        txtdest = (EditText) findViewById(R.id.txtdesti);
        txtprice = (EditText) findViewById(R.id.txtprice);
        txtdep = (TextView) findViewById(R.id.txtdep);
      //  tvTime = (TextView) findViewById(R.id.tvTime);
    }

    @Override protected void onStart() {
        super.onStart();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Log.i("GPS", "not enabled");
// Build an alert dialog here that requests the user
            // to enable location services when he clicks over "ok"
            enableLocationSettings();
        } else {
            Log.i("GPS", "enabled");
        }
    }
    private void enableLocationSettings(){
        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(settingsIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    public void updateLocationView() throws IOException {

    }



    private class GetCoordinates extends AsyncTask<String,Void,String> {
        ProgressDialog dialog = new ProgressDialog(Sender.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Please wait....");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String response;
            try{

                String address = strings[0];
                HttpDataHandler http = new HttpDataHandler();
                String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s",address);
                response = http.getHTTPData(url);
                return response;

            }
            catch (Exception ex)
            {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                JSONObject jsonObject = new JSONObject(s);

                String lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").get("lat").toString();
                String lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").get("lng").toString();

                a= Double.valueOf(lat.trim()).doubleValue();
                b= Double.valueOf(lng.trim()).doubleValue();


                if(dialog.isShowing())
                    dialog.dismiss();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged");
        this.loc = location;
        try {
            updateUI(loc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    @Override
    public void onProviderEnabled(String s) {
        getLocation();
    }

    @Override
    public void onProviderDisabled(String s) {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    private void getLocation() {
        try {
            if (canGetLocation) {
                Log.d(TAG, "Can get location");
                if (isGPS) {
                    // from GPS
                    Log.d(TAG, "GPS on");
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        Log.i("location",loc +"*******************************");
                        if (loc != null)
                            updateUI(loc);
                    }
                } else if (isNetwork) {
                    // from Network Provider
                    Log.d(TAG, "NETWORK_PROVIDER on");
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (loc != null)
                            updateUI(loc);
                    }
                } else {
                    loc.setLatitude(0);
                    loc.setLongitude(0);
                    updateUI(loc);
                }
            } else {
                Log.d(TAG, "Can't get location");
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getLastLocation() {
        try {
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, false);
            Location location = locationManager.getLastKnownLocation(provider);
            Log.d(TAG, provider);
            Log.d(TAG, location == null ? "NO LastLocation" : location.toString());
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList result = new ArrayList();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canAskPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                Log.d(TAG, "onRequestPermissionsResult");
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(
                                                        new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                } else {
                    Log.d(TAG, "No rejected permissions.");
                    canGetLocation = true;
                    getLocation();
                }
                break;
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("GPS is not Enabled!");
        alertDialog.setMessage("Do you want to turn on GPS?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(Sender.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void updateUI(Location location) throws IOException {
        Log.d(TAG, "updateUI");
       // location=this.loc;
       /* tvLatitude.setText(Double.toString());
        tvLongitude.setText(Double.toString());*/
        String address="**", state="++",country="--" ,city="//";
        if (location != null){
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            try{
            addresses = geocoder.getFromLocation(lat, lng, 1) ;
            }
            catch(IOException e)
            {
                addresses=null ;
            }

                if (addresses!=null) {
                    ; // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    try {
                        address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        city = addresses.get(0).getLocality();
                        state = addresses.get(0).getAdminArea();
                        country = addresses.get(0).getCountryName();
                    }
              catch (IndexOutOfBoundsException i)
              {

                  address="no address" ;
                  state=" ";
                  country=" unknown" ;
                  city=" " ;

              }
                }

                //String postalCode = addresses.get(0).getPostalCode();
                //String knownName = addresses.get(0).getFeatureName();
              else{
                    address="unknown" ;
                    state=" ";
                    country=" " ;
                    city=" " ;
                }




            pos=address+"==> "+state+"/"+city+ " "+country;
            txtdep.setText(pos);
            Log.i("showLocation","sssssssssssssssssssssssssss");
        } else{
            Log.i("showLocation","NULL");
            txtdep.setText("unknown") ;
        }
       // tvTime.setText(DateFormat.getTimeInstance().format(loc.getTime()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }
}


