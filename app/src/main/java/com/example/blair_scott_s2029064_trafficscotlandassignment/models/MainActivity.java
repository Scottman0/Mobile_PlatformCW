// Scott Blair (2022) -- Student ID: S2029064
package com.example.blair_scott_s2029064_trafficscotlandassignment.models;

// Import required libraries
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.example.blair_scott_s2029064_trafficscotlandassignment.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    private TextView rawDataDisplay;
    private Button plannedRoadworksBtn;
    private Button currentRoadworksBtn;
    private Button currentIncidentsBtn;
    private Button searchBtn;
    private String searchValue;
    private String result = "";
    List<Item> searchedRoadworks = new ArrayList<Item>();

    // Traffic Scotland Roadworks XML links
    private String urlCurrentIncidentsSource = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private String urlPlannedRoadworksSource = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private String urlCurrentRoadworksSource = "https://trafficscotland.org/rss/feeds/roadworks.aspx";

    int currentUrl = 1; // value to store which URL we are parsing data from

    Parser parser = new Parser(); // our class containing everything related to the XMLPullParser

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MyTag", "in onCreate");

        // enable buttons after 9 seconds (allow time for data to be parsed)
        new Handler().postDelayed(new Runnable() {
            public void run()
                {
                    System.out.println("Parsing data, buttons disabled temporarily...");
                    searchBtn.setEnabled(true);
                    plannedRoadworksBtn.setEnabled(true);
                    currentRoadworksBtn.setEnabled(true);
                    currentIncidentsBtn.setEnabled(true);
                    TextView searchTxt = findViewById(R.id.searchTxt);
                    searchTxt.setEnabled(true);
                }
            }, 9000    //Specific time in milliseconds
        );

        if (parser.currentIncidents.isEmpty()){
                parser.startCurrentIncidentsProgress(); // start parsing current incidents first on create which when complete
                // will parse planned roadworks and then finally current roadworks;
            }

        plannedRoadworksBtn = (Button) findViewById(R.id.plannedRoadworksBtn);
        plannedRoadworksBtn.setOnClickListener(this);
        currentRoadworksBtn = (Button) findViewById(R.id.currentRoadworksBtn);
        currentRoadworksBtn.setOnClickListener(this);
        currentIncidentsBtn = (Button) findViewById(R.id.currentIncidentsBtn);
        currentIncidentsBtn.setOnClickListener(this);
        TextView searchTxt = findViewById(R.id.searchTxt);
        searchBtn = (Button) findViewById(R.id.searchBtn);
        // when search button pressed loop through the items array from our parser class and get all items matching search value using a list
        searchBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                System.out.println("Search: " + searchTxt.getText());
                searchValue = searchTxt.getText().toString().toLowerCase();
                searchedRoadworks.clear();
                for (int i = 0; i < parser.items.size(); i++) {
                    if(parser.items.get(i).getTitle().toLowerCase().contains(searchValue) || parser.items.get(i).getFormattedPubDate().toLowerCase().contains(searchValue))
                    {
                        searchedRoadworks.add(parser.items.get(i));

                        // sort searched results by most recent pub date
                        Collections.sort(searchedRoadworks, new Comparator<Item>() {
                            public int compare(Item item, Item t1) {
                                return item.getStartDate().compareTo(t1.getStartDate());
                            }
                        });

                        System.out.println("Searched Tag: " + searchValue);
                        getItems("searchedRoadworks");
                    }
                }
            } catch (Exception e){
                    System.out.println("Error: check search tag is not null - " + e);
                }
            }

        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.plannedRoadworksBtn:
                getItems("plannedRoadworks");
                break;
            case R.id.currentRoadworksBtn:
                getItems("currentRoadworks");
                break;
            case R.id.currentIncidentsBtn:
                getItems("currentIncidents");
                break;
        }
    }

    public void getItems(String itemsToGet) {
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        RecyclerView recyclerView = findViewById(R.id.rv_list);

        switch (itemsToGet) {
            case "currentRoadworks":
                List<Item> currentRoadworksList = parser.currentRoadworks;
                Collections.sort(currentRoadworksList, new Comparator<Item>() {
                    public int compare(Item item, Item t1) {
                        return item.getStartDate().compareTo(t1.getStartDate());
                    }
                });
                Adapter adapter1 = new Adapter(this, currentRoadworksList);
                recyclerView.setAdapter(adapter1);
                recyclerView.setLayoutManager(new LinearLayoutManager((this)));
                break;
            case "currentIncidents":
                List<Item> currentIncidentsList = parser.currentIncidents;
                Adapter adapter2 = new Adapter(this, currentIncidentsList);
                recyclerView.setAdapter(adapter2);
                recyclerView.setLayoutManager(new LinearLayoutManager((this)));
                break;
            case "plannedRoadworks":
                List<Item> plannedRoadworksList = parser.plannedRoadworks;
                Collections.sort(plannedRoadworksList, new Comparator<Item>() {
                    public int compare(Item item, Item t1) {
                        return item.getStartDate().compareTo(t1.getStartDate());
                    }
                });
                Adapter adapter3 = new Adapter(this, plannedRoadworksList);
                recyclerView.setAdapter(adapter3);
                recyclerView.setLayoutManager(new LinearLayoutManager((this)));
                break;
            case "searchedRoadworks":
                Adapter adapter4 = new Adapter(this, searchedRoadworks);
                recyclerView.setAdapter(adapter4);
                recyclerView.setLayoutManager(new LinearLayoutManager((this)));
                break;

        }
    }
}

