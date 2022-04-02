package com.mis.acmeexplorer.trips;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class TripService {

    final private FirebaseFirestore db;

    public TripService(FirebaseFirestore db) {
        this.db = db;
    }

    public Task<QuerySnapshot> loadTrips() {
        Query mQuery = db.collection("trips")
                .orderBy("startDate", Query.Direction.DESCENDING);

        return mQuery.get();
    }
}
