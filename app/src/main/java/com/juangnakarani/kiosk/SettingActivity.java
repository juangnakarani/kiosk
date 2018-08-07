package com.juangnakarani.kiosk;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
//import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.juangnakarani.kiosk.database.DbHelper;
import com.juangnakarani.kiosk.model.Category;
import com.juangnakarani.kiosk.model.Product;

import java.math.BigDecimal;
import java.util.List;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_setting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
//                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
//                        preference.setSummary(R.string.summary_choose_ringtone);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else if (preference instanceof EditTextPreference) {
                if (preference.getKey().equals("pref_key_server")) {
                    preference.setSummary(stringValue);
                }
                if (preference.getKey().equals("pref_key_port")) {
                    preference.setSummary(stringValue);
                }
                if (preference.getKey().equals("pref_key_kiosk_name")) {
                    preference.setSummary(stringValue);
                }
                if (preference.getKey().equals("pref_key_kiosk_address")) {
                    preference.setSummary(stringValue);
                }
                if (preference.getKey().equals("pref_key_kiosk_phone")) {
                    preference.setSummary(stringValue);
                }
                if (preference.getKey().equals("pref_key_kiosk_slogan")) {
                    preference.setSummary(stringValue);
                }
                if (preference.getKey().equals("pref_key_email")) {
                    preference.setSummary(stringValue);
                }
                if (preference.getKey().equals("pref_key_password")) {
                    preference.setSummary(stringValue);
                }
            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }


    public static class MainPreferenceFragment extends PreferenceFragment {
        private int clickTimes = 5;
        private DbHelper db;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_main);
            db = new DbHelper(getActivity().getApplicationContext());
//             server EditText change listener
            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_key_kiosk_name)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_key_kiosk_address)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_key_kiosk_phone)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_key_kiosk_slogan)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_key_server)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_key_port)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_key_email)));
//            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_key_password)));

            Preference installDefaultData = findPreference("pref_key_default_data");
            installDefaultData.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    String key = preference.getKey();
//                    Log.i("chk", "pref click->" + key);
                    if (clickTimes > 1) {
                        clickTimes = clickTimes - 1;
                        Toast.makeText(getActivity().getApplicationContext(), clickTimes + " x lagi untuk install", Toast.LENGTH_SHORT).show();
                    }
                    if (clickTimes == 1) {
//                        Log.i("chk", "Installing default data...");

                        List<Category> categories = db.getAllCategory();
                        List<Product> products = db.getAllProducts();

                        if (categories.size() == 0 && products.size() == 0) {
                            Toast.makeText(getActivity().getApplicationContext(), "Installing default data...", Toast.LENGTH_SHORT).show();
                            db.installDefaultData();
                            return true;
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "Default data was installed", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }

                    return false;
                }
            });
//
//            // notification preference change listener
//            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_notifications_new_message_ringtone)));
//
//            // feedback preference click listener
//            Preference myPref = findPreference(getString(R.string.key_send_feedback));
//            myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//                public boolean onPreferenceClick(Preference preference) {
//                    sendFeedback(getActivity());
//                    return true;
//                }
//            });
        }

    }

}
