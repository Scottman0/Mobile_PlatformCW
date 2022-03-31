// Scott Blair (2022) -- Student ID: S2029064
package com.example.blair_scott_s2029064_trafficscotlandassignment.models;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
        TextView locationTxt = findViewById(R.id.detailedViewLocation);

        String title = "Title not set";
        String category = "Category not set";
        String description = "Description not set";
        String link = "Link not set";
        String pubDate = "pubDate not set";
        String location = "Location not set";

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            title = extras.getString("title");;
            category = extras.getString("category");
            description = extras.getString("description");
            link = extras.getString("link");
            pubDate = extras.getString("pubDate");
            location = extras.getString("location");
        }

        titleTxt.setText("Title: " + title);

        switch (category)
        {
            case "currentRoadworks":
                categoryTxt.setText("Category: Current Roadworks");
                break;
            case "plannedRoadworks":
                categoryTxt.setText("Category: Planned Roadworks");
                break;
            case "currentIncidents":
                categoryTxt.setText("Category: Current Incidents");
                break;
        }

        descriptionTxt.setText("Description: " + description);
        linkTxt.setText("Link: " + link);
        pubDateTxt.setText("Publication Date: " + pubDate);
        locationTxt.setText("Location: " + location);
    }

    }