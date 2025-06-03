package com.example.finalapp;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class Final_2_4Activity extends AppCompatActivity {
    private ListView newsListView;
    private ProgressBar progressBar;
    private TextView tvError;
    private List<HealthNews> newsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final24);

        // 初始化视图
        newsListView = findViewById(R.id.newsListView);
        progressBar = findViewById(R.id.progressBar);
        tvError = findViewById(R.id.tvError);

        // 设置列表点击事件
        newsListView.setOnItemClickListener((parent, view, position, id) -> {
            HealthNews news = newsList.get(position);
            Toast.makeText(this, "正在打开: " + news.getTitle(), Toast.LENGTH_SHORT).show();
            // 这里可以添加打开详情页的代码
        });

        // 开始加载数据
        loadHealthNews();
    }

    private void loadHealthNews() {
        progressBar.setVisibility(View.VISIBLE);
        tvError.setVisibility(View.GONE);

        new FetchNewsTask().execute();
    }

    private class FetchNewsTask extends AsyncTask<Void, Void, List<HealthNews>> {
        private String errorMessage = "";

        @Override
        protected List<HealthNews> doInBackground(Void... voids) {
            List<HealthNews> result = new ArrayList<>();

            try {
                // 从百度健康获取健康资讯
                Document doc = Jsoup.connect("https://jiankang.baidu.com/healthnews")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                        .timeout(10000)
                        .get();

                // 提取新闻列表
                Elements newsItems = doc.select(".news-list li");
                for (Element item : newsItems) {
                    Element titleElement = item.selectFirst(".title");
                    Element dateElement = item.selectFirst(".date");

                    if (titleElement != null && dateElement != null) {
                        String title = titleElement.text();
                        String date = dateElement.text();
                        String url = titleElement.absUrl("href");

                        result.add(new HealthNews(title, date, url));
                    }
                }

                // 如果结果为空，尝试备用选择器
                if (result.isEmpty()) {
                    Elements backupItems = doc.select(".list-item");
                    for (Element item : backupItems) {
                        Element titleElement = item.selectFirst("a");
                        Element dateElement = item.selectFirst(".time");

                        if (titleElement != null && dateElement != null) {
                            String title = titleElement.text();
                            String date = dateElement.text();
                            String url = titleElement.absUrl("href");

                            result.add(new HealthNews(title, date, url));
                        }
                    }
                }

            } catch (Exception e) {
                errorMessage = e.getMessage();
            }

            return result;
        }

        @Override
        protected void onPostExecute(List<HealthNews> result) {
            progressBar.setVisibility(View.GONE);

            if (result.isEmpty()) {
                tvError.setText("获取数据失败: " + (errorMessage.isEmpty() ? "无数据" : errorMessage));
                tvError.setVisibility(View.VISIBLE);
            } else {
                newsList = result;
                NewsAdapter adapter = new NewsAdapter(Final_2_4Activity.this, newsList);
                newsListView.setAdapter(adapter);
            }
        }
    }

    // 内部类 HealthNews
    private static class HealthNews {
        private String title;
        private String date;
        private String url;

        public HealthNews(String title, String date, String url) {
            this.title = title;
            this.date = date;
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public String getDate() {
            return date;
        }

        public String getUrl() {
            return url;
        }
    }

    // 内部类 NewsAdapter
    private static class NewsAdapter extends ArrayAdapter<HealthNews> {
        public NewsAdapter(Context context, List<HealthNews> newsList) {
            super(context, 0, newsList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HealthNews news = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(android.R.layout.simple_list_item_2, parent, false);
            }

            TextView tvTitle = convertView.findViewById(android.R.id.text1);
            TextView tvDate = convertView.findViewById(android.R.id.text2);

            tvTitle.setText(news.getTitle());
            tvDate.setText(news.getDate());

            return convertView;

        }
    }
}
