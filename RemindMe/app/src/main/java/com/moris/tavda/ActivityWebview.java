package com.moris.tavda;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public class ActivityWebview extends AppCompatActivity {

    private ObservableWebView wbNews;
    //    private FloatingActionButton floatingActionButton;
    String param_str;
    private FrameLayout mFullScreenContaine;
    private View mFullScreenView;

    private Elements elements;
    private FloatingActionButton floatingActionButton_share;
    private WebChromeClient.CustomViewCallback mFullscreenViewCallback;
    SwipeRefreshLayout swipeContainer;
    Parse parse;
//    private ProgressDialog mProgressDialog;
//
//    public void showProgressDialog() {
//        if (mProgressDialog == null) {
//            mProgressDialog = new ProgressDialog(this);
//            mProgressDialog.setCancelable(false);
//            mProgressDialog.setMessage("Loading...");
//        }
//
//        mProgressDialog.show();
//    }
//
//    public void hideProgressDialog() {
//        if (mProgressDialog != null && mProgressDialog.isShowing()) {
//            mProgressDialog.dismiss();
//        }
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ST);
        setContentView(R.layout.activity_web);
//        floatingActionButton = findViewById(R.id.fab2);
        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                parse = new Parse();
                Bundle extr = getIntent().getExtras();
                if (extr != null) param_str = extr.getString("INTENT_EXTRA_URL");
//        showProgressDialog();
//                final LinearLayout lyt_progress = (LinearLayout) findViewById(R.id.lyt_progress);
//                lyt_progress.setVisibility(View.VISIBLE);
//                lyt_progress.setAlpha(1.0f);
                parse.execute(param_str);
                setWebView();

            }
        });

//        wv = (ObservableWebView) findViewById(R.id.scorllableWebview);
//        wv.setOnScrollChangedCallback(new OnScrollChangedCallback(){
//            public void onScroll(int l, int t, int oldl, int oldt){
//                if(t> oldt){
//                    //Do stuff
//                    System.out.println("Swipe UP");
//                    //Do stuff
//                }
//                else if(t< oldt){
//                    System.out.println("Swipe Down");
//                }
//                Log.d(TAG,"We Scrolled etc...");
//            }
//        });
//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });
        floatingActionButton_share = findViewById(R.id.fab_share);

        parse = new Parse();
        Bundle extr = getIntent().getExtras();
        if (extr != null) param_str = extr.getString("INTENT_EXTRA_URL");
//        showProgressDialog();
        final LinearLayout lyt_progress = (LinearLayout) findViewById(R.id.lyt_progress);
        lyt_progress.setVisibility(View.VISIBLE);
        lyt_progress.setAlpha(1.0f);
        parse.execute(param_str);
        setWebView();

    }

    class Parse extends AsyncTask<String, Void, String> {
        // TODO: 8/14/2018 Прогрес бар
        @Override
        protected String doInBackground(String... params) {
            Document document;
            try {
                document = Jsoup.connect(params[0]).get();
                elements = document.select(".node.story");
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
            return document.html();
        }

        private Uri imageUri;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (elements != null) {
                String unencodedHtml = "<html lang=\"ru\"><head></head><body><style>\n" +
                        "img {\n" +
                        "    max-width: 100%;\n" +
                        "    width:auto;\n" +
                        "    height: auto;\n" +
                        "}\n" + " iframe {\n" +
                        "    position: absolute;\n" +
                        "    top: 0;\n" +
                        "    left: 0;\n" +
                        "    width: 100%;\n" +
                        "}" +
                        "</style>\n<div>" + elements.toString().replace("<span class=\"art-postauthoricon\">admin</span>","<p><a href=\""+param_str+"\"><strong>Источник новости: </strong>www.adm-tavda.ru</a></p>") + "</div>" + "<table align=\"center\" border=\"0\" cellpadding=\"1\" cellspacing=\"1\"><tbody><tr><td>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;</td><td style=\"text-align: center;\"><p>Материалы с официального сайта Тавдинского городского округа</p><p><a href=\"http://www.adm-tavda.ru/\">www.adm-tavda.ru</a></p></td></tr></tbody></table></body><html>";
//                        "</style>\n<div>" + elements.toString().replace("<span class=\"art-postauthoricon\">admin</span>","<p><a href=\""+param_str+"\"><strong>Источник новости:</strong></a></p><p><a href=\""+param_str+"\">сайт Тавдинского городского округа</a></p><p><a href=\""+param_str+"\">www.adm-tavda.ru</a></p>") + "</div>" + "<table align=\"center\" border=\"0\" cellpadding=\"1\" cellspacing=\"1\"><tbody><tr><td>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;</td><td style=\"text-align: center;\"><p>Материалы с официального сайта Тавдинского городского округа</p><p><a href=\"http://www.adm-tavda.ru/\">www.adm-tavda.ru</a></p></td></tr></tbody></table></body><html>";
//            String encodedHtml = Base64.encodeToString(unencodedHtml.getBytes(),
//                    Base64.NO_PADDING);
                wbNews.loadDataWithBaseURL("http://www.adm-tavda.ru", unencodedHtml, "text/html; charset=utf-8", "base64", "http://www.adm-tavda.ru");
                floatingActionButton_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int sharableImage = R.drawable.tavda1024;
                        String s = elements.select("img").attr("src");
                        if (s != "") {
                            Picasso.get().load("http://www.adm-tavda.ru/" + elements.select("img").attr("src").replaceAll("http://www.adm-tavda.ru/", "").replaceAll("http://adm-tavda.ru/", "")).into(new Target() {
                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {

                                }

                                @Override
                                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                                }

                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                        imageUri =  Uri.parse("http://www.adm-tavda.ru"+elements.select("img").attr("src"));
                                    Intent sendIntent = new Intent();
//                                sendIntent.setType("image/*");
                                    sendIntent.setType("text/plain");
                                    sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    sendIntent.setAction(Intent.ACTION_SEND);
                                    sendIntent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                                    sendIntent.putExtra(Intent.EXTRA_TEXT,  param_str);
//                                    sendIntent.putExtra(Intent.EXTRA_TEXT, param_str + "\n\n Новости города Тавда в мобильном приложении https://play.google.com/store/apps/details?id=com.moris.tavda.free \n");
                                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, elements.select("h2").text());
                                    sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//                    sendIntent.putExtra(Intent.EXTRA_EMAIL  , new String[] { "ssss@where.com" });
//                    sendIntent.putExtra(Intent.EXTRA_CONTENT_ANNOTATIONS, "EXTRA_CONTENT_ANNOTATIONS");
//                    sendIntent.putExtra(Intent.EXTRA_SPLIT_NAME,"dddddd");
                                    sendIntent.putExtra(Intent.EXTRA_HTML_TEXT, "<html><body><h1>Отправлено из мобильного приложения Тавда.</h1><a href='https://play.google.com/store/apps/details?id=com.moris.tavda.free&pcampaignid=pcampaignidMKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png'/></a></html></body>");
                                    startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.app_name)));
                                }
                            });
                        } else {
                            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), sharableImage);
                            String path = getExternalCacheDir() + "/tavda.jpg";
                            java.io.OutputStream out;
                            java.io.File file = new java.io.File(path);
                            if (!file.exists()) {
                                try {
                                    out = new java.io.FileOutputStream(file);
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                                    out.flush();
                                    out.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            path = file.getPath();
//                            imageUri = Uri.parse("file://" + path);
                            imageUri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", new File(path));
                            Intent sendIntent = new Intent();
                            sendIntent.setType("text/plain");
                            sendIntent.setAction(android.content.Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, elements.select("h2").text() + "\n\n" + param_str + "\n\n Новости города Тавда в мобильном приложении https://play.google.com/store/apps/details?id=com.moris.tavda.free \n");
//                            sendIntent.putExtra(Intent.EXTRA_TEXT, "Новости города Тавда в мобильном приложении https://play.google.com/store/apps/details?id=com.moris.tavda.free \n");
                            sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Тавда");
                            sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                            startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.app_name)));
                        }
                    }
                });
            } else {
                wbNews.loadDataWithBaseURL("http://www.adm-tavda.ru", "        <div>\n" +
                        "          <p></p>\n" +
                        "          <ul>\n" +
                        "            <li>Источник не доступен, Проверьте подключение к Wi-Fi или сотовой сети</li>\n" +
                        "          </ul>\n" +
                        "        </div>", "text/html; charset=utf-8", "base64", "http://www.adm-tavda.ru");
            }
//            hideProgressDialog();
//            final LinearLayout lyt_progress = (LinearLayout) findViewById(R.id.lyt_progress);
            swipeContainer.setRefreshing(false);

        }
    }

    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;

        String path = getExternalCacheDir() + "/tavda"+ System.currentTimeMillis()+".jpg";
        java.io.OutputStream out;
        java.io.File file = new java.io.File(path);
        if (!file.exists()) {
            try {
                out = new java.io.FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        path = file.getPath();
        bmpUri = FileProvider.getUriForFile(ExampleApplication.getContext(), BuildConfig.APPLICATION_ID + ".provider", new File(path));
/*        try {
//            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "tavda_" + System.currentTimeMillis() + ".png");
            File file = new File(getExternalCacheDir(), "tavda_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
//            bmpUri = Uri.fromFile(file);
            bmpUri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", file);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return bmpUri;
    }

    @Override
    public void onBackPressed() {
        if (wbNews.canGoBack()) {
            wbNews.goBack();
        } else super.onBackPressed();
        ;
    }

    private void setWebView() {
        wbNews = findViewById(R.id.webview);

        wbNews.setOnScrollChangedCallback((dx, dy) -> {
            if (dy > 0) {
                floatingActionButton_share.hide();
//                floatingActionButton.hide();
            } else {
                floatingActionButton_share.show();
//                floatingActionButton.show();
            }
        });

        WebSettings wbset = wbNews.getSettings();
        wbset.setJavaScriptEnabled(true);
        // FIXME: 8/12/2018 вырести в настройки
//        wbset.setBuiltInZoomControls(true);
//        wbset.setDisplayZoomControls(false);
        wbset.setDefaultTextEncodingName("utf-8");
//        wbNews.setWebViewClient(new SimpleWebViewClientImpl() {
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
                final LinearLayout lyt_progress = (LinearLayout) findViewById(R.id.lyt_progress);
//                swipeContainer.setRefreshing(false);
                lyt_progress.setVisibility(View.INVISIBLE);
                lyt_progress.setAlpha(1.0f);
            }
        });
        mFullScreenContaine = (FrameLayout) findViewById(R.id.fullscreen_container);
        wbNews.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
//                super.onShowCustomView(view, callback);
                mFullscreenViewCallback = callback;
                mFullScreenContaine.addView(view);
                mFullScreenView = view;
                floatingActionButton_share.hide();
                swipeContainer.setVisibility(View.GONE);
                mFullScreenContaine.setVisibility(View.VISIBLE);
                mFullScreenContaine.bringToFront();
                mFullScreenContaine.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LOW_PROFILE
                                | View.SYSTEM_UI_FLAG_IMMERSIVE);
            }

            @Override
            public void onHideCustomView() {
//                super.onHideCustomView();
                if (mFullScreenView == null) return;
                floatingActionButton_share.show();
                mFullScreenView.setVisibility(View.GONE);
                mFullScreenContaine.removeView(mFullScreenView);
                mFullScreenView = null;
                mFullscreenViewCallback.onCustomViewHidden();
                swipeContainer.setVisibility(View.VISIBLE);
                mFullScreenContaine.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        & ~View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        & ~View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        & ~View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        & ~View.SYSTEM_UI_FLAG_FULLSCREEN
                        & ~View.SYSTEM_UI_FLAG_LOW_PROFILE
                        & ~View.SYSTEM_UI_FLAG_IMMERSIVE);
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