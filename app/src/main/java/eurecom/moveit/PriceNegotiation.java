package eurecom.moveit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class PriceNegotiation extends AppCompatActivity
{

    public static final String REQUEST_KEY = "key";
    public static final String DATABASE = "bidDatabase";

    TextView fromText;
    TextView toText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_negotiation);

        this.fromText = findViewById(R.id.fromText);
        this.toText = findViewById(R.id.toText);

        //@Key to get the request in database, passed by Transporter when the activity is started
        String requestKey = getIntent().getStringExtra(REQUEST_KEY);

        //Connect to Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference requestDatabase = database.getReference(Send.DATABASE_FOR_REQUEST);

        Query currentRequest = requestDatabase.equalTo(requestKey);
        currentRequest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Request request = dataSnapshot.getValue(Request.class);

                fromText.setText("From: Alexis test");
                toText.setText("To: " + request.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.e("PriceNegotiation", "No request found for id");
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        //@TODO List existing bids and last bidder
        DatabaseReference bidDatabase = database.getReference(PriceNegotiation.DATABASE);
        bidDatabase.child(requestKey);

    }


    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.declineButton:
                //REFUSE REQUEST
                break;

            case R.id.submitButton:
                //NEW BID
                break;

            case R.id.acceptButton:
                //ACCEPT REQUEST
                break;
        }
    }
}
