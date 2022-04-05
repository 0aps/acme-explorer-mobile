package com.mis.acmeexplorer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.mis.acmeexplorer.trips.FilterDialogFragment;
import com.mis.acmeexplorer.trips.Filters;
import com.mis.acmeexplorer.trips.Trip;
import com.mis.acmeexplorer.trips.TripActivity;
import com.mis.acmeexplorer.trips.TripService;
import com.mis.acmeexplorer.trips.TripsAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements
        FilterDialogFragment.FilterListener {
    private TripService tripService;
    private FilterDialogFragment mFilterDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mFilterDialog = new FilterDialogFragment();
        tripService = new TripService(FirebaseFirestore.getInstance());
        loadTrips(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            this.logout();
        } else if (item.getItemId() == R.id.action_show_filters) {
            this.showFilterDialog();
        } else if (item.getItemId() == R.id.action_clear_filters) {
            this.clearFilters();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFilter(Filters filters) {
        loadTrips(filters);
    }

    public void logout() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                    }
                });
    }

    public void showFilterDialog() {
        mFilterDialog.show(getSupportFragmentManager(), FilterDialogFragment.TAG);
    }

    public void clearFilters() {
        mFilterDialog.resetFilters();

        onFilter(new Filters());
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private void loadTrips(Filters filters) {
        tripService.loadTrips(filters).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                List<Trip> types = documentSnapshots.toObjects(Trip.class);
                displayTrips(new ArrayList<>(types));
                showMessage(types.size() + " trips loaded successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showMessage("Error loading trips. Please verify. " + e.getMessage());
            }
        });
    }

    private void displayTrips(ArrayList<Trip> trips) {
        RecyclerView rvContacts = findViewById(R.id.rvTrips);
        TripsAdapter adapter = new TripsAdapter(trips, new TripsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Trip trip) {
                onTripClicked(trip);
            }
        });
        rvContacts.setAdapter(adapter);
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
    }

    private void onTripClicked(Trip trip) {
        Intent intent = new Intent(this, TripActivity.class);
        intent.putExtra("trip", trip);
        startActivity(intent);
    }

}