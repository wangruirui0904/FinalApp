package com.example.finalapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Final_2_3Activity extends AppCompatActivity {

    private EditText etSportName, etSportNote;
    private TextView tvTimer;
    private Button btnStart, btnPause, btnStop, btnNew;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final23);

        etSportName = findViewById(R.id.et_sport_name);
        etSportNote = findViewById(R.id.et_sport_note);
        tvTimer = findViewById(R.id.tv_timer);
        btnStart = findViewById(R.id.btn_start);
        btnPause = findViewById(R.id.btn_pause);
        btnStop = findViewById(R.id.btn_stop);
        btnNew = findViewById(R.id.btn_new);
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

                String record = "运动方式：" + sportName + "\n" +
                        "运动时间：" + time + "\n" +
                        "备注：" + note;

                sportRecords.add(record);
                adapter.notifyDataSetChanged();

                // 清空输入框和计时器
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
                // 清空输入框和计时器
                etSportName.setText("");
                etSportNote.setText("");
                tvTimer.setText("00:00:00");
                timeSwapBuff = 0;
                updatedTime = 0;
                isRunning = false;
                btnPause.setText("暂停");
            }
        });
    }
}