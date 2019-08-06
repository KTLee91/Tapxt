package com.springsthursday.tapxt.item;

import java.util.ArrayList;

public class UserInfoItem {
    private String phoneNumber = "";
    private String token = "";
    private String nickName = "";
    private String imageUrl = "";
    private int commentCount = 0;
    private int likesCount = 0;
    private int followingCount = 0;
    private int clapsCount = 0;
    private int createStoryCount = 0;
    private ArrayList<String> tags = new ArrayList<>();
    private ArrayList<FollowItem> follows = new ArrayList<>();
    private ArrayList<StoryItem> likeItems = new ArrayList<>();
    private ArrayList<StoryItem> viewedItems = new ArrayList<>();

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getToken() {
        return token;
    }

    public String getNickName() {
        return nickName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public int getClapsCount() {
        return clapsCount;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public ArrayList<FollowItem> getFollows() {
        return follows;
    }

    public ArrayList<StoryItem> getLikeItems() {
        return likeItems;
    }

    public ArrayList<StoryItem> getViewedItems() {
        return viewedItems;
    }

    public String getStoryCount(){
        return String.valueOf(likeItems.size());
    }

    public String getFollowingCountForString()
    {
        return String.valueOf(getFollowingCount());
    }

    public String getFollowerCount()
    {
        return String.valueOf(follows.size());
    }

    public String getCreateStoryCount() {
        return String.valueOf(createStoryCount);
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public void setClapsCount(int clapsCount) {
        this.clapsCount = clapsCount;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public void setFollows(ArrayList<FollowItem> follows) {
        this.follows = follows;
    }

    public void setLikeItems(ArrayList<StoryItem> likeItems) {
        this.likeItems = likeItems;
    }

    public void setViewedItems(ArrayList<StoryItem> viewedItems) {
        this.viewedItems = viewedItems;
    }

    public void setCreateStoryCount(int createStoryCount) {
        this.createStoryCount = createStoryCount;
    }
}
