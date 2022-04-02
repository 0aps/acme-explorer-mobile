package com.mis.acmeexplorer.trips;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mis.acmeexplorer.R;

import java.util.List;

public class TripsAdapter extends
        RecyclerView.Adapter<TripsAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView descriptionTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.trip_title);
            descriptionTextView = itemView.findViewById(R.id.trip_description);
        }
    }

    private List<Trip> trips;

    public TripsAdapter(List<Trip> $trips) {
        trips = $trips;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_trip, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trip trip = trips.get(position);

        holder.titleTextView.setText(trip.getTitle());
        holder.descriptionTextView.setText(trip.getDescription());
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }
}