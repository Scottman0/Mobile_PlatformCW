// Scott Blair (2022) -- Student ID: S2029064
package com.example.blair_scott_s2029064_trafficscotlandassignment.models;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.example.blair_scott_s2029064_trafficscotlandassignment.R;
import com.example.blair_scott_s2029064_trafficscotlandassignment.models.MainActivity;

public class Adapter extends RecyclerView.Adapter<Adapter.myViewHolder> {

    private Button detailedViewBtn;
    Context mContext;
    List<Item> mData;
    MainActivity main = new MainActivity();
    Date todaysDate = new Date();

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
        holder.tv_pubDate.setText("Date Posted: " + mData.get(position).getFormattedPubDate());

        if (mData.get(position).getEndDate() != null && mData.get(position).getStartDate() != null)
        {
            long daysRemaining = todaysDate.getTime() - mData.get(position).getEndDate().getTime();
            int daysRemainingInt = (int) (daysRemaining/1000/60/60/24);
            daysRemainingInt = Math.abs(daysRemainingInt);
            if (daysRemainingInt < 3)
            {
                holder.itemView.setBackgroundColor(Color.parseColor("#00ff08"));
            } else if (daysRemainingInt > 2 && daysRemainingInt < 7) {
                holder.itemView.setBackgroundColor(Color.parseColor("#f2ac72"));
            } else if (daysRemainingInt > 6) {
                holder.itemView.setBackgroundColor(Color.parseColor("#fc746d"));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title,tv_pubDate;

        public myViewHolder(View itemView) {
            super (itemView);
            tv_title = itemView.findViewById(R.id.card_name);
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
                    intent.putExtra("pubDate", mData.get(id).getFormattedPubDate());
                    intent.putExtra("location", mData.get(id).getLocation());
                    intent.putExtra("latitude", mData.get(id).getLatitude());
                    intent.putExtra("longitude", mData.get(id).getLongitude());
                    // if dates exist then send them as extras to display on RecyclerView
                    if (mData.get(id).getStartDate() != null && mData.get(id).getEndDate() != null) {
                        intent.putExtra("startDate", mData.get(id).getStartDate().toString());
                        intent.putExtra("endDate", mData.get(id).getEndDate().toString());
                        // calculate the number of days a roadwork was planned to last and the number of days remaining
                        long daysBetween = mData.get(id).getEndDate().getTime() - mData.get(id).getStartDate().getTime();
                        int daysBetweenInt = (int) (daysBetween/1000/60/60/24);
                        long daysRemaining = todaysDate.getTime() - mData.get(id).getEndDate().getTime();
                        int daysRemainingInt = (int) (daysRemaining/1000/60/60/24);
                        daysRemainingInt = Math.abs(daysRemainingInt);
                        System.out.println("Days Remaining: " + daysRemainingInt);
                        intent.putExtra("daysBetween", daysBetweenInt);
                        intent.putExtra("daysRemaining", daysRemainingInt);
                    }
                    intent.putExtra("detailedInfo", mData.get(id).getDetailedInfo());
                    mContext.startActivity(intent);
                    System.out.println(mData.get(getAdapterPosition()).getTitle());
                }
            }); // button for when we click detailed view on a roadwork
        }

    }


}
