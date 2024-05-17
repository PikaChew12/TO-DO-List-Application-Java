package com.example.oopproject;

import android.app.AlertDialog; // Import AlertDialog for displaying dialogs
import android.content.DialogInterface; // Import DialogInterface for dialog button actions
import android.content.Intent; // Import Intent for activity navigation
import android.content.SharedPreferences; // Import SharedPreferences for saving user preferences
import android.os.Bundle; // Import Bundle for activity state saving
import android.view.Menu; // Import Menu for options menu
import android.widget.EditText; // Import EditText for input fields
import androidx.appcompat.app.ActionBarDrawerToggle; // Import ActionBarDrawerToggle for drawer toggle functionality
import androidx.appcompat.app.AppCompatActivity; // Import AppCompatActivity for activity base class
import androidx.core.content.ContextCompat; // Import ContextCompat for context-related compatibility methods
import androidx.core.view.GravityCompat; // Import GravityCompat for drawer gravity constants
import androidx.viewpager2.widget.ViewPager2; // Import ViewPager2 for swipeable views
import com.google.android.material.floatingactionbutton.FloatingActionButton; // Import FloatingActionButton for action button
import com.google.android.material.tabs.TabLayout; // Import TabLayout for tabbed navigation
import com.google.android.material.tabs.TabLayoutMediator; // Import TabLayoutMediator for linking TabLayout and ViewPager2
import android.text.InputType; // Import InputType for input field types
import android.widget.LinearLayout; // Import LinearLayout for layout management
import androidx.drawerlayout.widget.DrawerLayout; // Import DrawerLayout for navigation drawer
import androidx.appcompat.widget.Toolbar; // Import Toolbar for action bar
import com.google.android.material.navigation.NavigationView; // Import NavigationView for navigation drawer items

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager; // ViewPager2 for displaying different pages
    private boolean currentMode; // Boolean to track current theme mode
    private DrawerLayout drawer; // DrawerLayout for navigation drawer
    private TasksPagerAdapter tasksPagerAdapter; // Adapter for managing pages in ViewPager2

    @Override
    protected void onCreate(Bundle savedInstanceState) { // onCreate method called when activity is created
        super.onCreate(savedInstanceState); // Call superclass onCreate method

        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE); // Get shared preferences
        currentMode = sharedPreferences.getBoolean("LightMode", false); // Get saved theme mode from preferences

        if (currentMode) {
            setTheme(R.style.Theme_OOPProject); // Set theme based on preference (dark theme)
        } else {
            setTheme(R.style.Theme_OOPProject); // Set theme based on preference (light theme)
        }

        setContentView(R.layout.activity_main); // Set the content view to activity_main layout

        Toolbar toolbar = findViewById(R.id.toolbar); // Find toolbar by ID
        setSupportActionBar(toolbar); // Set toolbar as the action bar
        getSupportActionBar().setDisplayShowTitleEnabled(false); // Disable default title display
        setupViewPager(); // Setup the ViewPager

        FloatingActionButton fab = findViewById(R.id.fab); // Find FloatingActionButton by ID
        fab.setOnClickListener(view -> showAddTaskDialog()); // Set click listener to show add task dialog

        drawer = findViewById(R.id.drawer_layout); // Find DrawerLayout by ID
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close); // Create drawer toggle
        drawer.addDrawerListener(toggle); // Add drawer listener to toggle
        toggle.syncState(); // Sync the state of the drawer toggle

        NavigationView navigationView = findViewById(R.id.nav_view); // Find NavigationView by ID
        navigationView.setNavigationItemSelectedListener(item -> { // Set navigation item selected listener
            int id = item.getItemId(); // Get the ID of the selected item

            if (id == R.id.nav_settings) { // Check if settings item is selected
                Intent intent = new Intent(this, SettingsActivity.class); // Create intent for SettingsActivity
                startActivity(intent); // Start SettingsActivity
            }

            DrawerLayout drawer = findViewById(R.id.drawer_layout); // Find DrawerLayout by ID
            drawer.closeDrawer(GravityCompat.START); // Close the drawer on item click
            return true; // Return true to indicate the event was handled
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // Handle results from other activities
        super.onActivityResult(requestCode, resultCode, data); // Call superclass method
        if (resultCode == RESULT_OK) { // Check if the result is OK
            recreate(); // Recreate the activity to apply changes
        }
    }

    @Override
    protected void onResume() { // Called when the activity is resumed
        super.onResume(); // Call superclass method
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE); // Get shared preferences
        boolean isDarkMode = sharedPreferences.getBoolean("DarkMode", false); // Get current theme mode

        if (isDarkMode != currentMode) { // Check if the theme mode has changed
            recreate(); // Recreate the activity to apply the theme change
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { // Create options menu
        getMenuInflater().inflate(R.menu.menu_tasks, menu); // Inflate the menu
        return true; // Return true to display the menu
    }

    private void setupViewPager() { // Setup ViewPager
        viewPager = findViewById(R.id.viewPager); // Find ViewPager by ID
        tasksPagerAdapter = new TasksPagerAdapter(getSupportFragmentManager(), getLifecycle()); // Initialize adapter
        viewPager.setAdapter(tasksPagerAdapter); // Set adapter to ViewPager

        TabLayout tabLayout = findViewById(R.id.tabLayout); // Find TabLayout by ID
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> { // Link TabLayout and ViewPager
            tab.setText(position == 0 ? "Active" : "Completed"); // Set tab text based on position
        }).attach(); // Attach the mediator
    }

    private void showAddTaskDialog() { // Show dialog to add new task
        AlertDialog.Builder builder = new AlertDialog.Builder(this); // Create dialog builder
        builder.setTitle("Add New Task"); // Set dialog title

        final EditText inputTitle = createEditText("Title"); // Create input field for title
        final EditText inputDescription = createEditText("Description"); // Create input field for description

        LinearLayout layout = new LinearLayout(this); // Create a new linear layout
        layout.setOrientation(LinearLayout.VERTICAL); // Set orientation to vertical
        layout.addView(inputTitle); // Add title input to layout
        layout.addView(inputDescription); // Add description input to layout
        builder.setView(layout); // Set the custom layout to dialog

        builder.setPositiveButton("Add", (dialog, which) -> { // Set positive button with click listener
            String title = inputTitle.getText().toString(); // Get title text
            String description = inputDescription.getText().toString(); // Get description text
            addNewTask(title, description); // Add new task with title and description
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel()); // Set negative button with cancel action

        AlertDialog dialog = builder.show(); // Show the dialog
        dialog.show(); // Ensure dialog is shown
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.orange)); // Set positive button text color
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.orange)); // Set negative button text color
    }

    private EditText createEditText(String hint) { // Create an EditText with a hint
        EditText input = new EditText(this); // Create new EditText
        input.setInputType(InputType.TYPE_CLASS_TEXT); // Set input type to text
        input.setHint(hint); // Set hint for input
        return input; // Return the EditText
    }

    private void addNewTask(String title, String description) { // Add new task to task manager
        Task newTask = new Task(title); // Create new Task with title
        newTask.setDescription(description); // Set description for the task
        TaskManager.getInstance(getApplicationContext()).addTask(newTask); // Add task to TaskManager
        if (viewPager.getCurrentItem() == 0) { // Check if the current tab is active tasks
            refreshActiveTasks(); // Refresh the active tasks list
        }
    }

    public void refreshActiveTasks() { // Refresh the list of active tasks
        TaskManager.getInstance(getApplicationContext()).UpdateLiveDatas(); // Update LiveData in TaskManager
    }
}
