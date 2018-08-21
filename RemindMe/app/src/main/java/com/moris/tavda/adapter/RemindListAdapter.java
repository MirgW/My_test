package com.moris.tavda.adapter;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moris.tavda.BuildConfig;
import com.moris.tavda.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.lang.ref.WeakReference;
import java.util.List;

import dto.RemindDTO;

public class RemindListAdapter extends RecyclerView.Adapter<RemindViewHolder> {
    private List<RemindDTO> data;

    public RemindDTO getItemData(int position) {
        return data.get(position);
    }
//    public RemindListAdapter(List<RemindDTO> data) {
//        this.data = data;
//    }

    private final WeakReference<LayoutInflater> mInflater;

    public RemindListAdapter(LayoutInflater inflater, List<RemindDTO> data) {
        mInflater = new WeakReference<LayoutInflater>(inflater);
        this.data = data;
    }


    @NonNull
    @Override
    public RemindViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.remaind_item, parent, false);
        return new RemindViewHolder(view);
    }

    Transformation transformation = new Transformation() {

        @Override
        public String key() {
            return "transformation" + " desiredWidth";
        }

        @Override
        public Bitmap transform(Bitmap source) {
            int targetWidth = 200;

            double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
            int targetHeight = (int) (targetWidth * aspectRatio);
            Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
            if (result != source) {
                // Same bitmap is returned if sizes are the same
                source.recycle();
            }
            return result;
        }

    };

    @Override
    public void onBindViewHolder(@NonNull RemindViewHolder holder, int position) {
        RemindDTO item = data.get(position);
        holder.title.setText(item.getTitle());
        holder.doc.setText(item.getDoc_DTO());
        holder.day.setText(item.getData_DTO());
        try {
            Picasso.get().load("http://www.adm-tavda.ru/" + item.getImg_DTO().replaceAll("http://www.adm-tavda.ru/", "").replaceAll("http://adm-tavda.ru/", ""))
                    .placeholder(R.drawable.ic_action_name)
                    .error(R.drawable.ic_action_name)
                    .transform(transformation)
                    .into(holder.img);
        } catch (Exception e) {
            e.printStackTrace();
            if (BuildConfig.DEBUG) {
                Log.d("onBindViewHolder", "onBindViewHolder: " + e.getMessage());
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
