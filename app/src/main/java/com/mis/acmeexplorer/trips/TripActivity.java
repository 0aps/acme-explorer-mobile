package com.mis.acmeexplorer.trips;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.mis.acmeexplorer.R;

public class TripActivity extends AppCompatActivity {

    private Trip trip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        Intent intent = getIntent();
        trip = (Trip) intent.getSerializableExtra("trip");

        setTitle(trip.getTitle());
    }
}