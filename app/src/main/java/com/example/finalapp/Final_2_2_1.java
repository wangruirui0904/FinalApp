package com.example.finalapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Final_2_2_1 extends AppCompatActivity {

    private EditText etTitle, etContent, etTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final221);
        etTitle = findViewById(R.id.EditText3);
        etContent = findViewById(R.id.EditText4);
        etTime = findViewById(R.id.EditText5);
        Button btnSave = findViewById(R.id.btn_save);
        Button btnCancel = findViewById(R.id.btn_cancel);

        btnSave.setOnClickListener(v -> saveMemo());

        btnCancel.setOnClickListener(v -> finish());
    }

    private void saveMemo() {
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();
        String time = etTime.getText().toString().trim();

        if (title.isEmpty()) {
            etTitle.setError("请输入标题");
            return;
        }

        if (time.isEmpty()) {
            etTime.setError("请输入时间");
            return;
        }


        Intent resultIntent = new Intent();
        resultIntent.putExtra("title", title);
        resultIntent.putExtra("content", content);
        resultIntent.putExtra("time", time);
        setResult(RESULT_OK, resultIntent);
        finish();

    }
}






