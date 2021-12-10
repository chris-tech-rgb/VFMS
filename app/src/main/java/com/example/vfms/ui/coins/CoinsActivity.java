package com.example.vfms.ui.coins;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vfms.BackgroundWorker;
import com.example.vfms.MainActivity;
import com.example.vfms.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("ALL")
public class CoinsActivity extends AppCompatActivity {

    private ArrayList<Coin> coinArrayList;
    private RecyclerView recyclerView;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coins);

        Intent intent = getIntent();
        username = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        recyclerView = findViewById(R.id.recycler_coins);
        recyclerView.setHasFixedSize(true);
        coinArrayList = new ArrayList<>();
        try {
            if (!setCoinInfo()) {
                Snackbar.make(findViewById(R.id.fab), R.string.server_failure, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        } catch (ExecutionException | InterruptedException | ParseException e) {
            e.printStackTrace();
        }
        setAdapter();
    }

    private void setAdapter() {
        RecyclerAdapterCoins adapter = new RecyclerAdapterCoins(coinArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private boolean setCoinInfo() throws ExecutionException, InterruptedException, ParseException {
        String type = "check_coins";
        BackgroundWorker backgroundWorker = new BackgroundWorker();
        String output = backgroundWorker.execute(type, username, type).get();
        if (output == null) return false;
        JSONParser jsonParser = new JSONParser();
        JSONObject outputJSON = (JSONObject) jsonParser.parse(output);
        long row = (long) outputJSON.get("number");
        JSONObject list_coins = (JSONObject) outputJSON.get("list_coins");
        for (int i = 0; i < row; i++) {
            assert list_coins != null;
            coinArrayList.add(new Coin((String) list_coins.get(Integer.toString(i))));
        }
        return true;
    }
}