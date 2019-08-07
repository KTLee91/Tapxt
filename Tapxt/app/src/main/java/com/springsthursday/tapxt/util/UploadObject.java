package com.springsthursday.tapxt.util;

import com.google.gson.annotations.SerializedName;

public class UploadObject {
    @SerializedName("key")
    private String key;

    public UploadObject(String key) {
        this.key = key;
    }
    public String getKey() {
        return key;
    }
}
