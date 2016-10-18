package com.xabber.android.data.message.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.xabber.android.R;
import com.xabber.android.data.message.MessageManager;
import com.xabber.android.data.message.entity.PayloadButtonsJSONParsed;

/**
 * Created by kushal on 10/12/16.
 */
public class ButtonTemplateViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    Button button;
    PayloadButtonsJSONParsed element;
    Context mContext;
    String user;
    String account;

    public ButtonTemplateViewHolder(Context context, View mView,String account, String user) {
        super(mView);
        mContext = context;
        button = (Button)mView.findViewById(R.id.message_element_single_button);
        this.user = user;
        this.account = account;
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
            MessageManager.getInstance().onPostBack(element.getPayload(), user);
            MessageManager.getInstance().sendSystmeMessage(account, user, element.getTitle());


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
