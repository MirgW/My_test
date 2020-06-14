package com.moris.tavda.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.moris.tavda.R;
import com.moris.tavda.SimpleWebViewClientImpl;

public class TODOFragment extends AbstractTabFragment {
    private static final int LAYOUT =R.layout.fragment_example;

    private WebView wv;

    @Nullable
    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public static TODOFragment getInstance(Context context) {
        Bundle arg = new Bundle();
        TODOFragment fragment = new TODOFragment();
        fragment.setArguments(arg);
        fragment.setContext(context);
        fragment.setTitle(context.getString(R.string.tab_item_todo));
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        wv = (WebView) view.findViewById(R.id.webviewmag);
//        WebSettings webSettings = webView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
        wv.getSettings().setLoadsImagesAutomatically(true);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wv.setWebViewClient(new SimpleWebViewClientImpl());
        wv.setWebChromeClient(new WebChromeClient() {
        });
//        wv.loadUrl("http://itavda.ru/avito");
//        wv.loadUrl("https://www.google.com/maps/d/u/0/embed?mid=1pPXktBkbZSVVOlWG-8fH6TBwAuQ&hl=ru&ie=UTF8&oe=UTF8&s=AARTsJq_Xu7m1aPK37sIE0VixF7tLZmLkg&msa=0&ll=56.245633484352624%2C53.07749833670046&spn=142.64145%2C281.25&z=9&output=embed");
//        wv.loadUrl("https://www.google.com/maps/d/drive?state=%7B%22ids%22%3A%5B%221pDPIPzqaAEsqcdPcxdOcV99hK9eLZV-N%22%5D%2C%22action%22%3A%22open%22%2C%22userId%22%3A%22118067589166761913867%22%7D&usp=sharing");
        wv.loadUrl("https://www.google.com/maps/place/%D0%90%D0%B4%D0%BC%D0%B8%D0%BD%D0%B8%D1%81%D1%82%D1%80%D0%B0%D1%86%D0%B8%D1%8F+%D0%A2%D0%B0%D0%B2%D0%B4%D0%B8%D0%BD%D1%81%D0%BA%D0%BE%D0%B3%D0%BE+%D0%93%D0%BE%D1%80%D0%BE%D0%B4%D1%81%D0%BA%D0%BE%D0%B3%D0%BE+%D0%9E%D0%BA%D1%80%D1%83%D0%B3%D0%B0,+%D0%93%D0%BE%D1%80%D0%BE%D0%B4%D1%81%D0%BA%D0%BE%D0%B9+%D0%9E%D1%82%D0%B4%D0%B5%D0%BB+%D0%A1%D1%82%D0%B0%D1%82%D0%B8%D1%81%D1%82%D0%B8%D0%BA%D0%B8,+%D1%83%D0%BB.+%D0%9A%D0%B8%D1%80%D0%BE%D0%B2%D0%B0,+%D0%B4.+118,+%D0%A2%D0%B0%D0%B2%D0%B4%D0%B0,+%D0%A1%D0%B2%D0%B5%D1%80%D0%B4%D0%BB%D0%BE%D0%B2%D1%81%D0%BA%D0%B0%D1%8F+%D0%BE%D0%B1%D0%BB.,+623950/@58.0427569,65.2747756,16z/data=!4m2!3m1!1s0x43bc49ebb2799611:0xb8d02a0917ba628b");
        return view;
    }
}
