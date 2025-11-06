package com.moris.tavda.fragment;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class AbstractTabFragment extends Fragment {
    private String title;
    protected Context context;
    protected View view;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

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
