package com.springsthursday.tapxt.BindingAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.springsthursday.tapxt.Code.Code;
import com.springsthursday.tapxt.view.StoryListFragment;

public class StoryListPagerAdapter extends FragmentStatePagerAdapter {

    private int tabCount;

    public StoryListPagerAdapter(FragmentManager fragmentManager, int tabCount)
    {
        super(fragmentManager);
        this.tabCount = tabCount;

    }

    @Override
    public Fragment getItem(int position) {
        StoryListFragment fragment;
        Bundle bundle;
        switch (position)
        {
            case 0:
                fragment = new StoryListFragment();
                bundle = new Bundle();
                bundle.putInt("StoryListType", Code.StoryListCategory.Viewed_STROYS);
                fragment.setArguments(bundle);
                return fragment;
            case 1:
                fragment = new StoryListFragment();
                bundle = new Bundle();
                bundle.putInt("StoryListType", Code.StoryListCategory.LIKED_STORYS);
                fragment.setArguments(bundle);
                return fragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
