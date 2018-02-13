package eurecom.moveit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignUp extends AppCompatActivity {

    public String UserNameS;
    String PwdS;
    String MailS;
    String phoneS;
    EditText UserName , Pwd, Mail, phone;
    TextView LoginLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Button b=(Button)findViewById(R.id.SubmitSI);
        UserName=(EditText)findViewById(R.id.UserName);
        Pwd=(EditText)findViewById(R.id.Pwd);
        Mail=(EditText)findViewById(R.id.Mail);
        phone=(EditText)findViewById(R.id.phone);

        LoginLink = (TextView) findViewById(R.id.link_login);

        // Write a message to the database
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Fetch inputs
                UserNameS=UserName.getText().toString();
                PwdS=Pwd.getText().toString();
                phoneS=phone.getText().toString();
                MailS=Mail.getText().toString();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("MoveIT");
                User user = new User();
                user.setMail(MailS);
                user.setPhone(phoneS);
                user.setPwd(PwdS);
                user.setUserName(UserNameS);
                myRef.push().setValue(user);

                Intent back=new Intent(SignUp.this, MainActivity.class) ;
                startActivity(back);
            }
        });

        LoginLink.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent loginIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(loginIntent);
            }
        });
    }





}
