package com.moris.tavda.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.moris.tavda.R;

public class IdeasFragment extends AbstractTabFragment {
    private static final int LAYOUT =R.layout.fragment_idea;


    @Nullable
    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public static IdeasFragment getInstance(Context context) {
        Bundle arg = new Bundle();
        IdeasFragment fragment = new IdeasFragment();
        fragment.setArguments(arg);
        fragment.setContext(context);
        fragment.setTitle(context.getString(R.string.tab_item_ideas));
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT,container,false);
        return view;
    }
}
