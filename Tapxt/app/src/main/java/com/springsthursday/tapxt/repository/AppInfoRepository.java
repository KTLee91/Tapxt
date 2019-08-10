package com.springsthursday.tapxt.repository;

import com.springsthursday.tapxt.item.AppInfoItem;

public class AppInfoRepository {

    private AppInfoItem item = new AppInfoItem();
    public static AppInfoRepository appInfoRepository;

    public static AppInfoRepository getInstance()
    {
        if(appInfoRepository == null)
        {
            appInfoRepository = new AppInfoRepository();
        }

        return appInfoRepository;
    }

    public void setVersion(String version)
    {
        this.item.setVersion(version);
    }

    public String getVersion()
    {
        return this.item.getVersion();
    }
}