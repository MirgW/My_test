package com.moris.tavda;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ActivityWebview extends AppCompatActivity {

    private WebView wbNews;
    private String newsLink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        setWebView();

    }

    private void setWebView() {
        wbNews = (WebView) findViewById(R.id.webview);
        WebSettings wbset = wbNews.getSettings();
        wbset.setJavaScriptEnabled(true);
        wbNews.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                wbNews.setVisibility(view.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                wbNews.setVisibility(view.VISIBLE);
            }
        });
        newsLink = getIntent().getExtras().getString("INTENT_EXTRA_URL");
        wbNews.loadUrl(newsLink);

    }
}