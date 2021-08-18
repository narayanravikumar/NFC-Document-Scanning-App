/* Class file that connects to firebase in order to retrieve login credentials as well as cross check the right credentials with custom authentication*/
package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {

    TextView registertv;
    Button loginbtn, needhelpbtn;
    EditText email,password;
    FirebaseAuth auth;

    //Main method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        email=(EditText) findViewById(R.id.etEmailIDatatLogin);
        password=(EditText) findViewById(R.id.etPasswordatLogin);
        registertv = (TextView) findViewById(R.id.tvRegister);
        loginbtn = (Button) findViewById(R.id.btnLogin);
        needhelpbtn = (Button) findViewById(R.id.btnNeedHelp);

        //Connecting to Firebase
        try {
            FileInputStream serviceAccount =
                    new FileInputStream("nfc-protector-firebase-adminsdk-h2y9l-803bb60a60.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setProjectId("nfc-protector")
                .setApplicationId("1:35608449255:android:82208789b230eedb831242")
                .setApiKey("AIzaSyB3QCNmiAilFr5jbz6qIq2KCdmp3kSSqqs")
                .build();

        FirebaseApp.initializeApp(this,options,"active");

        auth= FirebaseAuth.getInstance(FirebaseApp.getInstance("active"));


        //Task to verify the data in Firebase with user details
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email != null && password != null){
                    String emailtext=email.getText().toString();
                    String passwordtext = password.getText().toString();

                    //authenticate the user
                    auth.signInWithEmailAndPassword(emailtext,passwordtext).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Login Successful", Toast.LENGTH_LONG).show();
                                Intent intent= new Intent(MainActivity.this, Menu.class);
                                email.setText(null);
                                password.setText(null);
                                startActivity(intent);
                            }else{
                                Toast.makeText(getApplicationContext(),"Login Error", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else{
                    Intent intent= new Intent(MainActivity.this, MainActivity.class);
                    email.setText(null);
                    password.setText(null);
                    Toast.makeText(getApplicationContext(),"Email and Password Should Not Be NULL",Toast.LENGTH_LONG).show();
                    startActivity(intent);
                }
            }
        });

        needhelpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Enter credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Goes to register page
    public void register(View view) {
        Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
        startActivity(intent);

    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                finish();
            }
        });
        builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert=builder.create();
        alert.show();
    }
}