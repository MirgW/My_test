package com.moris.tavda;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ActivityWebview extends AppCompatActivity {

    private WebView wbNews;

    private Document document;
    private Elements elements;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        FloatingActionButton floatingActionButton=findViewById(R.id.fab2);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Parse parse = new Parse();
        parse.execute(getIntent().getExtras().getString("INTENT_EXTRA_URL"));
        setWebView();

    }

    class Parse extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                document = Jsoup.connect(params[0].toString()).get();
                elements = document.select(".node.story");
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return document.html();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String unencodedHtml = elements.toString()+"<table align=\"center\" border=\"0\" cellpadding=\"1\" cellspacing=\"1\"><tbody><tr><td>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;</td><td style=\"text-align: center;\"><p>Материалы с официального сайта Тавдинского городского округа</p><p><a href=\"http://www.adm-tavda.ru/\">www.adm-tavda.ru</a></p></td></tr></tbody></table>";
//            String encodedHtml = Base64.encodeToString(unencodedHtml.getBytes(),
//                    Base64.NO_PADDING);
            wbNews.loadDataWithBaseURL("http://www.adm-tavda.ru", unencodedHtml, "text/html; charset=utf-8", "base64","http://www.adm-tavda.ru");
        }
    }


    private void setWebView() {
        wbNews = findViewById(R.id.webview);
        WebSettings wbset = wbNews.getSettings();
        wbset.setJavaScriptEnabled(true);
        wbset.setBuiltInZoomControls(true);
        wbset.setDisplayZoomControls(false);
        wbset.setDefaultTextEncodingName("utf-8");
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
//        newsLink = getIntent().getExtras().getString("INTENT_EXTRA_URL");
//        wbNews.loadUrl(newsLink);
//        String unencodedHtml ="<html><body>"+"</body></html>";
//        String encodedHtml = Base64.encodeToString(unencodedHtml.getBytes(),
//                Base64.NO_PADDING);
//        wbNews.loadData(encodedHtml, "text/html", "base64");

    }
}