package com.xabber.android.data.message.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by kushal on 10/12/16.
 */
public class PayloadElementsJSONParsed {
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getItem_url() {
        return item_url;
    }

    public void setItem_url(String item_url) {
        this.item_url = item_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public ArrayList<PayloadButtonsJSONParsed> getButtons() {
        return buttons;
    }

    public void setButtons(ArrayList<PayloadButtonsJSONParsed> buttons) {
        this.buttons = buttons;
    }

    @SerializedName("title")
    String title;
    @SerializedName("item_url")
    String item_url;
    @SerializedName("image_url")
    String image_url;
    @SerializedName("subtitle")
    String subtitle;
    @SerializedName("buttons")
    ArrayList<PayloadButtonsJSONParsed> buttons;

}
