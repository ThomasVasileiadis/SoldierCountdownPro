package com.example.soldiercountdownpro;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.bumptech.glide.Glide;
import java.text.ParseException;
import java.util.Calendar;

import cn.iwgang.countdownview.CountdownView;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sp;
    private CountDownTimer countDownTimer;
    private boolean timerRunning;
    public long difference;
    public long elapsedTime;
    View countDownText;
    public Context mContext;
    private EditText hours;
    private EditText minutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext()); // Get all the saved preferences from the settings activity
        SharedPreferences sp = getApplicationContext().getSharedPreferences("MyUserPrefs",Context.MODE_PRIVATE);
        String imagePath = sp.getString("picturePath",""); //Get the saved imagePath and load it again whenever MainActivity is "created" again
        mContext = getApplicationContext();

        hours = (EditText)findViewById(R.id.editTextHH);
        minutes = (EditText) findViewById(R.id.editTextMM);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        ImageView personalPhotoView = (ImageView) findViewById(R.id.frame);
        Glide.with(getApplicationContext())
                .load(imagePath)
                .centerCrop()
                .into(personalPhotoView);

        personalPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImageFromGallery();
            }
        });

        boolean er = prefs.getBoolean("enableReminders", false);
//        ComponentName receiver = new ComponentName(mContext, AlarmBootReceiver.class);
//        PackageManager pm = mContext.getPackageManager();
        //If er(enableReminders) is true then enabled a scheduled alarm, else cancel it
        if (er){
            NotificationHelper.scheduleRepeatingRTCNotification(mContext, hours.getText().toString(), minutes.getText().toString());
            NotificationHelper.enableBootReceiver(mContext);
        }else {
            NotificationHelper.cancelAlarmRTC();
            NotificationHelper.disableBootReceiver(mContext);
        }


        boolean performSync = prefs.getBoolean("perform_sync", true);
        String syncInterval = prefs.getString("sync_interval", "30");

        //Getting values into MainActivity, for keynames, from sharedPreferences
        String keyname = prefs.getString("keyname", "");
        String keyname2 = prefs.getString("keyname2", "");
        String keyname3 = prefs.getString("keyname3", "");
        String keyname4 = prefs.getString("keyname4", "");

        //Calling Calculator and passing the keynames I just got to calculate the difference between two dates in milliseconds
        Calculator myCalculator = new Calculator(keyname, keyname3, keyname2, keyname4);

        try {
            elapsedTime = myCalculator.calculateElapsed();
            Toast.makeText(this, "Elapsed = "+ elapsedTime + "ms",
                    Toast.LENGTH_SHORT).show();
            CountdownView mCvCountdownView = (CountdownView)findViewById(R.id.countDownView);
            mCvCountdownView.start(elapsedTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            difference = myCalculator.calculateDifference();
            Toast.makeText(this, "Difference = "+ difference + "ms",
                    Toast.LENGTH_SHORT).show();
            CountdownView mCvCountdownView = (CountdownView)findViewById(R.id.countDownView);
            mCvCountdownView.start(difference);
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

    //Function that opens gallery to pick an image from
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

    //When the user selects something, then load the image in ImageView(personalPhotoView) and save imagePath in sharedPreferences
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

            ImageView personalPhotoView = (ImageView) findViewById(R.id.frame);
            Glide.with(getApplicationContext())
                    .load(picturePath)
                    .centerCrop()
                    .into(personalPhotoView);
            sp = getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sp.edit();
            editor.putString("picturePath", picturePath);
            editor.commit();
            Toast.makeText(this, "Profile picture set.", Toast.LENGTH_SHORT).show();


        }
    }
    //       prefs.edit().putBoolean("shouldWe", true).apply(); // This is how i add a value to the shared preferences
    //       boolean shouldWe = prefs.getBoolean("shouldWe", false); // This is how to get it back

}
