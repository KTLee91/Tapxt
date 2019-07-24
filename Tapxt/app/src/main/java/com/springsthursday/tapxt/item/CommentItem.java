package com.springsthursday.tapxt.item;

import android.databinding.ObservableField;

import com.springsthursday.tapxt.repository.UserInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.function.Predicate;

public class CommentItem implements Serializable {
    private static final long serializeableId = 3L;

    private String id = "";
    private String avatar = "";
    private String text = "";
    private String userID = "";
    private String userNickName = "";
    private boolean isMe = false;
    public ObservableField<String> clapsCount = new ObservableField<>("0");

    private ArrayList<String> claps = new ArrayList<>();

    public String getId() {
        return id;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getText() {
        return text;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setMe(boolean me) {
        isMe = me;
    }

    public ArrayList<String> getClaps()
    {
        return claps;
    }

    public void addClaps(String user)
    {
        claps.add(user);
    }

    public void deleteClaps(final String user)
    {
        claps.removeIf(new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return s.equals(user);
            }
        });
    }

    public boolean isClapsMe()
    {
        String nickName = UserInfo.getInstance().userInfoItem.getNickName();

        for(int i=0; i < claps.size(); i++)
        {
            if(nickName.equals(claps.get(i)))
                return true;
        }
        return false;
    }
}
