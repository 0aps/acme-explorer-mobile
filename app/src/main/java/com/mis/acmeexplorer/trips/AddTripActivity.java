package com.mis.acmeexplorer.trips;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mis.acmeexplorer.R;
import com.mis.acmeexplorer.core.DatePickerFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.UUID;

public class AddTripActivity extends AppCompatActivity implements View.OnClickListener {

    final private DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
    private EditText mTitleEditText;
    private EditText mDescriptionEditText;
    private EditText mPriceEditText;
    private EditText mStartDateEditText;
    private EditText mEndDateEditText;

    private TripService tripService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        mTitleEditText = findViewById(R.id.input_title);
        mDescriptionEditText = findViewById(R.id.input_description);
        mPriceEditText = findViewById(R.id.input_price);
        mStartDateEditText = findViewById(R.id.trip_start_date);
        mEndDateEditText = findViewById(R.id.trip_end_date);

        mStartDateEditText.setOnClickListener(this);
        mEndDateEditText.setOnClickListener(this);

        tripService = new TripService(FirebaseFirestore.getInstance());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.trip_start_date:
                onDateFieldClicked(mStartDateEditText);
                break;

            case R.id.trip_end_date:
                onDateFieldClicked(mEndDateEditText);
                break;
        }
    }

    public void onSaveTrip(View view) {
        Trip newTrip = new Trip();
        String price = mPriceEditText.getText().toString();
        newTrip.setTicker(UUID.randomUUID().toString());
        newTrip.setTitle(mTitleEditText.getText().toString());
        newTrip.setDescription(mDescriptionEditText.getText().toString());
        newTrip.setPrice(price.isEmpty() ? 0 : Integer.parseInt(price));
        try {
            newTrip.setStartDate(formatter.parse(mStartDateEditText.getText().toString()));
            newTrip.setEndDate(formatter.parse(mEndDateEditText.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tripService.addTrip(newTrip).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                setResult(RESULT_OK, null);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),
                        "Error saving trip. Please verify. " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void onDateFieldClicked(EditText editText) {
        DatePickerFragment newFragment = new DatePickerFragment(new DatePickerFragment.OnDateListener() {
            @Override
            public void onSelected(int year, int month, int day) {
                editText.setText(String.format(Locale.getDefault(),
                        "%d/%d/%d", month + 1, day, year));
            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}