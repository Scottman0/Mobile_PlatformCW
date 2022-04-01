// Scott Blair (2022) - Student ID: S2029064
package com.example.blair_scott_s2029064_trafficscotlandassignment.models;

import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Item {
    private String category;
    private String title;
    private String description;
    private String link;
    private String location; // map coordinates
    private String pubDate;
    private String coords[];
    private String detailedInfo;
    private Date startDate;
    private Date endDate;
    private long daysBetween;

    private SimpleDateFormat dateOutFormat =
            new SimpleDateFormat("EEEE h:mm a (MMM d)");   // Only includes date, not time

    private SimpleDateFormat dateInFormat =
            new SimpleDateFormat("EEE, dd MMM yyyy");     // Only includes date, not time

    // Constructors
    public Item()
    {
        category = "";
        title = "";
        description = "";
        link = "";
        location = "";
    }

    public Item(String category, String title, String description, String link, String pubDate)
    {
        this.category = category;
        this.title = title;
        this.description = description;
        this.link = link;
        this.pubDate = pubDate;
    }

    public Item(String title, String description, String link, String pubDate)
    {
        this.category = category;
        this.title = title;
        this.description = description;
        this.link = link;
        this.pubDate = pubDate;
    }

    // Getters
    public String getCategory()
    {
        //System.out.println("Category: " + category);
        return category;
    }

    public String getTitle()
    {
        //System.out.println("Title: " + title);
        return title;
    }

    public String getDescription()
    {
        //System.out.println("Description: " + description);
        return description;
    }

    public String getLink()
    {
        //System.out.println("Link: " + link);
        return link;
    }

    public String getLocation()
    {
        //System.out.println("Location: " + location);
        return location;
    }

    public String getLatitude()
    {
        coords = location.split(" ");
        return coords[0];
    }

    public String getLongitude()
    {
        coords = location.split(" ");
        return coords[1];
    }

    public String getPubDate()
    {
        //System.out.println("Pub Date: " + pubDate);
        return pubDate;
    }

    public String getFormattedPubDate()
    {
        try {
            if (pubDate != null) {              // make sure pubDate exists
                Date date = dateInFormat.parse(pubDate);
                String pubDateFormatted = dateInFormat.format(date);
                return pubDateFormatted;
            }
            else {
                return "Error: no pubDate";
            }
        }
        catch (ParseException e) {
            return "Error: No pubDate";      // don't throw exception
        }
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public String getDetailedInfo()
    {
        return detailedInfo;
    }

    // Setters
    public void setCategory(String category)
    {
        this.category = category;
        System.out.println("Category set to " + category);
    }

    public void setTitle(String title)
    {
        this.title = title;
        System.out.println("Title set to " + title);
    }

    public void setDescription(String description)
    {
        String[] splitter = description.split("<br />");

        String startDateStr = "";
        String endDateStr = "";

        startDateStr = splitter[0]; // set our start date to the value before the first breakpoint

        if(splitter.length > 1)
            endDateStr = splitter[1];

        if(splitter.length>2){
            detailedInfo = splitter[2];
        }

        startDateStr = startDateStr.substring(12);
        startDateStr.trim();
        try {
            endDateStr = endDateStr.substring(10);
        } catch (IndexOutOfBoundsException i){
            System.out.println("Error: " + i);
        }

        Date startDate = null;
        Date endDate = null;

        try {
            startDate = new SimpleDateFormat("EE, dd MMMM yyyy - kk:mm").parse(startDateStr);
            endDate = new SimpleDateFormat("EE, dd MMMM yyyy - kk:mm").parse(endDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        //System.out.println("Description set to " + description);
        //System.out.println("Start Date: " + startDate);
        //System.out.println("End Date: " + endDate);
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
