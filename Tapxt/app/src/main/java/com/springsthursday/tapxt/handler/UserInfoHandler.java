package com.springsthursday.tapxt.handler;

import com.apollographql.apollo.api.Response;
import com.springsthursday.tapxt.GetUserProfileQuery;
import com.springsthursday.tapxt.item.FollowItem;
import com.springsthursday.tapxt.item.UserInfoItem;
import com.springsthursday.tapxt.repository.UserInfo;

import java.util.ArrayList;

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

    public void setNumberNToken(String phoneNumber, String token)
    {
        userInfo.userInfoItem.setPhoneNumber(phoneNumber);
        userInfo.userInfoItem.setToken(token);
    }

    public void initilizeUserInfo(Response<GetUserProfileQuery.Data> response)
    {
        userInfo.userInfoItem.setNickName(response.data().me().nickname());
        userInfo.userInfoItem.setImageUrl(response.data().me().avatar());

        ArrayList<String> tags = new ArrayList<>();
        ArrayList<FollowItem> follows = new ArrayList<>();

        userInfo.userInfoItem.setNickName(response.data().me().nickname());
        userInfo.userInfoItem.setImageUrl(response.data().me().avatar());
        userInfo.userInfoItem.setClapsCount(response.data().me().clapsCount());
        userInfo.userInfoItem.setFollowingCount(response.data().me().followingCount());
        userInfo.userInfoItem.setLikesCount(response.data().me().likesCount());

        for(int j= 0; j<response.data().me().tags().size(); j++)
        {
            GetUserProfileQuery.Tag tag = response.data().me().tags().get(j);
            tags.add(tag.text());
        }

        userInfo.userInfoItem.setTags(tags);

        for(int k=0; k<response.data().me().follows().size(); k++)
        {
            GetUserProfileQuery.Follow follow = response.data().me().follows().get(k);
            FollowItem item = new FollowItem();

            item.setAvatar(follow.creator().avatar());
            item.setNickName(follow.creator().nickname());

            follows.add(item);
        }

        userInfo.userInfoItem.setFollows(follows);
    }
}