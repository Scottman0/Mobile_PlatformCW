package com.example.blair_scott_s2029064_trafficscotlandassignment.models;

//import com.example.blair_scott_s2029064_trafficscotlandassignment.models.MainActivity;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import com.example.blair_scott_s2029064_trafficscotlandassignment.models.MainActivity;

public class Parser extends AppCompatActivity {
    // Traffic Scotland Roadworks XML links
    private String urlCurrentIncidentsSource = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private String urlPlannedRoadworksSource = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private String urlCurrentRoadworksSource = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    private String result = "";
    boolean dataParsed = false;
    int currentUrl = 1; // value to store which URL we are parsing data from


    List<Item> items = new ArrayList<Item>();
    Item item = new Item();

    List<Item> currentRoadworks = new ArrayList<Item>();
    List<Item> plannedRoadworks = new ArrayList<Item>();
    List<Item> currentIncidents = new ArrayList<Item>();

    public void startCurrentIncidentsProgress() {
        // Run network access on a separate thread;
        new Thread(new Task(urlCurrentIncidentsSource)).start();
    }

    public void startPlannedRoadworksProgress() {
        // Run network access on a separate thread;
        new Thread(new Task(urlPlannedRoadworksSource)).start();
    }

    public void startCurrentRoadworksProgress() {
        // Run network access on a separate thread;
        new Thread(new Task(urlCurrentRoadworksSource)).start();
    }

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable {
        private String url;

        public Task(String aurl) {
            url = aurl;
        }

        @Override
        public void run() {
            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";
            Log.e("MyTag", "in run");
            try {
                Log.e("MyTag", "in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new
                        InputStreamReader(yc.getInputStream()));
                Log.e("MyTag", "after ready");

                while ((inputLine = in.readLine()) != null) {
                    result = result + inputLine;
                    //Log.e("MyTag",inputLine);
                }

                switch (currentUrl) // check which URL we are parsing for data (1 = current incidents, 2 = planned roadworks, 3 = current roadworks)
                {
                    case 1:
                        parseItems(result, "currentIncidents");
                        result = "";
                        currentUrl = 2;
                        startPlannedRoadworksProgress(); // parse the next set of data, (planned roadworks)
                        break;
                    case 2:
                        parseItems(result, "plannedRoadworks");
                        result = "";
                        currentUrl = 3;
                        startCurrentRoadworksProgress();    // parse the next set of data (current roadworks)
                        break;
                    case 3:
                        parseItems(result, "currentRoadworks");
                        result = "";
                        dataParsed = true;
                        break;
                }
                in.close();
            } catch (IOException ae) {
                Log.e("MyTag", "ioexception in run");
            }
            Parser.this.runOnUiThread(new Runnable() {
                public void run() {
                    Log.d("UI thread", "I am the UI thread");
                    //rawDataDisplay.setText(result);
                }
            });
        }
    }

    private void parseItems(String urlToParse, String categoryToParse) {
        try {
            // set up XML Pull Parser
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(urlToParse));

            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (xpp.getName().equalsIgnoreCase("item")) {
                            item = new Item();
                            item.setCategory(categoryToParse);
                            System.out.println(categoryToParse + " - object created");
                        } else if (xpp.getName().equalsIgnoreCase("title") && xpp.getDepth() == 4) {
                            String temp = xpp.nextText();
                            item.setTitle(temp);
                            //Log.e("MyTag", "Title is " + temp);
                        } else if (xpp.getName().equalsIgnoreCase("description") && xpp.getDepth() == 4) {
                            String temp = xpp.nextText();
                            item.setDescription(temp);
                        } else if (xpp.getName().equalsIgnoreCase("point") && xpp.getDepth() == 4) {
                            String temp = xpp.nextText();
                            item.setLocation(temp);
                            System.out.println("Parser location set to " + temp);
                        } else if (xpp.getName().equalsIgnoreCase("link") && xpp.getDepth() == 4) {
                            String temp = xpp.nextText();
                            item.setLink(temp);
                        } else if (xpp.getName().equalsIgnoreCase("pubDate") && xpp.getDepth() == 4) {
                            String temp = xpp.nextText();
                            item.setPubDate(temp);
                            //Log.e("MyTag", "Pub Date is " + temp);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (xpp.getName().equalsIgnoreCase("item"))
                        {
                            items.add(item);
                            if (categoryToParse == "currentRoadworks")
                            {
                                currentRoadworks.add(item);
                            }
                            else if (categoryToParse == "currentIncidents")
                            {
                                currentIncidents.add(item);
                            }
                            else if (categoryToParse == "plannedRoadworks")
                            {
                                plannedRoadworks.add(item);
                            }
                            System.out.println("Object saved...");
                        }
                        else if (xpp.getName().equalsIgnoreCase("channel")) {
                            System.out.println("End of XML Items");
                        }
                        break;
                }
                eventType = xpp.next(); // get the next event
            } // end of while

            if (eventType == XmlPullParser.END_DOCUMENT) {
                System.out.println("End of XML Document");
                int currentRoadworksSize = 0;
                int plannedRoadworksSize = 0;
                int currentIncidentsSize = 0;
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).getCategory().equals("currentRoadworks")) {
                        currentRoadworksSize++;
                    } else if (items.get(i).getCategory().equals("plannedRoadworks")) {
                        plannedRoadworksSize++;
                    } else if (items.get(i).getCategory().equals("currentIncidents")) {
                        currentIncidentsSize++;
                    }
                }
                // if the lists aren't empty display their size
                if (currentRoadworksSize != 0 && plannedRoadworksSize != 0 && currentIncidentsSize != 0) {
                    System.out.println("Current Roadworks Size: " + currentRoadworksSize);
                    System.out.println("Planned Roadworks Size: " + plannedRoadworksSize);
                    System.out.println("Current Incidents Size: " + currentIncidentsSize);
                }

            }
            //System.out.println("Current Incident List: " + currentIncidents.get(0));
        } catch (Exception err) {
            System.out.println("Error: " + err);
        }

    }
}