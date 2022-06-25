package com.moris.tavda;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class SimpleWebViewClientImpl extends WebViewClient {

    private Activity activity = null;

    public SimpleWebViewClientImpl(Activity activity) {
        this.activity = activity;
    }

    public SimpleWebViewClientImpl() {

    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        // все ссылки, в которых содержится 'javadevblog.com'
        // будут открываться внутри приложения
        //          if (url.contains("javadevblog.com")) {
        if (url.startsWith("http:") || url.startsWith("https:")) {
            return false;
        }
        //          }
        // все остальные ссылки будут спрашивать какой браузер открывать
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            activity.startActivity(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        String errorHtml = "<html><body><h2>Нет соединения с интернетом</h2></body></html>";
        view.loadDataWithBaseURL(null, errorHtml, "text/html", "UTF-8", null);
    }

    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        Log.d("KEYCODE_my", "KOD-"+ event.getKeyCode());
        Log.d("KEYCODE_my", "AKT-"+ event.getAction());
        if ((view.canGoBack()) && (event.getKeyCode() == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_DOWN)) {
            view.goBack();
            return true;
        } else
        if ((view.canGoBack()) && (event.getKeyCode() == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_UP)) {
            return true;
        } else
        if ((view.canGoBack()) && (event.getKeyCode() == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_MULTIPLE)) {
            return true;
        } else return true;// super.shouldOverrideKeyEvent(view, event);
    }
}
