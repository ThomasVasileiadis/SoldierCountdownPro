package com.example.soldiercountdownpro;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.bumptech.glide.Glide;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sp;
    private boolean timerRunning;
    public long difference;
    public long elapsedTime;
    public long elapsedDays;
    public long elapsedWeeks;
    public long daysLeft;
    public Context mContext;
    private EditText hours;
    private EditText minutes;
    public TextView months_left2,weeks_left2,daysLeft2,hrsLeft2,minLeft2,secLeft2,endDate2,kolopsaro;
    private Handler handler;
    public Calculator myCalculator;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext()); // Get all the saved preferences from the settings activity
        SharedPreferences sp = getApplicationContext().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        String imagePath = sp.getString("picturePath", ""); //Get the saved imagePath and load it again whenever MainActivity is "created" again
        mContext = getApplicationContext();

        hours = (EditText) findViewById(R.id.editTextHH);
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

        //If er(enableReminders) is true then enabled a scheduled alarm, else cancel it
        if (er) {
            NotificationHelper.scheduleRepeatingRTCNotification(mContext, hours.getText().toString(), minutes.getText().toString());
            NotificationHelper.enableBootReceiver(mContext);
        } else {
            NotificationHelper.cancelAlarmRTC();
            NotificationHelper.disableBootReceiver(mContext);
        }

        //Getting values into MainActivity, for keynames, from sharedPreferences
        String keyname = prefs.getString("keyname", "");
        String keyname2 = prefs.getString("keyname2", "");
        String keyname3 = prefs.getString("keyname3", "");
        String keyname4 = prefs.getString("keyname4", "");

        //NullPointerException error handling mechanism
        if (!keyname.equals("") && !keyname2.equals("") & !keyname3.equals("") && !keyname4.equals("")) {
            myCalculator = new Calculator(keyname, keyname3, keyname2, keyname4);
            try {
                elapsedTime = myCalculator.calculateElapsed();
            } catch (ParseException | java.text.ParseException e) {
                e.printStackTrace();
            }
            try {
                elapsedDays = myCalculator.calculateElapsedInDays();
            } catch (ParseException | java.text.ParseException e) {
                e.printStackTrace();
            }
            try {
                elapsedWeeks = myCalculator.calculateElapsedInWeeks();
            } catch (ParseException | java.text.ParseException e) {
                e.printStackTrace();
            }
            try {
                difference = myCalculator.calculateDifference();
            } catch (ParseException | java.text.ParseException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Please select homecoming and service started date and time.", Toast.LENGTH_LONG).show();
        }
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    //When the user taps on the settings
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
    public void initView() {

        months_left2 = findViewById(R.id.months_left);
        daysLeft2 = findViewById(R.id.days_left);
        hrsLeft2 = findViewById(R.id.hrs_left);
        minLeft2 = findViewById(R.id.min_left);
        secLeft2 = findViewById(R.id.sec_left);
        kolopsaro = findViewById(R.id.kolopsaro);
        /*invoke countDownStart() method for start count down*/
        countDownStart();
    }
    public void countDownStart() {
        handler = new Handler();

        Runnable runnable = new Runnable() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {

                handler.postDelayed(this, 1000);

                try {
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    /*parse endDateTime in future date*/
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    final String enddate = prefs.getString("keyname", "");
                    final String endtime = prefs.getString("keyname3","") + ":00";
                    Date futureDate = dateFormat.parse(enddate+" "+endtime);
                    Date currentDate = new Date();
                    /*if current date is not comes after future date*/
                    if (!currentDate.after(futureDate)) {

                        String nowdate = java.time.LocalDate.now().toString();
                        String nowtime = java.time.LocalTime.now().toString().substring(0,8);

                        DateTimeFormatter dateformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        LocalDate end_dt = LocalDate.parse(enddate, dateformatter);
                        LocalDate start_dt = LocalDate.parse(nowdate,dateformatter);

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        Date d1 = format.parse(enddate+" "+endtime);
                        Date d2 = format.parse(nowdate+" "+nowtime);
                        long ddiff = d1.getTime() - d2.getTime();
                        Period period = Period.between(start_dt,end_dt);

                        long years = period.getYears();
                        long months = period.getMonths();
                        long ddday = period.getDays();
                        while(years>0){
                            months = months + 12;
                            years--;
                        }
                        long days = ddiff / (24 * 60 * 60 * 1000);

                        long day;

                        String check = enddate.split("-")[1];

                        if(check.equals("01") || check.equals("03") || check.equals("05") || check.equals("07") || check.equals("08") || check.equals("10") || check.equals("12")){
                            day = ddiff / (24 * 60 * 60 * 1000)%31;
                        }
                        else if(check.equals("04") || check.equals("06") || check.equals("09") || check.equals("11")){
                            day = ddiff / (24 * 60 * 60 * 1000)%30;
                        }
                        else{
                            int checky = Integer.parseInt(enddate.split("-")[0]);
                            if(checky % 4 == 0) {
                                day = ddiff / (24 * 60 * 60 * 1000) % 29;
                            }
                            else{
                                day = ddiff / (24 * 60 * 60 * 1000) % 28;
                            }
                        }

                        long weeks = day / 7 % 4;

                        while(day>=7){
                            day = day - 7;
                        }

                        long hours = ddiff / (60 * 60 * 1000) % 24;
                        long minutes = ddiff / (60 * 1000) % 60;
                        long seconds = ddiff / 1000 % 60;

                        @SuppressLint("DefaultLocale") String monthsLeft = "" + String.format("%02d", months);
                        @SuppressLint("DefaultLocale") String dayLeft = "" + String.format("%02d", ddday);
                        @SuppressLint("DefaultLocale") String hrLeft = "" + String.format("%02d", hours);
                        @SuppressLint("DefaultLocale") String minsLeft = "" + String.format("%02d", minutes);
                        @SuppressLint("DefaultLocale") String secondLeft = "" + String.format("%02d", seconds);

                        months_left2.setText(monthsLeft+" MONTHS");
                        daysLeft2.setText(dayLeft+" DAYS");
                        hrsLeft2.setText(hrLeft+" HOURS");
                        minLeft2.setText(minsLeft+" MINUTES");
                        secLeft2.setText(secondLeft+" SECONDS");

                    } else {
                        textViewGone();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    private void textViewGone() {
        months_left2.setVisibility(View.GONE);
        weeks_left2.setVisibility(View.GONE);
        daysLeft2.setVisibility(View.GONE);
        hrsLeft2.setVisibility(View.GONE);
        minLeft2.setVisibility(View.GONE);
        secLeft2.setVisibility(View.GONE);
        kolopsaro.setVisibility(View.VISIBLE);
    }
}