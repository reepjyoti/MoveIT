package eurecom.moveit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    Button btnlogin ;
    TextView signupLink;
    EditText txtuser, txtpwd ;
    public List<User> listUser;
    String txtusers,txtpwds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Read from the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("MoveIT");
        addValueEventListener(myRef);

        init();

       signupLink.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent signUp = new Intent(getApplicationContext(), SignUp.class) ;
                startActivity(signUp);
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


                txtusers = txtuser.getText().toString();
                txtpwds = txtpwd.getText().toString();

                //@TODO FIX THIS STUPID RETARD SHIT
                User loggedUser = null;
                for (User u : listUser) {
                    if (u.getUserName().equals(txtusers) && u.getPwd().equals(txtpwds))
                        loggedUser = u;
                }

                if (loggedUser != null)
                {
                    Log.i("Login", "Succesfull login for user");
                    Intent loggedActivity = new Intent(getApplicationContext(), Users.class);

                    SessionManager sessionManager = new SessionManager(getApplicationContext());
                    sessionManager.createLoginSession(loggedUser.getUserName(), loggedUser.getMail(), loggedUser.getPhone());

                    startActivity(loggedActivity);

                } else
                    Toast.makeText(getBaseContext(), "Invalid username and/or password", Toast.LENGTH_LONG).show();


            }
        });


    }


    public void init () {
        signupLink = (TextView) findViewById(R.id.btnsignup);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        txtuser = (EditText) findViewById(R.id.txtname);
        txtpwd = (EditText) findViewById(R.id.txtpwd);

    }

    private void addValueEventListener(final DatabaseReference ref) {
    /*add ValueEventListener to update data in realtime*/
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listUser = new ArrayList<>();
            /*this is called when first passing the data and
            * then whenever the data is updated*/
               /*get the data children*/
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();//get the arrays of the DB9get the user

                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();//get its positions index
                while (iterator.hasNext()) {
                /*get the values as a Friend object*/
                    User value = iterator.next().getValue(User.class);

                /*add the friend to the list for the adapter*/
                    listUser.add(value);//add every time a user

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }
        }


