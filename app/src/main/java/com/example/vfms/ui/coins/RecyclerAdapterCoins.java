package com.example.vfms.ui.coins;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vfms.R;

import java.util.ArrayList;

public class RecyclerAdapterCoins extends RecyclerView.Adapter<RecyclerAdapterCoins.MyViewHolder> {

    private final ArrayList<Coin> coinArrayList;

    public RecyclerAdapterCoins(ArrayList<Coin> coinArrayList) {
        this.coinArrayList = coinArrayList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView coin;

        public MyViewHolder(final View view) {
            super(view);
            coin = view.findViewById(R.id.text_coin);
        }
    }

    @NonNull
    @Override
    public RecyclerAdapterCoins.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View coinsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_coins, parent, false);
        return new MyViewHolder(coinsView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterCoins.MyViewHolder holder, int position) {
        String value = coinArrayList.get(position).getValue();
        holder.coin.setText(value);
    }

    @Override
    public int getItemCount() {
        return coinArrayList.size();
    }
}
