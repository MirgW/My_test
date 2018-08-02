package com.moris.remindme.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moris.remindme.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import dto.RemindDTO;

public class RemindListAdapter extends RecyclerView.Adapter<RemindListAdapter.RemindViewHolder>{
    private List<RemindDTO> data;

    public RemindListAdapter(List<RemindDTO> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public RemindViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.remaind_item, parent, false);
        return new RemindViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RemindViewHolder holder, int position) {
        RemindDTO item = data.get(position);
        holder.title.setText(item.getTitle());
        holder.doc.setText(item.getDoc_DTO());
        holder.day.setText(item.getData_DTO());
           Picasso.get().load("http://www.adm-tavda.ru/"+item.getImg_DTO())
                .placeholder(R.drawable.ic_action_name)
                .error(R.drawable.ic_action_name)
                .into(holder.img);
        }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class RemindViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView title;
        TextView doc;
        TextView day;
        ImageView img;

        public RemindViewHolder(View itemView) {
            super(itemView);
            cardView= itemView.findViewById(R.id.cardView);
            title= itemView.findViewById(R.id.title);
            doc= itemView.findViewById(R.id.doc);
            day= itemView.findViewById(R.id.data);
            img = itemView.findViewById(R.id.item_img);
        }
    }
}
