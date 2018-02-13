package eurecom.moveit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class UserPackage extends AppCompatActivity {
    private String name;
    public List<Profil> List1;
    private ListView lv;
    public List<Profil> PackageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_package);
        lv = (ListView) findViewById(R.id.lv);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("SenderData2");

        addValueEventListener(myRef);
        addClicklistener();
    }

    private void Userpackage( ) {
        //Get user data from session
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> userDetails = sessionManager.getUserDetails();
        name= userDetails.get(SessionManager.KEY_NAME);

        PackageList = new ArrayList<>();


        for (Profil p : List1) {

            try {
            if (p.getNames().equals(name)) {

                    PackageList.add(p);

                }}
            catch (Exception e)
                {
                    Log.e("Transporter", "Incorrect data in DB");
                }


                }
        ArrayAdapter adapter = setupAdapter(PackageList);
        lv.setAdapter(adapter) ;
    }

private void addClicklistener(){
    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            // selected item
            String selected = PackageList.get(position).getDescri();

            Toast toast = Toast.makeText(getApplicationContext(), "Delete package: ", Toast.LENGTH_SHORT);
            toast.show();
            delete(selected);
        }
    });

}

    private ArrayAdapter setupAdapter(final List<Profil> PackageList) {
        /*get the adapter*/
        ArrayAdapter adapter = new ArrayAdapter(this,R.layout.profil, PackageList) {

            /*get the view*/
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.profil, parent, false);
                }

                ImageView thumbnail = (ImageView) convertView.findViewById(R.id.profil_thumbnail);
                ImageView delete = (ImageView) convertView.findViewById(R.id.profil_delete);

                TextView title = (TextView) convertView.findViewById(R.id.profil_title);
                TextView description = (TextView) convertView.findViewById(R.id.profil_description);

                Profil searchResult = PackageList.get(position);

                thumbnail.setImageResource(R.drawable.box);
                delete.setImageResource(R.drawable.close_button);
                title.setText("PACKAGE INFOS: \n" +"Price: "+ searchResult.getPrice() +" -Weight: "+ searchResult.getWeight()
                +" -Height: "+ searchResult.getHeight());
                description.setText(searchResult.getDescri());
                return convertView;
            }
        };
        return adapter;
    }

    private void addValueEventListener(final DatabaseReference ref) {
    /*add ValueEventListener to update data in realtime*/
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List1 = new ArrayList<>();
            /*this is called when first passing the data and
            * then whenever the data is updated*/
               /*get the data children*/
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();//get the arrays of the DB9get the user

                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();//get its positions index
                while (iterator.hasNext()) {
                /*get the values as a Friend object*/
                    try {
                        Profil value = iterator.next().getValue(Profil.class);
                        /*add the friend to the list for the adapter*/
                        List1.add(value);//add every time a user
                        Log.e("waaaaaaaaaaaa", value.getNames());

                    } catch (Exception e)
                    {
                        Log.e("Transporter", "Incorrect data in DB");
                    }
                }
                Userpackage( );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

public void delete(String d){

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    Query applesQuery = ref.child("SenderData2").orderByChild("descri").equalTo(d);

    applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                appleSnapshot.getRef().removeValue();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e("Transporter", "onCancelled", databaseError.toException());

        }
    });

}
}
