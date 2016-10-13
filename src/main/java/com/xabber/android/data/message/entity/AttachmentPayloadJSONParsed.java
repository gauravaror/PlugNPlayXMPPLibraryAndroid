package com.xabber.android.data.message.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by kushal on 10/12/16.
 */
public class AttachmentPayloadJSONParsed {
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTemplate_type() {
        return template_type;
    }

    public void setTemplate_type(String template_type) {
        this.template_type = template_type;
    }

    public ArrayList<PayloadElementsJSONParsed> getElements() {
        return elements;
    }

    public void setElements(ArrayList<PayloadElementsJSONParsed> elements) {
        this.elements = elements;
    }

    public ArrayList<PayloadButtonsJSONParsed> getButtons() {
        return buttons;
    }

    public void setButtons(ArrayList<PayloadButtonsJSONParsed> buttons) {
        this.buttons = buttons;
    }

    @SerializedName("url")
    String url;
    @SerializedName("text")
    String text;
    @SerializedName("template_type")
    String template_type;
    @SerializedName("elements")
    ArrayList<PayloadElementsJSONParsed> elements;
    @SerializedName("buttons")
    ArrayList<PayloadButtonsJSONParsed> buttons;

}
