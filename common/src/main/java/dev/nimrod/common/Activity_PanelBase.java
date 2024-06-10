package dev.nimrod.common;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.Locale;

public class Activity_PanelBase extends AppCompatActivity {

    private MaterialTextView main_MTV_use_counter;
    private MaterialTextView main_MTV_days_passed;
    private MaterialTextView main_MTV_avg_num;
    private MaterialButton main_BTN_add;
    private MaterialButton main_BTN_reset;

    private DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel);

        dataManager = new DataManager(this);

        findViews();
        dataManager.updateDaysPassed();
        initListeners();
        updateViews();
    }

    private void findViews() {
        main_MTV_use_counter = findViewById(R.id.main_MTV_use_counter);
        main_MTV_days_passed = findViewById(R.id.main_MTV_days_passed);
        main_MTV_avg_num = findViewById(R.id.main_MTV_avg_num);
        main_BTN_add = findViewById(R.id.main_BTN_add);
        main_BTN_reset = findViewById(R.id.main_BTN_reset);
    }

    private void initListeners() {
        main_BTN_add.setOnClickListener(v -> {
            dataManager.incrementUseCounter();
            updateViews();
        });
        main_BTN_reset.setOnClickListener(v -> {
            dataManager.resetUseCounter();
            updateViews();
        });
    }

    private void updateViews() {
        double averageUse = dataManager.calculateAverageUse();
        main_MTV_use_counter.setText(String.valueOf(dataManager.getUseCounter()));
        main_MTV_days_passed.setText(String.valueOf(dataManager.getDaysPassed()));
        main_MTV_avg_num.setText(String.format(Locale.getDefault(), "%.2f", averageUse));
    }
}
