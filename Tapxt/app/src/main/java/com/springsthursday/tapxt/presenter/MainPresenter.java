package com.springsthursday.tapxt.presenter;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.view.MenuItem;

import com.springsthursday.tapxt.Code.Code;
import com.springsthursday.tapxt.R;
import com.springsthursday.tapxt.constract.MainContract;

public class MainPresenter{

    private MainContract.View activity;
    public ObservableField<String> appbarTitle = new ObservableField<>();

    public MainPresenter(MainContract.View view){
        appbarTitle.set("");
        this.activity = view;
    }

    public boolean onNavigationClick(@NonNull MenuItem item)
    {
        String fragmentName = "";

        switch(item.getItemId())
        {
            case R.id.home:
                fragmentName = Code.FragmentName.FGAGMENT_HOME;
                appbarTitle.set("");
                break;
            case R.id.category:
                fragmentName = Code.FragmentName.FRAGMENT_CATEGORY;
                appbarTitle.set("카테고리");
                break;
            case R.id.profile:
                fragmentName = Code.FragmentName.FRAGMENT_PROFILE;
                appbarTitle.set("사용자 정보");
                break;
        }

        activity.changeFragment(fragmentName);

        return true;
    }
}
