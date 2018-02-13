package eurecom.moveit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Users extends AppCompatActivity {
    TextView btnsender ,btntransporter,btnpackage ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        init();

        btnsender.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent sendi=new Intent(Users.this, Send.class) ;

                startActivity(sendi);

            }
        });
        btntransporter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("working ", "Button Transporter ***************--------------");

                Intent transi=new Intent(Users.this, Transporter.class) ;

                startActivity(transi);

            }
        });
        btnpackage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent packi=new Intent(Users.this, UserPackage.class) ;

                startActivity(packi);

            }
        });
    }
    public void init () {
        btnsender = findViewById(R.id.btnsender);
        btntransporter = findViewById(R.id.btntransporter);
        btnpackage = findViewById(R.id.btnpackage);
    }

}
