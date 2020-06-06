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
        wv.setWebChromeClient(new WebChromeClient()
        {
            });
        wv.loadUrl("http://itavda.ru/avito");
        return view;
    }
}
