package com.example.oopproject;

import android.content.Intent; // Import for Intent
import android.os.Bundle; // Import for Bundle
import android.view.Menu; // Import for Menu
import android.widget.EditText; // Import for EditText
import androidx.appcompat.app.ActionBarDrawerToggle; // Import for ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity; // Import for AppCompatActivity
import androidx.core.content.ContextCompat; // Import for ContextCompat
import androidx.core.view.GravityCompat; // Import for GravityCompat
import androidx.drawerlayout.widget.DrawerLayout; // Import for DrawerLayout
import androidx.viewpager2.widget.ViewPager2; // Import for ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton; // Import for FloatingActionButton
import com.google.android.material.navigation.NavigationView; // Import for NavigationView
import com.google.android.material.tabs.TabLayout; // Import for TabLayout
import com.google.android.material.tabs.TabLayoutMediator; // Import for TabLayoutMediator
import android.app.AlertDialog; // Import for AlertDialog
import android.content.DialogInterface; // Import for DialogInterface
import android.text.InputType; // Import for InputType
import android.widget.LinearLayout; // Import for LinearLayout
import androidx.appcompat.widget.Toolbar; // Import for Toolbar

/*This class serves as the main activity for the application.
 It sets up the user interface elements including the toolbar, navigation drawer,
 ViewPager2 for swiping between tabs, and a FloatingActionButton for adding new tasks.
 The class handles the initialization and configuration of these components, interactions such as opening settings,
 adding new tasks through a dialog, and refreshing the list of active tasks.
  It also applies the selected theme during the creation and resumption of the activity.
 */

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager; // ViewPager2 for swiping between pages
    private DrawerLayout drawer; // DrawerLayout for navigation drawer
    private TasksPagerAdapter tasksPagerAdapter; // Adapter for ViewPager2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply the selected theme
        ThemeUtil.applyTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Set up the ViewPager for swiping between tabs
        setupViewPager();

        // Set up the FloatingActionButton to add new tasks
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> showAddTaskDialog());

        // Set up the DrawerLayout and ActionBarDrawerToggle for navigation drawer
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Set up the NavigationView for handling navigation item clicks
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_settings) {
                // Open SettingsActivity when settings item is clicked
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
            }

            // Close the drawer when an item is clicked
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reapply the theme in case it has changed
        ThemeUtil.applyTheme(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        getMenuInflater().inflate(R.menu.menu_tasks, menu);
        return true;
    }

    private void setupViewPager() {
        viewPager = findViewById(R.id.viewPager);
        tasksPagerAdapter = new TasksPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(tasksPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // Set the text for each tab
            tab.setText(position == 0 ? "Active" : "Completed");
        }).attach();
    }

    private void showAddTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Task");

        // Create input fields for task title and description
        final EditText inputTitle = createEditText("Title");
        final EditText inputDescription = createEditText("Description");

        // Create a LinearLayout to hold the input fields
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(inputTitle);
        layout.addView(inputDescription);
        builder.setView(layout);

        // Set up the buttons for the dialog
        builder.setPositiveButton("Add", (dialog, which) -> {
            String title = inputTitle.getText().toString();
            String description = inputDescription.getText().toString();
            addNewTask(title, description); // Add the new task
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        // Show the dialog
        AlertDialog dialog = builder.show();

    }

    private EditText createEditText(String hint) {
        EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT); // Set input type to text
        input.setHint(hint); // Set hint for the EditText
        return input;
    }

    private void addNewTask(String title, String description) {
        Task newTask = new Task(title); // Create a new Task with the given title
        newTask.setDescription(description); // Set the description for the task
        TaskManager.getInstance(getApplicationContext()).addTask(newTask); // Add the task to TaskManager
        if (viewPager.getCurrentItem() == 0) {
            refreshActiveTasks(); // Refresh the active tasks if the current tab is active tasks
        }
    }

    public void refreshActiveTasks() {
        // Update the LiveData in TaskManager to refresh the task list
        TaskManager.getInstance(getApplicationContext()).UpdateLiveDatas();
    }
}
