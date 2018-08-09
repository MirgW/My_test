package com.moris.tavda.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import dto.RemindDTO;


public class ClickRecyclerAdapter extends RemindListAdapter implements View.OnClickListener {
    private final OnItemClickListener mClickListener;

    public ClickRecyclerAdapter(LayoutInflater inflater, List<RemindDTO> data,OnItemClickListener listener) {
        super(inflater, data);
        mClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @NonNull
    @Override
    public RemindViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RemindViewHolder holder = super.onCreateViewHolder(parent, viewType);
        holder.itemView.setOnClickListener(this);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RemindViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.itemView.setTag(position);
    }

    @Override
    public void onClick(View v) {
        Integer position = (Integer)v.getTag();
        mClickListener.onItemClick(v, position);
    }
}
