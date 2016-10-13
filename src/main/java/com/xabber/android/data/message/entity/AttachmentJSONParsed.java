package com.xabber.android.data.message.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kushal on 10/12/16.
 */
public class AttachmentJSONParsed {
    @SerializedName("type")
    String type;

    public AttachmentPayloadJSONParsed getPayload() {
        return payload;
    }

    public void setPayload(AttachmentPayloadJSONParsed payload) {
        this.payload = payload;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @SerializedName("payload")
    AttachmentPayloadJSONParsed payload;
}
