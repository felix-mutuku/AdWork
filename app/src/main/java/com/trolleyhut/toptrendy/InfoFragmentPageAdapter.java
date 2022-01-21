package com.trolleyhut.toptrendy;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class InfoFragmentPageAdapter extends FragmentPagerAdapter {
//    public InfoFragmentPagerAdapter(FragmentManager fm) {
//        super(fm);
//    }

    public InfoFragmentPageAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new InfoOneFragment();
        } else if (position == 1) {
            return new InfoTwoFragment();
        } else if (position == 2) {
            return new InfoThreeFragment();
        } else {
            return new InfoFourFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
