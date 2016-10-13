package com.xabber.android.data.message.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by kushal on 10/12/16.
 */
public class MessageJSONParsed {
    @SerializedName("text")
    String text;

    public AttachmentJSONParsed getAttachment() {
        return attachment;
    }

    public void setAttachment(AttachmentJSONParsed attachment) {
        this.attachment = attachment;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @SerializedName("attachment")
    AttachmentJSONParsed attachment;

    public ArrayList<QuickRepliesJSONParsed> getQuick_replies() {
        return quick_replies;
    }

    public void setQuick_replies(ArrayList<QuickRepliesJSONParsed> quick_replies) {
        this.quick_replies = quick_replies;
    }

    @SerializedName("quick_replies")
    ArrayList<QuickRepliesJSONParsed> quick_replies;

    public ArrayList<PayloadButtonsJSONParsed> getButtons() {
        return buttons;
    }

    public void setButtons(ArrayList<PayloadButtonsJSONParsed> buttons) {
        this.buttons = buttons;
    }

    @SerializedName("buttons")
    ArrayList<PayloadButtonsJSONParsed> buttons;
}
