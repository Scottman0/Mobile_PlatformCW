package com.example.blair_scott_s2029064_trafficscotlandassignment.models;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;

// Class to parse and handle XML information using XMLPullParser
public class PlannedRoadworks {
    private String title;
    private String description;
    private String link;
    private String location; // map coordinates
    private String pubDate;

    // Constructors
    public void CurrentIncidents()
    {
        title = "";
        description = "";
        link = "";
        location = "";
    }

    // Getters
    public String getTitle()
    {
        System.out.println("Title: " + title);
        return title;
    }

    public String getDescription()
    {
        System.out.println("Description: " + description);
        return description;
    }

    public String getLink()
    {
        System.out.println("Link: " + link);
        return link;
    }

    public String getLocation()
    {
        System.out.println("Location: " + location);
        return location;
    }

    public String getPubDate()
    {
        System.out.println("Pub Date: " + pubDate);
        return pubDate;
    }

    // Setters
    public void setTitle(String title)
    {
        this.title = title;
        System.out.println("Title set to " + title);
    }

    public void setDescription(String description)
    {
        this.description = description;
        System.out.println("Description set to " + description);
    }

    public void setLink(String link)
    {
        this.link = link;
        System.out.println("Link set to " + link);
    }

    public void setLocation(String location)
    {
        this.location = location;
        System.out.println("Georss set to: " + location);
    }

    public void setPubDate(String pubDate)
    {
        this.pubDate = pubDate;
        System.out.println("Pub Date set to: " + pubDate);
    }

}
