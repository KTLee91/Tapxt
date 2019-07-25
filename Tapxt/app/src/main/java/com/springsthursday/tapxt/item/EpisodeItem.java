package com.springsthursday.tapxt.item;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class EpisodeItem implements Serializable {
    private static final long serializeableId = 2L;
    private String epidoseId = "";
    private String title = "";
    private int sequence = 0;
    private boolean isLiked = false;
    private String createdAt = "";
    private int likeCount = 0;
    private int inquiryCount = 0;
    private int commentCount = 0;

    public String getEpidoseId() {
        return epidoseId;
    }

    public String getTitle() {
        return this.sequence + "화 " + title;
    }

    public int getSequence() {
        return sequence;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public String getCreatedAt()  {
        return createdAt.substring(0,10);
    }

    public String getLikeCount() {
        return String.valueOf(likeCount);
    }

    public String getInquiryCount() {
        return String.valueOf(inquiryCount);
    }

    public String getCommentCount() {
        return String.valueOf(commentCount);
    }

    public void setEpidoseId(String epidoseId) {
        this.epidoseId = epidoseId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public void setInquiryCount(int inquiryCount) {
        this.inquiryCount = inquiryCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
