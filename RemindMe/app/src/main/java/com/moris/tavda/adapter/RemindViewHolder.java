package com.moris.tavda.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moris.tavda.R;

public class RemindViewHolder extends RecyclerView.ViewHolder {
    CardView cardView;
    TextView title;
    TextView doc;
    TextView day;
    TextView url_doc;
    ImageView img;

    public RemindViewHolder(View itemView) {
        super(itemView);
        cardView = itemView.findViewById(R.id.cardView);
        title = itemView.findViewById(R.id.title);
        doc = itemView.findViewById(R.id.doc);
        day = itemView.findViewById(R.id.data);
        img = itemView.findViewById(R.id.item_img);
    }
}