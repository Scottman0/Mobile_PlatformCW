// Scott Blair (2022) -- Student ID: S2029064
package com.example.blair_scott_s2029064_trafficscotlandassignment.models;

import android.content.Intent;
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
import com.example.blair_scott_s2029064_trafficscotlandassignment.models.MainActivity;

public class Adapter extends RecyclerView.Adapter<Adapter.myViewHolder> {

    private Button detailedViewBtn;
    Context mContext;
    List<Item> mData;
    MainActivity main = new MainActivity();

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
        holder.tv_pubDate.setText("Date: " + mData.get(position).getPubDate());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title,tv_description,tv_pubDate;

        public myViewHolder(View itemView) {
            super (itemView);
            tv_title = itemView.findViewById(R.id.card_name);
            tv_description = itemView.findViewById(R.id.card_description);
            tv_pubDate = itemView.findViewById(R.id.card_pubDate);
            itemView.findViewById(R.id.card_details).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = getAdapterPosition();
                    Intent intent = new Intent(mContext, DetailedView.class);
                    intent.putExtra("title", mData.get(id).getTitle());
                    intent.putExtra("category", mData.get(id).getCategory());
                    intent.putExtra("description", mData.get(id).getDescription());
                    intent.putExtra("link", mData.get(id).getLink());
                    intent.putExtra("pubDate", mData.get(id).getPubDate());
                    intent.putExtra("location", mData.get(id).getLocation());
                    intent.putExtra("latitude", mData.get(id).getLatitude());
                    intent.putExtra("longitude", mData.get(id).getLongitude());
                    mContext.startActivity(intent);
                    System.out.println(mData.get(getAdapterPosition()).getTitle());
                }
            }); // button for when we click detailed view on a roadwork
        }

    }


}
