package com.example.myapplication;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ScanActivity extends AppCompatActivity {

    public static final String ERROR_DETECTED = "No NFC tag detected!";
    public static final String WRITE_SUCCESS = "Text written to the NFC tag successfully!";
    public static final String WRITE_ERROR = "Error during writing, is the NFC tag close enough to your device?";
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];
    boolean writeMode;
    Tag myTag;
    Context context;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;


    TextView tvNFCContent, ida, namea, marks, surname;
    TextView message;
    Button btnWrite;
    ImageView imgLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        context = this;

        tvNFCContent = (TextView) findViewById(R.id.nfc_contents);
        ida = (TextView) findViewById(R.id.ID);
        namea = (TextView) findViewById(R.id.Name);
        surname = (TextView) findViewById(R.id.surname);
        marks = (TextView) findViewById(R.id.marks);
        message = (TextView) findViewById(R.id.edit_message);
        btnWrite = (Button) findViewById(R.id.button);

        imgLoaded = (ImageView) findViewById(R.id.Imageshow);
        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (myTag == null) {
                        Toast.makeText(context, ERROR_DETECTED, Toast.LENGTH_LONG).show();
                    } else {
                        write(message.getText().toString(), myTag);
                        Toast.makeText(context, WRITE_SUCCESS, Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    Toast.makeText(context, WRITE_ERROR, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                } catch (FormatException e) {
                    Toast.makeText(context, WRITE_ERROR, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

       nfcAdapter = NfcAdapter.getDefaultAdapter(this);
       if (nfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
        }
        Detail();
        readFromIntent(getIntent());

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writeTagFilters = new IntentFilter[]{tagDetected};


    }


    /******************************************************************************
     **********************************Read From NFC Tag***************************
     ******************************************************************************/
    private void readFromIntent(Intent intent) {
        String action = intent.getAction();
        NdefMessage[] msgs = null;
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }
            buildTagViews(msgs);
        }
    }

    public void Detail() {
        String text = "10";
        String ida1 = null;
        String namea1 = null;
        String surnamea1 = null;
        String marksa1 = null;

        if (text != null) {

            myRef = FirebaseDatabase.getInstance().getReference("Details");

            //orderByChild("Id").equalTo("10").

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot datas : dataSnapshot.getChildren()) {
                        String ida1 = datas.child("DocId").getValue().toString();
                        String name1 = datas.child("USN").getValue().toString();
                        String surname1 = datas.child("Name").getValue().toString();
                        String marks1 = datas.child("Marks").getValue().toString();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(ScanActivity.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
                }
            });
        }


        ida.setText("ID: " + ida1);
        namea.setText("NAME: " + namea1);
        surname.setText("ROLL NO: " + surnamea1);
        marks.setText("MARKS: " + marksa1);
    }

    private void buildTagViews(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0) return;

        String text = "";
//        String tagId = new String(msgs[0].getRecords()[0].getType());
        byte[] payload = msgs[0].getRecords()[0].getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16"; // Get the Text Encoding
        int languageCodeLength = payload[0] & 0063; // Get the Language Code, e.g. "en"
        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");

        try {
            // Get the Text

            text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
            tvNFCContent.setText("NFC CONTENT: " + text);
        } catch (Exception e) {

        }

        //String id = tvNFCContent.getText().toString();

        // if(id.equals(String.valueOf(""))){
        //tvNFCContent.setError("Enter id to get data");
        // }
        text = "10";
        String ida1 = null;
        String namea1 = null;
        String surnamea1 = null;
        String marksa1 = null;

        if (text != null) {

            myRef = FirebaseDatabase.getInstance().getReference("Details");

            myRef.orderByChild("Id").equalTo("10").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot datas : dataSnapshot.getChildren()) {
                        String ida1 = datas.child("DocId").getValue().toString();
                        String name1 = datas.child("USN").getValue().toString();
                        String surname1 = datas.child("Name").getValue().toString();
                        String marks1 = datas.child("Marks").getValue().toString();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(ScanActivity.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
                }
            });
        }


        ida.setText("ID: " + ida1);
        namea.setText("NAME: " + namea1);
        surname.setText("ROLL NO: " + surnamea1);
        marks.setText("MARKS: " + marksa1);
        try {


            // Show Image from DB in ImageView
            imgLoaded.post(new Runnable() {
                @Override
                public void run() {
                    //imgLoaded.setImageBitmap(Utils.getImage(bytes));

                }
            });
            imgLoaded.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent i1 = new Intent(Scan_page.this, Show_image.class);
                    //i1.putExtra("picture", bytes);
                    //startActivity(i1);
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Error " + e, Toast.LENGTH_LONG).show();
        }
          /* /
           // if (res.moveToFirst() && res!=null) {

           // }
            //ida.setText(idaa);
            namea.setText(nameaa);
            surname.setText(surnamea);
            marks.setText(marksa);*/

      /*  } catch (UnsupportedEncodingException e) {
            Log.e("UnsupportedEncoding", e.toString());
        }*/


    }


    /******************************************************************************
     **********************************Write to NFC Tag****************************
     ******************************************************************************/

    private void write(String text, Tag tag) throws IOException, FormatException {
        NdefRecord[] records = {createRecord(text)};
        NdefMessage message = new NdefMessage(records);
        // Get an instance of Ndef for the tag.
        Ndef ndef = Ndef.get(tag);
        // Enable I/O
        ndef.connect();
        // Write the message
        ndef.writeNdefMessage(message);
        // Close the connection
        ndef.close();
    }

    private NdefRecord createRecord(String text) throws UnsupportedEncodingException {
        String lang = "en";
        byte[] textBytes = text.getBytes();
        byte[] langBytes = lang.getBytes("US-ASCII");
        int langLength = langBytes.length;
        int textLength = textBytes.length;
        byte[] payload = new byte[1 + langLength + textLength];

        // set status byte (see NDEF spec for actual bits)
        payload[0] = (byte) langLength;

        // copy langbytes and textbytes into payload
        System.arraycopy(langBytes, 0, payload, 1, langLength);
        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);

        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload);

        return recordNFC;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        readFromIntent(intent);
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        WriteModeOff();
    }

    @Override
    public void onResume() {
        super.onResume();
        WriteModeOn();
    }


    /******************************************************************************
     **********************************Enable Write********************************
     ******************************************************************************/
    private void WriteModeOn() {
        writeMode = true;
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null);
    }

    /******************************************************************************
     **********************************Disable Write*******************************
     ******************************************************************************/
    private void WriteModeOff() {
        writeMode = false;
        nfcAdapter.disableForegroundDispatch(this);
    }


}

