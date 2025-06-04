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

        // 初始化适配器
        recipeAdapter = new RecipeAdapter(this, R.layout.item_recipe, recipeList);
        listView.setAdapter(recipeAdapter);

        // 模拟从网页提取数据
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

                // 提取标题和原料
                Pattern patternTitle = Pattern.compile("title=\"(.*?)\"");
                Pattern patternIngredients = Pattern.compile("<p class=\"subcontent\">(.*?)</p>");

                Matcher matcherTitle = patternTitle.matcher(html);
                Matcher matcherIngredients = patternIngredients.matcher(html);

                while (matcherTitle.find() && matcherIngredients.find()) {
                    String title = matcherTitle.group(1);
                    String ingredients = matcherIngredients.group(1);
                    recipeList.add(new Recipe(title, ingredients));
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