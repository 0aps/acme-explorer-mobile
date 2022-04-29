package com.mis.acmeexplorer;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.mis.acmeexplorer.trips.AddTripActivity;
import com.mis.acmeexplorer.trips.FilterDialogFragment;
import com.mis.acmeexplorer.trips.Filters;
import com.mis.acmeexplorer.trips.Trip;
import com.mis.acmeexplorer.trips.TripActivity;
import com.mis.acmeexplorer.trips.TripService;
import com.mis.acmeexplorer.trips.TripsAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements
        FilterDialogFragment.FilterListener,
        NavigationView.OnNavigationItemSelectedListener {
    private TripService tripService;
    private FilterDialogFragment mFilterDialog;
    private RecyclerView rvTrips;
    private TripsAdapter tripAdapter;

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        loadTrips(null);
                    }
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setNavigation();

        mFilterDialog = new FilterDialogFragment();
        tripService = new TripService(FirebaseFirestore.getInstance(), null);
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
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home) {
            startActivity(new Intent(this, HomeActivity.class));
        } else if (id == R.id.logout) {
            this.logout();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                setDismissibleRecycle();
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
        rvTrips = findViewById(R.id.rv_trips);
        tripAdapter = new TripsAdapter(trips, new TripsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Trip trip) {
                onTripClicked(trip);
            }
        });
        rvTrips.setAdapter(tripAdapter);
        rvTrips.setLayoutManager(new LinearLayoutManager(this));
    }

    private void onTripClicked(Trip trip) {
        Intent intent = new Intent(this, TripActivity.class);
        intent.putExtra("trip", trip);
        startActivity(intent);
    }

    public void onAddTrip(View view) {
        Intent homeIntent = new Intent(this, AddTripActivity.class);
        activityResultLauncher.launch(homeIntent);
    }

    private void setNavigation() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void setDismissibleRecycle() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Trip trip = tripAdapter.getItem(position);

                tripAdapter.removeItem(position);
                tripAdapter.notifyItemRemoved(position);
                tripService.deleteTrip(trip);
            }
        }).attachToRecyclerView(rvTrips);
    }

}