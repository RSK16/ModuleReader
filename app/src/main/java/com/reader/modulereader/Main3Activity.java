package com.reader.modulereader;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

public class Main3Activity extends AppCompatActivity {

    private TextView mText;
    private Button active;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        mText = (TextView) findViewById(R.id.provName);
        active = (Button) findViewById(R.id.active);

    }



}
