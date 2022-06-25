package com.moris.tavda.adapter;

import android.content.Context;

import com.moris.tavda.fragment.AbstractTabFragment;
import com.moris.tavda.fragment.BirthdaysFragment;
import com.moris.tavda.fragment.HistoryFragment;
import com.moris.tavda.fragment.TODOFragment;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabsPagerFragmentAdapter extends FragmentPagerAdapter {
    private Map<Integer,AbstractTabFragment> tabs;
    private Context context;

    public TabsPagerFragmentAdapter(Context context, FragmentManager fm) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;

        InitTabsMap(context);

//        tabs= new String[] {
//                .,
//                .getString(R.string.tab_item_ideas),
//                .getString(R.string.tab_item_todo),
//                .getString(R.string.tab_item_birthdays)
//        };
    }

    @Override
    public Fragment getItem(int position) {
        return tabs.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position).getTitle();
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    private void InitTabsMap(Context context) {
        tabs = new HashMap<>();
        tabs.put(0, HistoryFragment.getInstance(context));
//        tabs.put(1, IdeasFragment.getInstance(context));
        tabs.put(1, TODOFragment.getInstance(context));
        tabs.put(2, BirthdaysFragment.getInstance(context));
//        tabs.put(3, BirthdaysFragment.getInstance(context));
    }
}
