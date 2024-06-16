package dev.nimrod.common;

import android.content.Context;
import android.content.SharedPreferences;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DataManager {
    private static final String KEY_FIRST_LAUNCH_DATE = "firstLaunchDate";
    private static final String PREFS_NAME = "ReHubPrefs";
    private static final String KEY_USE_COUNTER = "useCounter";
    private static final String KEY_DAYS_PASSED = "daysPassed";
    private static final String KEY_LAST_UPDATE = "lastUpdate";

    private SharedPreferences sharedPreferences;

    private int useCounter = 0;

    public DataManager() {
    }

    public DataManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        useCounter = sharedPreferences.getInt(KEY_USE_COUNTER, 0);

        if (sharedPreferences.getString(KEY_FIRST_LAUNCH_DATE, "").isEmpty()) {
            setFirstLaunchDate(getCurrentDate());
        }
    }

    public int getUseCounter() {
        return useCounter;
    }

    public void incrementUseCounter() {
        useCounter++;
        saveUseCounter();
    }

    public void resetUseCounter() {
        useCounter = 0;
        saveUseCounter();
    }

    private void saveUseCounter() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_USE_COUNTER, useCounter);
        editor.apply();
    }

    public int getDaysPassed() {
        return sharedPreferences.getInt(KEY_DAYS_PASSED, 0);
    }

    public void setDaysPassed(int daysPassed) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_DAYS_PASSED, daysPassed);
        editor.apply();
    }

    public String getLastUpdate() {
        return sharedPreferences.getString(KEY_LAST_UPDATE, "");
    }

    public void setLastUpdate(String lastUpdate) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_LAST_UPDATE, lastUpdate);
        editor.apply();
    }

    public void updateDaysPassed() {
        String firstLaunchDate = getFirstLaunchDate();
        String today = getCurrentDate();

        int daysPassed = calculateDaysDifference(firstLaunchDate, today);
        setDaysPassed(daysPassed);
    }

    private int calculateDaysDifference(String startDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);
            long difference = end.getTime() - start.getTime();
            return (int) (difference / (1000 * 60 * 60 * 24));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    public double calculateAverageUse() {
        int daysPassed = getDaysPassed();
        return daysPassed > 0 ? (double) useCounter / daysPassed : 0;
    }

    private String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    private String getFirstLaunchDate() {
        return sharedPreferences.getString(KEY_FIRST_LAUNCH_DATE, "");
    }

    private void setFirstLaunchDate(String date) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_FIRST_LAUNCH_DATE, date);
        editor.apply();
    }

}
