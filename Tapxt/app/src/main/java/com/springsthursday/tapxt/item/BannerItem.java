package com.springsthursday.tapxt.item;

public class BannerItem {
    private String id = "";
    private String storyID = "";
    private String title = "";
    private String description = "";
    private String url = "";
    private String thumb = "";

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getThumb() {
        return thumb;
    }

    public String getStoryID() {
        return storyID;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public void setStoryID(String storyID) {
        this.storyID = storyID;
    }
}
