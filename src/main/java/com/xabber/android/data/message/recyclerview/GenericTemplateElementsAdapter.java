package com.xabber.android.data.message.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xabber.android.R;
import com.xabber.android.data.message.entity.PayloadElementsJSONParsed;

import java.util.ArrayList;

/**
 * Created by kushal on 10/12/16.
 */
public class GenericTemplateElementsAdapter extends RecyclerView.Adapter<GenericTemplateElementsViewHolder> {

    private  Context mContext;
    // list of items served by this adapter
    public ArrayList<PayloadElementsJSONParsed> mItems = new ArrayList<PayloadElementsJSONParsed>();
    public String account;
    public String user;

    public  GenericTemplateElementsAdapter(Context con, String account, String user) {
        this.user = user;
        this.account = account;
        mContext = con;
    }

    @Override
    public GenericTemplateElementsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_viewer_message_element, parent, false);
        return new GenericTemplateElementsViewHolder(mContext, view, account, user);
    }

    @Override
    public void onBindViewHolder(GenericTemplateElementsViewHolder holder, int position) {
        PayloadElementsJSONParsed element = mItems.get(position);
        holder.bindElement(element);

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
