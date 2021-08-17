package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
                                startActivity(intent);
                            }else{
                                Toast.makeText(getApplicationContext(),"Login Error", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(),"Email and Password Should Not Be NULL",Toast.LENGTH_LONG).show();
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

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.showmages, (android.view.Menu) menu);

        return super.onCreateOptionsMenu((android.view.Menu) menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.download1:
                Toast.makeText(getApplicationContext(), "Item 1 Selected", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void register(View view) {
        Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
        startActivity(intent);

    }
}