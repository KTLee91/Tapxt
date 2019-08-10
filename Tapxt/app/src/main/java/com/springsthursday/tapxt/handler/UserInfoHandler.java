package com.springsthursday.tapxt.handler;

import com.apollographql.apollo.api.Response;
import com.springsthursday.tapxt.GetUserProfileQuery;
import com.springsthursday.tapxt.item.FollowItem;
import com.springsthursday.tapxt.item.StoryItem;
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
        ArrayList<StoryItem> likeItems = new ArrayList<>();
        ArrayList<StoryItem> viewedItems = new ArrayList();

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

        for(int t=0; t<response.data().me().likes().size(); t++)
        {
            boolean isExistStory = false;

            GetUserProfileQuery.Like likeStory = response.data().me().likes().get(t);
            StoryItem item = new StoryItem();
            for(int c=0; c<likeItems.size(); c++) {
                StoryItem tmp = likeItems.get(c);

                if(tmp.getStoryid().equals(likeStory.episode().story().id()))
                {
                    isExistStory = true;
                }
            }

            if(isExistStory == false) {

                item.setCover(likeStory.episode().story().cover());
                item.setTitle(likeStory.episode().story().title());
                item.setStoryid(likeStory.episode().story().id());

                likeItems.add(item);
                likeItems.add(item);
                likeItems.add(item);
                likeItems.add(item);
                likeItems.add(item);
                likeItems.add(item);
                likeItems.add(item);likeItems.add(item);
                likeItems.add(item);
                likeItems.add(item);
                likeItems.add(item);


            }
        }

        userInfo.userInfoItem.setLikeItems(likeItems);

        for(int r =0; r<response.data().me().withoutDuplicationViewed().size(); r++)
        {
            GetUserProfileQuery.WithoutDuplicationViewed item = response.data().me().withoutDuplicationViewed().get(r);
            StoryItem storyItem = new StoryItem();

            storyItem.setCover(item.story().cover());
            storyItem.setTitle(item.story().title());
            storyItem.setStoryid(item.story().id());

            viewedItems.add(storyItem);
            viewedItems.add(storyItem);
            viewedItems.add(storyItem);
            viewedItems.add(storyItem);
            viewedItems.add(storyItem);
            viewedItems.add(storyItem);
            viewedItems.add(storyItem);
            viewedItems.add(storyItem);
            viewedItems.add(storyItem);
            viewedItems.add(storyItem);
        }

        userInfo.userInfoItem.setViewedItems(viewedItems);

        if(response.data().me().creator() != null)
        {
            if(response.data().me().creator().stories() != null)
                userInfo.userInfoItem.setCreateStoryCount(response.data().me().creator().stories().size());
        }
    }
}