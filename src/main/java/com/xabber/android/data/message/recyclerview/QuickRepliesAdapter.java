package com.xabber.android.data.message.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xabber.android.R;
import com.xabber.android.data.message.entity.PayloadButtonsJSONParsed;
import com.xabber.android.data.message.entity.QuickRepliesJSONParsed;
import com.xabber.android.ui.adapter.ChatMessageAdapter;

import org.jivesoftware.smack.chat.ChatMessageListener;

import java.util.ArrayList;

/**
 * Created by kushal on 10/12/16.
 */
public class QuickRepliesAdapter extends RecyclerView.Adapter<QuickRepliesViewHolder> {

    private  Context mContext;
    // list of items served by this adapter
    public ArrayList<QuickRepliesJSONParsed> mItems = new ArrayList<QuickRepliesJSONParsed>();

    public String account;
    public String user;

    public QuickRepliesAdapter(Context con, String accoun, String use, ChatMessageAdapter.Listener listener) {
        mContext = con;
        account = accoun;
        user =use;
        chatMessageListener = listener;
    }

    private ChatMessageAdapter.Listener chatMessageListener;
    @Override
    public QuickRepliesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_quick_reply, parent, false);
        return new QuickRepliesViewHolder(mContext, view, account, user, chatMessageListener);
    }

    @Override
    public void onBindViewHolder(QuickRepliesViewHolder holder, int position) {
        QuickRepliesJSONParsed element = mItems.get(position);
        holder.bindQuickReply(element);

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
