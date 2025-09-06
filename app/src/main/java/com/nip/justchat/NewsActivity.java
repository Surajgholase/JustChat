package com.nip.justchat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private ArrayList<NewsItem> newsList;
    private ProgressBar progressBar;

    // Free API (no key required) - GNews demo
    private static final String NEWS_URL =
            "https://raw.githubusercontent.com/Surajgholase/raw/refs/heads/main/news.json";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsList = new ArrayList<>();

        adapter = new NewsAdapter(this, newsList, item -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getUrl()));
            startActivity(browserIntent);
        });
        recyclerView.setAdapter(adapter);

        // 🔹 Hidden Long Press Feature
        View rootView = findViewById(android.R.id.content);
        rootView.setOnLongClickListener(v -> {
            Intent intent = new Intent(NewsActivity.this, MainActivity.class); // replace with your chat activity if different
            startActivity(intent);
            Toast.makeText(NewsActivity.this, "Hidden Chat Opened!", Toast.LENGTH_SHORT).show();
            return true; // consume the long press
        });

        loadNews();
    }

    private void loadNews() {
        progressBar.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, NEWS_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            JSONArray articles = response.getJSONArray("articles");
                            for (int i = 0; i < articles.length(); i++) {
                                JSONObject obj = articles.getJSONObject(i);

                                String title = obj.optString("title", "No Title");
                                String description = obj.optString("description", "No Description");
                                String imageUrl = obj.optString("image", ""); // GNews uses "image"
                                String url = obj.optString("url", "");

                                newsList.add(new NewsItem(title, description, imageUrl, url));
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(NewsActivity.this, "Parsing error!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(NewsActivity.this, "Failed to load news", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
    }
}
