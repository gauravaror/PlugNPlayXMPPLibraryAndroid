package com.xabber.android.data.message.template;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xabber.android.R;
import com.xabber.android.data.Application;
import com.xabber.android.data.OnPostBackListener;
import com.xabber.android.data.message.OnChatNewMessageListerner;
import com.xabber.android.data.message.entity.PayloadButtonsJSONParsed;
import com.xabber.android.data.message.entity.PayloadElementsJSONParsed;

/**
 * Created by kushal on 10/12/16.
 */
public class ButtonTemplateViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    Button button;
    PayloadButtonsJSONParsed element;
    Context mContext;
    String conferenceId;

    public ButtonTemplateViewHolder(Context context, View mView,String cId) {
        super(mView);
        mContext = context;
        button = (Button)mView.findViewById(R.id.message_element_single_button);
        conferenceId = cId;
        button.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        if (element.getType().equals("web_url")) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            if (element.getUrl() != null) {
                i.setData(Uri.parse(element.getUrl()));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);
            }
        } else if (element.getType().equals("postback")) {
            Application.getInstance().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (OnPostBackListener onPostBackListener
                            : Application.getInstance().getUIListeners(OnPostBackListener.class)) {
                        onPostBackListener.onPostBack(element.getPayload(), conferenceId);
                    }
                }
            });

        }

    }

    public void bindButton(PayloadButtonsJSONParsed buttonsJSONParsed) {
        element = buttonsJSONParsed;
        if (element.getTitle() != null) {
            button.setText(element.getTitle());
        }
        button.setText(element.getTitle());
    }
}