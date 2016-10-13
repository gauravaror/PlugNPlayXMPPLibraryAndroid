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
import com.xabber.android.data.message.entity.QuickRepliesJSONParsed;
import com.xabber.android.ui.adapter.ChatMessageAdapter;

import org.jivesoftware.smack.chat.ChatMessageListener;

/**
 * Created by kushal on 10/12/16.
 */
public class QuickRepliesViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    Button button;
    QuickRepliesJSONParsed element;
    Context mContext;
    String user;
    String account;
    ChatMessageAdapter.Listener chatMessageListener;

    public QuickRepliesViewHolder(Context context, View mView, String account, String user, ChatMessageAdapter.Listener listener) {
        super(mView);
        mContext = context;
        button = (Button)mView.findViewById(R.id.message_element_single_button);
        this.user = user;
        this.account = account;
        chatMessageListener = listener;
        button.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        if (element.getContent_type().equals("location")) {
            Intent i = new Intent(Intent.ACTION_VIEW);
        } else if (element.getContent_type().equals("text")) {
            MessageManager.getInstance().onPostBack(element.getPayload(), user);
            MessageManager.getInstance().sendSystmeMessage(account, user, element.getTitle());
        }
        chatMessageListener.removeQuickReplies();

    }

    public void bindQuickReply(QuickRepliesJSONParsed quickRepliesJSONParsed) {
        element = quickRepliesJSONParsed;
        if (element.getTitle() != null) {
            button.setText(element.getTitle());
        }
        if (element.getContent_type().equals("location")) {
            button.setText("Share Location");
        }
        button.setText(element.getTitle());
    }
}
