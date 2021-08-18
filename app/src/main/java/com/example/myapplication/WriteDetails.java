//Write details onto Firebase
package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class WriteDetails<context> extends AppCompatActivity {


    public static final int SELECT_PICTURE = 100;
    private static final String TAG = "StoreImageActivity";
    public Uri selectedImageUri;
    EditText editTextId, editName, editSurname, editMarks;
    Button btnAddData, btngetData, btnUpdate, btnDelete, btnviewAll, btnOpenGallery;
    TextView textd, textd1;
    //AppCompatImageView imgView;
    private Uri filePath;
    Uri imageUri;

    ImageView imgView;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    FirebaseStorage storage;
    StorageReference storageReference;
    Details details;
    String str1;
    int PICK_IMAGE = 200;
    ActivityResultLauncher<Intent> activityResultLauncher;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_details);
        textd = findViewById(R.id.tagid1);
        btnOpenGallery = (Button) findViewById(R.id.btnSelectImage);
        imgView = (ImageView) findViewById(R.id.imgView);
        storage= FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        btnAddData = (Button) findViewById(R.id.button_add);
        editTextId = (EditText) findViewById(R.id.editText_id);
        editName = (EditText) findViewById(R.id.editText_name);
        editSurname = (EditText) findViewById(R.id.editText_surname);
        editMarks = (EditText) findViewById(R.id.editText_Marks);
        btnDelete = (Button) findViewById(R.id.button_delete);


        try {
            activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        // Get the Uri of data
                        Bundle bumdle = result.getData().getExtras();
                        Bitmap bitmap = (Bitmap) bumdle.get("data");
                        imgView.setImageBitmap(bitmap);

                    }
                }
            });
        }catch (Exception e){
            Log.e("erooooooo",e.toString());
        }


        try{
            Intent i = getIntent();
            str1 = i.getStringExtra("message");
            textd.setText(str1);
            btnOpenGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //SelectImage();
                    Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(gallery, PICK_IMAGE);
                }
            });}catch (Exception e){
            Log.e("erorre",e.toString());
        }

        btnAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                upload();
                String str0 = editTextId.getText().toString();
                Intent i1 = new Intent(WriteDetails.this, WriteConfirm.class);
                i1.putExtra("message1", str1);
                i1.putExtra("message0", str0);


                String str2 = editName.getText().toString();
                i1.putExtra("message2", str2);

                String str3 = editSurname.getText().toString();
                i1.putExtra("message3", str3);


                String str4 = editMarks.getText().toString();
                i1.putExtra("message4", str4);
                i1.putExtra("picture", imageUri);

                if (TextUtils.isEmpty(str1) && TextUtils.isEmpty(str0) && TextUtils.isEmpty(str2) && TextUtils.isEmpty(str3) && TextUtils.isEmpty(str4)) {
                    // if the text fields are empty
                    // then show the below message.
                    Toast.makeText(WriteDetails.this, "Please add some data.", Toast.LENGTH_SHORT).show();
                } else {
                    // else call the method to add
                    // data to our database.
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // yourMethod();
                            addDatatoFirebase(str1, str0 , str3, str2, str4);
                            startActivity(i1);
                        }
                    }, 2000);   //2 seconds

                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Delete();

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                Query applesQuery = ref.child("details").orderByChild("id").equalTo(str1);

                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });

            }
        });




    }

    private  void Delete(){


        StorageReference ref= storageReference.child(str1);
        ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Toast.makeText(getApplicationContext(),"Deleted Data Base",Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @org.jetbrains.annotations.NotNull Exception e) {

                Toast.makeText(getApplicationContext(),"Unable to Deleted Data Base",Toast.LENGTH_LONG).show();

            }
        });


    }


    private  void upload(){


        StorageReference ref= storageReference.child(str1);
        ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(),"Image Uploaded",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.e("Errorrrr",e.toString());
                Toast.makeText(getApplicationContext(),"Image Uploaded Failed",Toast.LENGTH_LONG).show();
            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            imgView.setImageURI(imageUri);
        }
    }

    private void addDatatoFirebase(String id, String Docid, String Usn, String Name, String Marks) {
        // below 3 lines of code is used to set
        // data in our object class.
        database=FirebaseDatabase.getInstance();
        myRef = database.getReference("details");
        details = new Details(id,Docid,Usn,Name,Marks);



        myRef.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // inside the method of on Data change we are setting
                // our object class to our database reference.
                // data base reference will sends data to firebase.
                myRef.child(id).setValue(details);

                // after adding this data we are showing toast message.
                Toast.makeText(WriteDetails.this, "data added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                Toast.makeText(WriteDetails.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onBackPressed() {
        Intent intent = new Intent(WriteDetails.this, Menu.class);
        startActivity(intent);
    }

}

