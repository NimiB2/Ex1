package dev.nimrod.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Activity_PanelBase extends AppCompatActivity {

    private static final String PREFS_NAME = "ReHubPrefs";
    private static final String KEY_USE_COUNTER = "useCounter";
    private static final String KEY_DAYS_PASSED = "daysPassed";
    private static final String KEY_LAST_UPDATE = "lastUpdate";
    private MaterialTextView main_MTV_use_counter;
    private MaterialTextView main_MTV_days_passed;
    private MaterialTextView main_MTV_avg_num;
    private MaterialButton main_BTN_add;
    private MaterialButton main_BTN_reset;
    private int useCounter = 0;
    private int daysPassed = 0;
    private boolean flag=false;
    private double averageUse = 0.0;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel);

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        findViews();
        loadPreferences();
        checkAndUpdateDaysPassed();
        initViews();
    }

    private void initViews() {
        main_BTN_add.setOnClickListener(v -> incrementUseCounter(flag=true));

        main_BTN_reset.setOnClickListener(v -> incrementUseCounter(flag=false));
        updateViews();
    }

    private void findViews() {
        main_MTV_use_counter = findViewById(R.id.main_MTV_use_counter);
        main_MTV_days_passed = findViewById(R.id.main_MTV_days_passed);
        main_MTV_avg_num = findViewById(R.id.main_MTV_avg_num);
        main_BTN_add = findViewById(R.id.main_BTN_add);
        main_BTN_reset=findViewById(R.id.main_BTN_reset);
    }

    private void incrementUseCounter(boolean flag) {
        if(flag){
            useCounter++;
        }else{
            useCounter=0;
        }
        updateAverageUse();
        updateViews();
        savePreferences();
    }

    private void checkAndUpdateDaysPassed() {
        String lastUpdate = sharedPreferences.getString(KEY_LAST_UPDATE, "");
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        if (!today.equals(lastUpdate)) {
            daysPassed++;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_LAST_UPDATE, today);
            editor.apply();
        }

        updateAverageUse();
        savePreferences();
    }

    private void updateAverageUse() {
        double averageUse = daysPassed > 0 ? (double) useCounter / daysPassed : 0;
        main_MTV_avg_num.setText(String.format(Locale.getDefault(), "%.2f", averageUse));
    }

    private void updateViews() {
        main_MTV_use_counter.setText(String.valueOf(useCounter));
        main_MTV_days_passed.setText(String.valueOf(daysPassed));
    }

    private void savePreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_USE_COUNTER, useCounter);
        editor.putInt(KEY_DAYS_PASSED, daysPassed);
        editor.apply();
    }

    private void loadPreferences() {
        useCounter = sharedPreferences.getInt(KEY_USE_COUNTER, 0);
        daysPassed = sharedPreferences.getInt(KEY_DAYS_PASSED, 0);
    }

}