package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class WriteConfirm extends AppCompatActivity {

    TextView id, name, surname, marks, tagid;
    ImageView imgSet;
    Button chechImage;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    FirebaseStorage storage12 = FirebaseStorage.getInstance();
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_confirm);

        imgSet = findViewById(R.id.imageset);
        chechImage = findViewById(R.id.checkImage);




        tagid = findViewById(R.id.tagidvalue);
        Intent i0 = getIntent();
        String str0 = i0.getStringExtra("message1");
        tagid.setText(str0);

        id = findViewById(R.id.useridvalue);
        Intent i1 = getIntent();
        String str1 = i1.getStringExtra("message0");
        id.setText(str1);

        name = findViewById(R.id.usernamevalue);
        Intent i2 = getIntent();
        String str2 = i2.getStringExtra("message2");
        name.setText(str2);

        surname = findViewById(R.id.usersurnamevalue);
        Intent i3 = getIntent();
        String str3 = i3.getStringExtra("message3");
        surname.setText(str3);

        marks = findViewById(R.id.usermarksvalue);
        Intent i4 = getIntent();
        String str4 = i4.getStringExtra("message4");
        marks.setText(str4);
        String img = tagid.getText().toString();

        imgSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WriteConfirm.this,ShowImage.class);
                intent.putExtra("id",img);
                startActivity(intent);
            }
        });
        //Toast.makeText(getApplicationContext(), img, Toast.LENGTH_LONG).show();


        //storageReference = storage12.getReferenceFromUrl("gs://nfc-protector.appspot.com").child("ram.jpg");

        storageReference = FirebaseStorage.getInstance().getReference().child(img);
        chechImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final File lfile = File.createTempFile("1236", "jpeg");
                    storageReference.getFile(lfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            //Toast.makeText(getApplicationContext(),"image trying", Toast.LENGTH_LONG).show();
                            Bitmap bmp = BitmapFactory.decodeFile(lfile.getAbsolutePath());
                            imgSet.setImageBitmap(bmp);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Log.e("Main Problem",e.toString());
                            //Toast.makeText(getApplicationContext(),"image failed trying", Toast.LENGTH_LONG).show();


                            //Toast.makeText(getApplicationContext(), "Failed to retreive image", Toast.LENGTH_LONG).show();

                        }
                    });

                } catch (IOException e) {
                    Log.e("Errorrs", e.toString());
                }
            }
        });

    }

    public void onBackPressed() {
        Intent intent = new Intent(WriteConfirm.this, Menu.class);
        startActivity(intent);
    }
}


