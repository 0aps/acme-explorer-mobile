package com.mis.acmeexplorer.trips;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class TripService {

    final private FirebaseFirestore db;

    public TripService(FirebaseFirestore db) {
        this.db = db;
    }

    public Task<QuerySnapshot> loadTrips(@Nullable Filters filters) {
        Query query = db.collection("trips")
                .orderBy("startDate", Query.Direction.DESCENDING);

        if (filters != null) {
            if (filters.hasTitle()) {
                query = query.whereEqualTo("title", filters.getTitle());
            }
            if (filters.hasMinPrice()) {
                query = query.whereGreaterThanOrEqualTo("price", filters.getMinPrice());
            }
            if (filters.hasMaxPrice()) {
                query = query.whereLessThanOrEqualTo("price", filters.getMaxPrice());
            }
            if (filters.hasMinDate()) {
                query = query.whereGreaterThanOrEqualTo("startDate", filters.getMinDate());
            }
            if (filters.hasMaxDate()) {
                query = query.whereLessThanOrEqualTo("endDate", filters.getMaxDate());
            }
        }

        return query.get();
    }
}
