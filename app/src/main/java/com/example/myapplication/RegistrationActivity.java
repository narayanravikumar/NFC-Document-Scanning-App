//Class file to perform registration activity
package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    /*Button register=(Button) findViewById(R.id.register);
    EditText username,usn, email,pass,repass;
    FirebaseAuth fauth;
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
       /* username=(EditText) findViewById(R.id.etUserName);
        usn=(EditText) findViewById(R.id.etUSNatreg);
        email=(EditText) findViewById(R.id.etEmailIDatreg);
        fauth=FirebaseAuth.getInstance();
        pass=(EditText) findViewById(R.id.atPasswordatreg);
        repass=(EditText) findViewById(R.id.etRepasswordatreg);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pass==repass && username != null && usn != null && email != null && pass != null){

                }else{
                    Toast.makeText(getApplicationContext(),"Password doesn't Match With Retyped Password ",Toast.LENGTH_LONG).show();
                }

            }
        });*/
    }

}