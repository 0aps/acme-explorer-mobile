package com.mis.acmeexplorer.trips;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mis.acmeexplorer.HomeActivity;
import com.mis.acmeexplorer.R;
import com.mis.acmeexplorer.maps.IpService;
import com.mis.acmeexplorer.maps.MapActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class TripActivity extends AppCompatActivity {
    final private DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
    final private IpService ipService = new IpService(new OkHttpClient());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        Intent intent = getIntent();
        Trip trip = (Trip) intent.getSerializableExtra("trip");

        setTitle(trip.getTitle());

        TextView tickerTextView = findViewById(R.id.trip_ticker);
        TextView titleTextView = findViewById(R.id.trip_title);
        TextView descriptionTextView = findViewById(R.id.trip_description);
        TextView priceTextView = findViewById(R.id.trip_price);
        TextView startDateTextView = findViewById(R.id.trip_startDate);
        TextView endDateTextView = findViewById(R.id.trip_endDate);
        TextView longitudeTextView = findViewById(R.id.trip_longitude);
        TextView latitudeTextView = findViewById(R.id.trip_latitude);
        ImageView imageView = findViewById(R.id.trip_picture);

        tickerTextView.setText(trip.getTicker());
        titleTextView.setText(trip.getTitle());
        descriptionTextView.setText(trip.getDescription());
        priceTextView.setText(trip.getPriceString());
        startDateTextView.setText(formatter.format(trip.getStartDate()));
        endDateTextView.setText(formatter.format(trip.getEndDate()));
        longitudeTextView.setText(Double.toString(trip.getLongitude()));
        latitudeTextView.setText(Double.toString(trip.getLatitude()));
        Picasso.get().load(trip.getPicture()).into(imageView);

        getCurrentIp();
    }

    private void getCurrentIp() {
        ipService.getCurrentIp()
                .enqueue(new Callback() {
                    @Override
                    public void onResponse(@NonNull Call call, @NonNull final Response response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject json = new JSONObject(response.body().string());
                                    TextView currentIpTextView = findViewById(R.id.ip_value);
                                    currentIpTextView.setText(json.get("ip").toString());
                                } catch (JSONException | IOException e) {
                                    showIpErrorMessage();
                                }
                            }
                        });

                    }

                    @Override
                    public void onFailure(@NonNull final Call call, @NonNull IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showIpErrorMessage();
                            }
                        });
                    }
                });
    }

    void showIpErrorMessage() {
        Toast.makeText(getApplicationContext(),
                "There was an error getting the IP.",
                Toast.LENGTH_LONG).show();
    }

    public void viewTripMap(View view) {
        Intent intent = getIntent();
        Trip trip = (Trip) intent.getSerializableExtra("trip");

        Intent mapIntent = new Intent(this, MapActivity.class);
        mapIntent.putExtra("trip", trip);
        startActivity(mapIntent);
    }
}