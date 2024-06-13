package com.example.oopproject;

import androidx.fragment.app.Fragment; // Base class for all fragments
import androidx.fragment.app.FragmentManager; // Class for interacting with fragments within an activity
import androidx.lifecycle.Lifecycle; // Lifecycle class to represent the lifecycle of an object
import androidx.viewpager2.adapter.FragmentStateAdapter; // Adapter to provide fragments for ViewPager2

/*This class is an adapter for ViewPager2 that manages the fragments for
displaying active and completed tasks.
It uses an enum to define the positions of the tabs. The adapter creates the appropriate fragment
for each tab based on its position.
It overrides methods to provide the fragment, determine the number of items,
check for item validity, and assign a unique ID to each fragment.
The adapter is initialized with a FragmentManager and a Lifecycle.
 */


public class TasksPagerAdapter extends FragmentStateAdapter {

    // Define an enum to manage tab positions
    enum Tab {
        ACTIVE, // Tab for active tasks
        COMPLETED // Tab for completed tasks
    }

    // Constructor to initialize the adapter with a FragmentManager and Lifecycle
    public TasksPagerAdapter(FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager, lifecycle); // Call the superclass constructor
    }

    @Override
    public Fragment createFragment(int position) {
        // Return a new Fragment instance based on position using enum
        if (position == Tab.ACTIVE.ordinal()) {
            // Return a fragment for "Active" tasks
            return TasksFragment.newInstance(false);
        } else if (position == Tab.COMPLETED.ordinal()) {
            // Return a fragment for "Completed" tasks
            return TasksFragment.newInstance(true);
        } else {
            throw new IllegalStateException("Unexpected position " + position); // Throw an exception for unexpected positions
        }
    }

    @Override
    public long getItemId(int position) {
        // Each fragment type has a stable unique ID
        return position; // Return the position as the unique ID
    }

    @Override
    public int getItemCount() {
        // The count is based on the number of enum values
        return Tab.values().length; // Return the number of tabs
    }

    @Override
    public boolean containsItem(long itemId) {
        // Check if the item ID is within the range of the tabs
        return itemId >= 0 && itemId < getItemCount(); // Return true if the item ID is valid
    }
}
