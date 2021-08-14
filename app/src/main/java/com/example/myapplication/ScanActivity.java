package com.example.myapplication;

import android.app.PendingIntent;
import android.content.Context;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

    }
}
