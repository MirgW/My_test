package com.moris.tavda.adapter;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moris.tavda.BuildConfig;
import com.moris.tavda.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import dto.RemindDTO;

public class RemindListAdapter extends PagedListAdapter<RemindDTO, RemindViewHolder> {
    private List<RemindDTO> data;

    public RemindListAdapter(DiffUtil.ItemCallback<RemindDTO> diffUtilCallback) {
        super(diffUtilCallback);
    }


    private RemindListAdapter(@NonNull AsyncDifferConfig<RemindDTO> config) {
        super(config);
    }

    public RemindDTO getItemData(int position) {
//        return data.get(position);
        return getItem(position);    /*после paging a*/
    }
//    public RemindListAdapter(List<RemindDTO> data) {
//        this.data = data;
//    }

//    private final WeakReference<LayoutInflater> mInflater;
//
//    public RemindListAdapter(LayoutInflater inflater, List<RemindDTO> data) {
//        mInflater = new WeakReference<LayoutInflater>(inflater);
//        this.data = data;
//    }


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
//        RemindDTO item = data.get(position);
        RemindDTO item = getItem(position); /*после paging a*/
        if (item == null) {
            return;
        }
        holder.title.setText(item.getTitle());
        holder.doc.setText(item.getDoc_DTO());
        holder.day.setText(item.getData_DTO());
        try {
            Picasso.get().load("http://www.adm-tavda.ru/" + item.getImg_DTO().replaceAll("http://www.adm-tavda.ru/", "").replaceAll("http://adm-tavda.ru/", ""))
                    .placeholder(R.drawable.baseline_title_blue_a700_24dp)
                    .error(R.drawable.baseline_title_blue_a700_24dp)
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
//        return data.size();
        return super.getItemCount(); /*после paging a*/
    }

}
