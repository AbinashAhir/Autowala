package com.example.autowala;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.bson.Document;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback  {

    private AppBarConfiguration mAppBarConfiguration;
    private GoogleMap mMap;

    EditText selectPick, selectDest;


//    String Appid = "autowala-uoqmy";
//    private App app;
//    String phoneNumber = "8457823790";
//
//    MongoDatabase mongoDatabase;
//    MongoClient mongoClient;

    Button submitDest;
    TextView textView3;
    TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

//        if (getSupportActionBar() != null) {
//            getSupportActionBar().hide();
//        }

//        Realm.init(this);
//        app = new App(new AppConfiguration.Builder(Appid).build());
//
//        User user = app.currentUser();
//        mongoClient = user.getMongoClient("mongodb-atlas");
//        mongoDatabase = mongoClient.getDatabase("AutowalaDB");
//        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("Autowala");


        username = (TextView) findViewById(R.id.userName);
        selectPick = (EditText) findViewById(R.id.selectPick);
        selectDest = (EditText) findViewById(R.id.selectDest);

//        buttonPickup.setVisibility(View.VISIBLE);
//        buttonDestination.setVisibility(View.VISIBLE);

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Document queryFilter = new Document().append("phoneNumber",phoneNumber);
//                mongoCollection.findOne(queryFilter).getAsync(result -> {
//                    if (result.isSuccess()) {
//                        Toast.makeText(HomeActivity.this, "Found!", Toast.LENGTH_SHORT).show();
//                        Document resultData = result.get();
//                        textView3.setText(resultData.getString("userid"));
//                    }
//                    else{
//                        Toast.makeText(HomeActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
//                        textView3.setText("Not Found!");
//                    }
//                });
//            }
//        });



        selectPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(HomeActivity.this, selectPick);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
//                        Toast.makeText(
//                                HomeActivity.this,
//                                "You Clicked : " + item.getTitle(),
//                                Toast.LENGTH_SHORT
//                        ).show();
                        selectPick.setText(item.getTitle());
                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        });

        selectDest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(HomeActivity.this, selectDest);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
//                        Toast.makeText(
//                                HomeActivity.this,
//                                "You Clicked : " + item.getTitle(),
//                                Toast.LENGTH_SHORT
//                        ).show();
                        selectDest.setText(item.getTitle());
                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        });


        submitDest = (Button) findViewById(R.id.submitDest);

        submitDest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Searching for Available Auto-rickshaws...", Toast.LENGTH_SHORT).show();
            }
        });

//        selectDest.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                PopupMenu popup = new PopupMenu(HomeActivity.this, selectDest);
//                //Inflating the Popup using xml file
//                popup.getMenuInflater()
//                        .inflate(R.menu.popup_menu, popup.getMenu());
//
//                //registering popup with OnMenuItemClickListener
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    public boolean onMenuItemClick(MenuItem item) {
////                        Toast.makeText(
////                                HomeActivity.this,
////                                "You Clicked : " + item.getTitle(),
////                                Toast.LENGTH_SHORT
////                        ).show();
//                        selectDest.setText(item.getTitle());
//                        return true;
//                    }
//                });
//
//                popup.show();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_payments, R.id.nav_notifications,R.id.nav_history,R.id.nav_settings,R.id.nav_insurance,R.id.nav_help)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sarang = new LatLng(20.936009, 85.261085);
        mMap.addMarker(new MarkerOptions().position(sarang).title("Marker in Sarang"));

        float zoomLevel = 16.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sarang, zoomLevel));

    }


}