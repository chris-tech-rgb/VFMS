package com.example.vfms.ui.gallery;

import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vfms.R;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private ArrayList<Place> placeArrayList;

    public RecyclerAdapter(ArrayList<Place> placeArrayList) {
        this.placeArrayList = placeArrayList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView place;
        private TextView popularity;

        public MyViewHolder(final View view) {
            super(view);
            place = view.findViewById(R.id.place);
            popularity = view.findViewById(R.id.popularity);
        }
    }

    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
        String name = placeArrayList.get(position).getName();
        String popularity = placeArrayList.get(position).getPopularity();
        holder.place.setText(name);
        holder.popularity.setText(popularity);
    }

    @Override
    public int getItemCount() {
        return placeArrayList.size();
    }
}
