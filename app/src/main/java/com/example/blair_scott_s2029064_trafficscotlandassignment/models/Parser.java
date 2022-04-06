// Scott Blair (2022) -- Student ID: S2029064
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
import java.util.concurrent.Executors;

import com.example.blair_scott_s2029064_trafficscotlandassignment.models.MainActivity;

public class Parser extends AppCompatActivity {
    // Traffic Scotland Roadworks XML links
    private String urlCurrentIncidentsSource = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private String urlPlannedRoadworksSource = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private String urlCurrentRoadworksSource = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    private String result = "";
    boolean dataParsed = false;
    int currentUrl = 1; // value to store which URL we are parsing data from

    String oldCurrentIncidents = "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n" +
            "<rss version=\"2.0\" xmlns:georss=\"http://www.georss.org/georss\" xmlns:gml=\"http://www.opengis.net/gml\">\n" +
            "  <channel>\n" +
            "    <title>Traffic Scotland - OLD Current Incidents</title>\n" +
            "    <description>Current incidents on the road network e.g. accidents</description>\n" +
            "    <link>https://trafficscotland.org/currentincidents/</link>\n" +
            "    <language />\n" +
            "    <copyright />\n" +
            "    <managingEditor />\n" +
            "    <webMaster />\n" +
            "    <lastBuildDate>Wed, 06 Apr 2022 14:42:58 GMT</lastBuildDate>\n" +
            "    <docs>https://trafficscotland.org/rss/</docs>\n" +
            "    <rating />\n" +
            "    <generator>Traffic Scotland | www.trafficscotland.org</generator>\n" +
            "    <ttl>5</ttl>\n" +
            "    <item>\n" +
            "      <title>M8 Jct 15 - Jct 18 - Planned Roadworks</title>\n" +
            "      <description>The M8 both Eastbound and Westbound between Junctions 15 and Junction 18 is currently restricted due to essential bridge repairs.  Motorists are advised to expect delays in the area.</description>\n" +
            "      <link>http://tscot.org/01a13134</link>\n" +
            "      <georss:point>55.8692771239109 -4.24135563418866</georss:point>\n" +
            "      <author />\n" +
            "      <comments />\n" +
            "      <pubDate>Fri, 12 Mar 2021 20:53:52 GMT</pubDate>\n" +
            "    </item>\n" +
            "    <item>\n" +
            "      <title>A737 Johnstone - Breakdown</title>\n" +
            "      <description>All lanes restricted Northbound</description>\n" +
            "      <link>http://tscot.org/01c316371</link>\n" +
            "      <georss:point>55.8440105144569 -4.48990610367594</georss:point>\n" +
            "      <author />\n" +
            "      <comments />\n" +
            "      <pubDate>Wed, 06 Apr 2022 11:34:15 GMT</pubDate>\n" +
            "    </item>\n" +
            "    <item>\n" +
            "      <title>A720 A7 (Sheriffhall Rbt) - A68 (Millerhill Jct) - Queue</title>\n" +
            "      <description>2 lanes restricted Westbound</description>\n" +
            "      <link>http://tscot.org/01c316376</link>\n" +
            "      <georss:point>55.9107612785275 -3.06869855380554</georss:point>\n" +
            "      <author />\n" +
            "      <comments />\n" +
            "      <pubDate>Wed, 06 Apr 2022 14:42:58 GMT</pubDate>\n" +
            "    </item>\n" +
            "  </channel>\n" +
            "</rss>";

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
                System.out.println("Parsing old data...");
                parseItems(oldCurrentIncidents, "currentIncidents"); // parse old record of current incidents if no internet access or data fails to parse, (doesn't work with planned/current roadworks due to the description)
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