package com.azhariharisalhamdi.consumerapp.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.azhariharisalhamdi.consumerapp.R;
import com.azhariharisalhamdi.consumerapp.notification.AlarmReceiver;

//import java.sql.Time;

public class SettingsActivity extends AppCompatActivity {

    TextView changeLanguages, reminder_auto, setReminder;
    Switch activeAuto;
    public String TAG = "SettingsActivity";
    public static final String TYPE_ONE_TIME = "OneTimeAlarm";
    public static final String TYPE_REPEATING = "RepeatingAlarm";
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_TYPE = "type";
    private final int ID_ONETIME = 100;
    private final int ID_REPEATING = 101;

    private AlarmReceiver alarmReceiver;
    public String timeAuto = "21:55";
    public int hour, hour_meridiem, minute;
    public String meridiem;

    public String NOTIF_TITLE;
    public String NOTIF_TEXT;

    private String PREFS_NAME = "switch_check";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setTitle(R.string.search_activity_label);

        NOTIF_TITLE = this.getResources().getString(R.string.notif_content_title);
        NOTIF_TEXT = this.getResources().getString(R.string.notif_content_text);
        hour = getResources().getInteger(R.integer.time_hour);
        minute = getResources().getInteger(R.integer.time_minute);

        Log.d(TAG, "hour : "+hour);
        Log.d(TAG, "minute : "+minute);

        changeLanguages = findViewById(R.id.change_languages);
        reminder_auto = findViewById(R.id.reminder_auto);
        setReminder = findViewById(R.id.set_reminder);
        activeAuto = findViewById(R.id.activate);

//        Resources res = getResources();
        if(hour > 12) {
            hour_meridiem = hour - 12;
            meridiem = "PM";
        }else if(hour == 12){
            hour_meridiem = hour;
            meridiem = "PM";
        }else{
            hour_meridiem = hour;
            meridiem = "AM";
        }

        Time time = new Time();
        time.set(0, minute, hour_meridiem, 0, 0, 0);
        Log.i(TAG, time.format("%H.%M"));
        ;

        String set_reminder_auto = getResources().getString(R.string.auto_reminder, time.format("%H.%M"), meridiem);
        reminder_auto.setText(set_reminder_auto);

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

        time.set(0, minute, hour, 0, 0, 0);
        Log.i(TAG, time.format("%H:%M"));

        activeAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    alarmReceiver.setRepeatingAlarm(SettingsActivity.this, AlarmReceiver.TYPE_REPEATING, time.format("%H:%M"), NOTIF_TEXT);
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