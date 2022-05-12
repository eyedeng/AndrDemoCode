package com.example.andrdemocode.tabview.adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

/**
 * @author eye
 * @date 2022/5/10
 * @desc
 */
public class TabPagerAdapter extends FragmentStateAdapter {

    public static class TabPage {
        public final Fragment fragment;
        public final CharSequence title;

        public TabPage(Fragment fragment, String title) {
            this.fragment = fragment;
            this.title = title;
        }

        public static TabPage newPage(Fragment fragment, String title) {
            return new TabPage(fragment, title);
        }
    }

    private final List<TabPage> tabPages;

    public TabPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<TabPage> tabPages) {
        super(fragmentActivity);
        this.tabPages = tabPages;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return tabPages.get(position).fragment;
    }

    @Override
    public int getItemCount() {
        return tabPages.size();
    }

}
