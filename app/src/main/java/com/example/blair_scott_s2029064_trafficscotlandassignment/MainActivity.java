// Scott Blair (2022) -- Student ID: S2029064
package com.example.blair_scott_s2029064_trafficscotlandassignment;

// Import required libraries
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.example.blair_scott_s2029064_trafficscotlandassignment.models.Item;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    private TextView rawDataDisplay;
    private Button currentIncidentsButton;
    private Button plannedRoadworksButton;
    private Button currentRoadworksButton;
    private String result = "";

    // Traffic Scotland Roadworks XML links
    private String urlCurrentIncidentsSource = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private String urlPlannedRoadworksSource = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private String urlCurrentRoadworksSource = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    int currentUrl = 1; // value to store which URL we are parsing data from

    List<Item> items = new ArrayList<Item>();
    Item item = new Item();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MyTag", "in onCreate");
        // Set up the raw links to the graphical components
        rawDataDisplay = (TextView) findViewById(R.id.rawDataDisplay);

//        currentIncidentsButton = (Button) findViewById(R.id.currentIncidentsButton);
//        currentIncidentsButton.setOnClickListener(this);

        Log.e("MyTag", "after startButton");

        startCurrentIncidentsProgress(); // start parsing current incidents first on create which when complete
                                         // will parse planned roadworks and then finally current roadworks;


    }

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

    @Override
    public void onClick(View v) {
        Log.e("MyTag", "in onClick");
        int id = v.getId();

            Log.e("MyTag", "after startProgress");
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

                switch(currentUrl) // check which URL we are parsing for data (1 = current incidents, 2 = planned roadworks, 3 = current roadworks)
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
                        break;
                }
                in.close();
            } catch (IOException ae) {
                Log.e("MyTag", "ioexception in run");
            }
            MainActivity.this.runOnUiThread(new Runnable() {
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







// old code
//    private void parsePlannedRoadworksData(String urlToParse) {
//        try {
//            // set up XML Pull Parser
//            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//            factory.setNamespaceAware(true);
//            XmlPullParser xpp = factory.newPullParser();
//            xpp.setInput(new StringReader(urlToParse));
//
//            int eventType = xpp.getEventType();
//            while (eventType != XmlPullParser.END_DOCUMENT) {
//                switch (eventType) {
//                    case XmlPullParser.START_TAG:
//                        if (xpp.getName().equalsIgnoreCase("item")) {
//                            item = new Item();
//                            item.setCategory("plannedRoadworks");
//                            System.out.println("planned roadworks - object created");
//                        } else if (xpp.getName().equalsIgnoreCase("title") && xpp.getDepth() == 4) {
//                            String temp = xpp.nextText();
//                            item.setTitle(temp);
//                            //Log.e("MyTag", "Title is " + temp);
//                        } else if (xpp.getName().equalsIgnoreCase("description") && xpp.getDepth() == 4) {
//                            String temp = xpp.nextText();
//                            item.setDescription(temp);
//                        } else if (xpp.getName().equalsIgnoreCase("link") && xpp.getDepth() == 4) {
//                            String temp = xpp.nextText();
//                            item.setLink(temp);
//                        } else if (xpp.getName().equalsIgnoreCase("pubDate") && xpp.getDepth() == 4) {
//                            String temp = xpp.nextText();
//                            item.setPubDate(temp);
//                            //Log.e("MyTag", "Pub Date is " + temp);
//                        }
//                        break;
//                    case XmlPullParser.END_TAG:
//                        if (xpp.getName().equalsIgnoreCase("item"))
//                        {
//                            items.add(item);
//                            System.out.println("Planned Roadwork saved...");
//                        }
//                        else if (xpp.getName().equalsIgnoreCase("channel"))
//                            System.out.println("End of XML Items");
//                        break;
//                }
//                eventType = xpp.next(); // get the next event
//            }
//            if (eventType == XmlPullParser.END_DOCUMENT) {
//                System.out.println("End of XML Document");
//                System.out.println(items.size());
//
//                for (int i = 0; i < items.size(); i++) {
//                    if (items.get(i).getCategory().equals("plannedRoadworks"))
//                    System.out.println(items.get(i).getTitle());
//                }
//
//            }
//            //System.out.println("Planned Roadworks List: " + currentIncidents.get(0));
//        } catch (Exception err) {
//            System.out.println("Error: " + err);
//        }
//    }
//
//    private void parseCurrentIncidentsData(String urlToParse) {
//        try {
//            // set up XML Pull Parser
//            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//            factory.setNamespaceAware(true);
//            XmlPullParser xpp = factory.newPullParser();
//            xpp.setInput(new StringReader(urlToParse));
//
//            int eventType = xpp.getEventType();
//            while (eventType != XmlPullParser.END_DOCUMENT) {
//                switch (eventType) {
//                    case XmlPullParser.START_TAG:
//                        if (xpp.getName().equalsIgnoreCase("item")) {
//                            item = new Item();
//                            item.setCategory("currentIncidents");
//                            System.out.println("current incidents - object created");
//                        } else if (xpp.getName().equalsIgnoreCase("title") && xpp.getDepth() == 4) {
//                            String temp = xpp.nextText();
//                            item.setTitle(temp);
//                            //Log.e("MyTag", "Title is " + temp);
//                        } else if (xpp.getName().equalsIgnoreCase("description") && xpp.getDepth() == 4) {
//                            String temp = xpp.nextText();
//                            item.setDescription(temp);
//                        } else if (xpp.getName().equalsIgnoreCase("link") && xpp.getDepth() == 4) {
//                            String temp = xpp.nextText();
//                            item.setLink(temp);
//                        } else if (xpp.getName().equalsIgnoreCase("pubDate") && xpp.getDepth() == 4) {
//                            String temp = xpp.nextText();
//                            item.setPubDate(temp);
//                            //Log.e("MyTag", "Pub Date is " + temp);
//                        }
//                        break;
//                    case XmlPullParser.END_TAG:
//                        if (xpp.getName().equalsIgnoreCase("item"))
//                        {
//                        items.add(item);
//                        System.out.println("Current Incident saved...");
//                        }
//                        else if (xpp.getName().equalsIgnoreCase("channel"))
//                            System.out.println("End of XML Items");
//                            break;
//                }
//                eventType = xpp.next(); // get the next event
//            }
//            if (eventType == XmlPullParser.END_DOCUMENT) {
//                System.out.println("End of XML Document");
//                System.out.println("Current Incidents List Size: " + items.size());
//
//                /*
//                for (int i = 0; i < currentIncidents.size(); i++) {
//                    System.out.println(currentIncidents.get(i).getTitle());
//                  }
//                 */
//            }
//            //System.out.println("Current Incident List: " + currentIncidents.get(0));
//        } catch (Exception err) {
//            System.out.println("Error: " + err);
//        }
//    }
}
