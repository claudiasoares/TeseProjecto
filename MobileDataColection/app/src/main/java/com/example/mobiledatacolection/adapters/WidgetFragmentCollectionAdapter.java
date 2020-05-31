package com.example.mobiledatacolection.adapters;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.mobiledatacolection.fragmentos.WidgetFragment;
import com.example.mobiledatacolection.widget.interfaces.Widget;

public class WidgetFragmentCollectionAdapter extends FragmentStatePagerAdapter {

    public WidgetFragmentCollectionAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        return  new WidgetFragment();
    }

    @Override
    public int getCount() {
        return 0;
    }
}
