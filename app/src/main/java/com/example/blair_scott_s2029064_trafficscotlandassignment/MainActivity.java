package com.example.blair_scott_s2029064_trafficscotlandassignment;

// Import required libraries
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.example.blair_scott_s2029064_trafficscotlandassignment.models.CurrentIncidents;
import com.example.blair_scott_s2029064_trafficscotlandassignment.models.CurrentRoadworks;
import com.example.blair_scott_s2029064_trafficscotlandassignment.models.PlannedRoadworks;
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
    private String url1 = "";

    // Traffic Scotland Roadworks XML links
    private String urlCurrentIncidentsSource = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private String urlPlannedRoadworksSource = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private String urlCurrentRoadworksSource = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    int currentUrl = 0; // value to store which URL we are parsing data from

    List<CurrentRoadworks> currentRoadworks = new ArrayList<CurrentRoadworks>();
    List<PlannedRoadworks> plannedRoadworks = new ArrayList<PlannedRoadworks>();
    List<CurrentIncidents> currentIncidents = new ArrayList<CurrentIncidents>();
    CurrentRoadworks currentRoadwork = new CurrentRoadworks();
    PlannedRoadworks plannedRoadwork = new PlannedRoadworks();
    CurrentIncidents currentIncident = new CurrentIncidents();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MyTag", "in onCreate");
        // Set up the raw links to the graphical components
        rawDataDisplay = (TextView) findViewById(R.id.rawDataDisplay);
        currentIncidentsButton = (Button) findViewById(R.id.currentIncidentsButton);
        currentIncidentsButton.setOnClickListener(this);
        currentRoadworksButton = (Button) findViewById(R.id.currentRoadworksButton);
        currentRoadworksButton.setOnClickListener(this);
        plannedRoadworksButton = (Button) findViewById(R.id.plannedRoadworksButton);
        plannedRoadworksButton.setOnClickListener(this);
        Log.e("MyTag", "after startButton");

        // More Code goes here
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
        // check which button has been pressed and handle input
        switch (id) {
            case R.id.currentIncidentsButton:
                currentUrl = 1;
                startCurrentIncidentsProgress();
                break;
            case R.id.plannedRoadworksButton:
                currentUrl = 2;
                startPlannedRoadworksProgress();
                break;
            case R.id.currentRoadworksButton:
                currentUrl = 3;
                startCurrentRoadworksProgress();
                System.out.println("Button 3");
                break;
                }
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
                        parseCurrentIncidentsData(result);
                        result = "";
                        break;
                    case 2:
                        parsePlannedRoadworksData(result);
                        result = "";
                        break;
                    case 3:
                        parseCurrentRoadworksData(result);
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
                    rawDataDisplay.setText(result);
                }
            });
        }
    }

    private void parseCurrentRoadworksData(String urlToParse) {
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
                            currentRoadwork = new CurrentRoadworks();
                            System.out.println("current roadworks - object created");
                        } else if (xpp.getName().equalsIgnoreCase("title") && xpp.getDepth() == 4) {
                            String temp = xpp.nextText();
                            currentRoadwork.setTitle(temp);
                            //Log.e("MyTag", "Title is " + temp);
                        } else if (xpp.getName().equalsIgnoreCase("description") && xpp.getDepth() == 4) {
                            String temp = xpp.nextText();
                            currentRoadwork.setDescription(temp);
                        } else if (xpp.getName().equalsIgnoreCase("link") && xpp.getDepth() == 4) {
                            String temp = xpp.nextText();
                            currentRoadwork.setLink(temp);
                        } else if (xpp.getName().equalsIgnoreCase("pubDate") && xpp.getDepth() == 4) {
                            String temp = xpp.nextText();
                            currentRoadwork.setPubDate(temp);
                            //Log.e("MyTag", "Pub Date is " + temp);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (xpp.getName().equalsIgnoreCase("item"))
                        {
                            currentRoadworks.add(currentRoadwork);
                            System.out.println("Current Roadwork saved...");
                        }
                        else if (xpp.getName().equalsIgnoreCase("channel"))
                            System.out.println("End of XML Items");
                        break;
                }
                eventType = xpp.next(); // get the next event
            }
            if (eventType == XmlPullParser.END_DOCUMENT) {
                System.out.println("End of XML Document");

                /*
                for (int i = 0; i < currentRoadworks.size(); i++) {
                    System.out.println(currentRoadworks.get(i).getTitle());
                  }
                 */
            }
            //System.out.println("Current Incident List: " + currentIncidents.get(0));
        } catch (Exception err) {
            System.out.println("Error: " + err);
        }
    }

    private void parsePlannedRoadworksData(String urlToParse) {
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
                            plannedRoadwork = new PlannedRoadworks();
                            System.out.println("planned roadworks - object created");
                        } else if (xpp.getName().equalsIgnoreCase("title") && xpp.getDepth() == 4) {
                            String temp = xpp.nextText();
                            plannedRoadwork.setTitle(temp);
                            //Log.e("MyTag", "Title is " + temp);
                        } else if (xpp.getName().equalsIgnoreCase("description") && xpp.getDepth() == 4) {
                            String temp = xpp.nextText();
                            plannedRoadwork.setDescription(temp);
                        } else if (xpp.getName().equalsIgnoreCase("link") && xpp.getDepth() == 4) {
                            String temp = xpp.nextText();
                            plannedRoadwork.setLink(temp);
                        } else if (xpp.getName().equalsIgnoreCase("pubDate") && xpp.getDepth() == 4) {
                            String temp = xpp.nextText();
                            plannedRoadwork.setPubDate(temp);
                            //Log.e("MyTag", "Pub Date is " + temp);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (xpp.getName().equalsIgnoreCase("item"))
                        {
                            plannedRoadworks.add(plannedRoadwork);
                            System.out.println("Planned Roadwork saved...");
                        }
                        else if (xpp.getName().equalsIgnoreCase("channel"))
                            System.out.println("End of XML Items");
                        break;
                }
                eventType = xpp.next(); // get the next event
            }
            if (eventType == XmlPullParser.END_DOCUMENT) {
                System.out.println("End of XML Document");

                /*
                for (int i = 0; i < plannedRoadworks.size(); i++) {
                    System.out.println(plannedRoadworks.get(i).getTitle());
                  }
                 */
            }
            //System.out.println("Planned Roadworks List: " + currentIncidents.get(0));
        } catch (Exception err) {
            System.out.println("Error: " + err);
        }
    }

    private void parseCurrentIncidentsData(String urlToParse) {
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
                            currentIncident = new CurrentIncidents();
                            System.out.println("current incidents - object created");
                        } else if (xpp.getName().equalsIgnoreCase("title") && xpp.getDepth() == 4) {
                            String temp = xpp.nextText();
                            currentIncident.setTitle(temp);
                            //Log.e("MyTag", "Title is " + temp);
                        } else if (xpp.getName().equalsIgnoreCase("description") && xpp.getDepth() == 4) {
                            String temp = xpp.nextText();
                            currentIncident.setDescription(temp);
                        } else if (xpp.getName().equalsIgnoreCase("link") && xpp.getDepth() == 4) {
                            String temp = xpp.nextText();
                            currentIncident.setLink(temp);
                        } else if (xpp.getName().equalsIgnoreCase("pubDate") && xpp.getDepth() == 4) {
                            String temp = xpp.nextText();
                            currentIncident.setPubDate(temp);
                            //Log.e("MyTag", "Pub Date is " + temp);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (xpp.getName().equalsIgnoreCase("item"))
                        {
                        currentIncidents.add(currentIncident);
                        System.out.println("Current Incident saved...");
                        }
                        else if (xpp.getName().equalsIgnoreCase("channel"))
                            System.out.println("End of XML Items");
                            break;
                }
                eventType = xpp.next(); // get the next event
            }
            if (eventType == XmlPullParser.END_DOCUMENT) {
                System.out.println("End of XML Document");
                System.out.println("Current Incidents List Size: " + currentIncidents.size());

                /*
                for (int i = 0; i < currentIncidents.size(); i++) {
                    System.out.println(currentIncidents.get(i).getTitle());
                  }
                 */
            }
            //System.out.println("Current Incident List: " + currentIncidents.get(0));
        } catch (Exception err) {
            System.out.println("Error: " + err);
        }
    }
}
