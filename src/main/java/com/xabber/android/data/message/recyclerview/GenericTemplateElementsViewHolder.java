package com.xabber.android.data.message.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xabber.android.R;
import com.xabber.android.data.message.entity.PayloadElementsJSONParsed;
import com.xabber.android.utils.DividerItemDecoration;

/**
 * Created by kushal on 10/12/16.
 */
public class GenericTemplateElementsViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    ImageView element_image;
    TextView element_title;
    TextView element_subtitle;
    RecyclerView element_buttons;
    PayloadElementsJSONParsed element;
    Context mContext;
    String user;
    String account;

    public GenericTemplateElementsViewHolder(Context context, View mView, String account, String user) {
        super(mView);
        mContext = context;
        element_image = (ImageView) mView.findViewById(R.id.message_element_image_view);
        element_title = (TextView) mView.findViewById(R.id.message_element_title);
        element_subtitle = (TextView) mView.findViewById(R.id.message_element_subtitle);
        element_buttons = (RecyclerView) mView.findViewById(R.id.message_element_buttons);
        element_image.setOnClickListener(this);
        element_title.setOnClickListener(this);
        element_subtitle.setOnClickListener(this);
        this.user = user;
        this.account = account;
    }
    @Override
    public void onClick(View view) {
       Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(element.getItem_url()));
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(i);

    }

    public void bindElement(PayloadElementsJSONParsed elementsJSONParsed) {
        element = elementsJSONParsed;
        if (element.getTitle() != null) {
            element_title.setText(element.getTitle());
        }
        if (element.getSubtitle() != null) {
            element_subtitle.setText(element.getSubtitle());
            element_subtitle.setVisibility(View.VISIBLE);
        } else {
            element_subtitle.setVisibility(View.GONE);
        }
        if (element.getImage_url() != null) {
            Glide.with(mContext).load(Uri.parse(element.getImage_url())).crossFade().into(element_image);
            element_image.setVisibility(View.VISIBLE);
        } else {
            element_image.setVisibility(View.GONE);
        }
        if (element.getButtons() != null) {
            ButtonTemplateAdapter buttonTemplateAdapter = new ButtonTemplateAdapter(mContext, account, user);
            buttonTemplateAdapter.mItems = element.getButtons();
            element_buttons.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                element_buttons.addItemDecoration(new DividerItemDecoration(mContext.getDrawable(R.drawable.recycler_divider)));
            }
            element_buttons.setItemAnimator(new DefaultItemAnimator());
            element_buttons.setAdapter(buttonTemplateAdapter);
            element_buttons.setVisibility(View.VISIBLE);
        }
    }
}
