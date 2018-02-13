package eurecom.moveit;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.provider.ContactsContract;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class Send extends AppCompatActivity {

    ViewPager simpleViewPager;
    TabLayout tabLayout;

    Integer packageDimension[];
    String packageDescription;
    String destination;
    String from;

    public static final String DATABASE_FOR_REQUEST = "SenderData3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.container);

        simpleViewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        simpleViewPager.setAdapter(pagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                simpleViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    /**
     * Save the description of a package from the fragment
     *
     * @param height        Height of a package
     * @param width         Width of a package
     * @param depth         Depth of a package
     * @param description   Description of the package
     */
    public void saveDescription(Integer height, Integer width, Integer depth, String description)
    {
        Log.i("SendActivity", "Validate description form");

        this.packageDimension = new Integer[] {height, width, depth};
        this.packageDescription = description;
    }

    /**
     * Save the route informations of the package
     *
     * @param from          Address of the sending point
     * @param destination   Address of the destination point
     */
    public void saveDestination(String from, String destination)
    {
        Log.i("SendActivity", "Route of the packet validated");
        this.destination = destination;
        this.from = from;

    }

    /**
     * Get the Address from a string representing the address
     *
     * @param address Address string
     * @return Address object
     * @TODO: 09/02/2018 Deals with the errors
     */
    private Address getLocationFromAddress(String address)
    {
        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList;

        try {
            addressList = geocoder.getFromLocationName(address, 1);
            if (addressList == null)
                return null;

            return addressList.get(0);


        } catch (Exception e)
        {
            Log.i("Send", "Unable to convert address to location");
            return null;
        }

    }

    /**
     * Save a request a in DB
     *
     * @param price Price chosen by the sender
     */
    public void postRequest(Integer price)
    {
        Log.i("SendActivity", "Process request");

        //Convert addresses string to lat/long points
        Address fromAddress = this.getLocationFromAddress(this.from);
        Address toAddress = this.getLocationFromAddress(this.destination);

        //Add the fromPoint to the database
        Positions po=new Positions() ;
        po.setLati(fromAddress.getLatitude());
        po.setLongi(fromAddress.getLongitude());

        //Get user data from session
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> userDetails = sessionManager.getUserDetails();

        //@Todo to be removed
        Profil prof = new Profil();
        prof.setPrice(price.toString());
        prof.setLati(fromAddress.getLatitude());
        prof.setLongi(fromAddress.getLongitude());
        prof.setNames(userDetails.get(SessionManager.KEY_NAME));
        prof.setPhone(userDetails.get(SessionManager.KEY_PHONE));
        prof.setHeight(this.packageDimension[0].toString());
        prof.setWeight(this.packageDimension[1].toString());
        prof.setDescri(this.packageDescription);
        //END OF REMOVAL

        //Build the request object
        Request newRequest = new Request(userDetails.get(SessionManager.KEY_NAME), userDetails.get(SessionManager.KEY_PHONE));
        newRequest.setPrice(price);
        newRequest.setDimensions(this.packageDimension);
        newRequest.setDescription(this.packageDescription);
        newRequest.setFromLatitude(fromAddress.getLatitude());
        newRequest.setFromLongitude(fromAddress.getLongitude());
        newRequest.setToLatitude(toAddress.getLatitude());
        newRequest.setToLongitude(toAddress.getLongitude());

        //Add in Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("SenderData2");
        DatabaseReference myPos = database.getReference("Positions");
        DatabaseReference myRequest = database.getReference(DATABASE_FOR_REQUEST);

        myRequest.push().setValue(newRequest);

        myRef.push().setValue(prof); //@TODO TO BE REMOVED
        myPos.push().setValue(po) ;

        Log.i("SendActivity", "Request added");

        //@todo Send also a message
        Intent intent = new Intent(getApplicationContext(), Users.class);
        startActivity(intent);

    }
}
