package com.azhariharisalhamdi.githubuserapp.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.azhariharisalhamdi.githubuserapp.R;
import com.azhariharisalhamdi.githubuserapp.notification.AlarmReceiver;

public class SettingsActivity extends AppCompatActivity {

    TextView changeLanguages, setReminder;
    Switch activeAuto;

    public static final String TYPE_ONE_TIME = "OneTimeAlarm";
    public static final String TYPE_REPEATING = "RepeatingAlarm";
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_TYPE = "type";
    private final int ID_ONETIME = 100;
    private final int ID_REPEATING = 101;

    private AlarmReceiver alarmReceiver;
    public String timeAuto = "21:55";

    public String NOTIF_TITLE;
    public String NOTIF_TEXT;

    private String PREFS_NAME = "switch_check";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        NOTIF_TITLE = this.getResources().getString(R.string.notif_content_title);
        NOTIF_TEXT = this.getResources().getString(R.string.notif_content_text);

        setTitle(R.string.search_activity_label);

        changeLanguages = findViewById(R.id.change_languages);
        setReminder = findViewById(R.id.set_reminder);
        activeAuto = findViewById(R.id.activate);

        alarmReceiver = new AlarmReceiver();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean silent = settings.getBoolean("switchkey", false);
        activeAuto.setChecked(silent);

        changeLanguages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(mIntent);
            }
        });
        setReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent change = new Intent(SettingsActivity.this, ReminderSetter.class);
                startActivity(change);
            }
        });
        activeAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    alarmReceiver.setRepeatingAlarm(SettingsActivity.this, AlarmReceiver.TYPE_REPEATING, timeAuto, NOTIF_TEXT);
                }else{
                    alarmReceiver.cancelrepeatingAlarm(SettingsActivity.this, AlarmReceiver.TYPE_REPEATING, NOTIF_TEXT);
                }
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("switchkey", isChecked);
                editor.apply();
            }
        });
    }
}