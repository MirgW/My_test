package com.moris.tavda.fragment;

import android.content.Context;
import android.view.View;

import androidx.fragment.app.Fragment;

public class AbstractTabFragment extends Fragment {
    private String title;
    protected Context context;
    protected View view;

    public void setTitle(String title) {
        this.title = ""; // без надписей
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.view = null;
    }

    public String getTitle() {

        return title;
    }
}
