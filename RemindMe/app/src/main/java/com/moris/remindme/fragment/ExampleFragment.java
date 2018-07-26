package com.moris.remindme.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moris.remindme.R;

public class ExampleFragment extends Fragment {
    private static final int LAYOUT =R.layout.fragment_example;
    private View view;

    public static ExampleFragment getInstance() {
        Bundle arg = new Bundle();
        ExampleFragment fragment = new ExampleFragment();
        fragment.setArguments(arg);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT,container,false);
        return view;
    }
}
