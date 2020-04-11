package com.example.soldiercountdownpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Main");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext()); // Get all the saved preferences from the settings activity

        boolean performSync = prefs.getBoolean("perform_sync", true);
        String syncInterval = prefs.getString("sync_interval", "30");
        String fullName = prefs.getString("full_name", "");
        String email = prefs.getString("email_address", "");

        Toast.makeText(this, performSync + "", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, syncInterval, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, fullName, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, email, Toast.LENGTH_SHORT).show();

        prefs.edit().putBoolean("shouldWe", true).apply(); // This is how i add a value to the shared preferences
        boolean shouldWe = prefs.getBoolean("shouldWe", false); // This is how to get it back


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(this, Settings.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
