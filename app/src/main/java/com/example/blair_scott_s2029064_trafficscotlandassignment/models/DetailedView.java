// Scott Blair (2022) -- Student ID: S2029064
package com.example.blair_scott_s2029064_trafficscotlandassignment.models;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;

import com.example.blair_scott_s2029064_trafficscotlandassignment.R;
public class DetailedView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);
        TextView titleTxt = findViewById(R.id.detailedViewTitle);
        TextView categoryTxt = findViewById(R.id.detailedViewCategory);
        TextView descriptionTxt = findViewById(R.id.detailedViewDescription);
        TextView linkTxt = findViewById(R.id.detailedViewLink);
        TextView pubDateTxt = findViewById(R.id.detailedViewPubDate);
        TextView latitudeTxt = findViewById(R.id.detailedViewLatitude);
        TextView longitudeTxt = findViewById(R.id.detailedViewLongitude);

        String title = "Title not set";
        String category = "Category not set";
        String description = "Description not set";
        String link = "Link not set";
        String pubDate = "pubDate not set";
        String location = "Location not set";
        String latitude = "Latitude not set";
        String longitude = "Longitude not set";
        String startDate = "Start Date not set";
        String endDate = "End Date not set";
        String detailedInfo = "Detailed info not set";
        int daysBetween = 0;
        int daysRemaining = 0;

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            title = extras.getString("title");;
            category = extras.getString("category");
            description = extras.getString("description");
            link = extras.getString("link");
            pubDate = extras.getString("pubDate");
            location = extras.getString("location");
            latitude = extras.getString("latitude");
            longitude = extras.getString("longitude");
            startDate = extras.getString("startDate");
            endDate = extras.getString("endDate");
            detailedInfo = extras.getString("detailedInfo");
            daysBetween = extras.getInt("daysBetween");
            daysRemaining = extras.getInt("daysRemaining");
        }


        ConstraintLayout constraint = (ConstraintLayout)findViewById(R.id.constraintLayoutColour);

        // get category name to display in view
        switch (category)
        {
            case "currentRoadworks":
                categoryTxt.setText("Category: Current Roadworks");
                descriptionTxt.setText("Description: " + detailedInfo);
                if (daysRemaining < 3)
                {
                    constraint.setBackgroundColor(Color.parseColor("#00ff08"));
                } else if (daysRemaining > 2 && daysRemaining < 7) {
                    constraint.setBackgroundColor(Color.parseColor("#f2ac72"));
                } else if (daysRemaining > 6) {
                    constraint.setBackgroundColor(Color.parseColor("#fc746d"));
                }
                break;
            case "plannedRoadworks":
                categoryTxt.setText("Category: Planned Roadworks");
                descriptionTxt.setText("Description: " + detailedInfo);
                if (daysRemaining < 3)
                {
                    constraint.setBackgroundColor(Color.parseColor("#00ff08"));
                } else if (daysRemaining > 2 && daysRemaining < 7) {
                    constraint.setBackgroundColor(Color.parseColor("#f2ac72"));
                } else if (daysRemaining > 6) {
                    constraint.setBackgroundColor(Color.parseColor("#fc746d"));
                }
                break;
            case "currentIncidents":
                categoryTxt.setText("Category: Current Incidents");
                descriptionTxt.setText("Description: " + description);
                titleTxt.setTextColor(Color.BLACK);
                descriptionTxt.setTextColor(Color.BLACK);
                categoryTxt.setTextColor(Color.BLACK);
                linkTxt.setTextColor(Color.BLACK);
                latitudeTxt.setTextColor(Color.BLACK);
                longitudeTxt.setTextColor(Color.BLACK);
                pubDateTxt.setTextColor(Color.BLACK);

                break;
        }






        linkTxt.setText("Link: " + link);
        if (startDate != null & endDate != null) {
            pubDateTxt.setText("Dates: " + startDate + " to " + endDate + "\n - days remaining (approximate): " + daysRemaining);
        } else {
            pubDateTxt.setText("Published Date: " + pubDate);
        }
        titleTxt.setText("Title: " + title);
        latitudeTxt.setText("Latitude: " + latitude);
        longitudeTxt.setText("Longitude: " + longitude);
        System.out.println(latitude);
        System.out.println(longitude);
        System.out.println("Original Planned Amount of Days: " + daysBetween);


    }
    }
