package com.example.oopproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import android.text.InputType;
import android.widget.LinearLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private boolean currentMode;
    private DrawerLayout drawer;
    private TasksPagerAdapter tasksPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        currentMode = sharedPreferences.getBoolean("LightMode", false);

        if (currentMode) {
            setTheme(R.style.Theme_OOPProject);// Dark theme style

        } else {
            setTheme(R.style.Theme_OOPProject);// Light theme style

        }

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar); // Your toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setupViewPager();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> showAddTaskDialog());

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_settings) {
                // Open Settings Activity
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
            }

            // Close drawer on item click
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check which request we're responding to
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                recreate();  // Recreate the main activity to apply theme change
            }
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Check if the theme setting has changed
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean("DarkMode", false);

        // Assuming `currentMode` is a field tracking the current theme mode
        if (isDarkMode != currentMode) {
            recreate(); // Recreate the activity to apply the theme change
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Assuming you have a menu xml for task actions like sort, etc.
        getMenuInflater().inflate(R.menu.menu_tasks, menu);
        return true;
    }

    private void setupViewPager() {
        viewPager = findViewById(R.id.viewPager);
        tasksPagerAdapter = new TasksPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(tasksPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(position == 0 ? "Active" : "Completed");
        }).attach();
    }

    private void showAddTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Task");

        final EditText inputTitle = createEditText("Title");
        final EditText inputDescription = createEditText("Description");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(inputTitle);
        layout.addView(inputDescription);
        builder.setView(layout);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String title = inputTitle.getText().toString();
            String description = inputDescription.getText().toString();
            addNewTask(title, description);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        AlertDialog dialog = builder.show();
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.orange)); // Adjust the color as needed
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.orange)); // Adjust the color as needed
    }

    private EditText createEditText(String hint) {
        EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint(hint);
        return input;
    }

    private void addNewTask(String title, String description) {
        Task newTask = new Task(title);
        newTask.setDescription(description);
        TaskManager.getInstance(getApplicationContext()).addTask(newTask);
        // Assuming the first tab is active tasks, only refresh if the active tasks tab is shown
        if (viewPager.getCurrentItem() == 0) {
            refreshActiveTasks();
        }
    }


    public void refreshActiveTasks() {
        // This should ensure that the tasks are updated via LiveData, not directly calling updateTaskList()
        TaskManager.getInstance(getApplicationContext()).UpdateLiveDatas(); // This would re-trigger LiveData updates
    }



    public void refreshCompletedTasks() {
        tasksPagerAdapter.notifyItemChanged(1);
    }

    public void refreshAllFragments() {
        refreshActiveTasks();
        refreshCompletedTasks();
    }
}
