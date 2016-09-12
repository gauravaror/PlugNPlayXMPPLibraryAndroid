package com.xabber.android.utils;

import android.content.Context;
import android.content.Intent;

import com.xabber.android.ui.activity.LoadActivity;

/**
 * Created by kushal on 9/12/16.
 */
public class PlugNPlayXMPPLibrary {

    public static void initializeSdk(Context mContext) {
        mContext.startActivity(LoadActivity.createIntent(mContext).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

    }
}
