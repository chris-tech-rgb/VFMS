package com.example.vfms.ui.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vfms.BackgroundWorker;
import com.example.vfms.R;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class GalleryFragment extends Fragment {

    private ArrayList<Place> placeArrayList;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        recyclerView = root.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        placeArrayList = new ArrayList<>();
        try {
            setPlaceInfo();
        } catch (ExecutionException | InterruptedException | ParseException e) {
            e.printStackTrace();
        }
        setAdapter(root);

        return root;
    }

    private void setAdapter(View root) {
        RecyclerAdapter adapter = new RecyclerAdapter(placeArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    @SuppressWarnings("ConstantConditions")
    private void setPlaceInfo() throws ExecutionException, InterruptedException, ParseException {
        String type = "list";
        BackgroundWorker backgroundWorker = new BackgroundWorker();
        String output = backgroundWorker.execute(type, type, type).get();
        JSONParser jsonParser = new JSONParser();
        JSONObject outputJSON = (JSONObject) jsonParser.parse(output);
        long row = (long) outputJSON.get("number");
        JSONObject name_list = (JSONObject) jsonParser.parse((String) outputJSON.get("name_list"));
        JSONObject flow_list = (JSONObject) jsonParser.parse((String) outputJSON.get("flow_list"));
        Log.d("name_list", name_list.toString());
        Log.d("flow_list", flow_list.toString());
        for (int i = 0; i < row; i++) {
            placeArrayList.add(new Place((String) name_list.get(Integer.toString(i)), "人气 " + (long) flow_list.get((String) name_list.get(Integer.toString(i)))));
        }
    }
}