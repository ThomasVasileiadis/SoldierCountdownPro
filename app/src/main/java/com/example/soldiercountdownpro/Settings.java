package com.example.soldiercountdownpro;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
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
            final DatePreference dp= (DatePreference) findPreference("keyname");
            //dp.setText("2020-01-02");
            //dp.setSummary("2020-08-02");
            String blabla = dp.getText();
            dp.setSummary((String) blabla);
            dp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference,Object newValue) {
                    //your code to change values.
                    dp.setSummary((String) newValue);
                    return true;
                }
            });



//            findPreference("homecoming_date").setOnPreferenceClickListener(preference -> {
//               Toast.makeText(getActivity(), "This is a fucking test",Toast.LENGTH_SHORT).show();
//                return false;
//            });

        }
    }
}
