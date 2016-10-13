package com.xabber.android.data.message.template;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xabber.android.R;
import com.xabber.android.data.message.entity.PayloadButtonsJSONParsed;
import com.xabber.android.data.message.entity.PayloadElementsJSONParsed;

import java.util.ArrayList;

/**
 * Created by kushal on 10/12/16.
 */
public class ButtonTemplateAdapter extends RecyclerView.Adapter<ButtonTemplateViewHolder> {

    private  Context mContext;
    // list of items served by this adapter
    public ArrayList<PayloadButtonsJSONParsed> mItems = new ArrayList<PayloadButtonsJSONParsed>();

    public String account;
    public String user;

    public ButtonTemplateAdapter(Context con, String accoun, String use) {
        mContext = con;
        account = accoun;
        user =use;

    }

    @Override
    public ButtonTemplateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_viewer_message_button, parent, false);
        return new ButtonTemplateViewHolder(mContext, view, account, user);
    }

    @Override
    public void onBindViewHolder(ButtonTemplateViewHolder holder, int position) {
        PayloadButtonsJSONParsed element = mItems.get(position);
        holder.bindButton(element);

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
