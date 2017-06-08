package com.solaris.android_third_party.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.solaris.android_third_party.BaseActivity;
import com.solaris.android_third_party.R;
import com.solaris.android_third_party.fragment.NewsContentFragment;

public class NewsContentActivity extends BaseActivity {

    public static void startActivity(Context context, String newsTitle, String newsContent) {
        Intent intent = new Intent(context, NewsContentActivity.class);
        intent.putExtra("news_title", newsTitle);
        intent.putExtra("news_content", newsContent);

        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_content);

        String newsTitle = getIntent().getStringExtra("news_title");
        String newsContent = getIntent().getStringExtra("news_content");

        NewsContentFragment newsContentFragment = (NewsContentFragment) getSupportFragmentManager().findFragmentById(R.id.news_content_fragment);

        newsContentFragment.refresh(newsTitle, newsContent);
    }
}
