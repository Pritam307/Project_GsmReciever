package com.developer.pritampc.mysmsreciever;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MyPagerAdapter extends FragmentStatePagerAdapter {

    private static int INT_NUMS=2;
    private Context context;

    public MyPagerAdapter(FragmentManager fm,Context context) {
        super(fm);
        this.context=context;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                return new ReadSmsFrag();
            case 1:
                return new InsertContactFragment();
            default:
                return new ReadSmsFrag();
        }
    }

    @Override
    public int getCount() {
        return INT_NUMS;
    }
}
