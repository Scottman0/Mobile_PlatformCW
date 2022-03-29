// Scott Blair (2022) -- Student ID: S2029064
package com.example.blair_scott_s2029064_trafficscotlandassignment.models;

// Import required libraries
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.blair_scott_s2029064_trafficscotlandassignment.R;
import com.example.blair_scott_s2029064_trafficscotlandassignment.models.Item;
import java.util.ArrayList;
import java.util.List;
import com.example.blair_scott_s2029064_trafficscotlandassignment.models.Parser;

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
    Parser parser = new Parser(); // our class containing everything related to the XMLPullParser

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MyTag", "in onCreate");
        // Set up the raw links to the graphical components
        rawDataDisplay = (TextView) findViewById(R.id.rawDataDisplay);

//        currentIncidentsButton = (Button) findViewById(R.id.currentIncidentsButton);
//        currentIncidentsButton.setOnClickListener(this);

        parser.startCurrentIncidentsProgress(); // start parsing current incidents first on create which when complete
                                                // will parse planned roadworks and then finally current roadworks;
    }

    @Override
    public void onClick(View v) {
        Log.e("MyTag", "in onClick");
        int id = v.getId();
        Log.e("MyTag", "after startProgress");
    }
}
