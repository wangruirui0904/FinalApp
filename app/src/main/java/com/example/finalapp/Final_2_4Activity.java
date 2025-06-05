package com.example.finalapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Final_2_4Activity extends AppCompatActivity {

    private ListView listView;
    private RecipeAdapter recipeAdapter;
    private List<Recipe> recipeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final24);

        listView = findViewById(R.id.listView);

        recipeAdapter = new RecipeAdapter(this, R.layout.item_recipe, recipeList);
        listView.setAdapter(recipeAdapter);

        extractDataFromWeb();
    }

    private void extractDataFromWeb() {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("https://home.meishichina.com/recipe/jiankangshipu/")
                        .build();
                Response response = client.newCall(request).execute();
                String html = response.body().string();

                // 提取每个<div class="detail">中的内容
                Pattern patternDetail = Pattern.compile("<div class=\"detail\">(.*?)</div>", Pattern.DOTALL);
                Matcher matcherDetail = patternDetail.matcher(html);

                while (matcherDetail.find()) {
                    String detailContent = matcherDetail.group(1);

                    // 在<div class="detail">内容中提取标题
                    Pattern patternTitle = Pattern.compile("title=\"(.*?)\"");
                    Matcher matcherTitle = patternTitle.matcher(detailContent);

                    // 提取原料信息
                    Pattern patternIngredients = Pattern.compile("<p class=\"subcontent\">(.*?)</p>");
                    Matcher matcherIngredients = patternIngredients.matcher(detailContent);

                    if (matcherTitle.find() && matcherIngredients.find()) {
                        String title = matcherTitle.group(1);
                        String ingredients = matcherIngredients.group(1);
                        recipeList.add(new Recipe(title, ingredients));
                    }
                }

                runOnUiThread(() -> {
                    recipeAdapter.notifyDataSetChanged();
                });

            } catch (Exception e) {
                Log.e("Final_2_4Activity", "Error extracting data from web: " + e.getMessage());
            }
        }).start();
    }
}