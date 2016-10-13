package com.xabber.android.data.message.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kushal on 10/12/16.
 */
public class QuickRepliesJSONParsed {
    @SerializedName("content_type")
    String content_type;
    @SerializedName("title")
    String title;
    @SerializedName("payload")
    String payload;
    @SerializedName("image_url")
    String image_url;
}
