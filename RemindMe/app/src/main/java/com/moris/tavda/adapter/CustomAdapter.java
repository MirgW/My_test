package com.moris.tavda.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.moris.tavda.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    ArrayList<String> Topics;
    ArrayList<String> Nodes;
    ArrayList<String> time1s;
    ArrayList<String> time2s;
    Context context;

    public CustomAdapter(Context context, ArrayList<String> Topic, ArrayList<String> Node, ArrayList<String> time1, ArrayList<String> time2) {
        this.context = context;
        this.Topics = Topic;
        this.Nodes = Node;
        this.time1s = time1;
        this.time2s = time2;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
            // set the data in items
            holder.topic.setText(Topics.get(position));
            holder.Node.setText(Nodes.get(position));
            holder.time1.setText(time1s.get(position));
            holder.time2.setText(time2s.get(position));
            // implement setOnClickListener event on item view.
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // display a toast with person name on item click
                    Toast.makeText(context, Topics.get(position), Toast.LENGTH_SHORT).show();
                }
            });
    }

    @Override
    public int getItemCount() {
        return Topics.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView topic, Node, time1, time2;// init the item view's

        public MyViewHolder(View itemView) {
            super(itemView);

            // get the reference of item view's
            topic = (TextView) itemView.findViewById(R.id.Topic);
            Node = (TextView) itemView.findViewById(R.id.Node);
            time1 = (TextView) itemView.findViewById(R.id.time1);
            time2 = (TextView) itemView.findViewById(R.id.time2);
        }
    }
}
