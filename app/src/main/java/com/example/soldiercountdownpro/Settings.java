package com.example.soldiercountdownpro;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;


public class Settings extends PreferenceActivity {
    private  TextView mDisplayDate;
    private static DatePickerDialog.OnDateSetListener mDateSetListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
        mDisplayDate = (TextView) findViewById(R.id.tvDate);

    }

    public static class MyPreferenceFragment extends PreferenceFragment  {


        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);


            findPreference("homecoming_date").setOnPreferenceClickListener(preference -> {
               Toast.makeText(getActivity(), "This is a fucking test",Toast.LENGTH_SHORT).show();
                return false;
            });

        }
    }
}
