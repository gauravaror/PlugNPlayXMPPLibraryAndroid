package com.xabber.android.data.message;

import com.xabber.android.data.BaseUIListener;

/**
 * Created by kushal on 10/7/16.
 */
public interface OnChatNewMessageListerner extends BaseUIListener {
    /**
     * @param account
     * @param user
     */
    void onChatNewMessage(String account, String user);

}
