package com.example.soldiercountdownpro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.bumptech.glide.Glide;
import java.text.ParseException;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext()); // Get all the saved preferences from the settings activity


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        ImageView personalPhotoView = (ImageView) findViewById(R.id.personalPhoto);

        personalPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImageFromGallery();
            }
        });


        boolean er = prefs.getBoolean("enableReminders", false);

        if (er == true){
            Toast.makeText(this, "Reminders are enabled.",
                    Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Reminders are disabled.",
                    Toast.LENGTH_SHORT).show();
        }


        boolean performSync = prefs.getBoolean("perform_sync", true);
        String syncInterval = prefs.getString("sync_interval", "30");

        String keyname = prefs.getString("keyname", "");
        String keyname2 = prefs.getString("keyname2", "");
        String keyname3 = prefs.getString("keyname3", "");
        String keyname4 = prefs.getString("keyname4", "");

//        prefs.edit().putBoolean("shouldWe", true).apply(); // This is how i add a value to the shared preferences
//        boolean shouldWe = prefs.getBoolean("shouldWe", false); // This is how to get it back

        Calculator myCalculator = new Calculator(keyname, keyname3, keyname2, keyname4);
        try {
            System.out.println("Days passed: " + myCalculator.calculateDifference());
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

    public void addImageFromGallery() {
        try {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView personalPhotoView = (ImageView) findViewById(R.id.personalPhoto);
            Glide.with(getApplicationContext()).load(picturePath).centerCrop().into(personalPhotoView);
        }
    }
}
