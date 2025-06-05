package com.example.finalapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class Final_2_2Activity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> finalList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final22);

        Button btnRemember = findViewById(R.id.Button3_1);
        listView = findViewById(R.id.list_1);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, finalList);
        listView.setAdapter(adapter);

        btnRemember.setOnClickListener(v -> {
            Intent intent = new Intent(Final_2_2Activity.this, Final_2_2_1.class);
            startActivityForResult(intent, 1); // 使用startActivityForResult接收返回数据
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String title = data.getStringExtra("title");
            String content = data.getStringExtra("content");
            String time = data.getStringExtra("time");

            String memoItem = "[" + time + "] " + title + "\n" + content;
            finalList.add(memoItem);
            adapter.notifyDataSetChanged();
        }
    }
}