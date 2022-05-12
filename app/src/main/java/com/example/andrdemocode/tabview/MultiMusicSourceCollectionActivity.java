package com.example.andrdemocode.tabview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.andrdemocode.databinding.ActivityMultiMusicSourceCollectionBinding;
import com.example.andrdemocode.tabview.adapter.TabPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MultiMusicSourceCollectionActivity extends AppCompatActivity {
    private ActivityMultiMusicSourceCollectionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMultiMusicSourceCollectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        List<TabPagerAdapter.TabPage> tabPages = new ArrayList<>();
        tabPages.add(TabPagerAdapter.TabPage.newPage(SourceOneFragment.newInstance("", ""), "QQ音乐"));
        tabPages.add(TabPagerAdapter.TabPage.newPage(SourceTwoFragment.newInstance("", ""), "网易云音乐"));
        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(this, tabPages);
        binding.pager.setAdapter(tabPagerAdapter);

        new TabLayoutMediator(binding.tabLayout, binding.pager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(tabPages.get(position).title);
            }
        }).attach();
    }
}