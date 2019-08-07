package com.springsthursday.tapxt.util;

import com.google.gson.annotations.SerializedName;

public class UploadObject {
    @SerializedName("location")
    private String location;

    public UploadObject(String location) {
        this.location = location;
    }
    public String getAvatar() {
        return location;
    }
}
