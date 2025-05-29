package com.example.finalapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Final_2_1Activity extends AppCompatActivity {
    private EditText heightEditText, weightEditText;
    private TextView resultTextView, adviceTextView;
    private Button calculateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final21);

        heightEditText = findViewById(R.id.EditText1);
        weightEditText = findViewById(R.id.EditText2);
        resultTextView = findViewById(R.id.textView3);
        adviceTextView = findViewById(R.id.textView4);
        calculateButton = findViewById(R.id.button2_1);

        calculateButton.setOnClickListener(v -> calculateBMI(v));
    }

    public void calculateBMI(View view) {
        try {
            Log.i("BMI", "calculateBMI");

            String heightStr = heightEditText.getText().toString().trim();
            String weightStr = weightEditText.getText().toString().trim();

            if (heightStr.isEmpty() || weightStr.isEmpty()) {
                resultTextView.setText("请输入身高体重");
                adviceTextView.setText("");
                return;
            }

            float height = Float.parseFloat(heightStr);
            float weight = Float.parseFloat(weightStr);

            if (height <= 0 || weight <= 0) {
                resultTextView.setText("数值必须大于0");
                adviceTextView.setText("");
                return;
            }

            float bmi = calculateBmiValue(height, weight);
            updateResults(bmi);

        } catch (NumberFormatException e) {
            resultTextView.setText("请输入有效数字");
            adviceTextView.setText("");
        }
    }

    private float calculateBmiValue(float height, float weight) {
        return weight / (height * height);
    }

    private void updateResults(float bmi) {
        String Result = String.format("BMI: %.2f", bmi);
        resultTextView.setText(Result);

        String advice;
        if (bmi < 18) {
            advice = "您的体重过轻，建议增加营养";
        } else if (bmi < 24) {
            advice = "您的体重正常，继续保持";
        } else if (bmi < 28) {
            advice = "您的体重过重，建议控制饮食";
        } else {
            advice = "您的体重肥胖，建议制定减肥计划";
        }
        adviceTextView.setText(advice);
    }
}