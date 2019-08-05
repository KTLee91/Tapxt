package com.springsthursday.tapxt.repository;

import com.springsthursday.tapxt.item.AppSettingItem;
import com.springsthursday.tapxt.item.BannerItem;

import java.util.ArrayList;

public class AppSettingIngo {

    private AppSettingItem item = new AppSettingItem();
    public static AppSettingIngo appSettingIngo = new AppSettingIngo();

    public static AppSettingIngo getInstance()
    {
        if(appSettingIngo == null)
        {
            appSettingIngo = new AppSettingIngo();
        }

        return appSettingIngo;
    }

    public int getautoSpeed()
    {
        return item.getAutoLoadContentSpeed();
    }

    public void setAutoSpeed(int autoSpeed)
    {
        item.setAutoLoadContentSpeed(autoSpeed);
    }
}
