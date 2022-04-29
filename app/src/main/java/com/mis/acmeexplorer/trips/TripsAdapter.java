package com.mis.acmeexplorer.trips;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mis.acmeexplorer.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TripsAdapter extends
        RecyclerView.Adapter<TripsAdapter.ViewHolder> {

    final private List<Trip> trips;
    final private OnItemClickListener itemListener;

    public TripsAdapter(List<Trip> trips, OnItemClickListener itemListener) {
        this.trips = trips;
        this.itemListener = itemListener;
    }

    public interface OnItemClickListener {
        void onItemClick(Trip trip);
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
        holder.priceTextView.setText(trip.getPriceString());
        Picasso.get().load(trip.getPicture()).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListener.onItemClick(trip);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public Trip getItem(int position) {
        return trips.get(position);
    }

    public void removeItem(int position) {
        trips.remove(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView descriptionTextView;
        public TextView priceTextView;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.trip_title);
            descriptionTextView = itemView.findViewById(R.id.trip_description);
            priceTextView = itemView.findViewById(R.id.trip_price);
            imageView = itemView.findViewById(R.id.trip_picture);
        }
    }

}