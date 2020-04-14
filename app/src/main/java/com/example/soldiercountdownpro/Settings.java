package com.example.soldiercountdownpro;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.Calendar;

import static androidx.core.content.ContextCompat.getSystemService;


public class Settings extends PreferenceActivity {
    private TextView mDisplayDate;
    private static DatePickerDialog.OnDateSetListener mDateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
        mDisplayDate = (TextView) findViewById(R.id.tvDate);
    }

    public static class MyPreferenceFragment extends PreferenceFragment {


        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            // dp and blabla is for homecoming date
            final DatePreference dp = (DatePreference) findPreference("keyname");
            String blabla = dp.getText();
            dp.setSummary((String) blabla);
            dp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    //your code to change values.
                    dp.setSummary((String) newValue);
                    Toast.makeText(getActivity(), "Date has been set sucesfully.",
                            Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
            //dp2 and blabla2 is for service started date
            final DatePreference dp2 = (DatePreference) findPreference("keyname2");
            String blabla2 = dp2.getText();
            dp2.setSummary((String) blabla2);
            dp2.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    //your code to change values.
                    dp2.setSummary((String) newValue);
                    Toast.makeText(getActivity(), "Date has been set sucesfully.",
                            Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
            final TimePreference tp = (TimePreference) findPreference("keyname3");
            String blabla3 = tp.getText();
            tp.setSummary((String) blabla3);
            tp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    //your code to change values.
                    tp.setSummary((String) newValue);
                    Toast.makeText(getActivity(), "Time has been set sucesfully.",
                            Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
            final TimePreference tp2 = (TimePreference) findPreference("keyname4");
            String blabla4 = tp2.getText();
            tp2.setSummary((String) blabla4);
            tp2.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    //your code to change values.
                    tp2.setSummary((String) newValue);
                    Toast.makeText(getActivity(), "Time has been set sucesfully.",
                            Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

            //Reminder Preference
           // final ReminderPreference rp = (ReminderPreference) findPreference("enableReminders");
//            findPreference("enableReminders").setOnPreferenceClickListener(preference -> {
//
//                return false;
//            });
            Preference switchPref = (Preference) findPreference("enableReminders");
            switchPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean isOn = (boolean) newValue;
                    if (isOn==true) {
                        //switch is on
                        Toast toast = Toast.makeText(getActivity(), "Reminders are ON.", Toast.LENGTH_SHORT);
                        toast.show();
                        Calendar calendar = Calendar.getInstance();
                        Intent intent = new Intent(getContext(), NotificationReciever.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 100, intent,PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    } else {
                        //switch is off
                        Toast toast2 = Toast.makeText(getActivity(), "Reminders are OFF.", Toast.LENGTH_SHORT);
                        toast2.show();

                    }

                    return true;
                }
            });


        }
    }
}
