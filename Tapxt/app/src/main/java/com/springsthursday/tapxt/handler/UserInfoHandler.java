package com.springsthursday.tapxt.handler;

import com.springsthursday.tapxt.item.UserInfoItem;
import com.springsthursday.tapxt.repository.UserInfo;

public class UserInfoHandler {

    private static UserInfoHandler userInfoHandler = null;
    private static UserInfo userInfo = null;

    public static UserInfoHandler getInstance()
    {
        if(userInfoHandler == null)
        {
            userInfoHandler = new UserInfoHandler();
        }

        return userInfoHandler;
    }

    private UserInfoHandler()
    {
        userInfo = UserInfo.getInstance();
    }

    public void setUserInfo(UserInfoItem userInfo)
    {
        this.userInfo.userInfoItem = userInfo;
    }

    public UserInfoItem getUserInfo()
    {
        return this.userInfo.userInfoItem;
    }
}