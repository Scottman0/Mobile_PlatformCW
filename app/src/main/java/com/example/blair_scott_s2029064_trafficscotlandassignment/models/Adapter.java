package com.example.blair_scott_s2029064_trafficscotlandassignment.models;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.example.blair_scott_s2029064_trafficscotlandassignment.R;

public class Adapter extends RecyclerView.Adapter<Adapter.myViewHolder> {

    private Button detailedViewBtn;
    Context mContext;
    List<Item> mData;

    public Adapter(Context mContext, List<Item> mData)
    {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.card_item,parent,false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.tv_title.setText("Title: " + mData.get(position).getTitle());
        holder.tv_description.setText("Description: " + mData.get(position).getDescription());
        holder.tv_link.setText(mData.get(position).getLink());
        holder.tv_pubDate.setText("Date: " + mData.get(position).getPubDate());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title,tv_description,tv_link,tv_pubDate;

        public myViewHolder(View itemView) {
            super (itemView);
            tv_title = itemView.findViewById(R.id.card_name);
            tv_description = itemView.findViewById(R.id.card_description);
            tv_link = itemView.findViewById(R.id.card_link);
            tv_pubDate = itemView.findViewById(R.id.card_pubDate);
        }
    }
}
