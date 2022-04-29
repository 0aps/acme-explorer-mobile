package com.mis.acmeexplorer.trips;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import javax.annotation.Nullable;

public class TripService {

    final private FirebaseFirestore db;
    final private FirebaseStorage storage;

    public TripService(FirebaseFirestore db, FirebaseStorage storage) {
        this.db = db;
        this.storage = storage;
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

    public Task<DocumentReference> addTrip(Trip newTrip) {
        return db.collection("trips").add(newTrip);
    }

    public Task<Void> deleteTrip(Trip trip) {
        return db.collection("trips").document(trip.getId())
                .delete();
    }

    public UploadTask addTripPicture(Uri pictureFile) {
        StorageReference storageRef = storage.getReference()
                .child("images/" + pictureFile.getLastPathSegment());
        return storageRef.putFile(pictureFile);
    }
}
