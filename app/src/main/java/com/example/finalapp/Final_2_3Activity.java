package com.example.finalapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Final_2_3Activity extends AppCompatActivity {

    private EditText etSportName, etSportNote;
    private TextView tvTimer, tvDateTime;
    private Button btnStart, btnPause, btnStop, btnNew, btnSelectDateTime;
    private ListView lvSportRecords;

    private long startTime = 0L;
    private long timeInMilliseconds = 0L;
    private long timeSwapBuff = 0L;
    private long updatedTime = 0L;
    private boolean isRunning = false;

    private Handler customHandler = new Handler();
    private Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);

            tvTimer.setText(String.format("%02d:%02d:%03d", mins, secs, milliseconds));
            customHandler.postDelayed(this, 0);
        }
    };

    private ArrayList<String> sportRecords = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final23);

        etSportName = findViewById(R.id.et_sport_name);
        etSportNote = findViewById(R.id.et_sport_note);
        tvTimer = findViewById(R.id.tv_timer);
        tvDateTime = findViewById(R.id.tv_date_time);
        btnStart = findViewById(R.id.btn_start);
        btnPause = findViewById(R.id.btn_pause);
        btnStop = findViewById(R.id.btn_stop);
        btnNew = findViewById(R.id.btn_new);
        btnSelectDateTime = findViewById(R.id.btn_select_date_time);
        lvSportRecords = findViewById(R.id.lv_sport_records);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sportRecords);
        lvSportRecords.setAdapter(adapter);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRunning) {
                    startTime = SystemClock.uptimeMillis();
                    customHandler.postDelayed(updateTimerThread, 0);
                    isRunning = true;
                    btnPause.setText("暂停");
                }
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    customHandler.removeCallbacks(updateTimerThread);
                    timeSwapBuff += timeInMilliseconds;
                    isRunning = false;
                    btnPause.setText("继续");
                } else {
                    startTime = SystemClock.uptimeMillis();
                    customHandler.postDelayed(updateTimerThread, 0);
                    isRunning = true;
                    btnPause.setText("暂停");
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customHandler.removeCallbacks(updateTimerThread);
                isRunning = false;

                String sportName = etSportName.getText().toString();
                String note = etSportNote.getText().toString();
                String time = tvTimer.getText().toString();
                String dateTime = tvDateTime.getText().toString();

                String record = "日期和时间：" + dateTime + "\n" +
                        "运动方式：" + sportName + "\n" +
                        "运动时间：" + time + "\n" +
                        "备注：" + note;

                sportRecords.add(record);
                adapter.notifyDataSetChanged();

                etSportName.setText("");
                etSportNote.setText("");
                tvTimer.setText("00:00:00");

                btnPause.setText("暂停");
                timeSwapBuff = 0;
                updatedTime = 0;
            }
        });

        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSportName.setText("");
                etSportNote.setText("");
                tvTimer.setText("00:00:00");
                tvDateTime.setText("日期和时间：");
                timeSwapBuff = 0;
                updatedTime = 0;
                isRunning = false;
                btnPause.setText("暂停");
            }
        });

        btnSelectDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });
    }

    private void showDateTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        selectedHour = calendar.get(Calendar.HOUR_OF_DAY);
        selectedMinute = calendar.get(Calendar.MINUTE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                selectedYear = year;
                selectedMonth = month;
                selectedDay = dayOfMonth;
                showTimePicker();
            }
        }, selectedYear, selectedMonth, selectedDay);

        datePickerDialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                selectedHour = hourOfDay;
                selectedMinute = minute;
                updateDateTime();
            }
        }, selectedHour, selectedMinute, true);

        timePickerDialog.show();
    }

    private void updateDateTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String dateTime = dateFormat.format(calendar.getTime());
        tvDateTime.setText("日期和时间：" + dateTime);
    }
}