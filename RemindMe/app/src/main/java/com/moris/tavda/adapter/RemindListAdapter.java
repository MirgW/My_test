package com.moris.tavda.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moris.tavda.R;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.List;

import dto.RemindDTO;

public class RemindListAdapter extends RecyclerView.Adapter<RemindViewHolder> {
    private List<RemindDTO> data;

    public RemindDTO getItemData(int position){
        return data.get(position);
    }
//    public RemindListAdapter(List<RemindDTO> data) {
//        this.data = data;
//    }

    private final WeakReference<LayoutInflater> mInflater;
    public RemindListAdapter(LayoutInflater inflater,List<RemindDTO> data) {
        mInflater = new WeakReference<LayoutInflater>(inflater);
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
        Picasso.get().load("http://www.adm-tavda.ru/" + item.getImg_DTO().replaceAll("http://www.adm-tavda.ru/", "").replaceAll("http://adm-tavda.ru/", ""))
                .placeholder(R.drawable.ic_action_name)
                .error(R.drawable.ic_action_name)
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
