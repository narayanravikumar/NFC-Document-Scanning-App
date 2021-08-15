package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static java.lang.Boolean.TRUE;

public class WriteDetails extends AppCompatActivity {


    public static final int SELECT_PICTURE = 100;
    private static final String TAG = "StoreImageActivity";
    public Uri selectedImageUri;
    EditText editTextId, editName, editSurname, editMarks;
    Button btnAddData, btngetData, btnUpdate, btnDelete, btnviewAll, btnOpenGallery;
    TextView textd, textd1;
    AppCompatImageView imgView;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    Details details;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_details);

        editTextId = (EditText) findViewById(R.id.editText_id);
        editName = (EditText) findViewById(R.id.editText_name);
        editSurname = (EditText) findViewById(R.id.editText_surname);
        editMarks = (EditText) findViewById(R.id.editText_Marks);
        btnAddData = (Button) findViewById(R.id.button_add);
        btngetData = (Button) findViewById(R.id.button_view);
        btnviewAll = (Button) findViewById(R.id.button_viewAll);
        btnUpdate = (Button) findViewById(R.id.button_update);
        btnDelete = (Button) findViewById(R.id.button_delete);
        btnOpenGallery = (Button) findViewById(R.id.btnSelectImage);
        imgView = findViewById(R.id.imgView);
        textd = findViewById(R.id.tagid1);


        Intent i = getIntent();
        String str = i.getStringExtra("message");
        textd.setText(str);
        btnOpenGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });
        AddData();
        //getData();
        //updateData();
        //deleteData();
        //viewAll();

    }


    void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    imgView.setImageURI(selectedImageUri);
                } else {
                    Toast.makeText(this, "Error and null", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    public void AddData() {


        btnAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  try {

                      myRef = database.getReference("Details");
                      details = new Details();

                    Boolean isInserted = TRUE;
                    if (isInserted == true)
                        Toast.makeText(WriteDetails.this, "PLEASE VERIFY YOUR DATA", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(WriteDetails.this, "Data could not be Inserted", Toast.LENGTH_LONG).show();


                    String str1 = editTextId.getText().toString();
                    String str0 = textd.getText().toString();
                    Intent i1 = new Intent(WriteDetails.this, WriteConfirm.class);
                    i1.putExtra("message1", str1);
                    i1.putExtra("message0", str0);


                    String str2 = editName.getText().toString();
                    i1.putExtra("message2", str2);

                    String str3 = editSurname.getText().toString();
                    i1.putExtra("message3", str3);


                    String str4 = editMarks.getText().toString();
                    i1.putExtra("message4", str4);

                      if (TextUtils.isEmpty(str1) && TextUtils.isEmpty(str0) && TextUtils.isEmpty(str2) && TextUtils.isEmpty(str3) && TextUtils.isEmpty(str4)) {
                          // if the text fields are empty
                          // then show the below message.
                          Toast.makeText(WriteDetails.this, "Please add some data.", Toast.LENGTH_SHORT).show();
                      } else {
                          // else call the method to add
                          // data to our database.
                          addDatatoFirebase(str0, str1 , str2, str3, str4);
                      }


                    //i1.putExtra("picture", b);
                    startActivity(i1);
                } catch (Exception nullpointer) {
                }


            }
        });


    }
    private void addDatatoFirebase(String id, String Docid, String Usn, String Name, String Marks) {
        // below 3 lines of code is used to set
        // data in our object class.
        details.setId(id);
        details.setDocId(Docid);
        details.setUSN(Usn);
        details.setName(Name);
        details.setMarks(Marks);
        // we are use add value event listener method
        // which is called with database reference.
        myRef.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // inside the method of on Data change we are setting
                // our object class to our database reference.
                // data base reference will sends data to firebase.
                myRef.setValue(details);

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


    /*public void getData() {
        btngetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = editTextId.getText().toString();

                if (id.equals(String.valueOf(""))) {
                    editTextId.setError("Enter id to get data");
                    return;
                }
                Cursor res = myDb.getData(id);
                String data = null;
                if (res.moveToFirst()) {
                    data = "Id:" + res.getString(0) + "\n" + "Name :" + res.getString(1) + "\n\n" + "Surname :" + res.getString(2) + "\n\n" + "Marks :" + res.getString(3) + "\n\n";
                }
                showMessage("Data", data);
            }
        });
    }

    public void viewAll() {
        btnviewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = myDb.getAllData();
                if (res.getCount() == 0) {
                    // show message
                    showMessage("Error", "Nothing found");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()) {
                    buffer.append("Id:" + res.getString(0) + "\n");
                    buffer.append("Name :" + res.getString(1) + "\n\n");
                    buffer.append("Surname :" + res.getString(2) + "\n\n");
                    buffer.append("Marks :" + res.getString(3) + "\n\n");
                    buffer.append("TAG :" + res.getString(5) + "\n\n");
                }
                showMessage("Data", buffer.toString());
            }
        });
    }

    public void updateData() {
        btnUpdate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InputStream iStream = null;
                        try {
                            iStream = getContentResolver().openInputStream(selectedImageUri);
                            byte[] inputData = Utils.getBytes(iStream);

                            boolean isUpdate = myDb.updateData(editTextId.getText().toString(),
                                    editName.getText().toString(),
                                    editSurname.getText().toString(), editMarks.getText().toString(), inputData);
                            if (isUpdate == true)
                                Toast.makeText(Write_page_1.this, "Data Update", Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(Write_page_1.this, "Data could not be Updated", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {

                        }
                    }
                }
        );
    }

    public void deleteData() {
        btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer deletedRows = myDb.deleteData(editTextId.getText().toString());
                        if (deletedRows > 0)
                            Toast.makeText(Write_page_1.this, "Data Deleted", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(Write_page_1.this, "Data could not be Deleted", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }


    private void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.create();
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }*/
}
