package com.xabber.android.data;

/**
 * Created by kushal on 10/12/16.
 */
public interface OnPostBackListener extends BaseUIListener {
    /**
     * POSTBACK Button Called occurred.
     *
     * @param payload String with error description.
     * @param conferenceId String
     */
    void onPostBack(String payload, String conferenceId);

}
