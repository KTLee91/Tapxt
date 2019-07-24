package com.springsthursday.tapxt.repository;

import com.springsthursday.tapxt.item.UserInfoItem;

public class UserInfo {

    public static UserInfo userInfo = null;
    public static UserInfoItem userInfoItem = new UserInfoItem();

    //MovieDatabaseManager 싱글톤 패턴으로 구현
    public static UserInfo getInstance()
    {
        if(userInfo == null)
        {
            userInfo = new UserInfo();
        }

        return userInfo;
    }
}
