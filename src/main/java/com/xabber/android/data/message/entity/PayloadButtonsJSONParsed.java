package com.xabber.android.data.message.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kushal on 10/12/16.
 */
public class PayloadButtonsJSONParsed {
    @SerializedName("type")
    String type;
    @SerializedName("title")
    String title;
    @SerializedName("url")
    String url;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    @SerializedName("payload")
    String payload;
}
