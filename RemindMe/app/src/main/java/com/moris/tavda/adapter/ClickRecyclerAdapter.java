package com.moris.tavda.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import dto.RemindDTO;


public class ClickRecyclerAdapter extends RemindListAdapter implements View.OnClickListener {
    private OnItemClickListener mClickListener;

    public ClickRecyclerAdapter(DiffUtil.ItemCallback<RemindDTO> diffUtilCallback) {
        super(diffUtilCallback);
    }

    @Override
    public RemindDTO getItemData(int position) {
        return super.getItemData(position);
    }

//    protected ClickRecyclerAdapter(@NonNull DiffUtil.ItemCallback<RemindDTO> diffCallback) {
//        super(diffCallback);
//    }


//    public ClickRecyclerAdapter(LayoutInflater inflater, List<RemindDTO> data, OnItemClickListener listener) {
//        super(inflater,);
//        // super(inflater, data);
//        mClickListener = listener;
//    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @NonNull
    @Override
    public RemindViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RemindViewHolder holder = super.onCreateViewHolder(parent, viewType);
        holder.itemView.setOnClickListener(this);
        //R.layout.fragment_history.progress_bar
//        final LinearLayout lyt_progress = (LinearLayout) holder.itemView.findViewById(R.id.lyt_progress);
//        lyt_progress.setVisibility(View.INVISIBLE);
//        lyt_progress.setAlpha(1.0f);
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

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mClickListener = listener;
    }
}
