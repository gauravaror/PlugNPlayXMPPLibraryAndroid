package com.xabber.android.data.message.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kushal on 10/12/16.
 */
public class QuickRepliesJSONParsed {
    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    @SerializedName("content_type")
    String content_type;
    @SerializedName("title")
    String title;
    @SerializedName("payload")
    String payload;
    @SerializedName("image_url")
    String image_url;
}
