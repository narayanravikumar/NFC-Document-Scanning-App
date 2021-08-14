package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import static java.lang.Boolean.TRUE;

public class WriteDetails extends AppCompatActivity {


    public static final int SELECT_PICTURE = 100;
    private static final String TAG = "StoreImageActivity";
    public Uri selectedImageUri;
    EditText editTextId, editName, editSurname, editMarks;
    Button btnAddData, btngetData, btnUpdate, btnDelete, btnviewAll, btnOpenGallery;
    TextView textd, textd1;
    AppCompatImageView imgView;


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


                    //i1.putExtra("picture", b);
                    startActivity(i1);
                } catch (Exception nullpointer) {
                }


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
