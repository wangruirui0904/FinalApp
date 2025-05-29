package com.example.finalapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Final_2_3Activity extends AppCompatActivity {

    private EditText etSportName, etSportNote;
    private TextView tvTimer, tvSportName, tvTime, tvNote, tvCalories;
    private Button btnPause, btnStop, btnNew;
    private LinearLayout llResult;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final23);

        etSportName = findViewById(R.id.et_sport_name);
        etSportNote = findViewById(R.id.et_sport_note);
        tvTimer = findViewById(R.id.tv_timer);
        tvSportName = findViewById(R.id.tv_sport_name);
        tvTime = findViewById(R.id.tv_time);
        tvNote = findViewById(R.id.tv_note);
        tvCalories = findViewById(R.id.tv_calories);
        btnPause = findViewById(R.id.btn_pause);
        btnStop = findViewById(R.id.btn_stop);
        btnNew = findViewById(R.id.btn_new);
        llResult = findViewById(R.id.ll_result);

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
                int calories = calculateCalories(updatedTime); // 假设每分钟消耗10卡路里

                tvSportName.setText("运动方式：" + sportName);
                tvTime.setText("运动时间：" + time);
                tvNote.setText("备注：" + note);
                tvCalories.setText("消耗卡路里：" + calories);

                llResult.setVisibility(View.VISIBLE);

                // 保存运动记录到数据库或文件
                saveSportRecord(sportName, time, note, calories);

                btnPause.setText("暂停");
                timeSwapBuff = 0;
            }
        });

        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSportName.setText("");
                etSportNote.setText("");
                tvTimer.setText("00:00:00");
                llResult.setVisibility(View.GONE);
                timeSwapBuff = 0;
                updatedTime = 0;
                isRunning = false;
                btnPause.setText("暂停");
            }
        });
    }

    private int calculateCalories(long timeInMillis) {
        // 假设每分钟消耗10卡路里
        return (int) (timeInMillis / 60000) * 10;
    }

    private void saveSportRecord(String sportName, String time, String note, int calories) {
        // 这里可以将运动记录保存到数据库或文件中
        // 示例：保存到文件
        String record = sportName + "," + time + "," + note + "," + calories + "\n";
        // 将记录写入文件
        // File file = new File(getExternalFilesDir(null), "sport_records.txt");
        // try (FileOutputStream fos = new FileOutputStream(file, true)) {
        //     fos.write(record.getBytes());
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
    }
}