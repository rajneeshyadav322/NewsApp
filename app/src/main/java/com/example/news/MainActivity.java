package com.example.news;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements NewsItemClicked {

    NewsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fetchData();
        mAdapter = new NewsAdapter(this);
        recyclerView.setAdapter(mAdapter);
    }
    void fetchData() {
        OkHttpClient httpClient = new OkHttpClient();

        Request request = new Request.Builder()
                            .url("https://newsapi.org/v2/top-headlines?country=in&apiKey=bc0fde421a3545c0bb60d9ef2dff1d1a")
                            .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<News> newsList = new ArrayList<News>();
                            JSONObject jsonObject = null;
                            try {
                                String jsonData = response.body().string();
                                jsonObject = new JSONObject(jsonData);
                                JSONArray articles = jsonObject.getJSONArray("articles");
                                for(int i=0; i<articles.length(); i++) {
                                    JSONObject arrayObject = articles.getJSONObject(i);
                                    String title = arrayObject.getString("title");
                                    String url = arrayObject.getString("url");
                                    String urlToImage = arrayObject.getString("urlToImage");
                                    newsList.add(new News(title, url, urlToImage));
                                }
                                mAdapter.updateNews(newsList);
                            }
                            catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onItemClicked(News item) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(item.url));
    }
}