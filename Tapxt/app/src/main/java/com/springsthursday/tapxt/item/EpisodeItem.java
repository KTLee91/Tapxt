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
}
