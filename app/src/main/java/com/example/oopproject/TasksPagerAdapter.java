package com.example.oopproject;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TasksPagerAdapter extends FragmentStateAdapter {
    // Define an enum to manage tab positions
    enum Tab {
        ACTIVE,
        COMPLETED
    }

    public TasksPagerAdapter(FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @Override
    public Fragment createFragment(int position) {
        // Return a new Fragment instance based on position using enum
        if (position == Tab.ACTIVE.ordinal()) {
            // "Active" tasks
            return TasksFragment.newInstance(false);
        } else if (position == Tab.COMPLETED.ordinal()) {
            // "Completed" tasks
            return TasksFragment.newInstance(true);
        } else {
            throw new IllegalStateException("Unexpected position " + position);
        }
    }

    @Override
    public long getItemId(int position) {
        // Each fragment type has a stable unique ID.
        return position;
    }

    @Override
    public int getItemCount() {
        // The count is based on the number of enum values.
        return Tab.values().length;
    }

    @Override
    public boolean containsItem(long itemId) {
        // Check if the item ID is within the range of the tabs.
        return itemId >= 0 && itemId < getItemCount();
    }
}
