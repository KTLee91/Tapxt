package com.springsthursday.tapxt.item;

import java.io.Serializable;
import java.util.ArrayList;

public class StoryItem implements Serializable {
    private static final long serialVersionID = 1L;

    //Story
    private String storyid = "";
    private String title = "";
    private String description = "";
    private String cover = "";
    private ArrayList<String> tags = new ArrayList<>();

    //Creater
    private String creatorId = "";
    private String avator = "";
    private String nickName = "";
    private boolean isFollowed = false;

    //MainBanner
    private String url = "";
    private String thumb = "";

    //Episodes
    private ArrayList<EpisodeItem> episodeList = new ArrayList<>();

    //Feed
    private String feedTag = "";

    public String getStoryid() {
        return storyid;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCover() {
        return cover;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public String getAvator() {
        return avator;
    }

    public String getNickName() {
        return nickName;
    }

    public ArrayList<EpisodeItem> getEpisodeList() {
        return episodeList;
    }

    public int getEpisodeCount() { return episodeList.size(); }

    public String getUrl() {
        return url;
    }

    public String getThumb() {
        return thumb;
    }

    public boolean isFollowed() {
        return isFollowed;
    }

    public String getStringEpisodeCount()
    {
        return "에피소드 (" + episodeList.size() + ")";
    }

    public String getFeedTag() {
        return feedTag;
    }

    public void setStoryid(String storyid) {
        this.storyid = storyid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setEpisodeList(ArrayList<EpisodeItem> episodeList) {
        this.episodeList = episodeList;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public void setFollowed(boolean followed) {
        isFollowed = followed;
    }

    public void setFeedTag(String feedTag) {
        this.feedTag = feedTag;
    }

    public void addEpisodeItem(EpisodeItem item)
    {
        this.episodeList.add(item);
    }

    public void addTag(String Tag) {this.tags.add(Tag);}
}
