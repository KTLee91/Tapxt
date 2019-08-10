package com.springsthursday.tapxt.presenter;

import android.content.Context;
import android.databinding.ObservableField;
import android.view.View;

import com.springsthursday.tapxt.constract.SettingContract;
import com.springsthursday.tapxt.database.DatabaseManager;
import com.springsthursday.tapxt.repository.AppInfoRepository;
import com.springsthursday.tapxt.repository.UserInfo;
public class SettingPresenter {

    private SettingContract.View activity;
    private Context context;
    public ObservableField<String> version;

    public SettingPresenter(SettingContract.View view, Context context) {
        this.activity = view;
        this.context = context;

        version = new ObservableField<String>(AppInfoRepository.getInstance().getVersion());
    }

    public void logout(View vieww)
    {
        DatabaseManager.getInstance(context).updateNullToken(UserInfo.getInstance().userInfoItem.getPhoneNumber());
        activity.openLoginActivity();
    }
}
