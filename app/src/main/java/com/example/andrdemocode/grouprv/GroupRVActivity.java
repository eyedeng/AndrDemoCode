package com.example.andrdemocode.grouprv;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.andrdemocode.databinding.ActivityGroupRvactivityBinding;
import com.example.andrdemocode.grouprv.adapter.EventItem;
import com.example.andrdemocode.grouprv.adapter.EventsAdapter;
import com.example.andrdemocode.grouprv.adapter.HeaderItem;
import com.example.andrdemocode.grouprv.adapter.ListItem;
import com.example.andrdemocode.grouprv.model.Event;

import java.util.ArrayList;
import java.util.List;

public class GroupRVActivity extends AppCompatActivity {
    private ActivityGroupRvactivityBinding binding;
    private List<ListItem> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupRvactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initData(items);
        binding.recyclerView.setAdapter(new EventsAdapter(items));
    }

    private void initData(List<ListItem> items) {
        for (int i = 1; i <= 3; i++) {
            items.add(new HeaderItem(i));
            for (int j = 0; j < 5; j++) {
                items.add(new EventItem(new Event("title " + j)));
            }
        }
    }
}