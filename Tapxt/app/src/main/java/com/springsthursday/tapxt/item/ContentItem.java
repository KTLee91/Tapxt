package com.springsthursday.tapxt.item;
import android.databinding.ObservableField;
import android.graphics.Color;

public class ContentItem {

    private String episodeTitle = "";
    private boolean isLiked = false;

    private String name = "";
    private ObservableField<String> avatar = new ObservableField<>();
    private String text = "";
    private String url = "";
    private int contentSequence = 0;
    private int contentType = 0;
    private String contentTypeForString = "";

    //Scene
    private String sceneID = "";
    private String sceneTitle = "";
    private int sceneSequence = 1;
    private String sceneBackground = "";
    private String sceneSound = "";

    private Boolean isContextScene = false;

    //Property
    private String textColor = "";
    private String boxColor = "";
    private boolean isBold = false;
    private boolean isItalic = false;

    public String getEpisodeTitle() {
        return episodeTitle;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar.get();
    }

    public String getText() {
        return text;
    }

    public int getContentSequence() {
        return contentSequence;
    }

    public int getContentType() {
        return contentType;
    }

    public String getSceneTitle() {
        return sceneTitle;
    }

    public String getSceneBackground() {
        return sceneBackground;
    }

    public String getSceneSound() {
        return sceneSound;
    }

    public int getSceneSequence() {
        return sceneSequence;
    }

    public Boolean getContextScene() {
        return isContextScene;
    }

    public String getSceneID() {
        return sceneID;
    }

    public String getUrl() {
        return url;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public String getTextColor() {
        return textColor;
    }

    public String getBoxColor() {
        return boxColor;
    }

    public boolean isBold() {
        return isBold;
    }

    public boolean isItalic() {
        return isItalic;
    }

    public String getContentTypeForString() {
        return contentTypeForString;
    }

    public void setEpisodeTitle(String episodeTitle) {
        this.episodeTitle = episodeTitle;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatar(String avatar) {
        this.avatar.set(avatar);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setContentSequence(int contentSequence) {
        this.contentSequence = contentSequence;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public void setSceneTitle(String sceneTitle) {
        this.sceneTitle = sceneTitle;
    }

    public void setSceneBackground(String sceneBackground) {
        this.sceneBackground = sceneBackground;
    }

    public void setSceneSound(String sceneSound) {
        this.sceneSound = sceneSound;
    }

    public void setSceneSequence(int sceneSequence) {
        this.sceneSequence = sceneSequence;
    }

    public void setContextScene(Boolean contextScene) {
        isContextScene = contextScene;
    }

    public void setAvatar(ObservableField<String> avatar) {
        this.avatar = avatar;
    }

    public void setSceneID(String sceneID) {
        this.sceneID = sceneID;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public void setBoxColor(String boxColor) {
        this.boxColor = boxColor;
    }

    public void setBold(boolean bold) {
        isBold = bold;
    }

    public void setItalic(boolean italic) {
        isItalic = italic;
    }

    public void setContentTypeForString(String contentTypeForString) {
        this.contentTypeForString = contentTypeForString;
    }
}
