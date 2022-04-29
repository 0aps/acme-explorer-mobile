package com.mis.acmeexplorer.trips;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask.TaskSnapshot;
import com.mis.acmeexplorer.R;
import com.mis.acmeexplorer.core.DatePickerFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.UUID;

public class AddTripActivity extends AppCompatActivity implements View.OnClickListener {

    final private DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
    private ImageView mImageView;
    private EditText mTitleEditText;
    private EditText mDescriptionEditText;
    private EditText mPriceEditText;
    private EditText mStartDateEditText;
    private EditText mEndDateEditText;
    private EditText mLatitudeEditText;
    private EditText mLongitudeEditText;

    private TripService tripService;
    private Uri pictureFile;

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        Uri selectedImageUri = intent.getData();
                        if (null != selectedImageUri) {
                            pictureFile = selectedImageUri;
                            mImageView.setImageURI(selectedImageUri);
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        mImageView = findViewById(R.id.input_picture);
        mTitleEditText = findViewById(R.id.input_title);
        mDescriptionEditText = findViewById(R.id.input_description);
        mPriceEditText = findViewById(R.id.input_price);
        mStartDateEditText = findViewById(R.id.trip_start_date);
        mEndDateEditText = findViewById(R.id.trip_end_date);
        mLatitudeEditText = findViewById(R.id.input_latitude);
        mLongitudeEditText = findViewById(R.id.input_longitude);

        mStartDateEditText.setOnClickListener(this);
        mEndDateEditText.setOnClickListener(this);

        tripService = new TripService(FirebaseFirestore.getInstance(),
                FirebaseStorage.getInstance());
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

    public void onSelectImage(View view) {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(Intent.createChooser(i, "Select Picture"));
    }

    public void onSaveTrip(View view) {
        Trip newTrip = new Trip();
        String price = mPriceEditText.getText().toString();
        String latitude = mLatitudeEditText.getText().toString();
        String longitude = mLongitudeEditText.getText().toString();
        newTrip.setTicker(UUID.randomUUID().toString());
        newTrip.setTitle(mTitleEditText.getText().toString());
        newTrip.setDescription(mDescriptionEditText.getText().toString());
        newTrip.setPrice(price.isEmpty() ? 0 : Float.parseFloat(price));
        newTrip.setLatitude(latitude.isEmpty() ? 0 : Float.parseFloat(latitude));
        newTrip.setLongitude(longitude.isEmpty() ? 0 : Float.parseFloat(longitude));
        try {
            newTrip.setStartDate(formatter.parse(mStartDateEditText.getText().toString()));
            newTrip.setEndDate(formatter.parse(mEndDateEditText.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (!newTrip.isValid()) {
            Toast.makeText(getApplicationContext(),
                    "Please complete all input fields.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                .child("images/" + pictureFile.getLastPathSegment());

        storageRef.putFile(pictureFile).addOnSuccessListener(new OnSuccessListener<TaskSnapshot>() {
            @Override
            public void onSuccess(TaskSnapshot taskSnapshot) {
                if (taskSnapshot.getMetadata() != null) {
                    if (taskSnapshot.getMetadata().getReference() != null) {
                        taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUrl) {
                                newTrip.setPicture(downloadUrl.toString());
                                addTrip(newTrip);
                            }
                        });
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),
                        "Error uploading trip picture. Please verify. " + e.getMessage(),
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

    private void addTrip(Trip newTrip) {
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
}