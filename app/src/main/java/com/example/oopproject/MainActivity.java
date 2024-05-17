package com.example.oopproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.LinearLayout;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private DrawerLayout drawer;
    private TasksPagerAdapter tasksPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply the selected theme
        ThemeUtil.applyTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
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
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
            }

            drawer.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reapply theme in case it has changed
        ThemeUtil.applyTheme(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.tako_text));
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.tako_text));
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
        if (viewPager.getCurrentItem() == 0) {
            refreshActiveTasks();
        }
    }

    public void refreshActiveTasks() {
        TaskManager.getInstance(getApplicationContext()).UpdateLiveDatas();
    }
}
